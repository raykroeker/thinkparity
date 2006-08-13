/*
 * Nov 19, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;

/**
 * Test the document model get version content api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class ReadVersionContentTest extends DocumentTestCase {

    /** Test name. */
    private static final String NAME = "[GET VERSION CONTENT TEST]";

	/** Test datum. */
	private Fixture datum;

	/** Create GetVersionContentTest. */
	public ReadVersionContentTest() { super(NAME); }

	/**
	 * Test the document model get version content api.
	 *
	 */
	public void testGetVersionContent() {
	    final DocumentVersionContent versionContent =
            datum.documentModel.getVersionContent(datum.version.getArtifactId(), datum.version.getVersionId());

        assertNotNull(NAME, versionContent);
        assertEquals(NAME, datum.version, versionContent.getVersion());
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
        final File inputFile = getInputFiles()[0];
        final DocumentModel documentModel = getDocumentModel();
        final Document document = createDocument(inputFile);
        final DocumentVersion version = createDocumentVersion(document);
		datum = new Fixture(documentModel, version);
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		datum = null;
		super.tearDown();
	}

	/**
	 * Test data fixture.
	 */
	private class Fixture {
		private final DocumentModel documentModel;
		private final DocumentVersion version;
		private Fixture(final DocumentModel documentModel,
                final DocumentVersion version) {
			this.documentModel = documentModel;
			this.version = version;
		}
	}
}
