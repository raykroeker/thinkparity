/*
 * Created On: Jun 10, 2006 9:00:59 AM
 */
package com.thinkparity.ophelia.browser.application.session;

import java.util.Timer;
import java.util.TimerTask;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.AbstractJFrame;

import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.session.InvalidLocationException;

import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.Step;

import com.thinkparity.ophelia.browser.Constants.Session;
import com.thinkparity.ophelia.browser.application.AbstractApplication;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;

/**
 * The session application is responsible for creating and maintaining the
 * user's session.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.6
 */
public class SessionApplication extends AbstractApplication {

    /** The connect <code>Timer</code>. */
    private Timer connectTimer;

    /** The application's <code>EventDispatcher</code>. */
    private EventDispatcher ed;

    /**
     * Create SessionApplication. The session application does not require any
     * localization.
     * 
     * @param platform
     *            A thinkParity <code>Platform</code>.
     */
    public SessionApplication(final Platform platform) {
        super(platform, null);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#applyBusyIndicator()
     *
     */
    public void applyBusyIndicator() {}

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#end(com.thinkparity.ophelia.browser.platform.Platform)
     * 
     */
    public void end(final Platform platform) {
        logApiId();

        ed.end();
        ed = null;

        // if we are logged in; logout
        if (isLoggedIn()) {
            logout();
        } else {
            connectTimer.cancel();
            connectTimer = null;
        }

        notifyEnd();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#getConnection()
     * 
     */
    public Connection getConnection() {
        return getSessionModel().isOnline() ? Connection.ONLINE
                : Connection.OFFLINE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#getId()
     * 
     */
    public ApplicationId getId() {
        return ApplicationId.SESSION;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#getMainWindow()
     *
     */
    public AbstractJFrame getMainWindow() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#hibernate(com.thinkparity.ophelia.browser.platform.Platform)
     * 
     */
    public void hibernate(final Platform platform) {
        Assert.assertUnreachable("The session application does not support hibernation.");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#isDevelopmentMode()
     * 
     */
    public Boolean isDevelopmentMode() {
        return getPlatform().isDevelopmentMode();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#removeBusyIndicator()
     *
     */
    public void removeBusyIndicator() {}

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#restore(com.thinkparity.ophelia.browser.platform.Platform)
     * 
     */
    public void restore(final Platform platform) {
        Assert.assertUnreachable("The session application does not support hibernation.");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#start(com.thinkparity.ophelia.browser.platform.Platform)
     * 
     */
    public void start(final Platform platform) {
        logApiId();
        // start the event dispatcher
        ed = new EventDispatcher(this);
        ed.start();
        // connect
        if (!isLoggedIn()) {
            connectLater(0L);
        }
        // fire application started event
        notifyStart();
    }

    /**
     * Notify the session application the connection is offline.
     *
     */
    void fireConnectionOffline() {
        connectLater();
    }

    /**
     * Notify the session application the connection is online.
     *
     */
    void fireConnectionOnline() {
        connectTimer.cancel();
        connectTimer = null;
    }

    /**
     * Connect to the thinkParity server.
     *
     */
    private void connect() {
        try {
            getSessionModel().login(new ProcessAdapter() {
                @Override
                public void beginProcess() {
                    logger.logInfo("Begin login.");
                }
                @Override
                public void beginStep(Step step, Object data) {
                    if (null == data)
                        logger.logDebug("Begin login step {0}.", step);
                    else
                        logger.logDebug("Begin login step {0}:{1}.", step, data);
                }
                @Override
                public void determineSteps(final Integer steps) {
                    logger.logDebug("Login will complete in {0} steps.", steps);
                }
                @Override
                public void endProcess() {
                    logger.logDebug("Login complete.");
                }
                @Override
                public void endStep(final Step step) {
                    logger.logDebug("Login step {0} complete.", step);
                }
            });
        } catch (final InvalidCredentialsException icx) {
            logger.logError(icx, "Cannot login.");
            connectTimer.cancel();
        } catch (final InvalidLocationException ilx) {
            logger.logError(ilx, "Cannot login.");
            connectTimer.cancel();
        }
    }

    /**
     * Schedule a task to connect until we are back online.
     *
     */
    private void connectLater() {
        connectLater(Session.RECONNECT_DELAY);
    }

    /**
     * Establish a connection after a delay.
     * 
     * @param delay
     *            A number of milliseconds <code>Long</code> to delay before
     *            connecting.
     */
    private void connectLater(final Long delay) {
        logApiId();
        if (null != connectTimer) {
            connectTimer.cancel();
            connectTimer = null;
        }

        connectTimer = new Timer("TPS-OpheliaUI-SessionConnect", Boolean.TRUE);
        connectTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    connect();
                } catch (final Throwable t) {
                    logger.logError(t, "Error connecting to session.");
                }
            }
        }, delay, Session.CONNECT_TIMER_PERIOD);
    }

    /**
     * Determine if the user is logged in.
     * 
     * @return True if the user is logged in.
     */
    private Boolean isLoggedIn() {
        return getSessionModel().isLoggedIn();
    }

    /**
     * Logout.
     *
     */
    private void logout() {
        try {
            getSessionModel().logout();
        } catch (final Throwable t) {
            logger.logError(t, "Could not logout of session.");
        }
    }
}
