/*
 * Created On:  12-Apr-07 9:00:23 AM
 */
package com.thinkparity.desdemona.model.backup;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.session.InvalidLocationException;

import com.thinkparity.ophelia.model.events.SessionAdapter;
import com.thinkparity.ophelia.model.events.SessionListener;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.Step;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Backup Monitor<br>
 * <b>Description:</b>The runnable will monitor the session provided by the
 * session model. If any session errors or session termination events are fired
 * an attempt is made to re-establish the session. The monitor will also
 * periodically wake up to check the session state.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class BackupMonitor implements Runnable {

    /** A <code>Log4JWrapper</code>. */
    private static final Log4JWrapper LOGGER;

    static {
        LOGGER = new Log4JWrapper();
    }

    /** The backup monitor period <code>Long</code>. */
    private final long period;

    /** A run <code>boolean</code> indicator. */
    private boolean run;

    /** A <code>SessionListener</code>. */
    private final SessionListener sessionListener;

    /** A <code>SessionModel</code>. */
    private final SessionModel sessionModel;

    /**
     * Create BackupMonitor.
     *
     */
    BackupMonitor(final SessionModel sessionModel, final Long period) {
        super();
        this.period = period.longValue();
        this.run = true;
        this.sessionListener = new SessionAdapter() {
            @Override
            public void sessionError(final Throwable cause) {
                LOGGER.logError(cause, "Backup session error.");
                wakeUp();
            }
            @Override
            public void sessionEstablished() {
            }
            @Override
            public void sessionTerminated() {
                wakeUp();
            }
        };
        this.sessionModel = sessionModel;
    }

    /**
     * @see java.lang.Runnable#run()
     *
     */
    public void run() {
        sessionModel.addListener(sessionListener);
        LOGGER.logInfo("Backup monitor is online.");
        while (run) {
            try {
                Thread.sleep(period);
            } catch (final InterruptedException ix) {}
            if (run) {
                if (sessionModel.isOnline()) {
                    LOGGER.logInfo("Backup session is online.");
                } else {
                    LOGGER.logWarning("Backup session is offline.");
                    LOGGER.logInfo("Establishing backup session now.");
                    try {
                        sessionModel.login(new ProcessAdapter() {
                            @Override
                            public void beginProcess() {}
                            @Override
                            public void beginStep(final Step step, final Object data) {}
                            public void determineSteps(final Integer steps) {}
                            @Override
                            public void endProcess() {}
                            @Override
                            public void endStep(final Step step) {}
                        });
                    } catch (final InvalidCredentialsException icx) {
                        LOGGER.logFatal(icx, "Backup session cannot be established.");
                        run = false;
                    } catch (final InvalidLocationException ilx) {
                        LOGGER.logFatal(ilx, "Backup session cannot be established.");
                        run = false;
                    } catch (final Throwable t) {
                        LOGGER.logError(t, "Backup session login error.");
                    }
                }
            }
        }
        sessionModel.removeListener(sessionListener);
        LOGGER.logInfo("Backup monitor is offline.");
    }

    /**
     * Stop the backup monitor.
     *
     */
    public void stopMonitor() {
        run = false;
        wakeUp();
    }

    /**
     * Wake up the monitor.
     *
     */
    private void wakeUp() {
        synchronized (this) {
            notifyAll();
        }
    }
}
