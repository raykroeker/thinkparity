/*
 * Created On: Feb 4, 2006
 */
package com.thinkparity.ophelia.browser.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.l10n.L18nContext;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.backup.BackupModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.events.ContactListener;
import com.thinkparity.ophelia.model.events.ProfileListener;
import com.thinkparity.ophelia.model.migrator.MigratorModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.SessionModel;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.application.Application;
import com.thinkparity.ophelia.browser.platform.application.ApplicationListener;
import com.thinkparity.ophelia.browser.platform.application.ApplicationStatus;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.persistence.Persistence;
import com.thinkparity.ophelia.browser.platform.util.persistence.PersistenceFactory;
import com.thinkparity.ophelia.browser.util.localization.BrowserLocalization;
import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractApplication implements Application {

	/** Application listeners. */
	private static final Map<Class<?>, Set<ApplicationListener>> APPLICATION_LISTENERS;

    /** A list of stack filters for {@link #logApiId()}. */
	private static final List<StackUtil.Filter> LOG_API_ID_STACK_FILTERS;

    static {
		APPLICATION_LISTENERS = new HashMap<Class<?>, Set<ApplicationListener>>();
        LOG_API_ID_STACK_FILTERS = new ArrayList<StackUtil.Filter>(1);
        LOG_API_ID_STACK_FILTERS.add(new StackUtil.Filter() {
            public Boolean accept(final StackTraceElement stackElement) {
                return !stackElement.getMethodName().equals("logApiId");
            }
        });
        LOG_API_ID_STACK_FILTERS.add(new StackUtil.Filter() {
            public Boolean accept(final StackTraceElement stackElement) {
                return !(stackElement.getClassName().equals(AbstractApplication.class.getName()) &&
                        stackElement.getMethodName().equals("getCallerFrame"));
            }
        });
	}

    /** Application localization. */
	protected final Localization localization;

	/** An apache logger. */
	protected final Log4JWrapper logger;

	/** An avatar registry. */
	private final AvatarRegistry avatarRegistry;

    /** Application persistence. */
	private final Persistence persistence;

	/** The browser platform. */
	private final Platform platform;

	/** A thinkParity user's profile. */
    private Profile profile;

	/**
	 * The current application status.
	 * 
	 * @see #AbstractApplication(Platform, L18nContext)
	 * @see #getStatus()
	 * @see #setStatus(ApplicationStatus)
	 */
	private ApplicationStatus status;

	/**
	 * Create an AbstractApplication.
	 * 
	 */
	protected AbstractApplication(final Platform platform,
			final L18nContext l18nContext) {
		super();
		this.avatarRegistry = new AvatarRegistry();
		this.localization = new BrowserLocalization(l18nContext);
		this.logger = new Log4JWrapper();
		this.persistence = PersistenceFactory.getPersistence(getClass());
		this.platform = platform;
		this.status = ApplicationStatus.NEW;
	}

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#addListener(com.thinkparity.ophelia.browser.platform.application.ApplicationListener)
	 * 
	 */
	public void addListener(final ApplicationListener l) {
		Assert.assertNotNull(
                "[LBROWSER] [PLATFORM] [APPLICATION] [ADD LISTENER] [CANNOT ADD NULL LISTENER]", l);
		synchronized(APPLICATION_LISTENERS) {
			final Set<ApplicationListener> listeners = getListeners();
			Assert.assertNotTrue(
					"[LBROWSER] [PLATFORM] [APPLICATION] [ADD LISTENER] [CANNOT RE-ADD LISTENER]",
					listeners.contains(l));
			listeners.add(l);
			APPLICATION_LISTENERS.put(getClass(), listeners);
		}
	}

	/**
     * Add a contact listener.
     * 
     * @param contactListener
     *            A contact listener.
     */
    public void addListener(final ContactListener listener) {
        platform.getModelFactory().getContactModel(getClass()).addListener(listener);
    }

    /**
     * Add a profile listener.
     * 
     * @param listener
     *            A <code>ProfileListener</code>.
     */
    public void addListener(final ProfileListener listener) {
        platform.getModelFactory().getProfileModel(getClass()).addListener(listener);
    }

    /**
     * Obtain the parity artifact interface.
     * 
     * @return The parity artifact interface.
     */
    public ArtifactModel getArtifactModel() {
        return platform.getModelFactory().getArtifactModel(getClass());
    }

    /**
     * Obtain a backup model.
     * 
     * @return An instance of <code>BackupModel</code>.
     */
    public BackupModel getBackupModel() {
        return platform.getModelFactory().getBackupModel(getClass());
    }

    /**
     * Obtain the thinkParity contact model.
     * 
     * @return A <code>ContactModel</code>.
     */
    public ContactModel getContactModel() {
        return platform.getModelFactory().getContactModel(getClass());
    }

    /**
     * Obtain the parity container interface.
     * 
     * @return The parity container interface.
     */
    public ContainerModel getContainerModel() {
        return platform.getModelFactory().getContainerModel(getClass());
    }

    /**
	 * Obtain the parity document interface.
	 * 
	 * @return The parity document interface.
	 */
	public DocumentModel getDocumentModel() { 
		return platform.getModelFactory().getDocumentModel(getClass());
	}

    /**
     * Obtain an instance of migrator model.
     * 
     * @return An instance of <code>MigratorModel</code>.
     */
    public MigratorModel getMigratorModel() {
        return platform.getModelFactory().getMigratorModel(getClass());
    }

    /**
     * Obtain an instance of profile model.
     * 
     * @return An instance of <code>ProfileModel</code>.
     */
    public ProfileModel getProfileModel() {
        return platform.getModelFactory().getProfileModel(getClass());
    }

	/**
	 * Obtain the parity session interface.
	 * 
	 * @return The parity session interface.
	 */
	public SessionModel getSessionModel() {
		return platform.getModelFactory().getSessionModel(getClass());
	}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#getStatus()
	 * 
	 */
	public ApplicationStatus getStatus() { return status; }

	/**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#removeListener(com.thinkparity.ophelia.browser.platform.application.ApplicationListener)
	 * 
	 */
	public void removeListener(ApplicationListener l) {
		synchronized(APPLICATION_LISTENERS) {
			final Set<ApplicationListener> listeners = getListeners();
			Assert.assertTrue(
					"Cannot re-remove a listener.",
					listeners.contains(l));
			listeners.remove(l);
			APPLICATION_LISTENERS.put(getClass(), listeners);
		}
	}

    /**
     * Remove a contact listener.
     * 
     * @param contactListener
     *            A contact listener.
     */
    public void removeListener(final ContactListener listener) {
        platform.getModelFactory().getContactModel(getClass()).removeListener(listener);
    }

	/**
     * Remove a profile listener.
     * 
     * @param profileListener
     *            A profile listener.
     */
    public void removeListener(final ProfileListener listener) {
        platform.getModelFactory().getProfileModel(getClass()).removeListener(listener);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#setProfile(com.thinkparity.codebase.model.profile.Profile)
     */
    public void setProfile(final Profile profile) {
        Assert.assertIsNull("PROFILE ALREADY SET", this.profile);
        this.profile = profile;
    }

	/**
	 * Assert the status change is valid.
	 * 
	 * @param status
	 *            The status to change to.
	 */
	protected void assertStatusChange(final ApplicationStatus status) {
		final ApplicationStatus[] acceptableStatus;
		switch(this.status) {
		case NEW:
			acceptableStatus = new ApplicationStatus[] {
				ApplicationStatus.STARTING
			};
			break;
		case STARTING:
			acceptableStatus = new ApplicationStatus[] {
				ApplicationStatus.RUNNING
			};
			break;
		case RUNNING:
			acceptableStatus = new ApplicationStatus[] {
				ApplicationStatus.ENDING, ApplicationStatus.HIBERNATING
			};
			break;
		case HIBERNATING:
			acceptableStatus = new ApplicationStatus[] {
				ApplicationStatus.ENDING, ApplicationStatus.RESTORING
			};
			break;
		case RESTORING:
			acceptableStatus = new ApplicationStatus[] {
				ApplicationStatus.RUNNING
			};
			break;
		case ENDING:
			acceptableStatus = new ApplicationStatus[] {};
			break;
		default:
			throw Assert.createUnreachable(
					"Unknown application status:  " + status);
		}
		assertIsOneOf(acceptableStatus, status);
	}

	/**
     * Debug a variable.
     * 
     * @param name
     *            The variable name.
     * @param value
     *            The variable value.
     */
    protected <V> V debugVariable(final String name, final V value) {
        return logger.logVariable(name, value);
    }

	/**
	 * Check the registry for the avatar; if it does not exist create it;
	 * otherwise just return it.
	 * 
	 * @param id
	 *            The avatar id.
	 * @return The avatar.
	 */
	protected Avatar getAvatar(final AvatarId id) {
		if (avatarRegistry.contains(id)) {
            return avatarRegistry.get(id);
		} else {
            return AvatarFactory.create(id);
		}
	}

    /**
     * Obtain the build id.
     * 
     * @return The build id <code>String</code>.
     */
    protected String getBuildId() {
        return platform.getBuildId();
    }

	/**
	 * Obtain the platform.
	 * 
	 * @return The platform.
	 */
	protected Platform getPlatform() { return platform; }

	protected Boolean getPref(final String key, final Boolean defaultValue) {
		return persistence.get(key, defaultValue);
	}

    protected String getPref(final String key, final String defaultValue) {
		return persistence.get(key, defaultValue);
	}

    /**
     * Obtain the profile
     *
     * @return The Profile.
     */
    protected Profile getProfile() {
        return profile;
    }

    /**
     * Obtain the release name.
     * 
     * @return The release name <code>String</code>.
     */
    protected String getReleaseName() {
        return platform.getReleaseName();
    }

    protected String getString(final String localKey) {
		return localization.getString(localKey);
	}

    protected String getString(final String localKey, final Object[] arguments) {
		return localization.getString(localKey, arguments);
	}

    /** Log an api id. */
    protected final void logApiId() {
        logger.logApiId();
    }

	/**
     * Log an api id.
     * 
     * @param message
     *            A message to log.
     */
    protected final void logApiId(final Object message) {
        logger.logApiId();
    }

    /**
     * Log an error.
     * 
     * @param message
     *            An error message.
     * @param t
     *            The cause of the error.
     */
    protected <E extends Throwable> E logError(final E e,
            final String errorPattern, final Object... errorArguments) {
        return logger.logError(e, errorPattern, errorArguments);
    }



	/**
     * Log a variable. Note that only the variable value will be rendered.
     * 
     * @param name
     *            The variable name.
     * @param value
     *            The variable value.
     * @return The variable.
     */
    protected final <V> V logVariable(final String name, final V value) {
        return logger.logVariable(name, value);
    }

    /**
     * Log a warning.
     * 
     * @param message
     *            A warning message.
     */
    protected final void logWarn(final Object warningArgument) {
        logger.logWarning("{0}", warningArgument);
    }

    /**
     * Log a warning.
     * 
     * @param pattern
     *            A warning pattern.
     * @param arguments
     *            Warning arguments.
     */
    protected final void logWarn(final String warningPattern,
            final Object... warningArguments) {
        logger.logWarning(warningPattern, warningArguments);
    }

    /**
     * Log a warning.
     * 
     * @param message
     *            A warning message.
     */
    protected final <W extends Throwable> W logWarn(final W w,
            final String warningPattern, final Object... warningArguments) {
        return logger.logWarning(w, warningPattern, warningArguments);
    }

	/**
	 * Notify all application listeners that the application has ended.
	 *
	 */
	protected void notifyEnd() {
		synchronized(APPLICATION_LISTENERS) {
			final Set<ApplicationListener> listeners = getListeners();
			for(final ApplicationListener l : listeners) {
				l.notifyEnd(this);
			}
		}
	}

	/**
	 * Notify all application listeners that the application has hibernated.
	 *
	 */
	protected void notifyHibernate() {
		synchronized(APPLICATION_LISTENERS) {
			final Set<ApplicationListener> listeners = getListeners();
			for(final ApplicationListener l : listeners) {
				l.notifyHibernate(this);
			}
		}
	}

	/**
	 * Notify all application listeners that the application has restored.
	 *
	 */
	protected void notifyRestore() {
		synchronized(APPLICATION_LISTENERS) {
			final Set<ApplicationListener> listeners = getListeners();
			for(final ApplicationListener l : listeners) {
				l.notifyRestore(this);
			}
		}
	}

	/**
	 * Notify all application listeners that the application has started.
	 *
	 */
	protected void notifyStart() {
		synchronized(APPLICATION_LISTENERS) {
			final Set<ApplicationListener> listeners = getListeners();
			for(final ApplicationListener l : listeners) {
				l.notifyStart(this);
			}
		}
	}

	protected void setPref(final String key, final Boolean value) {
		persistence.set(key, value);
	}

    protected String setPref(final String key, final String value) {
		final String p = getPref(key, (String) null);
		persistence.set(key, value);
		return p;
	}

    /**
	 * Set the status.
	 * 
	 * @param status
	 *            The new status.
	 */
	protected void setStatus(final ApplicationStatus status) {
		assertStatusChange(status);
		this.status = status;
	}

    /**
     * Translate an application error into a browser exception.
     * 
     * @param t
     *            An application error.
     * @return A browser exception.
     */
	protected BrowserException translateError(final Throwable t) {
	    if(BrowserException.class.isAssignableFrom(t.getClass())) {
            return (BrowserException) t;
        }
        else {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, errorId);
            return new BrowserException(errorId, t);
        }
    }

    private void assertIsOneOf(final ApplicationStatus[] acceptableStatus,
			final ApplicationStatus targetStatus) {
		for(final ApplicationStatus status : acceptableStatus) {
			if(status == targetStatus) { return; }
		}
		throw new IllegalArgumentException(
				"Cannot move application status from:  " +
				this.status + " to:  " + targetStatus + ".");
	}

    /**
	 * Obtain the listeners for this class from the static list. Note that the
	 * execution of this api must exist from within a synchronized code-block.
	 * 
	 * @return The set of application listeners for this class.
	 */
	private Set<ApplicationListener> getListeners() {
		final Set<ApplicationListener> listeners;
		if(APPLICATION_LISTENERS.containsKey(getClass())) {
			listeners = APPLICATION_LISTENERS.get(getClass());
		}
		else { listeners = new HashSet<ApplicationListener>(); }
		return listeners;
	}
}
