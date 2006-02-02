/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;

/**
 * Test the project model list api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class ListTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see ListTest#setUp()
	 * @see ListTest#tearDown()
	 */
	private class Fixture {
		private final Collection<Project> expectedProjects;
		private final UUID parentId;
		private final ProjectModel projectModel;
		private Fixture(final Collection<Project> expectedProjects,
				final UUID parentId, final ProjectModel projectModel) {
			this.expectedProjects = expectedProjects;
			this.parentId = parentId;
			this.projectModel = projectModel;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a ListTest.
	 */
	public ListTest() { super("testList"); }

	/**
	 * Test the project model list api.
	 */
	public void testList() {
		try {
			Collection<Project> projectList;
			for(Fixture datum : data) {
				if(null == datum.parentId) {
					projectList = datum.projectModel.list();
				}
				else {
					projectList = datum.projectModel.list(datum.parentId);
				}

				assertNotNull(projectList);
				assertEquals(datum.expectedProjects.size(), projectList.size());
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(4);
		final Project testProject = createTestProject("testList");
		final ProjectModel projectModel = getProjectModel();
		Collection<Project> expectedProjectList;
		String name, description;

		expectedProjectList = new Vector<Project>(1);
		expectedProjectList.add(projectModel.getInbox());
		expectedProjectList.add(projectModel.getMyProjects());
		data.add(new Fixture(expectedProjectList, null, projectModel));

		expectedProjectList = new Vector<Project>(3);
		name = "p.0";
		description = "Project:  " + name;
		expectedProjectList.add(projectModel.create(testProject.getId(), name, description));
		name = "p.1";
		description = "Project:  " + name;
		expectedProjectList.add(projectModel.create(testProject.getId(), name, description));
		name = "p.2";
		description = "Project:  " + name;
		expectedProjectList.add(projectModel.create(testProject.getId(), name, description));
		data.add(new Fixture(expectedProjectList, testProject.getId(), projectModel));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
