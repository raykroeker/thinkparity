/*
 * Aug 6, 2005
 */
package com.thinkparity.model.parity.model;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;


/**
 * AbstractModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModelImpl {

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(AbstractModelImpl.class);

	/**
	 * Handle to the parity model preferences.
	 */
	protected final Preferences preferences;

	/**
	 * Handle to the parity model workspace.
	 */
	protected final Workspace workspace;

	/**
	 * Create an AbstractModelImpl
	 * 
	 * @param workspace
	 *            Handle to an existing parity model workspace.
	 */
	protected AbstractModelImpl(final Workspace workspace) {
		super();
		this.workspace = workspace;
		this.preferences = (null == workspace ? null : workspace.getPreferences());
	}

	/**
	 * Assert that the calling method has not yet been implemented.
	 *
	 */
	protected void assertNYI() {
		Assert.assertNotYetImplemented("The calling method has not yet been implemented.");
	}
}
