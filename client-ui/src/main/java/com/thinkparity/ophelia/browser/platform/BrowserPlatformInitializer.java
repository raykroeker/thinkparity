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

import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.profile.Profile;
import com.thinkparity.ophelia.browser.util.ModelFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserPlatformInitializer {

    /** A thinkParity <code>Environment</code>. */
    private final Environment environment;

    /** A thinkParity browser platform <code>Profile</code>. */
    private final Profile profile;

    /**
     * Create BrowserPlatformInitializer.
     * 
     * @param profile
     *            A browser profile to initialize.
     */
    BrowserPlatformInitializer(final Environment environment,
            final Profile profile) {
        super();
        this.environment = environment;
        this.profile = profile;
    }

    /**
	 * Initialize the browser2 platform.
	 *
	 */
	void initialize(final Mode mode) {
        // init model
        final Workspace workspace =
            WorkspaceModel.getModel(environment).getWorkspace(
                    new File(profile.getParityWorkspace()));
        ModelFactory.getInstance().initialize(environment, workspace);

        initLogging(workspace);
        redirectStreams(mode, workspace);

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
    private void redirectStreams(final Mode mode, final Workspace workspace) {
        final File output = new File(MessageFormat.format(
                "{0}{1}{2}", workspace.getLogDirectory().getAbsolutePath(),
                File.separatorChar, "System.out.log"));
        final File err = new File(MessageFormat.format(
                "{0}{1}{2}", workspace.getLogDirectory().getAbsolutePath(),
                File.separatorChar, "System.err.log"));
        switch (mode) {
        case DEMO:
        case PRODUCTION:
        case TESTING:       // redirect output\err streams
            try {
                System.setOut(new PrintStream(new FileOutputStream(output)));
                System.setErr(new PrintStream(new FileOutputStream(err)));
            } catch (final IOException iox) {
                throw new BrowserException("", iox);
            }
            break;
        case DEVELOPMENT:   // do not redirect output\err streams
            break;
        default:
            throw Assert.createUnreachable("Unknown mode.");
        }
    }
}
