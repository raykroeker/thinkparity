/*
 * Created On: Jun 10, 2006 9:00:59 AM
 * $Id$
 */
package com.thinkparity.browser.application.session;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.thinkparity.browser.Constants.Session;
import com.thinkparity.browser.application.AbstractApplication;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;

/**
 * The session application is responsible for creating and maintaining the
 * user's session.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class SessionApplication extends AbstractApplication {

    /**
     * Obtain an api id.
     * 
     * @param api
     *            An api name.
     * @return The api id.
     */
    private static StringBuffer getApiId(final String api) {
        return getApplicationId().append(" ").append(" [SESSION] ")
            .append(api);
    }

    /**
     * Obtain the application id.
     * 
     * @return The application id.
     */
    private static StringBuffer getApplicationId() {
        return new StringBuffer("[LBROWSER] [APPLICATION]");
    }

    /**
     * Obtain an error id.
     * 
     * @param api
     *            An api name.
     * @param error
     *            An error string.
     * @return An error id.
     */
    private static String getErrorId(final String api, final String error) {
        return getApiId(api).append(" ").append(error).toString();
    }

    /** An apache logger. */
    final Logger logger;

    /** The thinkParity connection. */
    private Connection connection;

    /** A timer to reconnect after a set duration. */
    private Timer connectTimer;

    /** The session application event dispatcher. */
    private EventDispatcher ed;

    /** Create SessionApplication. */
    public SessionApplication(final Platform platform) {
        super(platform, null);
        this.logger = platform.getLogger(getClass());
    }

    /** @see com.thinkparity.browser.platform.application.Application#end(com.thinkparity.browser.platform.Platform) */
    public void end(final Platform platform) {
        logger.info(getApiId("[END]"));

        ed.end();
        ed = null;

        if(!isConnected()) {
            connectTimer.cancel();
            connectTimer = null;
        }

        notifyEnd();
    }

    /** @see com.thinkparity.browser.platform.application.Application#getConnection() */
    public Connection getConnection() { return connection; }

    /** @see com.thinkparity.browser.platform.application.Application#getId() */
    public ApplicationId getId() { return ApplicationId.SESSION; }

    /** @see com.thinkparity.browser.platform.application.Application#getLogger(java.lang.Class) */
    public Logger getLogger(final Class clasz) {
        return getPlatform().getLogger(clasz);
    }

    /** @see com.thinkparity.browser.platform.application.Application#hibernate(com.thinkparity.browser.platform.Platform) */
    public void hibernate(final Platform platform) {
        throw Assert.createUnreachable(getErrorId("[HIBERNATE]", "[CANNOT HIBERNATE SESSION APPLICATION]"));
    }

    /** @see com.thinkparity.browser.platform.application.Application#isDevelopmentMode() */
    public Boolean isDevelopmentMode() {
        return getPlatform().isDevelopmentMode();
    }

    /** @see com.thinkparity.browser.platform.application.Application#restore(com.thinkparity.browser.platform.Platform) */
    public void restore(final Platform platform) {
        throw Assert.createUnreachable(getErrorId("[HIBERNATE]", "[CANNOT RESTORE SESSION APPLICATION]"));
    }

    /** @see com.thinkparity.browser.platform.Saveable#restoreState(com.thinkparity.browser.platform.util.State) */
    public void restoreState(final State state) {
        throw Assert.createNotYetImplemented("SessionApplication#restoreState");
    }

    /** @see com.thinkparity.browser.platform.Saveable#saveState(com.thinkparity.browser.platform.util.State) */
    public void saveState(final State state) {
        throw Assert.createNotYetImplemented("SessionApplication#saveState");
    }

    /** @see com.thinkparity.browser.platform.application.Application#start(com.thinkparity.browser.platform.Platform) */
    public void start(final Platform platform) {
        logger.info(getApiId("[START]"));

        ed = new EventDispatcher(this);
        ed.start();

        if(!isConnected()) { connectLater(0L); }

        notifyStart();
    }

    /**
     * Notify the session application the connection is offline.
     *
     */
    void fireConnectionOffline() {
        logger.info(getApiId("[FIRE CONNECTION OFFLINE]"));
        connection = Connection.OFFLINE;
        connectLater();
    }

    /**
     * Notify the session application the connection is online.
     *
     */
    void fireConnectionOnline() {
        logger.info(getApiId("[FIRE CONNECTION ONLINE]"));
        connection = Connection.ONLINE;

        connectTimer.cancel();
        connectTimer = null;
    }

    /**
     * Connect to the thinkParity server.
     *
     */
    private void connect() {
        try { getSessionModel().login(); }
        catch(final ParityException px) {
            logger.error(getErrorId("[RECONNECT]", "[CANNOT LOGIN]"), px);
        }
    }

    /**
     * Schedule a task to connect until we are back online.
     *
     */
    private void connectLater() { connectLater(Session.RECONNECT_DELAY); }

    /**
     * Schedule a task to connect until we are back online.
     * 
     * @param delay
     *            The time to delay before the initial run of the task.
     */
    private void connectLater(final Long delay) {
        connectTimer = new Timer(
                getApplicationId().append(" [RECONNECT TIMER]").toString(),
                Boolean.FALSE);
        connectTimer.schedule(new TimerTask() {
            public void run() { if(isOnline()) { connect(); } }
        }, delay, Session.RECONNECT_PERIOD);
    }

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
    private Boolean isOnline() { return getPlatform().isOnline(); }
}
