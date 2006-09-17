/*
 * Nov 19, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.document.DocumentVersionContent;

import com.thinkparity.ophelia.OpheliaTestUser;

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
	 * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
        final File inputFile = getInputFiles()[0];
        final DocumentModel documentModel = getDocumentModel(OpheliaTestUser.JUNIT);
        final Document document = createDocument(OpheliaTestUser.JUNIT, inputFile);
        final DocumentVersion version = createDocumentVersion(OpheliaTestUser.JUNIT, document);
		datum = new Fixture(documentModel, version);
	}

	/**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
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
