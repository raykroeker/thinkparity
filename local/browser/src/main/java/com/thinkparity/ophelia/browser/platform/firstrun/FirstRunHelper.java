/*
 * Created On: Jun 10, 2006 10:26:01 AM
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.ophelia.browser.platform.Platform;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public final class FirstRunHelper {

    /** An apache logger. */
    private final Log4JWrapper logger;

    /** The thinkParity <code>Platform</code>. */
    private final Platform platform;

    /** The thinkParity <code>Workspace</code>. */
    private final Workspace workspace;

    /**
     * Create FirstRunHelper.
     * 
     * @param platform
     *            A thinkParity <code>Platform</code>.
     */
    public FirstRunHelper(final Platform platform) {
        super();
        this.platform = platform;

        this.logger = new Log4JWrapper(getClass());
        this.workspace = platform.getModelFactory().getWorkspace(getClass());
    }

    /**
     * Execute first run functionality for the browser platform.
     * 
     * @return True if first run completed.
     */
    public void firstRun() {
        boolean loginDone = false;
        String username = null;
        String password = null;
        LoginWindow window;
        while (!loginDone) {
            loginDone = true;
            window = new LoginWindow(username, password);
            window.setVisibleAndWait();

            username = window.getUsername();
            password = window.getPassword();
            if (null != username && null != password) {
                final Credentials credentials = new Credentials();
                credentials.setPassword(password);
                credentials.setUsername(username);
                try {
                    platform.initializeWorkspace(new ProcessMonitor() {
                        /**
                         * @see com.thinkparity.ophelia.model.util.ProcessMonitor#beginProcess()
                         *
                         */
                        public void beginProcess() {
                            logger.logInfo("Begin workspace initialize.");
                        }
                        /**
                         * @see com.thinkparity.ophelia.model.util.ProcessMonitor#beginStep(com.thinkparity.ophelia.model.util.Step, java.lang.Object)
                         *
                         */
                        public void beginStep(final Step step, final Object data) {
                            logger.logInfo("Begin {0}.", step);
                        }
                        /**
                         * @see com.thinkparity.ophelia.model.util.ProcessMonitor#determineSteps(java.lang.Integer)
                         *
                         */
                        public void determineSteps(final Integer steps) {
                            logger.logInfo("Determine {0}.", steps);
                        }
                        /**
                         * @see com.thinkparity.ophelia.model.util.ProcessMonitor#endProcess()
                         *
                         */
                        public void endProcess() {
                            logger.logInfo("End workspace initialize.");
                        }
                        /**
                         * @see com.thinkparity.ophelia.model.util.ProcessMonitor#endStep(com.thinkparity.ophelia.model.util.Step)
                         *
                         */
                        public void endStep(final Step step) {
                            logger.logInfo("End {0}.", step);
                        }
                    }, workspace, credentials);
                } catch (final InvalidCredentialsException icx) {
                    loginDone = false;
                }
            }
        }
    }
}
