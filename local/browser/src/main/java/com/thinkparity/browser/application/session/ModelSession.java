/*
 * Feb 3, 2006
 */
package com.thinkparity.browser.application.session;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.session.ui.SessionWindow;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.SessionListener;
import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ModelSession {

	private static final ModelSession singleton;

	private static final Object singletonLock;

	static {
		singleton = new ModelSession();
		singletonLock = new Object();
	}

	public static void establishSession() {
		synchronized(singletonLock) { singleton.doEstablishSession(); }
	}

	public static Status getStatus() {
		synchronized(singletonLock) { return singleton.doGetStatus(); }
	}

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * The parity session model.
	 * 
	 */
	private final SessionModel sessionModel;

	/**
	 * The parity session status.
	 * 
	 */
	private Status status;

	/**
	 * Create a ModelSession [Singleton]
	 * 
	 */
	private ModelSession() {
		super();
		this.sessionModel = SessionModel.getModel();
		this.sessionModel.addListener(new SessionListener() {
			public void sessionEstablished() { doUpdateStatus(); }
			public void sessionTerminated() { doUpdateStatus(); }
			public void sessionTerminated(Throwable cause) { doUpdateStatus(); }
		});
	}

	private void doEstablishSession() { doEstablishSession(null); }

	private void doEstablishSession(final ParityException previousError) {
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
			System.out.println(sessionWindow.isOpen());
			sessionWindow.processEventQueue();
			System.out.println(sessionWindow.isOpen());
		}
	}

	private Status doGetStatus() { return status; }

	private void doUpdateStatus() {
		status = sessionModel.isLoggedIn() ? Status.ONLINE : Status.OFFLINE;
	}
}
