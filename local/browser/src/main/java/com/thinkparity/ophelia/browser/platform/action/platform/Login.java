/**
 * Created On: Mar 5, 2007 11:29:45 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.platform;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.model.session.OfflineException;
import com.thinkparity.ophelia.model.workspace.InitializeMediator;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingWorker;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Login extends AbstractAction {

    /** The browser platform. */
    private final Platform platform;

    /**
     * Create Login.
     * 
     * @param platform
     *            The browser platform.
     */
    public Login(final Platform platform) {
        super(ActionId.PLATFORM_LOGIN);
        this.platform = platform;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    public void invoke(Data data) {
        final InitializeMediator mediator = (InitializeMediator) data.get(DataKey.INITIALIZE_MEDIATOR);
        final ThinkParitySwingMonitor monitor = (ThinkParitySwingMonitor) data.get(DataKey.MONITOR);
        final Credentials credentials = (Credentials) data.get(DataKey.CREDENTIALS);
        invoke(monitor, credentials, mediator);
    }

    /**
     * Invoke the login swing worker.
     * 
     * @param monitor
     *            A <code>ThinkParitySwingMonitor</code>.
     * @param credentials
     *            The <code>Credentials</code>.
     * @param initializeMediator
     *            An <code>InitializeMediator</code>.
     */
    public void invoke(final ThinkParitySwingMonitor monitor,
            final Credentials credentials,
            final InitializeMediator mediator) {
        final ThinkParitySwingWorker<Login> worker = new LoginWorker(this,
                credentials, mediator);
        worker.setMonitor(monitor);
        worker.start();
    }

    /**
     * Obtain a localized string.
     * 
     * @param key
     *            A <code>String</code>.
     * @param data
     *            An <code>Object[]</code>.
     * @return A <code>String</code>.
     */
    protected String getString(final String key, final Object[] arguments) {
        return super.getString(key, arguments);
    }

    /** Data keys. */
    public enum DataKey {CREDENTIALS, INITIALIZE_MEDIATOR, MONITOR}

    /** <b>Title:</b>Login Swing Worker<br> */
    private static class LoginWorker extends ThinkParitySwingWorker<Login> {
        /** The <code>Credentials</code>.. */
        private final Credentials credentials;
        /** The login <code>ProcessMonitor</code>. */
        private final LoginProcessAdapter loginMonitor;
        /** The <code>InitializeMediator</code>. */
        private final InitializeMediator mediator;
        /** The thinkParity <code>Workspace</code>. */
        private final Workspace workspace;
        /**
         * Create LoginWorker.
         * 
         * @param login
         *            A <code>Login</code>.
         * @param credentials
         *            A set of <code>Credentials</code>.
         * @param mediator
         *            An <code>InitializeMediator</code>.
         */
        private LoginWorker(final Login login, final Credentials credentials,
                final InitializeMediator mediator) {
            super(login);
            this.credentials = credentials;
            this.mediator = mediator;
            this.loginMonitor = newLoginMonitor();
            this.workspace = action.platform.getModelFactory().getWorkspace(
                    getClass());
        }
        /**
         * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingWorker#getErrorHandler(java.lang.Throwable)
         *
         */
        @Override
		public Runnable getErrorHandler(final Throwable t) {
			return new Runnable() {
			    /**
			     * @see java.lang.Runnable#run()
			     *
			     */
			    @Override
				public void run() {
				    action.logger.logError(t, "An error has occurred for login.");
				}
			};
		}
        /**
         * @see com.thinkparity.codebase.swing.SwingWorker#construct()
         * 
         */
        @Override
        public Object run() {
            monitor.monitor(action.getString("StepInitializing"));
            try {
                action.platform.initializeWorkspace(loginMonitor, mediator,
                        workspace, credentials);
                monitor.complete();
            } catch (final InvalidCredentialsException icx) {
                action.logger.logError(icx, "An invalid credentials error has occurred in login.");
                monitor.reset();
                monitor.setError("ErrorInvalidCredentials");
                return null;
            } catch (final OfflineException ox) {
                action.logger.logError(ox, "An offline error has occurred in login.");
                monitor.reset();
                monitor.setError("ErrorOffline");
                return null;
            } catch (final Throwable t) {
                action.logger.logError(t, "An unexpected error has occurred in login.");
                monitor.reset();
                monitor.setError("ErrorUnexpected");
                return null;
            }
            return null;
        }

		/**
         * @see com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingWorker#setMonitor(com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor)
         *
         */
        @Override
        public void setMonitor(final ThinkParitySwingMonitor monitor) {
            super.setMonitor(monitor);
            loginMonitor.setMonitor(monitor);
        }

        /**
         * Create a new process monitor.
         * 
         * @return A <code>ProcessMonitor</code>.
         */
        private LoginProcessAdapter newLoginMonitor() {
            return new LoginProcessAdapter(action);
        }
    }
}
