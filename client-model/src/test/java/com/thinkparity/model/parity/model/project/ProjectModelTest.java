/*
 * Aug 8, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.util.Vector;

import com.thinkparity.model.ModelTestCase;

/**
 * ProjectModelTest
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProjectModelTest extends ModelTestCase {

	/**
	 * Test data definition for the create project test.
	 * @see ProjectModelTest#setUpCreateProject()
	 * @see ProjectModelTest#testCreateProject()
	 */
	private class CreateProjectData {
		private final String description;
		private final String name;
		private final Project parent;
		private final ProjectModel projectModel;
		private CreateProjectData(final String name, final String description,
				final Project parent, final ProjectModel projectModel) {
			this.name = name;
			this.description = description;
			this.parent = parent;
			this.projectModel = projectModel;
		}
	}

	/**
	 * Test data for the create project test.
	 */
	private Vector<CreateProjectData> createProjectData;

	/**
	 * Create a ProjectModelTest
	 * @param name
	 */
	public ProjectModelTest() {
		super("Test:  Project model.");
	}

	public void testCreateProject() {
		try {
			for(CreateProjectData data : createProjectData) {
				data.projectModel.createProject(data.parent, data.name, data.description);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	public void testGetRootProject() {
		try {
			final Project rootProject = getRootProject();
			assertNotNull(rootProject);
			assertNotNull(rootProject.getMetaDataDirectory());
			assertNotNull(rootProject.getMetaDataFile());
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		setUpCreateProject();
	}

	/**
	 * Initialize the test data for testCreateProject.
	 * 
	 * @throws Exception
	 */
	protected void setUpCreateProject() throws Exception {
		final Project parent = createTestProject("testCeateProject");
		final ProjectModel projectModel = getProjectModel();
		createProjectData = new Vector<CreateProjectData>(10);
		createProjectData.add(new CreateProjectData("Prj.0", "Project 0", parent, projectModel));
		createProjectData.add(new CreateProjectData("Prj.1", "", parent, projectModel));
		createProjectData.add(new CreateProjectData("Prj.2", null, parent, projectModel));
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownCreateProject();
	}

	/**
	 * @see ProjectModelTest#tearDown()
	 * @see ProjectModelTest#testCreateProject()
	 */
	protected void tearDownCreateProject() {
		createProjectData.clear();
		createProjectData = null;
	}
}
