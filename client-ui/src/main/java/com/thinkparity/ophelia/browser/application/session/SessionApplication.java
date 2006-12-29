/*
 * Created On: Jun 10, 2006 9:00:59 AM
 */
package com.thinkparity.ophelia.browser.application.session;

import java.util.Timer;
import java.util.TimerTask;

import com.thinkparity.codebase.Application;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.session.DefaultLoginMonitor;

import com.thinkparity.ophelia.browser.Constants.Session;
import com.thinkparity.ophelia.browser.application.AbstractApplication;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.util.State;

import org.apache.log4j.Logger;

/**
 * The session application is responsible for creating and maintaining the
 * user's session.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.6
 */
public class SessionApplication extends AbstractApplication {

    /** The connect timer name <code>String</code>. */
    private static final String CONNECT_TIMER_NAME = new StringBuffer()
            .append(Application.OPHELIA).append(" - ").append("Connect Timer")
            .toString();

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
     * @see com.thinkparity.ophelia.browser.platform.application.Application#end(com.thinkparity.ophelia.browser.platform.Platform)
     * 
     */
    public void end(final Platform platform) {
        logApiId();

        ed.end();
        ed = null;

        // if we are connected; disconnect; otherwise the timer is running and
        // needs to be cancelled.
        debugVariable("isConnected()", isConnected());
        debugVariable("connectTimer", connectTimer);
        if (isConnected()) {
            disconnect();
        } else {
            // we can be not connected; and not attempting to connect
            if (null != connectTimer) {
                connectTimer.cancel();
                connectTimer = null;
            }
        }

        notifyEnd();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.Application#getConnection()
     * 
     */
    public Connection getConnection() {
        return getSessionModel().isLoggedIn() ? Connection.ONLINE
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
     * @see com.thinkparity.ophelia.browser.platform.application.Application#getLogger(java.lang.Class)
     * 
     */
    public Logger getLogger(final Class clasz) {
        return getPlatform().getLogger(clasz);
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
     * @see com.thinkparity.ophelia.browser.platform.application.Application#restore(com.thinkparity.ophelia.browser.platform.Platform)
     * 
     */
    public void restore(final Platform platform) {
        Assert.assertUnreachable("The session application does not support hibernation.");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.Saveable#restoreState(com.thinkparity.ophelia.browser.platform.util.State)
     * 
     */
    public void restoreState(final State state) {
        throw Assert.createNotYetImplemented("The session application does not support state.");
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.Saveable#saveState(com.thinkparity.ophelia.browser.platform.util.State)
     * 
     */
    public void saveState(final State state) {
        throw Assert.createNotYetImplemented("The session application does not support state.");
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
        if (!isConnected()) {
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
        logApiId();
        connectLater();
    }

    /**
     * Notify the session application the connection is online.
     *
     */
    void fireConnectionOnline() {
        logApiId();
        connectTimer.cancel();
        connectTimer = null;
    }

    /**
     * Connect to the thinkParity server.
     *
     */
    private void connect() {
        getSessionModel().login(new DefaultLoginMonitor());
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
        connectTimer = new Timer(CONNECT_TIMER_NAME, Boolean.FALSE);
        connectTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    logVariable("isOnline()", isOnline());
                    if (isOnline()) {
                        if (Connection.OFFLINE == getConnection()) {
                            connect();
                        } else {
                            logWarn("User already online.");
                        }
                    } else {
                        logWarn("Server not online.");
                    }
                } catch (final Throwable t) {
                    logError(t, "Error connecting to session.");
                }
            }
        }, delay, Session.CONNECT_TIMER_PERIOD);
    }

    /**
     * Disconnect from the thinkParity server.
     *
     */
    private void disconnect() {
        getSessionModel().logout();
    }

    /**
     * Determine if the connection is established.
     * 
     * @return True if the connected is established.
     */
    private Boolean isConnected() {
        return getSessionModel().isLoggedIn();
    }

    /**
     * Determine if the platform is online.
     * 
     * @return True if the platform is online; false otherwise.
     */
    private Boolean isOnline() {
        return getPlatform().isOnline();
    }
}
