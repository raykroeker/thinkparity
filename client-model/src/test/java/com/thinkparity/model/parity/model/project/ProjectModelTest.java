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

	/**
	 * CreateProjectData
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class CreateProjectData {
		private String description;
		private String name;
		private CreateProjectData(final String name, final String description) {
			this.name = name;
			this.description = description;
		}
	}

	private Vector<CreateProjectData> testCreateProjectData;

	/**
	 * Create a ProjectModelTest
	 * @param name
	 */
	public ProjectModelTest() {
		super("Test:  Project model.");
	}

	public void testCreateProject() {
		Project rootProject = null;
		try { rootProject = getRootProject(); }
		catch(ParityException px) { fail(px.getMessage()); }

		for (Iterator<CreateProjectData> i = testCreateProjectData.iterator(); i
				.hasNext();) {
			testCreateProject(rootProject, i.next());
		}
	}

	/**
	 * Obtain the root project for the specified workspace.
	 */
	public void testGetRootProject() {
		try {
			final Project rootProject = getRootProject();
			assertNotNull(rootProject);
			assertNotNull(rootProject.getMetaDataDirectory());
			assertNotNull(rootProject.getMetaDataFile());
		}
		catch(ParityException px) { fail(px.getMessage()); }
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

	private Project getRootProject() throws ParityException {
		return getProjectModel().getRootProject(getWorkspace());
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
}
