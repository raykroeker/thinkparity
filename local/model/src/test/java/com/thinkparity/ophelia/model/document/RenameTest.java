/*
 * Created On: Thu Jun 01 2006 17:15 PDT
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;

import com.thinkparity.ophelia.model.document.Document;
import com.thinkparity.ophelia.model.document.DocumentModel;

/**
 * Test the parity document interface rename api.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class RenameTest extends DocumentTestCase {

    /** The test name. */
    private static final String NAME = "[RENAME TEST]";

    /** The test data. */
	private Fixture datum;
	
	/** Create RenameTest. */
	public RenameTest() { super(NAME); }

    /** Test the parity document interface close api. */ 
	public void testRename() {
        datum.documentModel.rename(datum.document.getId(), datum.documentName);
        final Document document = datum.documentModel.get(datum.document.getId());
        assertEquals(NAME + " [DOCUMENT NAME DOES NOT MATCH EXPECTATION]", datum.documentName, document.getName());
	}

	/**
	 * @see com.thinkparity.ophelia.model.document.DocumentTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final File inputFile = getInputFiles()[0];
        final DocumentModel documentModel = getDocumentModel();
        final Document document = createDocument(inputFile);
        datum = new Fixture(document, documentModel, System.currentTimeMillis()
                + "." + document.getName());
	}

	/**
	 * @see com.thinkparity.ophelia.model.document.DocumentTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		datum = null;
		super.tearDown();
	}

	private class Fixture {
		private final Document document;
		private final DocumentModel documentModel;
        private final String documentName;
		private Fixture(final Document document,
                final DocumentModel documentModel, final String documentName) {
			this.documentModel = documentModel;
			this.document = document;
            this.documentName = documentName;
		}
	}
}
