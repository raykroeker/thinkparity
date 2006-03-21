/*
 * Mar 17, 2006
 */
package com.thinkparity.browser.application.system;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.AbstractApplication;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.L18nContext;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.message.system.SystemMessage;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SysApp extends AbstractApplication {

	/**
	 * An apache logger.
	 * 
	 */
	final Logger logger;

	/**
	 * The event dispatcher for the system application.
	 * 
	 */
	private EventDispatcher ed;

	/**
	 * The system application impl.
	 * 
	 */
	private SysAppImpl impl;

	/**
	 * Create a System.
	 * 
	 * @param platform
	 *            The platform.
	 */
	public SysApp(final Platform platform) {
		super(platform, L18nContext.SYS_APP);
		this.logger = platform.getLogger(getClass());
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#end(com.thinkparity.browser.platform.Platform)
	 * 
	 */
	public void end(final Platform platform) {
		logger.info("[BROWSER2] [APP] [SYS] [END]");

		impl.end();
		impl = null;

		ed.end();
		ed = null;

		notifyEnd();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#getId()
	 * 
	 */
	public ApplicationId getId() { return ApplicationId.SYS_APP; }

	/**
	 * @see com.thinkparity.browser.platform.application.Application#hibernate(com.thinkparity.browser.platform.Platform)
	 * 
	 */
	public void hibernate(final Platform platform) {
		logger.info("[BROWSER2] [APP] [SYS] [HIBERNATE]");

		impl.end();
		impl = null;

		ed.end();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#restore(com.thinkparity.browser.platform.Platform)
	 * 
	 */
	public void restore(final Platform platform) {
		logger.info("[BROWSER2] [APP] [SYS] [RESTORE]");

		impl = new SysAppImpl(this);
		impl.start();

		ed.start();
	}

	/**
	 * @see com.thinkparity.browser.platform.Saveable#restoreState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void restoreState(State state) {
		throw Assert.createNotYetImplemented("System#restoreState");
	}

	/**
	 * @see com.thinkparity.browser.platform.Saveable#saveState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void saveState(State state) {
		throw Assert.createNotYetImplemented("System#saveState");
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#start(com.thinkparity.browser.platform.Platform)
	 * 
	 */
	public void start(final Platform platform) {
		logger.info("[BROWSER2] [APP] [SYS] [START]");

		impl = new SysAppImpl(this);
		impl.start();

		ed = new EventDispatcher(this);
		ed.start();

		notifyStart();
	}

	protected String getString(final String localKey) {
		return super.getString(localKey);
	}

	protected String getString(final String localKey, final Object[] arguments) {
		return super.getString(localKey, arguments);
	}

	/**
	 * Notification that an artifact has been received.
	 * 
	 * @param artifact
	 *            The artifact.
	 */
	void notifyReceived(final Artifact artifact) {
		impl.notifyReceived(artifact);
	}

	/**
	 * Notification that an artifact version has been received.
	 * 
	 * @param artifactVersion
	 *            The artifact version.
	 */
	void notifyReceived(final ArtifactVersion artifactVersion) {
		impl.notifyReceived(artifactVersion);
	}

	/**
	 * Notification that a system message was created.
	 * 
	 * @param systemMessage
	 *            The system message.
	 */
	void notifyReceived(final SystemMessage systemMessage) {
		impl.notifyReceived(systemMessage);
	}

	/**
	 * Handle the system tray event.
	 * 
	 */
	void systemTrayActionPerformed(final ActionEvent e) {
		getPlatform().restore(ApplicationId.BROWSER2);
		impl.resetQueue();
	}
}
