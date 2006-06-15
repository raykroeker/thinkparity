/*
 * Feb 4, 2006
 */
package com.thinkparity.browser.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.thinkparity.browser.BrowserException;
import com.thinkparity.browser.Version;
import com.thinkparity.browser.Constants.Directories;
import com.thinkparity.browser.Constants.Java;
import com.thinkparity.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.application.Application;
import com.thinkparity.browser.platform.application.ApplicationFactory;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationRegistry;
import com.thinkparity.browser.platform.application.window.WindowRegistry;
import com.thinkparity.browser.platform.firstrun.FirstRunHelper;
import com.thinkparity.browser.platform.online.OnlineHelper;
import com.thinkparity.browser.platform.update.UpdateHelper;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;
import com.thinkparity.browser.profile.Profile;

import com.thinkparity.codebase.Mode;

import com.thinkparity.model.parity.model.workspace.Preferences;

/**
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserPlatform implements Platform {

	/**
     * Obtain a log id for the platform class.
     * 
     * @return A log id.
     */
    private static StringBuffer getLogId(final String suffix) {
        return new StringBuffer("[LBROWSER] [PLATFORM] ").append(suffix);
    }

	/**
	 * The platform's logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * The application registry.
	 * 
	 */
	private final ApplicationRegistry applicationRegistry;

	/**
	 * The avatar registry.
	 * 
	 */
	private final AvatarRegistry avatarRegistry;

    /** The platform's first run helper. */
    private final FirstRunHelper firstRunHelper;

	/** The parity model factory. */
	private final ModelFactory modelFactory;

	/**
	 * The platform settings.
	 * 
	 */
	private final BrowserPlatformPersistence persistence;

	/**
	 * The parity preferences.
	 * 
	 */
	private final Preferences preferences;

    /** The platform online helper. */
    private final OnlineHelper onlineHelper;

	/** The platform update helper. */
    private final UpdateHelper updateHelper;

	/**
	 * The window registry.
	 * 
	 */
	private final WindowRegistry windowRegistry;

	/**
	 * Create a BrowserPlatform [Singleton]
	 * 
	 */
	public BrowserPlatform(final Profile profile) {
        new BrowserPlatformInitializer(profile).initialize();
		this.applicationRegistry = new ApplicationRegistry();
		this.avatarRegistry = new AvatarRegistry();
		this.windowRegistry = new WindowRegistry();
		this.modelFactory = ModelFactory.getInstance();
		this.preferences = modelFactory.getPreferences(getClass());

		this.logger = LoggerFactory.getLogger(getClass());
		this.persistence = new BrowserPlatformPersistence(this);

        this.firstRunHelper = new FirstRunHelper(this);
        this.onlineHelper = new OnlineHelper(this);
        this.updateHelper = new UpdateHelper(this);
	}

	/**
     * @see com.thinkparity.browser.platform.Platform#end()
     * 
     */
    public void end() {
        for(final ApplicationId id : ApplicationId.values()) {
            if(applicationRegistry.contains(id))
                applicationRegistry.get(id).end(this);
        }
    }

	/**
	 * @see com.thinkparity.browser.platform.Platform#getAvatarRegistry()
	 * 
	 */
	public AvatarRegistry getAvatarRegistry() { return avatarRegistry; }

	/**
	 * @see com.thinkparity.browser.platform.Platform#getLogger(java.lang.Class)
	 * 
	 */
	public Logger getLogger(final Class clasz) {
		return LoggerFactory.getLogger(clasz);
	}

	/**
	 * @see com.thinkparity.browser.platform.Platform#getModelFactory()
	 * 
	 */
	public ModelFactory getModelFactory() { return modelFactory; }

	/**
	 * @see com.thinkparity.browser.platform.Platform#getPersistence()
	 * 
	 */
	public BrowserPlatformPersistence getPersistence() {
		return persistence;
	}

	/**
	 * @see com.thinkparity.browser.platform.Platform#getPreferences()
	 * 
	 */
	public Preferences getPreferences() { return preferences; }

    /**
	 * @see com.thinkparity.browser.platform.Platform#getWindowRegistry()
	 * 
	 */
	public WindowRegistry getWindowRegistry() { return windowRegistry; }

	/**
	 * @see com.thinkparity.browser.platform.Platform#hibernate(com.thinkparity.browser.platform.application.ApplicationId)
	 * 
	 */
	public void hibernate(final ApplicationId applicationId) {
		applicationRegistry.get(applicationId).hibernate(this);
	}

	/** @see com.thinkparity.browser.platform.Platform#isDevelopmentMode() */
	public Boolean isDevelopmentMode() { return Version.getMode() == Mode.DEVELOPMENT; }

	/** @see com.thinkparity.browser.platform.Platform#isTestingMode() */
	public Boolean isTestingMode() {
		if(isDevelopmentMode()) { return Boolean.TRUE; }
		return Version.getMode() == Mode.TESTING;
	}

    /**
	 * @see com.thinkparity.browser.platform.application.ApplicationListener#notifyEnd(com.thinkparity.browser.platform.application.Application)
	 * 
	 */
	public void notifyEnd(final Application application) {
        logger.info(getLogId("[NOTIFY END]"));
        logger.debug(application);
	}

	/**
	 * @see com.thinkparity.browser.platform.application.ApplicationListener#notifyHibernate(com.thinkparity.browser.platform.application.Application)
	 * 
	 */
	public void notifyHibernate(final Application application) {
		logger.info(getLogId("[NOTIFY HIBERNATE]"));
        logger.debug(application);
	}

    /**
	 * @see com.thinkparity.browser.platform.application.ApplicationListener#notifyRestore(com.thinkparity.browser.platform.application.Application)
	 * 
	 */
	public void notifyRestore(final Application application) {
        logger.info(getLogId("[NOTIFY RESTORE]"));
        logger.debug(application);
	}

	/**
	 * @see com.thinkparity.browser.platform.application.ApplicationListener#notifyStart(com.thinkparity.browser.platform.application.Application)
	 * 
	 */
	public void notifyStart(final Application application) {
        logger.info(getLogId("[NOTIFY START]"));
        logger.debug(application);
	}

	/** @see com.thinkparity.browser.platform.Platform#restart() */
    public void restart() { restart(System.getProperties()); }

    /** @see com.thinkparity.browser.platform.Platform#restart(java.util.Properties) */
    public void restart(final Properties properties) {
        logger.info(getLogId("[RESTART]"));
        logger.debug(properties);

        // attach a process to the jvm shutdown
        final List<String> commands = new ArrayList<String>();
        commands.add(Java.EXECUTABLE);
        if(properties.containsKey("parity.image.name"))
            commands.add(Java.OPTION_PARITY_IMAGE.format(new Object[] {properties.getProperty("parity.image.name")}));
        commands.add(Java.OPTION_PARITY_INSTALL.format(new Object[] {Directories.PARITY_INSTALL.getAbsolutePath()}));
        commands.add(Java.OPTION_PARITY_SERVER_HOST.format(new Object[] {preferences.getServerHost()}));
        commands.add(Java.OPTION_PARITY_SERVER_PORT.format(new Object[] {preferences.getServerPort().toString()}));
        commands.add(Java.OPTION_CLASS_PATH);
        commands.add(Java.OPTION_CLASS_PATH_VALUE);
        commands.add(Java.MAIN_CLASS);
        for(final String command : commands) { logger.debug(command); }

        final ProcessBuilder pb = new ProcessBuilder();
        pb.command(commands);
        logger.debug(Directories.PARITY_INSTALL);
        pb.directory(Directories.PARITY_INSTALL);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try { pb.start(); }
                catch(final IOException iox) {
                    throw new BrowserException(getLogId("[RESTARTING PLATFORM] [RESTART IO ERROR]").toString(), iox);
                }
            }
        });
        end();
    }

    /**
	 * @see com.thinkparity.browser.platform.Platform#restore(com.thinkparity.browser.platform.application.ApplicationId)
	 * 
	 */
	public void restore(final ApplicationId applicationId) {
		logger.info(getLogId("[RESTORE]"));
        logger.debug(applicationId);
		applicationRegistry.get(applicationId).restore(this);
	}

    /**
     * @see com.thinkparity.browser.platform.Platform#start()
     * 
     */
	public void start() {
        logger.info(getLogId("[START]"));
        if(isUpdateAvailable()) { update(); }
        else {
            if(isFirstRun()) {
                if(firstRun()) {
                    ApplicationFactory.create(this, ApplicationId.BROWSER2).start(this);
                    ApplicationFactory.create(this, ApplicationId.SYS_APP).start(this);
                    ApplicationFactory.create(this, ApplicationId.SESSION).start(this);
                }
            }
            else {
                ApplicationFactory.create(this, ApplicationId.BROWSER2).start(this);
                ApplicationFactory.create(this, ApplicationId.SYS_APP).start(this);
                ApplicationFactory.create(this, ApplicationId.SESSION).start(this);
            }
        }
	}

    /** Update the browser. */
    public void update() {
        logger.info(getLogId("[UPDATE]"));
        updateHelper.update();
    }

    /** Perform first run initialization. */
    private Boolean firstRun() { return firstRunHelper.firstRun(); }

	/**
     * Determine if this is the first time the platform has been run.
     *
     * @return True if this is the first run of the platform.
     */
    private Boolean isFirstRun() { return firstRunHelper.isFirstRun(); }

    /**
     * Determine whether or not an update is available.
     *
     * @return True if a newer release is available.
     */
    private Boolean isUpdateAvailable() { return updateHelper.isAvailable(); }

    /** @see com.thinkparity.browser.platform.Platform#isOnline() */
    public Boolean isOnline() { return onlineHelper.isOnline(); }
}
