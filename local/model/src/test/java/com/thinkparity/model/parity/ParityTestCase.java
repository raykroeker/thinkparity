/*
 * Aug 6, 2005
 */
package com.thinkparity.model.parity;

import java.net.URL;

import junit.framework.TestCase;

import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * ParityTestCase
 * Abstract root for all of the parity test cases.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ParityTestCase extends TestCase {

	/**
	 * Helper class for the parity test cases.  Used to offload the
	 * implementation of the data retreival functionality.
	 */
	private final ParityTestCaseHelper parityTestHelper;

	/**
	 * Workspace for the parity software;
	 */
	protected Workspace workspace;

	/**
	 * Handle to the project model's api.
	 */
	protected final ProjectModel projectModel;

	/**
	 * Create a ParityTestCase
	 * @param name
	 */
	protected ParityTestCase(String name) {
		super(name);
		this.parityTestHelper = new ParityTestCaseHelper();
		this.projectModel = ProjectModel.getModel();
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this.workspace = parityTestHelper.getWorkspace();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		this.workspace = null;
	}

	protected URL getExpectedWorkspaceURL() {
		return parityTestHelper.getExpectedWorkspaceURL();
	}

	protected URL getExpectedWorkspaceDataURL() {
		return parityTestHelper.getExpectedWorkspaceDataURL();
	}

	protected String getUniqueProjectName(final Integer index) {
		return parityTestHelper.getUniqueProjectName(index);
	}
}
