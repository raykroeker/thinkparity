/*
 * Created On: Jun 10, 2006 9:00:59 AM
 */
package com.thinkparity.ophelia.browser.application.session;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.Application;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.ophelia.browser.Constants.Session;
import com.thinkparity.ophelia.browser.application.AbstractApplication;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.util.State;
import com.thinkparity.ophelia.model.session.DefaultLoginMonitor;

/**
 * The session application is responsible for creating and maintaining the
 * user's session.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public class SessionApplication extends AbstractApplication {

    /** Thread name for the connect timer. */
    private static final String CONNECT_TIMER_NAME = new StringBuffer()
            .append(Application.OPHELIA)
            .append(" - ").append("Connect Timer")
            .toString();

    /** The thinkParity connection. */
    private Connection connection;

    /** A timer to reconnect after a set duration. */
    private Timer connectTimer;

    /** The session application event dispatcher. */
    private EventDispatcher ed;

    /**
     * Create SessionApplication. The session application does not require any
     * localization.
     * 
     * @param platform
     *            A thinkParity platform.
     */
    public SessionApplication(final Platform platform) { super(platform, null); }

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
        if(isConnected()) { disconnect(); }
        else {
            connectTimer.cancel();
            connectTimer = null;
        }

        notifyEnd();
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.Application#getConnection() */
    public Connection getConnection() { return connection; }

    /** @see com.thinkparity.ophelia.browser.platform.application.Application#getId() */
    public ApplicationId getId() { return ApplicationId.SESSION; }

    /** @see com.thinkparity.ophelia.browser.platform.application.Application#getLogger(java.lang.Class) */
    public Logger getLogger(final Class clasz) {
        return getPlatform().getLogger(clasz);
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.Application#hibernate(com.thinkparity.ophelia.browser.platform.Platform) */
    public void hibernate(final Platform platform) {
        Assert.assertUnreachable("SessionApplication#hibernate");
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.Application#isDevelopmentMode() */
    public Boolean isDevelopmentMode() {
        return getPlatform().isDevelopmentMode();
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.Application#restore(com.thinkparity.ophelia.browser.platform.Platform) */
    public void restore(final Platform platform) {
        Assert.assertUnreachable("SessionApplication#restore");
    }

    /** @see com.thinkparity.ophelia.browser.platform.Saveable#restoreState(com.thinkparity.ophelia.browser.platform.util.State) */
    public void restoreState(final State state) {
        throw Assert.createNotYetImplemented("SessionApplication#restoreState");
    }

    /** @see com.thinkparity.ophelia.browser.platform.Saveable#saveState(com.thinkparity.ophelia.browser.platform.util.State) */
    public void saveState(final State state) {
        throw Assert.createNotYetImplemented("SessionApplication#saveState");
    }

    /** @see com.thinkparity.ophelia.browser.platform.application.Application#start(com.thinkparity.ophelia.browser.platform.Platform) */
    public void start(final Platform platform) {
        logApiId();

        ed = new EventDispatcher(this);
        ed.start();

        logVariable("isConnected()", isConnected());
        if(!isConnected()) {
            connectLater(0L);
        }

        notifyStart();
    }

    /**
     * Notify the session application the connection is offline.
     *
     */
    void fireConnectionOffline() {
        logApiId();
        connection = Connection.OFFLINE;
        connectLater();
    }

    /**
     * Notify the session application the connection is online.
     *
     */
    void fireConnectionOnline() {
        logApiId();
        connection = Connection.ONLINE;

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
     * Schedule a task to connect until we are back online.
     * 
     * @param delay
     *            The time to delay before the initial run of the task.
     */
    private void connectLater(final Long delay) {
        logApiId();
        connectTimer = new Timer(CONNECT_TIMER_NAME, Boolean.FALSE);
        connectTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    logVariable("isOnline()", isOnline());
                    if (isOnline()) {
                        connect();
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
    private void disconnect() { getSessionModel().logout(); }

    /**
     * Determine if the user is connected.
     * 
     * @return True if the user is connected.
     */
    private Boolean isConnected() { return getSessionModel().isLoggedIn(); }

    /**
     * Determine if the platform is online.
     * 
     * @return True if the platform is online; false otherwise.
     */
    private Boolean isOnline() {
        return getPlatform().isOnline();
    }
}
