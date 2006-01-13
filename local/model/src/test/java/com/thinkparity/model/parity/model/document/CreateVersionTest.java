/*
 * Nov 19, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.Iterator;
import java.util.Vector;

import com.thinkparity.model.JUnitTestFile;
import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.parity.model.project.Project;

/**
 * Test the document model create version api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreateVersionTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 */
	private class Fixture {
		private final DocumentAction action;
		private final DocumentActionData actionData;
		private final DocumentContent content;
		private final Document document;
		private final DocumentModel documentModel;
		private Fixture(final DocumentAction action,
				final DocumentActionData actionData,
				final DocumentContent content, final Document document,
				final DocumentModel documentModel) {
			this.action = action;
			this.actionData = actionData;
			this.content = content;
			this.document = document;
			this.documentModel = documentModel;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a CreateVersionTest.
	 */
	public CreateVersionTest() { super("testCreateVersion"); }

	/**
	 * Test the document model create version api.
	 */
	public void testCreateVersion() {
		try {
			Iterator<DocumentVersion> iVersions;
			DocumentVersion version;
			DocumentVersionContent versionContent;
			for(Fixture datum : data) {
				datum.documentModel.createVersion(
						datum.document.getId(), datum.action, datum.actionData);
				// the version we want to compare to will be the last one in
				// the list
				iVersions =
					datum.documentModel.listVersions(datum.document.getId()).iterator();
				version = null;
				while(iVersions.hasNext()) { version = iVersions.next(); }

				assertNotNull(version);
				assertEquals(datum.action, version.getAction());
				assertEquals(datum.actionData, version.getActionData());
				assertEquals(datum.document.getId(), version.getDocumentId());
				assertEquals(datum.document, version.getSnapshot());

				versionContent = datum.documentModel.getVersionContent(datum.document.getId(), version.getVersionId());
				assertNotNull(versionContent);
				assertEquals(datum.document.getId(), versionContent.getDocumentId());
				assertEquals(datum.content, versionContent.getSnapshot());
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(getJUnitTestFilesSize());
		final Project testProject = createTestProject(getName());
		final DocumentModel documentModel = getDocumentModel();
		final DocumentAction action = DocumentAction.CREATE;
		DocumentActionData actionData;
		String name, description;
		Document document;
		DocumentContent content;

		for(JUnitTestFile testFile : getJUnitTestFiles()) {
			actionData = new DocumentActionData();
			actionData.setDataItem("now", "" + System.currentTimeMillis());

			name = testFile.getName();
			description = name;
			document = documentModel.create(testProject.getId(), name, description, testFile.getFile());
			content = documentModel.getContent(document.getId());
			data.add(new Fixture(action, actionData, content, document, documentModel));
		}
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
