/*
 * Aug 8, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.util.Iterator;
import java.util.Vector;

import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * ProjectModelTest
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProjectModelTest extends ModelTestCase {

	private Vector<CreateProjectData> testCreateProjectData;

	/**
	 * Create a ProjectModelTest
	 * @param name
	 */
	public ProjectModelTest() {
		super("Test:  Project model.");
	}

	/**
	 * Obtain the root project for the specified workspace.
	 */
	public void testGetRootProject() {
		try {
			final Project rootProject =
				ProjectModel.getRootProject(getWorkspace());
			assertNotNull(rootProject);
			assertNotNull(rootProject.getMetaDataDirectory());
			assertNotNull(rootProject.getMetaDataFile());
		}
		catch(ParityException px) { fail(px.getMessage()); }
	}

	public void testCreateProject() {
		Project rootProject = null;
		try { rootProject = ProjectModel.getRootProject(getWorkspace()); }
		catch(ParityException px) { fail(px.getMessage()); }

		for (Iterator<CreateProjectData> i = testCreateProjectData.iterator(); i
				.hasNext();) {
			testCreateProject(rootProject, i.next());
		}
	}

	private void testCreateProject(final Project parentProject,
			final CreateProjectData createProjectData) {
		try {
			getProjectModel().createProject(parentProject, createProjectData.name,
					createProjectData.description);
		}
		catch(ParityException px) {
			fail(px.getMessage() + Separator.SystemNewLine +
					"name:  " + createProjectData.name + Separator.SystemNewLine +
					"description:  " + createProjectData.description);
		}
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this.testCreateProjectData = new Vector<CreateProjectData>(10);
		this.testCreateProjectData.add(new CreateProjectData(
				getUniqueProjectName(0), ""));
		this.testCreateProjectData.add(new CreateProjectData(
				getUniqueProjectName(2), "JUnit test create project."));
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * CreateProjectData
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class CreateProjectData {
		private String name;
		private String description;
		private CreateProjectData(final String name, final String description) {
			this.name = name;
			this.description = description;
		}
	}
}
