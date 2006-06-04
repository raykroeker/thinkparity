/*
 * Mar 17, 2006
 */
package com.thinkparity.browser.application.system;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.AbstractApplication;
import com.thinkparity.browser.application.system.tray.TrayNotification;
import com.thinkparity.browser.model.util.ModelUtil;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.action.ActionFactory;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.ActionRegistry;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationRegistry;
import com.thinkparity.browser.platform.application.ApplicationStatus;
import com.thinkparity.browser.platform.application.L18nContext;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemApplication extends AbstractApplication {

	/** An apache logger. */
	final Logger logger;

	/** The action registry. */
    private final ActionRegistry actionRegistry;

	/** The application registry. */
    private final ApplicationRegistry applicationRegistry;

	/** The event dispatcher. */
	private EventDispatcher ed;

	/** The application impl. */
	private SystemApplicationImpl impl;

	/**
	 * Create a System.
	 * 
	 * @param platform
	 *            The platform.
	 */
	public SystemApplication(final Platform platform) {
		super(platform, L18nContext.SYS_APP);
        this.actionRegistry = new ActionRegistry();
        this.applicationRegistry = new ApplicationRegistry();
		this.logger = platform.getLogger(getClass());
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
	 * @see com.thinkparity.browser.platform.application.Application#end(com.thinkparity.browser.platform.Platform)
	 * 
	 */
	public void end(final Platform platform) {
		logger.info("[BROWSER2] [APP] [SYS] [END]");

		impl.end();
		impl = null;

		ed.end();
		ed = null;

		notifyEnd();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#getId()
	 * 
	 */
	public ApplicationId getId() { return ApplicationId.SYS_APP; }

	/**
     * @see com.thinkparity.browser.platform.application.Application#getLogger(java.lang.Class)
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
	 * @see com.thinkparity.browser.platform.application.Application#hibernate(com.thinkparity.browser.platform.Platform)
	 * 
	 */
	public void hibernate(final Platform platform) {
		logger.info("[BROWSER2] [APP] [SYS] [HIBERNATE]");

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
            applicationRegistry.getStatus(ApplicationId.BROWSER2);
    }

    /** @see com.thinkparity.browser.platform.application.Application#isDevelopmentMode() */
    public Boolean isDevelopmentMode() {
        return getPlatform().isDevelopmentMode();
    }

    /**
	 * @see com.thinkparity.browser.platform.application.Application#restore(com.thinkparity.browser.platform.Platform)
	 * 
	 */
	public void restore(final Platform platform) {
		logger.info("[BROWSER2] [APP] [SYS] [RESTORE]");

		impl = new SystemApplicationImpl(this);
		impl.start();

		ed = new EventDispatcher(this);
		ed.start();

		notifyRestore();
	}

    /**
	 * @see com.thinkparity.browser.platform.Saveable#restoreState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void restoreState(State state) {
		throw Assert.createNotYetImplemented("System#restoreState");
	}

    /** Run the exit platform action. */
    public void runExitPlatform() {
        if(!actionRegistry.contains(ActionId.PLATFORM_QUIT))
            ActionFactory.createAction(ActionId.PLATFORM_QUIT, getPlatform());

        run(ActionId.PLATFORM_QUIT, new Data(0));
    }

    /** Run the login action. */
    public void runLogin() {
        if(!actionRegistry.contains(ActionId.PLATFORM_LOGIN))
            ActionFactory.createAction(ActionId.PLATFORM_LOGIN, getPlatform());

        runLater(ActionId.PLATFORM_LOGIN, new Data(0));
    }

    /** Run the logout action. */
    public void runLogout() {
        if(!actionRegistry.contains(ActionId.PLATFORM_LOGOUT))
            ActionFactory.createAction(ActionId.PLATFORM_LOGOUT, getPlatform());

        run(ActionId.PLATFORM_LOGOUT, new Data(0));
    }

    /** Run the move to front action. */
    public void runMoveBrowserToFront() {
        if(!actionRegistry.contains(ActionId.MOVE_BROWSER_TO_FRONT))
            ActionFactory.createAction(ActionId.MOVE_BROWSER_TO_FRONT, getPlatform());

        runLater(ActionId.MOVE_BROWSER_TO_FRONT, new Data(0));
    }

    /** Run the restart platform action. */
    public void runRestartPlatform() {
        if(!actionRegistry.contains(ActionId.PLATFORM_RESTART))
            ActionFactory.createAction(ActionId.PLATFORM_RESTART, getPlatform());
        run(ActionId.PLATFORM_RESTART, new Data(0));
    }

    /** Run the restore browser action. */
	public void runRestoreBrowser() {
        if(!actionRegistry.contains(ActionId.RESTORE_BROWSER))
            ActionFactory.createAction(ActionId.RESTORE_BROWSER, getPlatform());

        runLater(ActionId.RESTORE_BROWSER, new Data(0));
    }

    /**
	 * @see com.thinkparity.browser.platform.Saveable#saveState(com.thinkparity.browser.platform.util.State)
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
	 * @see com.thinkparity.browser.platform.application.Application#start(com.thinkparity.browser.platform.Platform)
	 * 
	 */
	public void start(final Platform platform) {
		logger.info("[BROWSER2] [APP] [SYS] [START]");

		impl = new SystemApplicationImpl(this);
		impl.start();

		ed = new EventDispatcher(this);
		ed.start();

		notifyStart();
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
     * @param document
     *            The document.
     */
    void fireDocumentCreated(final Document document) {
        fireNotification(getString(
                "Notification.DocumentCreatedMessage",
                new Object[] {document.getName()}));
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

    /** Notify the session has been established. */
    void fireSessionEstablished() {
        impl.reloadConnectionStatus(Platform.Connection.ONLINE);
    }

    /** Notify the session has been terminated. */
    void fireSessionTerminated() {
        impl.reloadConnectionStatus(Platform.Connection.OFFLINE);
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
            if(actionRegistry.contains(actionId)) {
                actionRegistry.get(actionId).invoke(data);
            }
            else { ActionFactory.createAction(actionId).invoke(data); }
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
