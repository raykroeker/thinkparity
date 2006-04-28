/*
 * Mar 17, 2006
 */
package com.thinkparity.browser.application.system.sesmon;

import com.thinkparity.browser.application.system.SystemApplication;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.api.events.SessionListener;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionMonitor {

	/**
	 * The system application.
	 * 
	 */
	private final SystemApplication sysApp;

	/**
	 * The parity session listener.
	 * 
	 */
	private SessionListener sessionListener;

	/**
	 * Create a SessionMonitor.
	 * 
	 * @param sysApp
	 *            The system application.
	 */
	public SessionMonitor(final SystemApplication sysApp) {
		super();
		this.sysApp = sysApp;
	}

	/**
	 * End the session monitor.
	 *
	 */
	public void end() {
		Assert.assertNotNull("Session monitor already ended.", sessionListener);
		sysApp.getSessionModel().removeListener(sessionListener);
	}

	/**
	 * Start the session monitor.
	 *
	 */
	public void start() {
		Assert.assertIsNull("Session monitor already started.", sessionListener);
		this.sessionListener = new SessionListener() {
			public void sessionEstablished() {
				throw Assert.createNotYetImplemented("#sessionEstablished");
			}
			public void sessionTerminated() {
				throw Assert.createNotYetImplemented("#sessionTerminated");
			}
			public void sessionTerminated(Throwable cause) {
				throw Assert.createNotYetImplemented("#sessionTerminated");
			}
		};
		sysApp.getSessionModel().addListener(sessionListener);
	}
}
