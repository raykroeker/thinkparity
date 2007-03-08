/*
 * Created On: Feb 4, 2006
 */
package com.thinkparity.ophelia.browser.platform;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.sort.StringComparator;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.Constants.Directories;
import com.thinkparity.ophelia.browser.Constants.Java;
import com.thinkparity.ophelia.browser.Constants.PropertyNames;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionInvocation;
import com.thinkparity.ophelia.browser.platform.action.ActionRegistry;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.action.platform.Login;
import com.thinkparity.ophelia.browser.platform.application.Application;
import com.thinkparity.ophelia.browser.platform.application.ApplicationFactory;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.platform.application.window.WindowRegistry;
import com.thinkparity.ophelia.browser.platform.event.LifeCycleEvent;
import com.thinkparity.ophelia.browser.platform.event.LifeCycleListener;
import com.thinkparity.ophelia.browser.platform.firstrun.FirstRunHelper;
import com.thinkparity.ophelia.browser.platform.migrator.MigratorHelper;
import com.thinkparity.ophelia.browser.platform.online.OnlineHelper;
import com.thinkparity.ophelia.browser.platform.plugin.PluginHelper;
import com.thinkparity.ophelia.browser.platform.plugin.PluginRegistry;
import com.thinkparity.ophelia.browser.profile.Profile;
import com.thinkparity.ophelia.browser.util.ModelFactory;

import org.apache.log4j.Logger;

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
     * @param environment
     *            The <code>Environment</code> to connect to.
     * @param profile
     *            The platform <code>Profile</code> to open.
     * 
     * @return The platform.
     */
    public static Platform create(final Mode mode,
            final Environment environment, final Profile profile) {
        Assert.assertIsNull("The platform has already been created.", SINGLETON);
        SINGLETON = new BrowserPlatform(mode, environment, profile);
        return BrowserPlatform.getInstance();
    }

    /**
     * Obtain the platform instance.
     * 
     * @return The thinkParity platform.
     */
    public static Platform getInstance() {
        Assert.assertNotNull("The platform has not yet been created.", SINGLETON);
        return SINGLETON;
    }

    /** The platform's log4j wrapper. */
	protected final Log4JWrapper logger;

    /** Action registry. */
    private final ActionRegistry actionRegistry;

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

	/** The platform migrator helper. */
    private final MigratorHelper migratorHelper;

    /** A thinkParity <code>Mode</code>. */
    private final Mode mode;

    /** The parity model factory. */
	private final ModelFactory modelFactory;

	/** The platform online helper. */
    private final OnlineHelper onlineHelper;

	/** The platform settings. */
	private final BrowserPlatformPersistence persistence;

	/** The platform plugin helper. */
    private final PluginHelper pluginHelper;

	/** The thinkParity <code>WindowRegistry</code>. */
	private final WindowRegistry windowRegistry;

    /** A thinkParity <code>Workspace</code>. */
    private final Workspace workspace;

    /**
     * Create BrowserPlatform.
     * 
     * @param profile
     *            A profile to open.
     */
	private BrowserPlatform(final Mode mode, final Environment environment,
            final Profile profile) {
        super();
        this.environment = environment;
        this.mode = mode;

        this.workspace = WorkspaceModel.getInstance(
                environment).getWorkspace(new File(profile.getParityWorkspace()));
        new BrowserPlatformInitializer(this).initialize(workspace);
        this.actionRegistry = new ActionRegistry();
        this.applicationFactory = ApplicationFactory.getInstance(this);
		this.applicationRegistry = new ApplicationRegistry();
		this.avatarRegistry = new AvatarRegistry();
		this.windowRegistry = new WindowRegistry();
		this.modelFactory = ModelFactory.getInstance();

		this.logger = new Log4JWrapper();
		this.persistence = new BrowserPlatformPersistence(this);

        this.firstRunHelper = new FirstRunHelper(this);
        this.listenerHelper = new ListenerHelper(this);
        this.onlineHelper = new OnlineHelper(this);
        this.pluginHelper = new PluginHelper(this);
        this.migratorHelper = new MigratorHelper(this);
	}

    /**
     * @see com.thinkparity.ophelia.browser.platform.Platform#addListener(com.thinkparity.ophelia.browser.platform.event.LifeCycleListener)
     */
    public void addListener(final LifeCycleListener listener) {
        listenerHelper.addListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.Platform#createTempFile(java.lang.String)
     */
    public File createTempFile(final String suffix) throws IOException {
        return workspace.createTempFile(suffix);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.Platform#end()
     * 
     */
    public void end() {
        notifyLifeCycleEnding();
        endApplications();
        endPlugins();
        closeWorkspace();
        notifyLifeCycleEnded();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.Platform#getAvailableLocales()
     *
     */
    public Locale[] getAvailableLocales() {
        final Locale[] locales = Locale.getAvailableLocales();
        final List<Locale> availableLocales = new ArrayList<Locale>(locales.length);
        boolean didHit = false;
        for (final Locale locale : locales) {
            didHit = false;
            for (final Locale availableLocale : availableLocales) {
                if (availableLocale.getCountry().equals(locale.getCountry())) {
                    didHit = true;
                    break;
                }
            }
            if (!didHit)
                availableLocales.add(locale);
        }
        Collections.sort(availableLocales, new Comparator<Locale>() {
            final StringComparator stringComparator =
                new StringComparator(Boolean.TRUE);
            public int compare(final Locale o1, final Locale o2) {
                return stringComparator.compare(o1.getDisplayCountry(), o2.getDisplayCountry());
            }
        });
        return availableLocales.toArray(new Locale[] {});
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.Platform#getAvailableTimeZones()
     *
     */
    public TimeZone[] getAvailableTimeZones() {
        final String[] timeZoneIDs = TimeZone.getAvailableIDs();
        final List<TimeZone> available = new ArrayList<TimeZone>(timeZoneIDs.length);
        for (final String timeZoneID : timeZoneIDs) {
            available.add(TimeZone.getTimeZone(timeZoneID));
        }
        final List<TimeZone> clone = new ArrayList<TimeZone>(available.size());
        clone.addAll(available);
        FilterManager.filter(available, new Filter<TimeZone>() {
            public Boolean doFilter(final TimeZone o) {
                for (final TimeZone timeZone : clone) {
                    if (o.hasSameRules(timeZone))
                        return Boolean.FALSE;
                }
                return Boolean.TRUE;
            }
        });
        Collections.sort(available, new Comparator<TimeZone>() {
            final StringComparator stringComparator =
                new StringComparator(Boolean.TRUE);
            public int compare(final TimeZone o1, final TimeZone o2) {
                return stringComparator.compare(o1.getDisplayName(),
                        o2.getDisplayName());
            }
        });
        return available.toArray(new TimeZone[] {});
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
     * @see com.thinkparity.ophelia.browser.platform.Platform#getLocale()
     *
     */
    public Locale getLocale() {
        return persistence.getLocale();
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
     * @see com.thinkparity.ophelia.browser.platform.Platform#getTimeZone()
     *
     */
    public TimeZone getTimeZone() {
        return persistence.getTimeZone();
    }

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

	/**
     * @see com.thinkparity.ophelia.browser.platform.Platform#initializeWorkspace(com.thinkparity.ophelia.model.util.ProcessMonitor,
     *      com.thinkparity.ophelia.model.workspace.Workspace,
     *      com.thinkparity.codebase.model.session.Credentials)
     * 
     */
    public void initializeWorkspace(final ProcessMonitor monitor,
            final Workspace workspace, final Credentials credentials)
            throws InvalidCredentialsException {
        WorkspaceModel.getInstance(environment).initialize(monitor, workspace,
                credentials);
    }

    /** @see com.thinkparity.ophelia.browser.platform.Platform#isDevelopmentMode() */
	public Boolean isDevelopmentMode() {
        switch (mode) {
        case DEMO:
        case PRODUCTION:
        case TESTING:
            return Boolean.FALSE;
        case DEVELOPMENT:
            return Boolean.TRUE;
        default:
            throw Assert.createUnreachable("UNKNOWN MODE");
        }
    }

	/** @see com.thinkparity.ophelia.browser.platform.Platform#isOnline() */
    public Boolean isOnline() {
        return onlineHelper.isOnline();
    }

	/**
     * @see com.thinkparity.ophelia.browser.platform.Platform#isSignUpAvailable()
     */
    @Deprecated
    public Boolean isSignUpAvailable() {
        return getModelFactory().getProfileModel(getClass()).isSignUpAvailable();
    }

    /** @see com.thinkparity.ophelia.browser.platform.Platform#isTestingMode() */
	public Boolean isTestingMode() {
        switch (mode) {
        case DEMO:
        case DEVELOPMENT:
        case PRODUCTION:
            return Boolean.FALSE;
        case TESTING:
            return Boolean.TRUE;
        default:
            throw Assert.createUnreachable("UNKNOWN MODE");
        }
	}

	/**
     * @see com.thinkparity.ophelia.browser.platform.Platform#isWorkspaceInitialized(com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    public Boolean isWorkspaceInitialized(final Workspace workspace) {
        return WorkspaceModel.getInstance(environment).isInitialized(workspace);
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

    /**
     * @see com.thinkparity.ophelia.browser.platform.Platform#restart()
     *
     */
    public void restart() {
        restart(System.getProperties());
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.Platform#restart(java.util.Properties)
     *
     */
    public void restart(final Properties properties) {
        logger.logApiId();
        logger.logVariable("properties", properties);
        // attach a process to the jvm shutdown
        final List<String> commands = new ArrayList<String>();
        commands.add(Java.EXECUTABLE);
        if(properties.containsKey("parity.image.name"))
            commands.add(Java.OPTION_PARITY_IMAGE.format(new Object[] {properties.getProperty("parity.image.name")}));
        commands.add(Java.OPTION_PARITY_INSTALL.format(new Object[] {PropertyNames.ThinkParity.DIR, Directories.ThinkParity.DIR.getAbsolutePath()}));
        commands.add(Java.OPTION_CLASS_PATH);
        commands.add(Java.OPTION_CLASS_PATH_VALUE);
        commands.add(Java.MAIN_CLASS);
        for (final String command : commands) {
            logger.logVariable("command", command);
        }
        final ProcessBuilder pb = new ProcessBuilder();
        pb.command(commands);
        pb.directory(Directories.ThinkParity.DIR);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    pb.start();
                } catch (final IOException iox) {
                    throw new BrowserException("Could not restart browser platform.", iox);
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
     * @see com.thinkparity.ophelia.browser.platform.Platform#runLogin(java.lang.String, java.lang.String, com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor)
     */
    public void runLogin(final String username, final String password, final ThinkParitySwingMonitor monitor) {
        final Data data = new Data(3);
        data.set(Login.DataKey.MONITOR, monitor);
        data.set(Login.DataKey.PASSWORD, password);
        data.set(Login.DataKey.USERNAME, username);
        invoke(ActionId.PLATFORM_LOGIN, data);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.Platform#runResetPassword()
     */
    public void runResetPassword() {
        invoke(ActionId.PLATFORM_RESET_PASSWORD, Data.emptyData());
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.Platform#start()
     * 
     */
	public void start() {
	    notifyLifeCycleStarting();
        logApiId();
        if (!isWorkspaceInitialized()) {
            initializeWorkspace();
        } 
        if (!isWorkspaceInitialized()) {
            deleteWorkspace();
            end();
        } else {
            if (isMigrationPossible()) {
                migrate();
                restart();
            } else {
                startPlugins();
                startApplications();
            }
        notifyLifeCycleStarted();
        }
	}

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
     * Close the workspace.
     *
     */
    private void closeWorkspace() {
        WorkspaceModel.getInstance(environment).close(
                modelFactory.getWorkspace(getClass()));
    }

    /**
     * Delete the workspace.
     *
     */
    private void deleteWorkspace() {
        WorkspaceModel.getInstance(environment).delete(
                modelFactory.getWorkspace(getClass()));
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

    /**
     * Obtain the action from the controller's cache. If the action does not
     * exist in the cache it is created and stored.
     * 
     * @param id
     *            The action id.
     * @return The action.
     * 
     * @see ActionId
     */
    private ActionInvocation getAction(final ActionId id) {
        if (actionRegistry.contains(id)) {
            return actionRegistry.get(id);
        } else {
            return ActionFactory.create(id);
        }
    }

    /**
     * Perform first run initialization.
     * 
     */
    private void initializeWorkspace() {
        firstRunHelper.firstRun();
    }

    /**
     * Invoke an action.
     * 
     * @param actionId
     *            The action id.
     * @param data
     *            The action data.
     */
    private void invoke(final ActionId actionId, final Data data) {
        try {
            getAction(actionId).invokeAction(data);
        } catch(final Throwable t) {
            logger.logError(t, "Could not invoke action {0} with data {1}.", actionId, data);
            throw new BrowserException("Could not invoke platform action.", t);
        }
    }

    /**
     * Determine if an update is available.
     * 
     * @return True if an update is available.
     */
    private boolean isMigrationPossible() {
        return migratorHelper.isMigrationPossible().booleanValue();
    }

    /**
     * Determine if the workspace has been initialized.
     * 
     * @return True if the workspace has been initialized.
     */
    private Boolean isWorkspaceInitialized() {
        return WorkspaceModel.getInstance(environment).isInitialized(
                modelFactory.getWorkspace(getClass()));
    }

    /**
     * Migrate.
     * 
     */
    private void migrate() {
        migratorHelper.migrate();
    }

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
        applicationFactory.create(ApplicationId.SYSTEM).start(this);
        applicationFactory.create(ApplicationId.BROWSER).start(this);
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
