/*
 * Created On: 13-Oct-06 5:13:36 PM
 */
package com.thinkparity.ophelia.browser.mode.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.StringTokenizer;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.script.Script;

import com.thinkparity.ophelia.browser.BrowserException;

/**
 * <b>Title:</b>thinkParity Scenario Manager<br>
 * <b>Description:</b>A scenario manager is responsible for selecting a demo
 * scenario; then creating necessary profiles and data to execute the scenario.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DemoManager implements DemoProvider {

    /** The demo configuration resource name. */
    private static final String CONFIGURATION_NAME;

    static {
        CONFIGURATION_NAME = "demo.properties";
    }

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /**
     * Create DemoManager.
     *
     */
    public DemoManager() {
        super();
        this.logger = new Log4JWrapper();
    }

    /**
     * Open the demo manager window.
     * 
     */
    public void execute() {
        try {
            final DemoManagerWindow window = new DemoManagerWindow();
            window.setResizable(false);
            window.setDemoManager(this);
            window.setDemoProvider(this);
            window.setVisibleAndWait();
        } catch (final Throwable t) {
            throw new BrowserException("", t);
        }
    }

    /**
     * Execute a scenario.
     * 
     * @param monitor
     *            An execution monitor.
     * @param scenario
     *            A demo script <code>Scenario</code>.
     */
    void execute(final ExecutionMonitor monitor, final Scenario scenario) {
        scenario.execute(monitor);
    }

    /**
     * Read the demo configuration.
     * 
     * @return A list of demo scenarios.
     * @throws IOException
     */
    public Demo readDemo() {
        try {
            return doReadDemo();
        } catch (final Throwable t) {
            throw new BrowserException("", t);
        }
    }

    /**
     * Read the available scenarios in the file system.
     * 
     * @param fileSystem
     *            A demo <code>FileSystem</code>.
     */
    public Demo readDemo(final FileSystem fileSystem) {
        try {
            return doReadDemo(fileSystem);
        } catch (final Throwable t) {
            throw new BrowserException("", t);
        }
    }

    /**
     * Write the demo scenarios.
     * 
     * @param fileSystem
     *            A <code>FileSystem</code>.
     */
    void writeDemo(final File fileSystemRoot) {
        try {
            doWriteDemo(fileSystemRoot);
        } catch (final Throwable t) {
            FileUtil.deleteTree(fileSystemRoot);
            throw new BrowserException("", t);
        }
    }

    /**
     * Configure a demo scenario.
     * 
     * @param demo
     *            A <code>Demo</code>.
     * @param name
     *            A scenario name <code>String</code>.
     * @param demoResourceLoader
     *            A demo resource loader <code>ClassLoader</code>.
     */
    private void configureScenario(final Demo demo, final String name,
            final ClassLoader demoResourceLoader) {
        final String displayName = demo.getScenarioProperty(name, "DisplayName");
        final String scripts = demo.getScenarioProperty(name, "Scripts");
        if (null == displayName || null == scripts) {
            logger.logWarning("Scenario {0} has not been configured correctly.", name);
        } else {
            final Scenario scenario = new Scenario(Environment.valueOf(demo.getProperty("environment")));
            scenario.setDisplayName(displayName);
            scenario.setName(name);
            final StringTokenizer tokenizer = new StringTokenizer(scripts, ",");
            while (tokenizer.hasMoreTokens()) {
                configureScenarioScript(demo, scenario, tokenizer.nextToken(), demoResourceLoader);
            }
            demo.addScenario(scenario);
        }
    }

    /**
     * Configure a demo scenario script.
     * 
     * @param demo
     *            A <code>Demo</code>.
     * @param scenario
     *            A demo <code>Scenario</code>.
     * @param name
     *            A script name <code>String</code>.
     * @param demoLoader
     *            A demo resource loader <code>ClassLoader</code>
     */
    private void configureScenarioScript(final Demo demo,
            final Scenario scenario, final String name,
            final ClassLoader demoResourceLoader) {
        final String username = demo.getScriptProperty(scenario.getName(), name, "Credentials.Username");
        final String password = demo.getScriptProperty(scenario.getName(), name, "Credentials.Password");
        if (null == username || null == password) {
            logger.logWarning("Demo script {0} for scenario {1} is not configured correctly.", name, scenario.getName());
        } else {
            final Credentials credentials = new Credentials();
            credentials.setPassword(password);
            credentials.setUsername(username);
            scenario.addScript(new Script() {
                public String getName() {
                    return name;
                }
                public InputStream open() {
                    // TODO Should support other script types
                    return demoResourceLoader.getResourceAsStream(
                            MessageFormat.format("{0}/{1}.groovy",
                                    scenario.getName().toLowerCase(), name));
                }
                public InputStream openResource(final String name) {
                    return demoResourceLoader.getResourceAsStream(
                            MessageFormat.format("{0}/resources/{1}",
                                    scenario.getName().toLowerCase(), name));
                }
            }, credentials);
        }
    }

    /**
     * Read the default demo.
     * 
     * @return A <code>Demo</code>.
     * @throws IOException
     */
    private Demo doReadDemo() throws IOException {
        // create a resource loader
        final URL defaultRoot = ResourceUtil.getURL("demo/");
        final URLClassLoader resourceLoader = new URLClassLoader(
                new URL[] { defaultRoot }, null);
        return doReadDemo(resourceLoader);
    }

    /**
     * Read a demo. This will use the demo resource loader to load the demo
     * configuration; as well as all scripts and resources.
     * 
     * @param demoResourceLoader
     *            A demo resource loader <code>ClassLoader</code>.
     * @return A <code>Demo</code>.
     * @throws IOException
     */
    private Demo doReadDemo(final ClassLoader demoResourceLoader) throws IOException {
        final Demo demo = new Demo();

        final URL demoConfiguration = demoResourceLoader.getResource(CONFIGURATION_NAME);

        if (null == demoConfiguration) {
            logger.logWarning("No {0} found in demo root.  Please configure the demo properly.",
                    CONFIGURATION_NAME);
        } else {
            // load demo configuration
            final Properties configuration = new Properties();
            final InputStream demoConfigurationStream = demoConfiguration.openStream();
            try {
                configuration.load(demoConfiguration.openStream());
            } finally {
                demoConfigurationStream.close();
            }
            demo.setConfiguration(configuration);
    
            final String environment = demo.getProperty("environment");
            if(null == environment) {
                logger.logWarning("Demo environment has not been configured.");
            } else {
                final String scenarios = demo.getProperty("scenarios");
                if (null == scenarios) {
                    logger.logWarning("Demo scenarios have not been configured.");
                }
                final StringTokenizer tokenizer = new StringTokenizer(scenarios, ",");
                while (tokenizer.hasMoreTokens()) {
                    configureScenario(demo, tokenizer.nextToken(), demoResourceLoader);
                }
            }
        }
        return demo;
    }

    /**
     * Read a demo from a file system.
     * 
     * @param fileSystem
     *            A demo <code>FileSystem</code>.
     * @return A <code>Demo</code>.
     * @throws IOException
     */
    private Demo doReadDemo(final FileSystem fileSystem) throws IOException {
        // create a resource loader
        final URL[] demoRoot = new URL[] {
                fileSystem.getRoot().toURI().toURL() 
        };
        final URLClassLoader resourceLoader = new URLClassLoader(demoRoot, null);
        return doReadDemo(resourceLoader);
    }

    /**
     * Write the default demo to a file system.
     * 
     * @param demo
     *            A <code>Demo</code>.
     * @param fileSystemRoot
     *            A file system root <code>File</code>.
     * @throws IOException
     */
    private void doWriteDemo(final File fileSystemRoot) throws IOException {
        FileUtil.deleteTree(fileSystemRoot);
        Assert.assertTrue(fileSystemRoot.mkdir(),
                "Could not create directory {0}.", fileSystemRoot);
        final FileSystem fileSystem = new FileSystem(fileSystemRoot);
        final URL defaultRoot = ResourceUtil.getURL("demo/");
        final URLClassLoader resourceLoader = new URLClassLoader(
                new URL[] { defaultRoot }, null);

        writeFile("demo.properties", resourceLoader, fileSystem);
        writeFile("bootstrap/Bootstrap.groovy", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/Andrews Developments Inc - Revenue Canada Audit/1995 Balance Sheet.txt", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/Andrews Developments Inc - Revenue Canada Audit/1995 Income Statement.txt", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/Andrews Developments Inc - Revenue Canada Audit/1995 Jan - Dec Cash Flow.txt", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/Andrews Developments Inc - Revenue Canada Audit/1996, 1995, 1994 Financial History.txt", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/CTI Incorporation/Business Plan.txt", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/CTI Incorporation/Corporate Structure.txt", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/CTI Incorporation/Feasibility Analysis.txt", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/West Coast Software - Revenue Canada Audit/2004 Balance Sheet.txt", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/West Coast Software - Revenue Canada Audit/2004 Income Statement.txt", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/West Coast Software - Revenue Canada Audit/2004 Jan - Dec Cash Flow.txt", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/West Coast Software - Revenue Canada Audit/2005, 2004, 2003 Financial History.txt", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/Wilson Consulting Group - Corporate Merger/Asset List.txt", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/Wilson Consulting Group - Corporate Merger/Corporate Structure.txt", resourceLoader, fileSystem);
        writeFile("bootstrap/resources/Wilson Consulting Group - Corporate Merger/Target Share Divison Plan.txt", resourceLoader, fileSystem);
        writeFile("bspa/Create.groovy", resourceLoader, fileSystem);
        writeFile("bspa/Publish.groovy", resourceLoader, fileSystem);
        writeFile("bspa/resources/GreenTouch Partnership Agreement.doc", resourceLoader, fileSystem);
        writeFile("bspa/resources/GreenTouch Partnership Agreement.v2.doc", resourceLoader, fileSystem);
        writeFile("cto/Create.groovy", resourceLoader, fileSystem);
        writeFile("cto/Publish.groovy", resourceLoader, fileSystem);
        writeFile("cto/resources/Business Plan.txt", resourceLoader, fileSystem);
        writeFile("cto/resources/Corporate Structure.txt", resourceLoader, fileSystem);
        writeFile("cto/resources/Feasibility Analysis.txt", resourceLoader, fileSystem);
    }

    /**
     * Write a file from the file loader at the given path to the file system at
     * the given path.
     * 
     * @param path
     *            A file path <code>String</code>.
     * @param fileLoader
     *            A file loader <code>ClassLoader</code>.
     * @param fileSystem
     *            A <code>FileSystem</code>.
     * @throws IOException
     */
    private void writeFile(final String path, ClassLoader fileLoader,
            final FileSystem fileSystem) throws IOException {
        final File file = fileSystem.createFile(path);
        final InputStream input = fileLoader.getResourceAsStream(path);
        final FileOutputStream output = new FileOutputStream(file);
        try {
            StreamUtil.copy(input, output);
        } finally {
            output.flush();
            output.close();
            input.close();
        }
    }
}
