/*
 * Aug 6, 2005
 */
package com.thinkparity.ophelia.model.workspace;

import java.util.Vector;


import com.thinkparity.ophelia.model.ModelTestCase;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * Test the workspace model getWorkspace api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class GetWorkspaceTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see GetWorkspaceTest#setUp()
	 * @see GetWorkspaceTest#tearDown()
	 */
	private class Fixture {
		private final WorkspaceModel workspaceModel;
		private Fixture(final WorkspaceModel workspaceModel) {
			this.workspaceModel = workspaceModel;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a GetWorkspaceTest
	 */
	public GetWorkspaceTest() { super("testGetWorkspace"); }

	/**
	 * Test the workspace model getWorkspace api.
	 */
	public void testGetWorkspace() {
		try {
			Workspace workspace;
			for(Fixture datum : data) {
				workspace = datum.workspaceModel.getWorkspace();

				assertNotNull(workspace);
				assertNotNull(workspace.getDataDirectory());
                assertNotNull(workspace.getIndexDirectory());
                assertNotNull(workspace.getPreferences());
                assertNotNull(workspace.getWorkspaceDirectory());
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new Vector<Fixture>(1);
		data.add(new Fixture(getWorkspaceModel()));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
