/*
 * Aug 6, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import java.net.URL;

import com.thinkparity.model.ModelTestCase;

/**
 * WorkspaceModelTest
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class WorkspaceModelTest extends ModelTestCase {

	private WorkspaceModel workspaceModel;

	/**
	 * Create a WorkspaceModelTest
	 * @param name
	 */
	public WorkspaceModelTest() { super("Test:  Workspace model."); }

	/**
	 * Test getWorkspace()
	 *
	 */
	public void testGetWorkspace() {
		final Workspace workspace = workspaceModel.getWorkspace();
		WorkspaceModelTest.assertNotNull("Workspace is null.", workspace);

		final URL dataURL = workspace.getDataURL();
		WorkspaceModelTest.assertNotNull(dataURL);

		final URL loggerURL = workspace.getLoggerURL();
		WorkspaceModelTest.assertNotNull(loggerURL);

		final Preferences prefs = workspace.getPreferences();
		WorkspaceModelTest.assertNotNull(prefs);

		final URL url = workspace.getWorkspaceURL();
		WorkspaceModelTest.assertNotNull(url);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		workspaceModel = getWorkspaceModel();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		workspaceModel = null;
	}
}
