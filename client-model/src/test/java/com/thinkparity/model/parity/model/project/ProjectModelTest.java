/*
 * Aug 8, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.util.Collection;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * ProjectModelTest
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProjectModelTest extends ModelTestCase {

	private class CreateData {
		private final String description;
		private final String name;
		private final Project parent;
		private final ProjectModel projectModel;
		private CreateData(final String name, final String description,
				final Project parent, final ProjectModel projectModel) {
			this.name = name;
			this.description = description;
			this.parent = parent;
			this.projectModel = projectModel;
		}
	}

	private class DeleteData {
		private final Project project;
		private final ProjectModel projectModel;
		private DeleteData(final Project project,
				final ProjectModel projectModel) {
			this.project = project;
			this.projectModel = projectModel;
		}
	}
	
	private class ListData {
		private final Collection<Project> expectedProjects;
		private final Project parent;
		private final ProjectModel projectModel;
		private ListData(final Collection<Project> expectedProjects,
				final Project parent, final ProjectModel projectModel) {
			this.expectedProjects = expectedProjects;
			this.parent = parent;
			this.projectModel = projectModel;
		}
	}

	private Vector<CreateData> createData;
	private Vector<DeleteData> deleteData;
	private Vector<ListData> listData;

	/**
	 * Create a ProjectModelTest
	 */
	public ProjectModelTest() { super("Test:  Project model"); }

	public void testCreate() {
		try {
			for(CreateData data : createData) {
				data.projectModel.create(data.parent, data.name, data.description);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}
	public void testDelete() {
		try {
			for(DeleteData data : deleteData) {
				data.projectModel.delete(data.project);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	public void testGetInbox() {
		try {
			final Project inbox = getProjectModel().getInbox();
			assertNotNull(inbox);
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	public void testGetMyProjects() {
		try {
			final Project myProjects = getProjectModel().getMyProjects();
			assertNotNull(myProjects);
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	public void testList() {
		try {
			Collection<Project> projectList;
			for(ListData data : listData) {
				if(null == data.parent) {
					projectList = data.projectModel.list();
				}
				else {
					projectList = data.projectModel.list(data.parent);
				}
				ProjectModelTest.assertNotNull(projectList);
				ProjectModelTest.assertEquals(
						data.expectedProjects.size(),
						projectList.size());
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		setUpCreate();
		setUpDelete();
		setUpList();
	}

	/**
	 * Create create data structure.
	 * 
	 * @throws Exception
	 */
	protected void setUpCreate() throws Exception {
		final Project testProject = createTestProject("testCreate");
		final ProjectModel projectModel = getProjectModel();
		createData = new Vector<CreateData>(10);
		createData.add(new CreateData("Prj.1", "Project1", testProject, projectModel));
		createData.add(new CreateData("Prj.2", "", testProject, projectModel));
		createData.add(new CreateData("Prj.3", null, testProject, projectModel));
	}

	/**
	 * Set up delete data structure.
	 * 
	 * @throws Exception
	 */
	protected void setUpDelete() throws Exception {
		final ProjectModel projectModel = getProjectModel();
		final DocumentModel documentModel = getDocumentModel();
		final Project testProject = createTestProject("testDelete");
		deleteData = new Vector<DeleteData>(3);
		String name, description;
		Project project;

		// scenario 1:  simple project
		name = "Prj.1";
		description = name;
		project = projectModel.create(testProject, name, description);
		deleteData.add(new DeleteData(project, projectModel));

		// scenario 2:  simple project with sub-projects
		name = "Prj.2";
		description = name;
		project = projectModel.create(testProject, name, description);
		projectModel.create(project, name + "Sub1", description + "Sub1");
		projectModel.create(project, name + "Sub2", description + "Sub2");
		projectModel.create(project, name + "Sub3", description + "Sub3");
		projectModel.create(project, name + "Sub4", description + "Sub4");
		deleteData.add(new DeleteData(project, projectModel));

		// scenario 3:  simple project with sub-documents
		name = "Prj.3";
		description = name;
		project = projectModel.create(testProject, name, description);
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			documentModel.create(project, name, description, testFile.getFile());
		}
		deleteData.add(new DeleteData(project, projectModel));

		// scenario 4:  simple project with sub-documents & sub-projects
		name = "Prj.4";
		description = name;
		project = projectModel.create(testProject, name, description);
		projectModel.create(project, name + "Sub1", description + "Sub1");
		projectModel.create(project, name + "Sub2", description + "Sub2");
		projectModel.create(project, name + "Sub3", description + "Sub3");
		projectModel.create(project, name + "Sub4", description + "Sub4");
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			documentModel.create(project, name, description, testFile.getFile());
		}
		deleteData.add(new DeleteData(project, projectModel));
	}

	protected void setUpList() throws Exception {
		listData = new Vector<ListData>(4);
		final Project testProject = createTestProject("testList");
		final ProjectModel projectModel = getProjectModel();
		Collection<Project> expectedProjectList;
		String name, description;

		expectedProjectList = new Vector<Project>(1);
		expectedProjectList.add(projectModel.getInbox());
		expectedProjectList.add(projectModel.getMyProjects());
		listData.add(new ListData(expectedProjectList, null, projectModel));

		expectedProjectList = new Vector<Project>(3);
		name = "p.0";
		description = "Project:  " + name;
		expectedProjectList.add(projectModel.create(testProject, name, description));
		name = "p.1";
		description = "Project:  " + name;
		expectedProjectList.add(projectModel.create(testProject, name, description));
		name = "p.2";
		description = "Project:  " + name;
		expectedProjectList.add(projectModel.create(testProject, name, description));
		listData.add(new ListData(expectedProjectList, testProject, projectModel));
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownCreate();
		tearDownDelete();
		tearDownList();
	}

	/**
	 * Clean up create data structure.
	 * 
	 * @throws Exception
	 */
	protected void tearDownCreate() throws Exception {
		createData.clear();
		createData = null;
	}

	/**
	 * Clean up delete data structure.
	 * 
	 * @throws Exception
	 */
	protected void tearDownDelete() throws Exception {
		deleteData.clear();
		deleteData = null;
	}

	protected void tearDownList() throws Exception {
		listData.clear();
		listData = null;
	}
}
