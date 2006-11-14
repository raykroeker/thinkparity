/*
 * Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.platform;

import java.io.File;
import java.util.Properties;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

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
        // note that only renderers should be configured; and that the existing
        // configuration should not be reset
        final Properties logging = new Properties();
        logging.setProperty("log4j.renderer.java.awt.Point", "com.thinkparity.codebase.log4j.or.PointRenderer");
        logging.setProperty("log4j.renderer.java.awt.event.MouseEvent", "com.thinkparity.codebase.log4j.or.MouseEventRenderer");
        PropertyConfigurator.configure(logging);
    }

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
