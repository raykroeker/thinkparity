/*
 * Dec 19, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.model.JUnitTestFile;
import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * Test the project model rename api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RenameTest extends ModelTestCase {

	private class Fixture {
		private final Collection<UUID> documentIds;
		private final DocumentModel documentModel;
		private final String name;
		private final UUID projectId;
		private final ProjectModel projectModel;
		private Fixture(final Collection<UUID> documentIds,
				final DocumentModel documentModel, final String name,
				final UUID projectId, final ProjectModel projectModel) {
			super();
			this.documentIds = documentIds;
			this.documentModel = documentModel;
			this.name = name;
			this.projectId = projectId;
			this.projectModel = projectModel;
		}
	}

	private Vector<Fixture> data;

	/**
	 * Create a RenameTest.
	 * 
	 */
	public RenameTest() { super("testRename"); }

	/**
	 * Test the project model rename api.
	 *
	 */
	public void testRename() {
		try {
			Project project;
			for(Fixture datum : data) {
				datum.projectModel.rename(datum.projectId, datum.name);
				project = datum.projectModel.get(datum.projectId);

				assertNotNull(project.getName());
				assertEquals(datum.name, project.getName());

				for(UUID documentId : datum.documentIds) {
					datum.documentModel.open(documentId);
				}
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new Vector<Fixture>(3);
		final DocumentModel documentModel = getDocumentModel();
		final ProjectModel projectModel = getProjectModel();
		final Project testProject = createTestProject(getName());

		Project project;
		String name, description;

		name = "Project 1";
		description = name + ":  " + getName();
		project = projectModel.create(testProject.getId(), name, description);
		data.add(new Fixture(new Vector<UUID>(0), documentModel, name + ".Rename", project.getId(), projectModel));

		name = "Project 2";
		description = name + ":  " + getName();
		project = projectModel.create(testProject.getId(), name, description);
		Vector<UUID> documentIds = new Vector<UUID>(getJUnitTestFilesSize());
		Document document;
		for(JUnitTestFile testFile : getJUnitTestFiles()) {
			document =documentModel.create(project.getId(), testFile.getName(),
					testFile.getName(), testFile.getFile());
			documentIds.add(document.getId());
		}
		data.add(new Fixture(documentIds, documentModel, "Project 1", project.getId(), projectModel));

		name = "Project 3";
		description = name + ":  " + getName();
		project = projectModel.create(testProject.getId(), name, description);
		data.add(new Fixture(new Vector<UUID>(0), documentModel, name, project.getId(), projectModel));
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		data.clear();
		data = null;
	}

}
