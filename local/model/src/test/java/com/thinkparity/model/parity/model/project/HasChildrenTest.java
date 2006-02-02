/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.io.File;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * Test the project model hasChildren api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class HasChildrenTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see HasChildrenTest#setUp()
	 * @see HasChildrenTest#tearDown()
	 */
	private class Fixture {
		private final Boolean expectedHasChildren;
		private final UUID projectId;
		private final ProjectModel projectModel;
		private Fixture(final Boolean expectedHasChildren, final UUID projectId, final ProjectModel projectModel) {
			this.expectedHasChildren = expectedHasChildren;
			this.projectId = projectId;
			this.projectModel = projectModel;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a HasChildrenTest.
	 */
	public HasChildrenTest() { super("testHasChildren"); }

	/**
	 * Test the project model hasChildren api.
	 */
	public void testHasChildren() {
		try {
			for(Fixture datum : data) {

				assertEquals(
						datum.expectedHasChildren,
						datum.projectModel.hasChildren(datum.projectId));
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(3);
		final Project testProject = createTestProject("testHasChildren");
		final DocumentModel documentModel = getDocumentModel();
		final ProjectModel projectModel = getProjectModel();
		Project project;
		String name, description;
		
		name = "1";
		description = name;
		project = projectModel.create(testProject.getId(), name, description);
		data.add(new Fixture(Boolean.FALSE, project.getId(), projectModel));

		name = "2";
		description = name;
		project = projectModel.create(testProject.getId(), name, description);
		name = "2.1";
		description = name;
		projectModel.create(project.getId(), name, description);
		name = "2.2";
		description = name;
		projectModel.create(project.getId(), name, description);
		name = "2.3";
		description = name;
		projectModel.create(project.getId(), name, description);
		data.add(new Fixture(Boolean.TRUE, project.getId(), projectModel));

		name = "3";
		description = name;
		project = projectModel.create(testProject.getId(), name, description);
		for(File testFile : getInputFiles()) {
			name = testFile.getName();
			description = name;
			documentModel.create(project.getId(), name, description, testFile);
		}
		data.add(new Fixture(Boolean.TRUE, project.getId(), projectModel));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
