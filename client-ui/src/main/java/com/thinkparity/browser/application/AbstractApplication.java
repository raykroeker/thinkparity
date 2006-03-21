/*
 * Feb 4, 2006
 */
package com.thinkparity.browser.application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.avatar.AvatarFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.application.Application;
import com.thinkparity.browser.platform.application.ApplicationListener;
import com.thinkparity.browser.platform.application.ApplicationStatus;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.l10n.ApplicationL18n;
import com.thinkparity.browser.platform.util.persistence.Persistence;
import com.thinkparity.browser.platform.util.persistence.PersistenceFactory;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.l10n.L18n;
import com.thinkparity.codebase.l10n.L18nContext;

import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractApplication implements Application {

	/**
	 * Application listeners.
	 * 
	 */
	private static final Map<Class, Set<ApplicationListener>> APPLICATION_LISTENERS;

	static {
		APPLICATION_LISTENERS = new HashMap<Class, Set<ApplicationListener>>();
	}

	/**
	 * Application localization.
	 * 
	 */
	protected final L18n l18n;

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * The current application status.
	 * 
	 * @see #AbstractApplication(Platform, L18nContext)
	 * @see #getStatus()
	 * @see #setStatus(ApplicationStatus)
	 */
	private ApplicationStatus status;

	private final AvatarRegistry avatarRegistry;

	/**
	 * Provides preferences persistence for the application.
	 * 
	 */
	private final Persistence persistence;

	/**
	 * The platform.
	 * 
	 */
	private final Platform platform;

	/**
	 * Create an AbstractApplication.
	 * 
	 */
	protected AbstractApplication(final Platform platform,
			final L18nContext l18nContext) {
		super();
		this.avatarRegistry = new AvatarRegistry();
		this.l18n = new ApplicationL18n(l18nContext);
		this.logger = platform.getLogger(getClass());
		this.persistence = PersistenceFactory.getPersistence(getClass());
		this.platform = platform;
		this.status = ApplicationStatus.NEW;
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#addListener(com.thinkparity.browser.platform.application.ApplicationListener)
	 * 
	 */
	public void addListener(final ApplicationListener l) {
		Assert.assertNotNull("Cannot add a null listener.", l);
		synchronized(APPLICATION_LISTENERS) {
			final Set<ApplicationListener> listeners = getListeners();
			Assert.assertNotTrue(
					"Cannot re-add a listener.",
					listeners.contains(l));
			listeners.add(l);
			APPLICATION_LISTENERS.put(getClass(), listeners);
		}
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
	 * Obtain the parity session interface.
	 * 
	 * @return The parity session interface.
	 */
	public SessionModel getSessionModel() {
		return platform.getModelFactory().getSessionModel(getClass());
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#getStatus()
	 * 
	 */
	public ApplicationStatus getStatus() { return status; }

	/**
	 * @see com.thinkparity.browser.platform.application.Application#removeListener(com.thinkparity.browser.platform.application.ApplicationListener)
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
	 * Check the registry for the avatar; if it does not exist create it;
	 * otherwise just return it.
	 * 
	 * @param id
	 *            The avatar id.
	 * @return The avatar.
	 */
	protected Avatar getAvatar(final AvatarId id) {
		if(avatarRegistry.contains(id)) { return avatarRegistry.get(id); }
		else { return AvatarFactory.create(id); }
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

	protected String getString(final String localKey) {
		return l18n.getString(localKey);
	}

	protected String getString(final String localKey, final Object[] arguments) {
		return l18n.getString(localKey, arguments);
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
