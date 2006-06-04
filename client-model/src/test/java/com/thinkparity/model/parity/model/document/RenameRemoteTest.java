/*
 * Created On: Fri Jun 02 2006 12:17 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;

import com.thinkparity.codebase.assertion.TrueAssertion;

import com.thinkparity.model.parity.ParityException;

/**
 * Test the parity document interface rename api once a document has been sent.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class RenameRemoteTest extends DocumentTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [DOCUMENT] [RENAME REMOTE TEST]";

    /** The test data. */
	private Fixture datum;
	
	/** Create RenameRemoteTest. */
	public RenameRemoteTest() { super(NAME); }

    /** Test the parity document interface close api. */ 
	public void testRename() {
        testLogger.info(NAME + " [TEST RENAME]");
        Boolean didAssert = Boolean.FALSE;

        try { datum.dModel.rename(datum.documentId, datum.documentName); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
        catch(final TrueAssertion ta) { didAssert = Boolean.TRUE; }

        if(!didAssert) { fail(NAME + " [DID NOT ASSERT]"); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final DocumentModel dModel = getDocumentModel();

        // 0:  a new document with a name
        final File file0 = getInputFiles()[0];
        final Document d0 = dModel.create(file0.getName(), file0.getName(), file0);
        addTeam(d0.getId());
        datum = new Fixture(dModel, d0.getId(), "New " + file0.getName());
	}

	/**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		datum = null;
		super.tearDown();
	}

	private class Fixture {
		private final DocumentModel dModel;
		private final Long documentId;
        private final String documentName;
		private Fixture(final DocumentModel dModel, final Long documentId,
                final String documentName) {
			this.dModel = dModel;
			this.documentId = documentId;
            this.documentName = documentName;
		}
	}
}
