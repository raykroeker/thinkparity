/*
 * Feb 4, 2006
 */
package com.thinkparity.browser;

import java.util.Hashtable;
import java.util.Map;

import com.thinkparity.browser.application.ApplicationFactory;
import com.thinkparity.browser.application.session.Status;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.Session;
import com.thinkparity.browser.platform.application.Application;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.PropertiesUtil;

/**
 * The Browser2Platform is the main controller for the parity user interface.
 * The platform has the ability to run various applications which can run
 * independently of one another.
 * 
 * The platform has 1 session which is shared between all applications.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Browser2Platform implements Platform {

	/**
	 * The registry of running applications.
	 * 
	 */
	private static final Map<ApplicationId,Application> applicationRegistry;

	static {
		final StringBuffer buffer = new StringBuffer();
		PropertiesUtil.print(
				buffer, "--- Parity Platform ---", System.getProperties());
		System.out.println(buffer);
		Initializer.getInstance().initialize();

		applicationRegistry = new Hashtable<ApplicationId,Application>(3, 1.0F);
	}

	/**
	 * The parity ui platform session.
	 * 
	 */
	private final Session session;

	/**
	 * Create a Browser2Platform.
	 * 
	 */
	Browser2Platform() {
		super();
		this.session = new Session();
	}

	/**
	 * @see com.thinkparity.browser.platform.Platform#end()
	 * 
	 */
	public void end() {
		endApplication(ApplicationId.BROWSER);
	}

	/**
	 * @see com.thinkparity.browser.platform.Platform#getApplication(com.thinkparity.browser.platform.application.ApplicationId)
	 * 
	 */
	public Application getApplication(final ApplicationId applicationId) {
		return applicationRegistry.get(applicationId);
	}

	/**
	 * @see com.thinkparity.browser.platform.Platform#getSession()
	 * 
	 */
	public Session getSession() { return session; }

	/**
	 * @see com.thinkparity.browser.platform.Platform#launchApplication(com.thinkparity.browser.platform.application.ApplicationId)
	 * 
	 */
	public void launchApplication(final ApplicationId applicationId) {
		Application application = getApplication(applicationId);
		if(null == application) {
			startApplication(applicationId);
			application = getApplication(applicationId);
		}
		application.launch();
	}

	/**
	 * @see com.thinkparity.browser.platform.Platform#start()
	 * 
	 */
	public void start() {
		startApplication(ApplicationId.SESSION);
		{
			final com.thinkparity.browser.application.session.Session session =
				(com.thinkparity.browser.application.session.Session) getApplication(ApplicationId.SESSION);
			if(Status.ONLINE == session.getStatus())
				startApplication(ApplicationId.BROWSER);
		}
	}

	/**
	 * End the application specified by the id. This will use the factory to
	 * obtain the application; save any of its state information; and call the
	 * application's end api.
	 * 
	 * @param applicationId
	 *            The application id.
	 */
	private void endApplication(final ApplicationId applicationId) {
		final Application application = getApplication(applicationId);
		final State state = getApplicationState(applicationId);
		application.save(state);
		saveApplicationState(applicationId, state);
		application.end();
		synchronized(applicationRegistry) {
			applicationRegistry.remove(applicationId);
		}
	}

	/**
	 * Obtain the persisted application's state.
	 * 
	 * @param applicationId
	 *            The application id.
	 * @return The application state; or null if no state existed.
	 */
	private State getApplicationState(final ApplicationId applicationId) {
		return null;
	}

	/**
	 * Save the application's state.
	 * 
	 * @param applicationId
	 *            The application id.
	 * @param state
	 *            The application state.
	 */
	private void saveApplicationState(final ApplicationId applicationId,
			final State state) {}

	/**
	 * Start the application specified by the id.  This will use the factory
	 * to create the application; restore any of its state information; and
	 * call the application's start api.
	 * 
	 * @param applicationId
	 *            The application id.
	 */
	private void startApplication(final ApplicationId applicationId) {
		final Application application = ApplicationFactory.create(applicationId);
		application.restore(getApplicationState(applicationId));
		application.start();
		synchronized(applicationRegistry) {
			applicationRegistry.put(applicationId, application);
		}
	}
}
