/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.util.UUID;
import java.util.Vector;

import com.thinkparity.model.JUnitTestFile;
import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * Test the project model delete api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class DeleteTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see DeleteTest#setUp()
	 * @see DeleteTest#tearDown()
	 */
	private class Fixture {
		private final UUID projectId;
		private final ProjectModel projectModel;
		private Fixture(final UUID projectId,
				final ProjectModel projectModel) {
			this.projectId = projectId;
			this.projectModel = projectModel;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a DeleteTest.
	 */
	public DeleteTest() { super("testDelete"); }

	/**
	 * Test the project model delete api.
	 */
	public void testDelete() {
		try {
			Project project;
			for(Fixture datum : data) {
				datum.projectModel.delete(datum.projectId);

				project = datum.projectModel.get(datum.projectId);
				assertNull(project);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		final ProjectModel projectModel = getProjectModel();
		final DocumentModel documentModel = getDocumentModel();
		final Project testProject = createTestProject("testDelete");
		data = new Vector<Fixture>(3);
		String name, description;
		Project project;

		// scenario 1:  simple project
		name = "Prj.1";
		description = name;
		project = projectModel.create(testProject.getId(), name, description);
		data.add(new Fixture(project.getId(), projectModel));

		// scenario 2:  simple project with sub-projects
		name = "Prj.2";
		description = name;
		project = projectModel.create(testProject.getId(), name, description);
		projectModel.create(project.getId(), name + "Sub1", description + "Sub1");
		projectModel.create(project.getId(), name + "Sub2", description + "Sub2");
		projectModel.create(project.getId(), name + "Sub3", description + "Sub3");
		projectModel.create(project.getId(), name + "Sub4", description + "Sub4");
		data.add(new Fixture(project.getId(), projectModel));

		// scenario 3:  simple project with sub-documents
		name = "Prj.3";
		description = name;
		project = projectModel.create(testProject.getId(), name, description);
		for(JUnitTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			documentModel.create(project.getId(), name, description, testFile.getFile());
		}
		data.add(new Fixture(project.getId(), projectModel));

		// scenario 4:  simple project with sub-documents & sub-projects
		name = "Prj.4";
		description = name;
		project = projectModel.create(testProject.getId(), name, description);
		projectModel.create(project.getId(), name + "Sub1", description + "Sub1");
		projectModel.create(project.getId(), name + "Sub2", description + "Sub2");
		projectModel.create(project.getId(), name + "Sub3", description + "Sub3");
		projectModel.create(project.getId(), name + "Sub4", description + "Sub4");
		for(JUnitTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			documentModel.create(project.getId(), name, description, testFile.getFile());
		}
		data.add(new Fixture(project.getId(), projectModel));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
