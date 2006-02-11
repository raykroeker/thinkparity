/*
 * Nov 19, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;

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
		private final Long documentId;
		private final DocumentModel documentModel;
		private final DocumentVersionContent versionContent;
		private final Long versionId;
		private Fixture(final Long documentId,
				final DocumentModel documentModel,
				final DocumentVersionContent versionContent,
				final Long versionId) {
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
				assertEquals(datum.versionContent.getDocumentContent(), versionContent.getDocumentContent());
				assertEquals(datum.versionContent.getVersionId(), versionContent.getVersionId());
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new Vector<Fixture>(4);
		final DocumentModel documentModel = getDocumentModel();
		final File testFile = getInputFile("JUnitTestFramework.txt");
		final String name = testFile.getName();
		final String description = name;
		final Document document =
			documentModel.create(name, description, testFile);
		DocumentVersion version;
		DocumentVersionContent versionContent;

		version = documentModel.createVersion(document.getId());
		versionContent = documentModel.getVersionContent(document.getId(), version.getVersionId());
		data.add(new Fixture(document.getId(), documentModel, versionContent, version.getVersionId()));

		version = documentModel.createVersion(document.getId());
		versionContent = documentModel.getVersionContent(document.getId(), version.getVersionId());
		data.add(new Fixture(document.getId(), documentModel, versionContent, version.getVersionId()));

		version = documentModel.createVersion(document.getId());
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
