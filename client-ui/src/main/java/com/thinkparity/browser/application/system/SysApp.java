/*
 * Mar 17, 2006
 */
package com.thinkparity.browser.application.system;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.AbstractApplication;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.action.ActionFactory;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.ActionRegistry;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationRegistry;
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

	/** An apache logger. */
	final Logger logger;

	/** The action registry. */
    private final ActionRegistry actionRegistry;

	/** The application registry. */
    private final ApplicationRegistry applicationRegistry;

	/** The event dispatcher. */
	private EventDispatcher ed;

	/** The application impl. */
	private SysAppImpl impl;

	/**
	 * Create a System.
	 * 
	 * @param platform
	 *            The platform.
	 */
	public SysApp(final Platform platform) {
		super(platform, L18nContext.SYS_APP);
        this.actionRegistry = new ActionRegistry();
        this.applicationRegistry = new ApplicationRegistry();
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
     * @see com.thinkparity.browser.platform.application.Application#getLogger(java.lang.Class)
     * 
     */
    public Logger getLogger(final Class clasz) {
        return getPlatform().getLogger(clasz);
    }

	/**
	 * @see com.thinkparity.browser.platform.application.Application#hibernate(com.thinkparity.browser.platform.Platform)
	 * 
	 */
	public void hibernate(final Platform platform) {
		logger.info("[BROWSER2] [APP] [SYS] [HIBERNATE]");

		impl.end();
		impl = null;

		ed.end();
		ed = null;

		notifyHibernate();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.Application#restore(com.thinkparity.browser.platform.Platform)
	 * 
	 */
	public void restore(final Platform platform) {
		logger.info("[BROWSER2] [APP] [SYS] [RESTORE]");

		impl = new SysAppImpl(this);
		impl.start();

		ed = new EventDispatcher(this);
		ed.start();

		notifyRestore();
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

	/** Display the about dialogue. */
    void displayAbout() {}

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

    /** Run the exit platform action. */
    void runExitPlatform() {
        if(!actionRegistry.contains(ActionId.PLATFORM_QUIT))
            ActionFactory.createAction(ActionId.PLATFORM_QUIT, getPlatform());

        run(ActionId.PLATFORM_QUIT, new Data(0));
    }

    /** Run the restore browser action. */
	void runRestoreBrowser() {
        if(!actionRegistry.contains(ActionId.RESTORE_BROWSER))
            ActionFactory.createAction(ActionId.RESTORE_BROWSER, getPlatform());

        runLater(ActionId.RESTORE_BROWSER, new Data(0));
    }

    /**
     * Run an action.
     * 
     * @param actionId
     *            The action id.
     * @param data
     *            The action data.
     */
    private void run(final ActionId actionId, final Data data) {
        try {
            if(actionRegistry.contains(actionId)) {
                actionRegistry.get(actionId).invoke(data);
            }
            else { ActionFactory.createAction(actionId).invoke(data); }
        }
        catch(final Exception x) {
            logger.error("[LBROWSER] [APPLICATION] [SYSTEM] [RUN ACTION] [UNKNOWN ERROR]", x);
            throw new RuntimeException(x);
        }
    }

    /**
     * Run an action that doesn't need to be run imeediately.
     * 
     * @param actionId
     *            The action id.
     * @param data
     *            The action data.
     * @see SwingUtilities#invokeLater(java.lang.Runnable)
     * @see SysApp#run(ActionId, Data)
     */
    private void runLater(final ActionId actionId, final Data data) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { SysApp.this.run(actionId, data); }
        });
    }
}
