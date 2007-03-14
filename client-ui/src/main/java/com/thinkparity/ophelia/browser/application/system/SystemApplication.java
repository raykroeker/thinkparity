/*
 * Created On:  Mar 17, 2006
 */
package com.thinkparity.ophelia.browser.application.system;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.events.ContactEvent;
import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.AbstractApplication;
import com.thinkparity.ophelia.browser.application.system.notify.Notification;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionRegistry;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.Show;
import com.thinkparity.ophelia.browser.platform.action.platform.browser.Iconify;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.platform.application.ApplicationStatus;
import com.thinkparity.ophelia.browser.platform.application.L18nContext;
import com.thinkparity.ophelia.browser.platform.util.State;

import org.apache.log4j.Logger;

/**
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class SystemApplication extends AbstractApplication {

	/** The action registry. */
    private final ActionRegistry actionRegistry;

	/** The application registry. */
    private final ApplicationRegistry applicationRegistry;

	/** The system application's connection. */
    private Connection connection;

	/** The event dispatcher. */
	private EventDispatcher ed;

	/** A fuzzy date formatter. */
    private final FuzzyDateFormat fuzzyDateFormat;

	/** The application impl. */
	private SystemApplicationImpl impl;

    /**
     * Create SystemApplication.
     * 
     * @param platform
     *            A thinkParity platform.
     */
	public SystemApplication(final Platform platform) {
		super(platform, L18nContext.SYS_APP);
        this.actionRegistry = new ActionRegistry();
        this.applicationRegistry = new ApplicationRegistry();
        this.fuzzyDateFormat = new FuzzyDateFormat();
	}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#end(com.thinkparity.ophelia.browser.platform.Platform)
	 * 
	 */
	public void end(final Platform platform) {
		logApiId();

		impl.end();
		impl = null;

		ed.end();
		ed = null;

		notifyEnd();
	}

	/** @see com.thinkparity.ophelia.browser.platform.application.Application#getConnection() */
    public Connection getConnection() { return connection; }

	/**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#getId()
	 * 
	 */
	public ApplicationId getId() { return ApplicationId.SYSTEM; }

	/**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#getLogger(java.lang.Class)
     * 
     */
    public Logger getLogger(final Class clasz) {
        return getPlatform().getLogger(clasz);
    }

	public String getString(final String localKey) {
		return super.getString(localKey);
	}

	public String getString(final String localKey, final Object[] arguments) {
		return super.getString(localKey, arguments);
	}

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#hibernate(com.thinkparity.ophelia.browser.platform.Platform)
	 * 
	 */
	public void hibernate(final Platform platform) {
        logApiId();

		impl.end();
		impl = null;

		ed.end();
		ed = null;

		notifyHibernate();
	}

    /**
     * Determine whether or not the browser is running.
     * 
     * @return True if the browser is running false otherwise.
     */
    public Boolean isBrowserRunning() {
        return ApplicationStatus.RUNNING ==
            applicationRegistry.getStatus(ApplicationId.BROWSER);
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.Application#isDevelopmentMode() */
    public Boolean isDevelopmentMode() {
        return getPlatform().isDevelopmentMode();
    }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#restore(com.thinkparity.ophelia.browser.platform.Platform)
	 * 
	 */
	public void restore(final Platform platform) {
        logApiId();

		impl = new SystemApplicationImpl(this);
		impl.start();

		ed = new EventDispatcher(this);
		ed.start();

		notifyRestore();
	}

    /**
	 * @see com.thinkparity.ophelia.browser.platform.Saveable#restoreState(com.thinkparity.ophelia.browser.platform.util.State)
	 * 
	 */
	public void restoreState(final State state) {}

    /** Run the iconify action. */
    public void runIconify(final Boolean iconify) {
        final Data data = new Data(1);
        data.set(Iconify.DataKey.ICONIFY, iconify);
        runLater(ActionId.PLATFORM_BROWSER_ICONIFY, data);
    }

    /** Run the exit platform action. */
    public void runExitPlatform() {
        runLater(ActionId.PLATFORM_QUIT, Data.emptyData());
    }

    /** Run the login action. */
    public void runLogin() {
        runLater(ActionId.PLATFORM_LOGIN, Data.emptyData());
    }

    /** Run the move to front action. */
    public void runMoveBrowserToFront() {
        runLater(ActionId.PLATFORM_BROWSER_MOVE_TO_FRONT, Data.emptyData());
    }

    /** Run the restart platform action. */
    public void runRestartPlatform() {
        run(ActionId.PLATFORM_RESTART, Data.emptyData());
    }

    /** Run the restore browser action. */
	public void runRestoreBrowser() {
        runLater(ActionId.PLATFORM_BROWSER_RESTORE, Data.emptyData());
    }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.Saveable#saveState(com.thinkparity.ophelia.browser.platform.util.State)
	 * 
	 */
	public void saveState(State state) {
		throw Assert.createNotYetImplemented("System#saveState");
	}

    /**
     * Set the auto login preference.
     *
     * @param autoLogin
     *      True to auto login; false to manually login.
     */
    public void setAutoLogin(final Boolean autoLogin) {
        getPlatform().getPersistence().setAutoLogin(autoLogin);
    }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.Application#start(com.thinkparity.ophelia.browser.platform.Platform)
	 * 
	 */
	public void start(final Platform platform) {
        logApiId();

        connection = getSessionModel().isLoggedIn() ?
                Connection.ONLINE : Connection.OFFLINE;

		impl = new SystemApplicationImpl(this);
		impl.start();

		ed = new EventDispatcher(this);
		ed.start();

		notifyStart();
	}

    /**
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#getProfile()
     */
    @Override
    protected Profile getProfile() {
        return super.getProfile();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#translateError(java.lang.Throwable)
     */
    @Override
    protected BrowserException translateError(final Throwable t) {
        return super.translateError(t);
    }

    /** Notify the connection is offline. */
    void fireConnectionOffline() {
        connection = Connection.OFFLINE;
        impl.reloadConnectionStatus(connection);
    }

    /** Notify the connection is online. */
    void fireConnectionOnline() {
        connection = Connection.ONLINE;
        impl.reloadConnectionStatus(connection);
    }

    /**
     * Fire the contact incoming invitation created event.
     * 
     * @param e
     *            A <code>ContactEvent</code>.
     */
    void fireContactIncomingInvitationCreated(final ContactEvent e) {
        final Data data = new Data(1);
        data.set(Show.DataKey.INVITATION_ID, e.getIncomingInvitation().getId());
        final String name = getName(e.getIncomingInvitation().getExtendedBy());
        fireNotification(ActionId.CONTACT_SHOW, data, name,
                "Notification.ContactIncomingInvitationCreatedMessage", name);
    }

    /**
     * Fire the container published event.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    void fireContainerPublished(final ContainerEvent e) {
        final Data data = new Data(1);
        data.set(com.thinkparity.ophelia.browser.platform.action.container.Show.DataKey.CONTAINER_ID, e.getContainer().getId());
        if (null == e.getPreviousVersion()) {
            // this is the first publish event
            fireNotification(ActionId.CONTAINER_SHOW,
                    data, e.getContainer().getName(),
                    "Notification.ContainerPublishedFirstTimeMessage",
                    fuzzyDateFormat.format(e.getVersion().getUpdatedOn()),
                    getName(e.getTeamMember()));
        } else {
            // this is a subsequent publish event
            fireNotification(ActionId.CONTAINER_SHOW,
                    data, e.getContainer().getName(),
                    "Notification.ContainerPublishedNotFirstTimeMessage",
                    fuzzyDateFormat.format(e.getVersion().getUpdatedOn()),
                    getName(e.getTeamMember()));
        }
    }

    /**
     * Fire a notification message.
     * 
     * @param actionId
     *            A <code>ActionId</code>.
     * @param data
     *            The action <code>Data</code>.
     * @param title
     *            A notification title <code>String</code>.
     * @param messageKey
     *            A notification message key <code>String</code>.
     * @param messageArguments
     *            A variable length list of message argument <code>Object</code>.
     */
    private void fireNotification(final ActionId actionId,
            final Data data, final String title,
            final String messageKey, final Object... messageArguments) {
        impl.fireNotification(new Notification() {
            public String getMessage() {
                return getString(messageKey, messageArguments);
            }
            public String getTitle() {
                return title;
            }
            public void invokeAction() {
                run(actionId, data);
            }
        });
    }

    /**
     * Extract the name from the user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return The <code>User</code>'s name.
     */
    private String getName(final User user) {
        return user.getName();
    }

    /**
     * Run an action.
     * 
     * @param actionId
     *            The action id.
     * @param data
     *            The action data.
     */
    private void run(final ActionId actionId, final Data data) {
        try {
            if (actionRegistry.contains(actionId)) {
                actionRegistry.get(actionId).invokeAction(data);
            }
            else { ActionFactory.create(actionId).invokeAction(data); }
        } catch(final Throwable t) {
            logger.logError(t,
                    "Could not run system application action {0} with data {1}.",
                    actionId, data);
            throw new RuntimeException(t);
        }
    }

    /**
     * Run an action that doesn't need to be run imeediately.
     * 
     * @param actionId
     *            The action id.
     * @param data
     *            The action data.
     * @see SwingUtilities#invokeLater(java.lang.Runnable)
     * @see SystemApplication#run(ActionId, Data)
     */
    private void runLater(final ActionId actionId, final Data data) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { SystemApplication.this.run(actionId, data); }
        });
    }
}
