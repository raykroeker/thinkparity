/*
 * Oct 17, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestUser;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class PreferencesTest extends ModelTestCase {

	/**
	 * Create a PreferencesTest.
	 * @param name
	 */
	public PreferencesTest() { super("Preferences test"); }

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private Workspace workspace;
	private Preferences preferences;

	public void testGetUsername() {
		final ModelTestUser modelTestUser = getModelTestUser();
		// workspace exists?
		// user has logged in previously?

		// 0.  false, false
		deleteWorkspace(modelTestUser);
		workspace = getWorkspaceModel().getWorkspace();
		
		preferences = workspace.getPreferences();

		// 1.  false, true (NOT POSSIBLE)
		// 2.  true, false
		preferences = workspace.getPreferences();
		// 3.  true, true
		preferences = workspace.getPreferences();
	}
}
