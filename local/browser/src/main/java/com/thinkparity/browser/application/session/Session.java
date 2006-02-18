/*
 * Feb 4, 2006
 */
package com.thinkparity.browser.application.session;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.thinkparity.browser.application.AbstractApplication;
import com.thinkparity.browser.application.session.ui.SessionWindow;
import com.thinkparity.browser.application.session.ui.SessionWindow.ReturnCode;
import com.thinkparity.browser.platform.application.Application;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.SessionListener;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * The session application.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Session extends AbstractApplication {

	/**
	 * Synchronization lock.
	 * 
	 */
	private static final Object lock;

	/**
	 * The parity preferences.
	 * 
	 */
	private static final Preferences preferences;

	/**
	 * The singleton instance of the session application.
	 * 
	 */
	private static final Application singleton;

	static {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		preferences = workspace.getPreferences();

		singleton = new Session();
		lock = new Object();
	}

	/**
	 * Obtain the session application.
	 * 
	 * @return The session application.
	 */
	public static Application getSession() {
		return singleton;
	}

	/**
	 * The session api.
	 * 
	 */
	private final SessionModel sessionModel;

	/**
	 * The session status.
	 * 
	 * @see #getStatus()
	 * @see Status
	 */
	private Status status;

	/**
	 * Create a Session.
	 * 
	 */
	private Session() {
		super();
		this.sessionModel = SessionModel.getModel();
		this.sessionModel.addListener(new SessionListener() {
			public void sessionEstablished() { /*updateStatus();*/ }
			public void sessionTerminated() { /*updateStatus();*/ }
			public void sessionTerminated(final Throwable cause) {
				/*updateStatus();*/
			}
		});
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#end()
	 * 
	 */
	public void end() {
		try { sessionModel.logout(); }
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#getId()
	 * 
	 */
	public ApplicationId getId() { return ApplicationId.SESSION; }

	/**
	 * Obtain the session status.
	 * 
	 * @return The session status.
	 * 
	 * @see Status
	 */
	public Status getStatus() { return status; }

	/**
	 * @see com.thinkparity.browser.platform.application.Application#hibernate()
	 * 
	 */
	public void hibernate() {}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#launch()
	 * 
	 */
	public void launch() {
		if(doAutoLogin()) { autoLogin(); }
		else { login(); }
	}

	/**
	 * Login.  Display the login UI; and login.
	 *
	 */
	public void login() {
		if(Status.ONLINE == status) { return; }
		else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() { SessionWindow.open(); }
				});
			}
			catch(final InterruptedException ix) { throw new RuntimeException(ix); }
			catch(final InvocationTargetException itx) { throw new RuntimeException(itx); }
			final SessionWindow sessionWindow = SessionWindow.getSessionWindow();
			sessionWindow.processEventQueue();
			if(ReturnCode.LOGIN == sessionWindow.getReturnCode())
				login(sessionWindow.extractUsername(),
						sessionWindow.extractPassword(),
						sessionWindow.extractDoRememberPassword());
		}
	}

	/**
	 * @see com.thinkparity.browser.platform.Saveable#restore(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void restore(final State state) {}

	/**
	 * @see com.thinkparity.browser.platform.Saveable#save(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void save(final State state) {}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#start()
	 * 
	 */
	public void start() { launch(); }

	/**
	 * Grab the username\password from the preferences; and login.
	 *
	 */
	private void autoLogin() {
		login(getPrefUsername(), getPrefPassword(), Boolean.TRUE);
	}

	/**
	 * Check the "Remeber password" preference, and if it is set; return true.
	 * 
	 * @return True; if the "Remeber password" preference is set; false
	 *         otherwise.
	 */
	private Boolean doAutoLogin() { return getPref("AutoLogin", Boolean.FALSE); }

	/**
	 * Get the password preference.
	 * 
	 * @return The password preference.
	 */
	private String getPrefPassword() { return preferences.getPassword(); }

	/**
	 * Get the username preference.
	 * 
	 * @return The username preference.
	 */
	private String getPrefUsername() { return preferences.getUsername(); }

	/**
	 * Login to the parity session. If successful; set the username preference.
	 * 
	 * @param username
	 *            The parity username.
	 * @param password
	 *            The parity password.
	 * @param doRememberPassword
	 *            A flag indicating whether or not we should remember the
	 *            password for next time.
	 */
	private void login(final String username, final String password,
			final Boolean doRememberPassword) {
		try {
			sessionModel.login(username, password);
			setAutoLogin(doRememberPassword, password);
			updateStatus();
		}
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

	/**
	 * Set the auto login preference.
	 * 
	 * @param doAutoLogin
	 *            The auto login preference.
	 */
	private void setAutoLogin(final Boolean doAutoLogin, final String password) {
		setPref("AutoLogin", doAutoLogin);
		if(doAutoLogin) {
			preferences.clearPassword();
			preferences.setPassword(password);
		}
		else { preferences.clearPassword(); }
	}

	/**
	 * Check the status of the session model; and update the local status
	 * variable.
	 * 
	 */
	private void updateStatus() {
		synchronized(lock) {
			if(sessionModel.isLoggedIn()) { status = Status.ONLINE; }
			else { status = Status.OFFLINE; }
		}
	}
}
