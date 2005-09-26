/*
 * Aug 6, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import java.net.URL;

import com.thinkparity.model.parity.ParityTest;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * WorkspaceModelTest
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class WorkspaceModelTest extends ParityTest {

	/**
	 * Create a WorkspaceModelTest
	 * @param name
	 */
	public WorkspaceModelTest() {
		super("Test:  Workspace model.");
	}

	/**
	 * Test the workspace reference.
	 * 
	 */
	public void testGetWorkspace() {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		WorkspaceModelTest.assertNotNull(workspace);
		final URL workspaceURL = workspace.getWorkspaceURL();
		final URL workspaceDataURL = workspace.getDataURL();
		WorkspaceModelTest.assertNotNull(workspaceURL);
		WorkspaceModelTest.assertNotNull(workspaceDataURL);
		WorkspaceModelTest.assertEquals(getExpectedWorkspaceDataURL(), workspaceDataURL);
		WorkspaceModelTest.assertEquals(getExpectedWorkspaceURL(), workspaceURL);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception { super.setUp(); }

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception { super.tearDown(); }
}
