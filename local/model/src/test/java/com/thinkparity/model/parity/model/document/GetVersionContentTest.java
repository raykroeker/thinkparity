/*
 * Nov 19, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Vector;

import com.thinkparity.model.parity.ParityException;

/**
 * Test the document model get version content api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class GetVersionContentTest extends DocumentTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [DOCUMENT] [GET VERSION CONTENT TEST]";

	/** Test data. */
	private Vector<Fixture> data;

	/** Create GetVersionContentTest. */
	public GetVersionContentTest() { super(NAME); }

	/**
	 * Test the document model get version content api.
	 *
	 */
	public void testGetVersionContent() {
		DocumentVersionContent versionContent = null;
		for(Fixture datum : data) {
		    try {
		        versionContent = datum.documentModel.getVersionContent(
                        datum.documentId, datum.versionId);
            }
		    catch(final ParityException px) { fail(createFailMessage(px)); }
			assertNotNull(NAME, versionContent);
			assertEquals(NAME, datum.eVersionContent, versionContent);
		}
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new Vector<Fixture>(4);
		final DocumentModel documentModel = getDocumentModel();
		final File testFile = getInputFile("JUnitTestFramework.txt");
		final Document document = create(testFile);
		DocumentVersion version;
		DocumentVersionContent eVersionContent;

		version = documentModel.createVersion(document.getId());
		eVersionContent = documentModel.getVersionContent(document.getId(), version.getVersionId());
		data.add(new Fixture(document.getId(), documentModel, eVersionContent, version.getVersionId()));

		version = documentModel.createVersion(document.getId());
        eVersionContent = documentModel.getVersionContent(document.getId(), version.getVersionId());
		data.add(new Fixture(document.getId(), documentModel, eVersionContent, version.getVersionId()));

		version = documentModel.createVersion(document.getId());
        eVersionContent = documentModel.getVersionContent(document.getId(), version.getVersionId());
		data.add(new Fixture(document.getId(), documentModel, eVersionContent, version.getVersionId()));
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		super.tearDown();
	}

	/**
	 * Test data fixture.
	 */
	private class Fixture {
		private final Long documentId;
		private final DocumentModel documentModel;
		private final DocumentVersionContent eVersionContent;
		private final Long versionId;
		private Fixture(final Long documentId,
				final DocumentModel documentModel,
				final DocumentVersionContent eVersionContent,
				final Long versionId) {
			this.documentId = documentId;
			this.documentModel = documentModel;
			this.versionId = versionId;
			this.eVersionContent = eVersionContent;
		}
	}
}
