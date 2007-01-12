/*
 * Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.platform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Properties;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.util.ModelFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * <b>Title:</b>thinkParity Browser Platform Initializer<br>
 * <b>Description:</b>The browser platform initializer is responsible for
 * bringing the model online as well as any other browser related services.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class BrowserPlatformInitializer {

    /** The thinkParity <code>BrowserPlatform</code>. */
    private final BrowserPlatform platform;

    /**
     * Create BrowserPlatformInitializer.
     * 
     * @param platform
     *            The thinkParity <code>BrowserPlatform</code>.
     */
    BrowserPlatformInitializer(final BrowserPlatform platform) {
        super();
        this.platform = platform;
    }

    /**
	 * Initialize the platform.
	 *
	 */
	void initialize(final Workspace workspace) {
	    final Environment environment = platform.getEnvironment();
        ModelFactory.getInstance().initialize(environment, workspace);

        initLogging(workspace);
        redirectStreams(workspace);

        final Properties properties = System.getProperties();
        for(final Object key : properties.keySet()) {
            Logger.getLogger(BrowserPlatformInitializer.class)
                    .debug(new StringBuffer()
                        .append(key).append(":")
                        .append(properties.get(key)));
        }
	}

    /**
     * Initialize the logging based upon the application's mode.
     * 
     */
    private void initLogging(final Workspace workspace) {
        // note that only renderers should be configured; and that the existing
        // configuration should not be reset
        final Properties logging = new Properties();
        logging.setProperty("log4j.renderer.java.awt.Point", "com.thinkparity.codebase.log4j.or.PointRenderer");
        logging.setProperty("log4j.renderer.java.awt.event.MouseEvent", "com.thinkparity.codebase.log4j.or.MouseEventRenderer");
        PropertyConfigurator.configure(logging);
    }

    /**
     * Redirect the output and err streams.
     *
     */
    private void redirectStreams(final Workspace workspace) {
        final File output = new File(MessageFormat.format(
                "{0}{1}{2}", workspace.getLogDirectory().getAbsolutePath(),
                File.separatorChar, "System.out.log"));
        final File err = new File(MessageFormat.format(
                "{0}{1}{2}", workspace.getLogDirectory().getAbsolutePath(),
                File.separatorChar, "System.err.log"));
        if (!platform.isDevelopmentMode().booleanValue()) {
            // redirect output\err streams
            try {
                System.setOut(new PrintStream(new FileOutputStream(output)));
                System.setErr(new PrintStream(new FileOutputStream(err)));
            } catch (final IOException iox) {
                throw new BrowserException("", iox);
            }
        }
    }
}
