/*
 * Nov 18, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;

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
	    datum.documentModel.openVersion(datum.version.getArtifactId(), datum.version.getVersionId());
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final File inputFile = getInputFile("JUnitTestFramework.txt");
		final DocumentModel documentModel = getDocumentModel();
		final Document document = createDocument(inputFile);
        modifyDocument(document);
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

	/** Test datum definition. */
	private class Fixture {
		private final DocumentModel documentModel;
		private final DocumentVersion version;
		private Fixture(final DocumentModel documentModel, final DocumentVersion version) {
			this.documentModel = documentModel;
			this.version = version;
		}
	}
}
