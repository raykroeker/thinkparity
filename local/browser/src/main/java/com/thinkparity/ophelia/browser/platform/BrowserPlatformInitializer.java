/*
 * Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.platform;

import java.io.File;
import java.util.Properties;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.config.ConfigFactory;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

import com.thinkparity.ophelia.browser.Version;
import com.thinkparity.ophelia.browser.profile.Profile;
import com.thinkparity.ophelia.browser.util.ModelFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserPlatformInitializer {

    /**
     * Initialize the logging based upon the application's mode.
     * 
     */
    private static void initLogging(final Workspace workspace) {
        final Properties log4j = ConfigFactory.newInstance("log4j.properties");
        switch (Version.getMode()) {
        case DEVELOPMENT:
            log4j.setProperty("log4j.logger.com.thinkparity", "DEBUG,FILE");
            break;
        case DEMO:
        case PRODUCTION:
            log4j.setProperty("log4j.logger.com.thinkparity", "WARN,FILE");
            break;
        case TESTING:
            log4j.setProperty("log4j.logger.com.thinkparity", "INFO,FILE");
            break;
        default:
            Assert.assertUnreachable("UNKNOWN MODE");
        }
        log4j.setProperty("log4j.appender.FILE.File",
                new File(workspace.getLogDirectory(), "thinkParity.log").getAbsolutePath());
        PropertyConfigurator.configure(log4j);
        new Log4JWrapper().logInfo("{0} - {1} - {2}",
                Version.getName(), Version.getMode(), Version.getBuildId());
    }

    /** A thinkParity browser platform <code>Profile</code>. */
    private final Profile profile;

    /** A thinkParity <code>Environment</code>. */
    private final Environment environment;

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
	void initialize() {
        initializePropertyParityArchive();

        // init model
        final Workspace workspace =
            WorkspaceModel.getModel(environment).getWorkspace(
                    new File(profile.getParityWorkspace()));
        ModelFactory.getInstance().initialize(environment, workspace);

        initLogging(workspace);

        final Properties properties = System.getProperties();
        for(final Object key : properties.keySet()) {
            Logger.getLogger(BrowserPlatformInitializer.class)
                    .debug(new StringBuffer()
                        .append(key).append(":")
                        .append(properties.get(key)));
        }
	}

    private void initializePropertyParityArchive() {
        System.setProperty("parity.archive.directory", profile.getParityArchive());
    }
}
