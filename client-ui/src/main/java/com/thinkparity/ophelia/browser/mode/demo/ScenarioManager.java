/*
 * Created On: 13-Oct-06 5:13:36 PM
 */
package com.thinkparity.ophelia.browser.mode.demo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.model.script.Script;

/**
 * <b>Title:</b>thinkParity Scenario Manager<br>
 * <b>Description:</b>A scenario manager is responsible for selecting a demo
 * scenario; then creating necessary profiles and data to execute the scenario.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ScenarioManager {

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /**
     * Create ScenarioManager.
     *
     */
    public ScenarioManager() {
        super();
        this.logger = new Log4JWrapper();
    }

    /**
     * Display the secenarios from when the user can select.
     * 
     * @return A <code>Scenario</code>.
     */
    public Scenario select() {
        try {
            final ScenarioManagerWindow window = new ScenarioManagerWindow();
            window.setResizable(false);
            window.setScenarios(readScenarios());
            window.addWindowListener(new WindowAdapter() {
                public void windowClosed(final WindowEvent e) {
                    synchronized(window) { window.notifyAll(); }
                }
            });
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    window.open();
                }
            });
            synchronized (window) {
                window.wait();
            }
            return window.getSelectedScenario();
        } catch (final Throwable t) {
            throw new BrowserException("", t);
        }
    }

    /**
     * Configure a scenario.
     * 
     * @param scenarios
     *            A <code>List&lt;Scenario&gt;</code>.
     * @param properties
     *            The scenario <code>Properties</code>.
     * @param scenarioName
     *            A scenario name <code>String</code>.
     */
    private void configureScenario(final List<Scenario> scenarios,
            final Environment environment, final Properties properties,
            final String scenarioName) {
        final String displayName = properties.getProperty(
                MessageFormat.format("demo.scenario.{0}.DisplayName", scenarioName), null);
        final String resourcePath = MessageFormat.format("demo/{0}/resources", scenarioName.toLowerCase());
//            properties.getProperty(
//                MessageFormat.format("demo.scenario.{0}.ResourcePath", scenarioName), null);
        final String scripts = properties.getProperty(
                MessageFormat.format("demo.scenario.{0}.Scripts", scenarioName), null);
        if (null == displayName || null == resourcePath || null == scripts) {
            logger.logWarning("Scenario {0} is not configured correctly.", scenarioName);
        } else {
            final Scenario scenario = new Scenario();
            scenario.setDisplayName(displayName);
            scenario.setEnvironment(environment);
            scenario.setName(scenarioName);
            scenario.setResourcePath(resourcePath);
            final StringTokenizer tokenizer = new StringTokenizer(scripts, ",");
            while (tokenizer.hasMoreTokens()) {
                configureScenarioScript(scenario, properties, tokenizer.nextToken());
            }
            scenarios.add(scenario);
        }
    }

    /**
     * Configure a scenario script.
     * 
     * @param scenario
     *            A <code>Scenario</code>.
     * @param properties
     *            The demo <code>Properties</code>.
     * @param scriptName
     *            A script name <code>String</code>.
     */
    private void configureScenarioScript(final Scenario scenario,
            final Properties properties, final String scriptName) {
        final String scriptPath = MessageFormat.format("demo/", "");
//            properties.getProperty(
//                MessageFormat.format("demo.scenario.{0}.{1}.ScriptPath", scenario.getName(), scriptName), null);
        final String username = properties.getProperty(
                MessageFormat.format("demo.scenario.{0}.{1}.Credentials.Username", scenario.getName(), scriptName), null);
        final String password = properties.getProperty(
                MessageFormat.format("demo.scenario.{0}.{1}.Credentials.Password", scenario.getName(), scriptName), null);
        if (null == scriptPath || null == username || null == password) {
            logger.logWarning("Demo script {0} for scenario {1} is not configured correctly.", scriptName, scenario.getName());
        } else {
            final Credentials credentials = new Credentials();
            credentials.setPassword(password);
            credentials.setUsername(username);
            scenario.addScript(new Script() {
                public String getName() {
                    return scriptName;
                }
                public InputStream open() {
                    return ResourceUtil.getInputStream(scriptPath);
                }
                public InputStream openResource(String name) {
                    return ResourceUtil.getInputStream(new StringBuffer(
                            scenario.getResourcePath())
                            .append("/").append(scriptPath).toString());
                }
            }, credentials);
        }
    }

    /**
     * Read the demo configuration.
     * 
     * @return A list of demo scenarios.
     * @throws IOException
     */
    private List<Scenario> readScenarios() throws IOException {
        final List<Scenario> scenarios = new ArrayList<Scenario>();

        // load demo.properties
        final Properties properties = new Properties();
        properties.load(ResourceUtil.getInputStream("demo.properties"));

        final String environment =
            properties.getProperty("demo.environment", null);
        if(null == environment) {
            logger.logWarning("Demo is not configured correctly.");
        } else {
            final StringTokenizer tokenizer =
                new StringTokenizer(properties.getProperty("demo.scenarios", ""), ",");
            while (tokenizer.hasMoreTokens()) {
                configureScenario(scenarios, Environment.valueOf(environment),
                        properties, tokenizer.nextToken());
            }
        }

        return scenarios;
    }

}
