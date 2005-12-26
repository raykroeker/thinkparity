/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.util.UUID;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;

/**
 * Test the project model create api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class CreateTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see CreateTest#setUp()
	 * @see CreateTest#tearDown()
	 */
	private class Fixture {
		private final String description;
		private final String name;
		private final UUID parentId;
		private final ProjectModel projectModel;
		private Fixture(final String name, final String description,
				final UUID parentId, final ProjectModel projectModel) {
			this.name = name;
			this.description = description;
			this.parentId = parentId;
			this.projectModel = projectModel;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a CreateTest.
	 */
	public CreateTest() { super("testCreate"); }

	/**
	 * Test the project model create api.
	 */
	public void testCreate() {
		try {
			for(Fixture datum : data) {
				datum.projectModel.create(datum.parentId, datum.name, datum.description);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		final Project testProject = createTestProject("testCreate");
		final ProjectModel projectModel = getProjectModel();
		data = new Vector<Fixture>(10);
		data.add(new Fixture("Prj.1", "Project1", testProject.getId(), projectModel));
		data.add(new Fixture("Prj.2", "", testProject.getId(), projectModel));
		data.add(new Fixture("Prj.3", null, testProject.getId(), projectModel));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
