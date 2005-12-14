/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
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
		private final Project project;
		private final ProjectModel projectModel;
		private Fixture(final Boolean expectedHasChildren, final Project project, final ProjectModel projectModel) {
			this.expectedHasChildren = expectedHasChildren;
			this.project = project;
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
						datum.projectModel.hasChildren(datum.project));
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
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
		project = projectModel.create(testProject, name, description);
		data.add(new Fixture(Boolean.FALSE, project, projectModel));

		name = "2";
		description = name;
		project = projectModel.create(testProject, name, description);
		name = "2.1";
		description = name;
		projectModel.create(project, name, description);
		name = "2.2";
		description = name;
		projectModel.create(project, name, description);
		name = "2.3";
		description = name;
		projectModel.create(project, name, description);
		data.add(new Fixture(Boolean.TRUE, project, projectModel));

		name = "3";
		description = name;
		project = projectModel.create(testProject, name, description);
		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			documentModel.create(project.getId(), name, description, testFile.getFile());
		}
		data.add(new Fixture(Boolean.TRUE, project, projectModel));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
