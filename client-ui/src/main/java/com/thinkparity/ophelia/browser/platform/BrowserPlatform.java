/*
 * Created On: Feb 4, 2006
 */
package com.thinkparity.ophelia.browser.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.Version;
import com.thinkparity.ophelia.browser.Constants.Directories;
import com.thinkparity.ophelia.browser.Constants.Java;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.ophelia.browser.platform.application.Application;
import com.thinkparity.ophelia.browser.platform.application.ApplicationFactory;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.platform.application.window.WindowRegistry;
import com.thinkparity.ophelia.browser.platform.event.LifeCycleEvent;
import com.thinkparity.ophelia.browser.platform.event.LifeCycleListener;
import com.thinkparity.ophelia.browser.platform.firstrun.FirstRunHelper;
import com.thinkparity.ophelia.browser.platform.online.OnlineHelper;
import com.thinkparity.ophelia.browser.platform.plugin.PluginHelper;
import com.thinkparity.ophelia.browser.platform.plugin.PluginRegistry;
import com.thinkparity.ophelia.browser.platform.update.UpdateHelper;
import com.thinkparity.ophelia.browser.profile.Profile;
import com.thinkparity.ophelia.browser.util.ModelFactory;
import com.thinkparity.ophelia.model.workspace.Preferences;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserPlatform implements Platform {

    /** A singleton instance. */
    private static BrowserPlatform SINGLETON;

    /**
     * Create an instance of the platform.
     * 
     * @param profile
     *            A profile to open.
     * @return The platform.
     */
    public static Platform create(final Profile profile,
            final Environment environment) {
        Assert.assertIsNull("PLATFORM ALREADY CREATED", SINGLETON);
        SINGLETON = new BrowserPlatform(profile, environment);
        return BrowserPlatform.getInstance();
    }

	/**
     * Obtain the platform instance.
     * 
     * @return The thinkParity platform.
     */
    public static Platform getInstance() {
        Assert.assertNotNull("PLATFORM NOT YET CREATED", SINGLETON);
        return SINGLETON;
    }

	/**
     * Obtain a log id for the platform class.
     * 
     * @return A log id.
     */
    private static StringBuffer getLogId(final String suffix) {
        return new StringBuffer("[LBROWSER] [PLATFORM] ").append(suffix);
    }

	/** The platform's log4j wrapper. */
	protected final Log4JWrapper logger;

	/** An application factory. */
    private final ApplicationFactory applicationFactory;

    /** An application registry. */
	private final ApplicationRegistry applicationRegistry;

    /** An avatar registry. */
	private final AvatarRegistry avatarRegistry;

	/** The thinkParity <code>Environment</code>. */
    private final Environment environment;

	/** The platform's first run helper. */
    private final FirstRunHelper firstRunHelper;

	/**
     * The listener helper. Manages all listeners as well as the listener
     * notification.
     */
    private final ListenerHelper listenerHelper;

    /** The parity model factory. */
	private final ModelFactory modelFactory;

	/** The platform online helper. */
    private final OnlineHelper onlineHelper;

    /** The platform settings. */
	private final BrowserPlatformPersistence persistence;

    /** The platform plugin helper. */
    private final PluginHelper pluginHelper;

    /** The parity preferences. */
	private final Preferences preferences;

    /** The platform update helper. */
    private final UpdateHelper updateHelper;

	/**
	 * The window registry.
	 * 
	 */
	private final WindowRegistry windowRegistry;

    /**
     * Create BrowserPlatform.
     * 
     * @param profile
     *            A profile to open.
     */
	private BrowserPlatform(final Profile profile, final Environment environment) {
        new BrowserPlatformInitializer(profile, environment).initialize();
        this.applicationFactory = ApplicationFactory.getInstance(this);
		this.applicationRegistry = new ApplicationRegistry();
		this.avatarRegistry = new AvatarRegistry();
        this.environment = environment;
		this.windowRegistry = new WindowRegistry();
		this.modelFactory = ModelFactory.getInstance();
		this.preferences = modelFactory.getPreferences(getClass());

		this.logger = new Log4JWrapper();
		this.persistence = new BrowserPlatformPersistence(this);

        this.firstRunHelper = new FirstRunHelper(this);
        this.listenerHelper = new ListenerHelper(this);
        this.onlineHelper = new OnlineHelper(this);
        this.pluginHelper = new PluginHelper(this);
        this.updateHelper = new UpdateHelper(this);
	}

	/**
     * @see com.thinkparity.ophelia.browser.platform.Platform#addListener(com.thinkparity.ophelia.browser.platform.event.LifeCycleListener)
     */
    public void addListener(final LifeCycleListener listener) {
        listenerHelper.addListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.Platform#end()
     * 
     */
    public void end() {
        notifyLifeCycleEnding();
        logApiId();
        endApplications();
        endPlugins();
        notifyLifeCycleEnded();
    }

	/**
	 * @see com.thinkparity.ophelia.browser.platform.Platform#getAvatarRegistry()
	 * 
	 */
	public AvatarRegistry getAvatarRegistry() { return avatarRegistry; }

    /**
     * @see com.thinkparity.ophelia.browser.platform.Platform#getEnvironment()
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.Platform#getLogger(java.lang.Class)
	 * 
	 */
	public Logger getLogger(final Class clasz) {
		return Logger.getLogger(clasz);
	}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.Platform#getModelFactory()
	 * 
	 */
	public ModelFactory getModelFactory() { return modelFactory; }

	/**
	 * @see com.thinkparity.ophelia.browser.platform.Platform#getPersistence()
	 * 
	 */
	public BrowserPlatformPersistence getPersistence() {
		return persistence;
	}

	/**
     * @see com.thinkparity.ophelia.browser.platform.Platform#getPluginRegistry()
     */
    public PluginRegistry getPluginRegistry() {
//        return pluginHelper.getRegistry();
        return null;
    }

	/**
	 * @see com.thinkparity.ophelia.browser.platform.Platform#getPreferences()
	 * 
	 */
	public Preferences getPreferences() { return preferences; }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.Platform#getWindowRegistry()
	 * 
	 */
	public WindowRegistry getWindowRegistry() { return windowRegistry; }

	/**
	 * @see com.thinkparity.ophelia.browser.platform.Platform#hibernate(com.thinkparity.ophelia.browser.platform.application.ApplicationId)
	 * 
	 */
	public void hibernate(final ApplicationId applicationId) {
		applicationRegistry.get(applicationId).hibernate(this);
	}

	/** @see com.thinkparity.ophelia.browser.platform.Platform#isDevelopmentMode() */
	public Boolean isDevelopmentMode() { return Version.getMode() == Mode.DEVELOPMENT; }

	/** @see com.thinkparity.ophelia.browser.platform.Platform#isOnline() */
    public Boolean isOnline() {
        return onlineHelper.isOnline();
    }

    /** @see com.thinkparity.ophelia.browser.platform.Platform#isTestingMode() */
	public Boolean isTestingMode() {
		if(isDevelopmentMode()) { return Boolean.TRUE; }
		return Version.getMode() == Mode.TESTING;
	}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.application.ApplicationListener#notifyEnd(com.thinkparity.ophelia.browser.platform.application.Application)
	 * 
	 */
	public void notifyEnd(final Application application) {
        logApiId();
        logVariable("application", application);
	}

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.ApplicationListener#notifyHibernate(com.thinkparity.ophelia.browser.platform.application.Application)
	 * 
	 */
	public void notifyHibernate(final Application application) {
		logApiId();
        logVariable("application", application);
	}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.application.ApplicationListener#notifyRestore(com.thinkparity.ophelia.browser.platform.application.Application)
	 * 
	 */
	public void notifyRestore(final Application application) {
        logApiId();
        logVariable("application", application);
	}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.application.ApplicationListener#notifyStart(com.thinkparity.ophelia.browser.platform.application.Application)
	 * 
	 */
	public void notifyStart(final Application application) {
        logApiId();
        logVariable("application", application);
	}

    /**
     * @see com.thinkparity.ophelia.browser.platform.Platform#removeListener(com.thinkparity.ophelia.browser.platform.event.LifeCycleListener)
     */
    public void removeListener(final LifeCycleListener listener) {
        listenerHelper.addListener(listener);
    }

    /** @see com.thinkparity.ophelia.browser.platform.Platform#restart() */
    public void restart() { restart(System.getProperties()); }

    /** @see com.thinkparity.ophelia.browser.platform.Platform#restart(java.util.Properties) */
    public void restart(final Properties properties) {
        logApiId();
        logVariable("properties", properties);

        // attach a process to the jvm shutdown
        final List<String> commands = new ArrayList<String>();
        commands.add(Java.EXECUTABLE);
        if(properties.containsKey("parity.image.name"))
            commands.add(Java.OPTION_PARITY_IMAGE.format(new Object[] {properties.getProperty("parity.image.name")}));
        commands.add(Java.OPTION_PARITY_INSTALL.format(new Object[] {Directories.PARITY_INSTALL.getAbsolutePath()}));
        Assert.assertNotYetImplemented("BrowserPlatform#restart");
        commands.add(Java.OPTION_CLASS_PATH);
        commands.add(Java.OPTION_CLASS_PATH_VALUE);
        commands.add(Java.MAIN_CLASS);
        for (final String command : commands) {
            logVariable("command", command);
        }

        final ProcessBuilder pb = new ProcessBuilder();
        pb.command(commands);
        logVariable("Directories.PARITY_INSTALL", Directories.PARITY_INSTALL);
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
	 * @see com.thinkparity.ophelia.browser.platform.Platform#restore(com.thinkparity.ophelia.browser.platform.application.ApplicationId)
	 * 
	 */
	public void restore(final ApplicationId applicationId) {
		logApiId();
        logVariable("applicationId", applicationId);
		applicationRegistry.get(applicationId).restore(this);
	}

    /**
     * @see com.thinkparity.ophelia.browser.platform.Platform#start()
     * 
     */
	public void start() {
	    notifyLifeCycleStarting();
        logApiId();
        if (isUpdateAvailable()) {
            update();
        } else {
            if (isFirstRun()) {
                if (firstRun()) {
                    startPlugins();
                    startApplications();
                }
            } else {
                startPlugins();
                startApplications();
            }
        }
        notifyLifeCycleStarted();
	}

    /** Update the browser. */
    public void update() {
        logApiId();
        updateHelper.update();
    }

    /**
     * @see Log4JWrapper#logApiId()
     */
    protected final void logApiId() {
        logger.logApiId();
    }

    /**
     * @see Log4JWrapper#logApiId()
     */
    protected final <V> V logVariable(final String name, final V value) {
        return logger.logVariable(name, value);
    }

    /**
     * End all applications.
     *
     */
    private void endApplications() {
        for (final ApplicationId id : ApplicationId.values()) {
            if(applicationRegistry.contains(id))
                applicationRegistry.get(id).end(this);
        }
    }

    /**
     * End all plugins.
     *
     */
    private void endPlugins() {
        pluginHelper.end();
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
    private Boolean isUpdateAvailable() { return Boolean.FALSE; }

	private void notifyLifeCycleEnded() {
        final LifeCycleEvent e = listenerHelper.createEvent();
        listenerHelper.notifyListeners(new EventNotifier<LifeCycleListener>() {
            public void notifyListener(final LifeCycleListener listener) {
                listener.ended(e);
            }
        });
    }

    private void notifyLifeCycleEnding() {
        final LifeCycleEvent e = listenerHelper.createEvent();
        listenerHelper.notifyListeners(new EventNotifier<LifeCycleListener>() {
            public void notifyListener(final LifeCycleListener listener) {
                listener.ending(e);
            }
        });
    }

    private void notifyLifeCycleStarted() {
        final LifeCycleEvent e = listenerHelper.createEvent();
        listenerHelper.notifyListeners(new EventNotifier<LifeCycleListener>() {
            public void notifyListener(final LifeCycleListener listener) {
                listener.started(e);
            }
        });
    }

    private void notifyLifeCycleStarting() {
        final LifeCycleEvent e = listenerHelper.createEvent();
        listenerHelper.notifyListeners(new EventNotifier<LifeCycleListener>() {
            public void notifyListener(final LifeCycleListener listener) {
                listener.starting(e);
            }
        });
    }

    /**
     * Start all applications.
     *
     */
    private void startApplications() {
        applicationFactory.create(ApplicationId.BROWSER).start(this);
        applicationFactory.create(ApplicationId.SYSTEM).start(this);
        applicationFactory.create(ApplicationId.SESSION).start(this);
    }

    /**
     * Start all plugins.
     *
     */
    private void startPlugins() {
        pluginHelper.start();
    }


}
