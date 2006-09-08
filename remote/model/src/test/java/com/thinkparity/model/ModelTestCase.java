/*
 * Created On: Tue Jun 06 2006 10:31 PDT
 * $Id$
 */
package com.thinkparity.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.NetworkUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.artifact.Artifact;

import com.raykroeker.junitx.TestCase;
import com.raykroeker.junitx.TestSession;
import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.artifact.ArtifactModel;

/**
 * An abstraction of the parity remote model test case.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class ModelTestCase extends TestCase {

    static {
        final TestSession testSession = getTestSession();
        final File log4jFile = new File(testSession.getSessionDirectory(), "desdemona.log4j");
        System.setProperty("thinkparity.log4j.file", log4jFile.getAbsolutePath());
    }

    /**
     * Create ModelTestCase.
     *
     * @param name
     *      The test name.
     */
    protected ModelTestCase(final String name) { super(name); }

    /**
     * Create an artifact.
     *
     * @return The new artifact.
     */
    protected Artifact createArtifact() throws ParityServerModelException {
        final UUID artifactUniqueId = UUID.randomUUID();
        return getArtifactModel().create(artifactUniqueId);
    }

    /**
     * Obtain the parity artifact interface.
     *
     * @return The parity artifact interface.
     */
    protected ArtifactModel getArtifactModel() {
        //return ArtifactModel.getModel();
        return null;
    }

    /** @see com.raykroeker.junitx.TestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();
        startHypersonic();
        startWildfire();
    }

    /** @see com.raykroeker.junitx.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        stopWildfire();
        stopHypersonic();
        super.tearDown();
    }

    private void stopHypersonic() {}

    private void stopWildfire() {}

    /**
     * Start the hsqldb database on localhost in a separate process.
     *
     */
    private void startHypersonic() throws IOException {
        // options
        final List<String> options = new ArrayList<String>();
        options.add("-server");
        // classpath
        final List<File> classpath = new ArrayList<File>();
        final StringTokenizer classpathTokenizer = new StringTokenizer(System.getProperty("java.class.path"), File.pathSeparator);
        String classpathElement;
        while(classpathTokenizer.hasMoreTokens()) {
            classpathElement = classpathTokenizer.nextToken();
            // pull hsqldb.jar from the current classpath
            if(classpathElement.endsWith("hsqldb.jar")) {
                classpath.add(new File(classpathElement));
                break;
            }
        }
        // main
        final String main = "org.hsqldb.Server";
        // arguments
        final List<String> arguments = new ArrayList<String>();
        arguments.add("-address");
        arguments.add(NetworkUtil.getMachine());
        arguments.add("-port");
        arguments.add("9002");
        arguments.add("-silent");
        arguments.add("false");
        arguments.add("-database.0");
        final String databaseLocation = new StringBuffer(getTestSession().getSessionDirectory().getAbsolutePath())
                .append(File.separator).append("hsqldb")
                .append(File.separator).append("desdemona")
                .append(File.separator).append("db")
                .toString();
        arguments.add(databaseLocation);
        arguments.add("-dbname.0");
        arguments.add("desdemona");
        
        try { startJavaProcess(options, classpath, main, arguments); }
        catch(final IOException iox) { fail("[MODEL TEST CASE] [COULD NOT START DATABASE]"); }
    }

    /**
     * Start a java process.
     * 
     * @param options
     *            The options.
     * @param classpath
     *            The classpath.
     * @param main
     *            The main class.
     * @param arguments
     *            The arguments.
     * @throws IOException
     */
    private void startJavaProcess(final List<String> options,
            final List<File> classpath, final String main,
            final List<String> arguments) throws IOException {
        // java
        final List<String> commands = new ArrayList<String>();
        commands.add(new StringBuffer(System.getProperty("java.home"))
                .append(File.separator).append("bin")
                .append(File.separator).append("java")
                .toString());
        // ooptions
        for(final String option : options)
            commands.add(option);
        // classpath
        commands.add("-cp");
        final StringBuffer classpathBuffer = new StringBuffer();
        for(final File classpathElement : classpath) {
            if(0 < classpathBuffer.length()) {
                classpathBuffer.append(File.pathSeparator);
            }
            classpathBuffer.append(classpathElement.getAbsolutePath());
        }
        commands.add(classpathBuffer.toString());
        // main
        commands.add(main);
        for(final String argument : arguments)
            commands.add(argument);
        final ProcessBuilder pb = new ProcessBuilder();
        pb.command(commands);
        pb.directory(getTestSession().getSessionDirectory());
        pb.redirectErrorStream(true);
        final Process p = pb.start();
        final BufferedReader outputReader = new BufferedReader(new InputStreamReader(
                new BufferedInputStream(p.getInputStream())));
        new Thread("JAVA PROCESS OUTPUT READER") {
            final Logger logger = Logger.getLogger(getClass());
            @Override
            public void run() {
                try {
                    while(true) {
                        if(outputReader.ready()) {
                            logger.debug(outputReader.readLine());
                        }
                    }
                }
                catch(final IOException iox) {
                    fail(createFailMessage(iox));
                }
            }
        }.start();
    }

    private void startWildfire() { Assert.assertNotYetImplemented("ModelTestCase#startWidlfire()"); }
}