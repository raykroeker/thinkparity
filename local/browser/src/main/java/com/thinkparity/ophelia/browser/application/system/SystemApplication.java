/*
 * Created On:  Mar 17, 2006
 */
package com.thinkparity.ophelia.browser.application.system;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.User;



import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.AbstractApplication;
import com.thinkparity.ophelia.browser.application.system.tray.TrayNotification;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionRegistry;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.platform.application.ApplicationStatus;
import com.thinkparity.ophelia.browser.platform.application.L18nContext;
import com.thinkparity.ophelia.browser.platform.util.State;
import com.thinkparity.ophelia.browser.util.ModelUtil;
import com.thinkparity.ophelia.model.message.SystemMessage;

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
	}

    /**
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#debugVariable(java.lang.String, java.lang.Object)
     */
    @Override
    public void debugVariable(final String name, final Object value) {
        super.debugVariable(name, value);
    }

	/** Display the about dialogue. */
    public void displayAbout() {}

	/**
     * Get the auto login preference.
     *
     * @return True if auto login is set; false otherwise.
     */
    public Boolean doAutoLogin() {
        return getPlatform().getPersistence().doAutoLogin();
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
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#logApiId()
     */
    @Override
    public void logApiId() { super.logApiId(); }

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
     * @see com.thinkparity.ophelia.browser.application.AbstractApplication#logApiId(java.lang.Object)
     */
    @Override
    protected void logApiId(final Object message) { super.logApiId(message); }

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
     * Notify a document key has been closed.
     * 
     * @param document
     *            The document.
     */
    void fireDocumentClosed(final Document document) {
        fireNotification(getString(
                "Notification.DocumentClosedMessage",
                new Object[] {document.getName()}));
    }

    /**
     * Notify a document key has created.
     * 
     * @param user
     *            The originating user.
     * @param document
     *            The document.
     */
    void fireDocumentCreated(final User user, final Document document) {
        fireNotification(getString(
                "Notification.DocumentCreatedMessage",
                new Object[] {document.getName(), getName(user)}));
    }

    /**
     * Notify a document key request has been accepted.
     * 
     * @param document
     *            The document.
     */
    void fireDocumentKeyRequestAccepted(final User user, final Document document) {
        fireNotification(getString(
                "Notification.DocumentKeyRequestAcceptedMessage",
                new Object[] {getName(user), document.getName()}));
    }

    /**
     * Notify a document key request has been accepted.
     * 
     * @param document
     *            The document.
     */
    void fireDocumentKeyRequestDeclined(final User user, final Document document) {
        fireNotification(getString(
                "Notification.DocumentKeyRequestDeclinedMessage",
                new Object[] {getName(user), document.getName()}));
    }

    /**
     * Notify a document key has been requested.
     * 
     * @param document
     *            The document.
     */
    void fireDocumentKeyRequested(final User user, final Document document) {
        fireNotification(getString(
                "Notification.DocumentKeyRequestedMessage",
                new Object[] {getName(user), document.getName()}));
    }
    void fireDocumentReactivated(final User user, final Document document,
            final DocumentVersion version) {
        fireNotification(getString(
                "Notification.DocumentReactivatedMessage",
                new Object[] {document.getName(), getName(user)}));
    }

    /**
     * Notify a document team member has been added.
     *
     * @param user
     *      A user.
     * @param document
     *      A document.
     */
    void fireDocumentTeamMemberAdded(final User user, final Document document) {
        fireNotification(getString(
                "Notification.DocumentTeamMemberAddedMessage",
                new Object[] {getName(user), document.getName()}));
    }

    /**
     * Notify a document has been updated.
     * 
     * @param document
     *            The document.
     */
    void fireDocumentUpdated(final Document document) {
        fireNotification(getString(
                "Notification.DocumentUpdatedMessage",
                new Object[] {document.getName()}));
    }

    /**
     * Notify a system message has been created.
     * 
     * @param systemMessage
     *            The system message.
     */
    void fireSystemMessageCreated(final SystemMessage systemMessage) {
        fireNotification(getString(
                "Notification.SystemMessageCreatedMessage",
                new Object[] {systemMessage}));
    }

    /**
     * Notify an update.
     * 
     * @param notificationMessage
     *            The notification message.
     */
    private void fireNotification(final String notificationMessage) {
        final TrayNotification notification = new TrayNotification();
        notification.setMessage(notificationMessage);
        impl.fireNotification(notification);
    }

    private String getName(final User user) { return ModelUtil.getName(user); }

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
                actionRegistry.get(actionId).invoke(data);
            }
            else { ActionFactory.create(actionId).invoke(data); }
        }
        catch(final Exception x) {
            logger.error("[LBROWSER] [APPLICATION] [SYSTEM] [RUN ACTION] [UNKNOWN ERROR]", x);
            throw new RuntimeException(x);
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
