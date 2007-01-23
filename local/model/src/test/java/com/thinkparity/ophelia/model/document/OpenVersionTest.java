/*
 * Nov 18, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;

import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.util.Opener;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * Test the document model open version api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class OpenVersionTest extends DocumentTestCase {

    /** Test name. */
    private static final String NAME = "[TEST OPEN VERSION]";

	/** Test datum. */
	private Fixture datum;

	/** Create OpenVersionTest. */
	public OpenVersionTest() { super(NAME); }

	/**
	 * Test the document model open version api.
	 *
	 */
	public void testOpenVersion() {
        final Opener opener = new Opener() {
            public void open(final File file) {
                assertNotNull("File to open is null.", file);
                assertTrue("File to open does not exist.", file.exists());
            }
        };
	    datum.documentModel.openVersion(datum.version.getArtifactId(), datum.version.getVersionId(), opener);
	}

	/**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final File inputFile = getInputFile("JUnitTestFramework.txt");
		final DocumentModel documentModel = getDocumentModel(OpheliaTestUser.JUNIT);
		final Document document = createDocument(OpheliaTestUser.JUNIT, inputFile);
        modifyDocument(OpheliaTestUser.JUNIT, document.getId());
        final DocumentVersion version = createDocumentVersion(OpheliaTestUser.JUNIT, document, currentDateTime());
		datum = new Fixture(documentModel, version);
	}

	/**
	 * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		datum = null;
		super.tearDown();
	}

	/** Test datum definition. */
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
