/*
 * Nov 19, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.util.UUID;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.parity.model.project.Project;

/**
 * Test the document model get version content api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class GetVersionContentTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 */
	private class Fixture {
		private final UUID documentId;
		private final DocumentModel documentModel;
		private final DocumentVersionContent versionContent;
		private final String versionId;
		private Fixture(final UUID documentId,
				final DocumentModel documentModel,
				final DocumentVersionContent versionContent,
				final String versionId) {
			this.documentId = documentId;
			this.documentModel = documentModel;
			this.versionId = versionId;
			this.versionContent = versionContent;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a GetVersionContentTest.
	 * @param name
	 */
	public GetVersionContentTest() { super("testGetVersionContent"); }

	/**
	 * Test the document model get version content api.
	 *
	 */
	public void testGetVersionContent() {
		try {
			DocumentVersionContent versionContent;
			for(Fixture datum : data) {
				versionContent =
					datum.documentModel.getVersionContent(datum.documentId, datum.versionId);
				assertNotNull(versionContent);
				assertEquals(datum.versionContent.getDocumentId(), versionContent.getDocumentId());
				assertEquals(datum.versionContent.getSnapshot(), versionContent.getSnapshot());
				assertEquals(datum.versionContent.getVersionId(), versionContent.getVersionId());
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(4);
		final Project testProject = createTestProject(getName());
		final DocumentModel documentModel = getDocumentModel();
		final ModelTestFile testFile =
			getJUnitTestFile("JUnit Test Framework.txt");
		final String name = testFile.getName();
		final String description = name;
		final Document document =
			documentModel.create(testProject.getId(), name, description, testFile.getFile());
		DocumentVersion version;
		DocumentVersionContent versionContent;

		version = documentModel.createVersion(
					document.getId(), DocumentAction.CREATE, new DocumentActionData());
		versionContent = documentModel.getVersionContent(document.getId(), version.getVersionId());
		data.add(new Fixture(document.getId(), documentModel, versionContent, version.getVersionId()));

		version = documentModel.createVersion(
				document.getId(), DocumentAction.CREATE, new DocumentActionData());
		versionContent = documentModel.getVersionContent(document.getId(), version.getVersionId());
		data.add(new Fixture(document.getId(), documentModel, versionContent, version.getVersionId()));

		version = documentModel.createVersion(
				document.getId(), DocumentAction.CREATE, new DocumentActionData());
		versionContent = documentModel.getVersionContent(document.getId(), version.getVersionId());
		data.add(new Fixture(document.getId(), documentModel, versionContent, version.getVersionId()));
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
