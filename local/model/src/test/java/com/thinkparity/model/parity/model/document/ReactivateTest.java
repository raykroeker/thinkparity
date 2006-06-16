/*
 * Created On: Thu Jun 01 2006 17:15 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;

import com.thinkparity.model.parity.ParityException;

/**
 * Test the parity document interface rename api.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class ReactivateTest extends DocumentTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [DOCUMENT] [REACTIVATE TEST]";

    /** The test data. */
	private Fixture datum;
	
	/** Create CloseTest. */
	public ReactivateTest() { super(NAME); }

    /** Test the parity document interface close api. */ 
	public void testRename() {
        try { datum.dModel.reactivate(datum.documentId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
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
        addTeam(d0);
        dModel.close(d0.getId());
        datum = new Fixture(dModel, d0.getId());
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
		private Fixture(final DocumentModel dModel, final Long documentId) {
			this.dModel = dModel;
			this.documentId = documentId;
		}
	}
}
