/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;

/**
 * Test the document model list api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class ListTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see ListTest#setUp()
	 * @see ListTest#tearDown()
	 */
	private class Fixture {
		private final DocumentModel documentModel;
		private final Collection<Document> expectedDocumentList;
		private final UUID projectId;
		private Fixture(final DocumentModel documentModel, final Collection<Document> expectedDocumentList, final UUID projectId) {
			this.documentModel = documentModel;
			this.expectedDocumentList = expectedDocumentList;
			this.projectId = projectId;
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
	 * Test the document model list api.
	 */
	public void testList() {
		try {
			Collection<Document> documentList;
			for(Fixture datum : data) {
				documentList = datum.documentModel.list(datum.projectId);
				
				assertNotNull(documentList);
				assertEquals(documentList.size(), datum.expectedDocumentList.size());
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		final Integer scenarioCount = 4;
		data = new Vector<Fixture>(scenarioCount);
		final Project testProject = createTestProject(getName());
		final DocumentModel documentModel = getDocumentModel();
		final ProjectModel projectModel = getProjectModel();
		Collection<Document> documentList;
		Project project;
		String name, description;
		Document document;

		for(int i = 0; i < scenarioCount; i++) {
			name = "p." + i;
			description = "Project:  " + name;
			project = projectModel.create(testProject.getId(), name, description);
			documentList = new Vector<Document>(getJUnitTestFilesSize());
			for(ModelTestFile testFile : getJUnitTestFiles()) {
				name = testFile.getName();
				description = "Document:  " + name;
				document =
					documentModel.create(project.getId(), name, description, testFile.getFile());
				documentList.add(document);
			}
			data.add(new Fixture(documentModel, documentList, project.getId()));
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
