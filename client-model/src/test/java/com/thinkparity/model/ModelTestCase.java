/*
 * Aug 6, 2005
 */
package com.thinkparity.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import junit.framework.TestCase;

import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * ModelTestCase
 * Abstract root for all of the parity test cases.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ModelTestCase extends TestCase {

	/**
	 * Helper class for the parity test cases.  Used to offload the
	 * implementation of the data retreival functionality.
	 */
	private final ModelTestCaseHelper helper;


	/**
	 * Create a ModelTestCase
	 * @param name
	 */
	protected ModelTestCase(String name) {
		super(name);
		this.helper = new ModelTestCaseHelper(this);
	}

	protected void deleteWorkspace(final ModelTestUser modelTestUser) {
		helper.deleteWorkspace(modelTestUser);
	}

	protected URL getExpectedWorkspaceDataURL() {
		return helper.getExpectedWorkspaceDataURL();
	}

	protected URL getExpectedWorkspaceURL() {
		return helper.getExpectedWorkspaceURL();
	}

	protected ModelTestUser getModelTestUser() {
		return helper.getModelTestUserJUnit0();
	}

	protected ProjectModel getProjectModel() {
		return ProjectModel.getModel();
	}

	protected String getUniqueProjectName(final Integer index) {
		return helper.getUniqueProjectName(index);
	}

	protected Workspace getWorkspace() {
		return getWorkspaceModel().getWorkspace();
	}

	protected WorkspaceModel getWorkspaceModel() {
		return WorkspaceModel.getModel();
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception { super.setUp(); }

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception { super.tearDown(); }

	protected String getFailMessage(final Throwable t) {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter, true);
		t.printStackTrace(printWriter);
		return stringWriter.toString();
	}
}
