/*
 * Created On: Jun 6, 2006 9:02:27 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;

import com.thinkparity.model.parity.ParityException;

/**
 * Test the document model delete api; where the user is the key holder; and the
 * document has not been distributed.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class DeleteAsKeyHolderNotDistributed extends DocumentTestCase {

    private static final String NAME = "[LMODEL] [DOCUMENT MODEL] [DELETE AS KEY HOLDER NOT DISTRIBUTED]";
	/** Test data. */
	private Fixture datum;

	/** Create DeleteAsNotKeyHolder. */
	public DeleteAsKeyHolderNotDistributed() { super(NAME); }

	/** Test the parity document interface delete api. */
	public void testDeleteAsNotKeyHolder() {
        testLogger.info(NAME);
        Document d;
        try { datum.dModel.delete(datum.documentId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        d = null;
        try { d = datum.dModel.get(datum.documentId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
        assertNull(NAME +"[DOCUMENT NOT NULL]",  d);
	}

	/** @see junit.framework.TestCase#setUp() */
	protected void setUp() throws Exception {
		super.setUp();
		final DocumentModel dModel = getDocumentModel();

        final File file1 = getInputFiles()[1];
		final Document d1 = create(file1);
        datum = new Fixture(dModel, d1.getId());
	}

    /** @see junit.framework.TestCase#tearDown() */
	protected void tearDown() throws Exception {
		datum = null;
		super.tearDown();
	}

	/** Test data fixture. */
	private class Fixture {
		private final DocumentModel dModel;
		private final Long documentId;
		private Fixture(final DocumentModel dModel, final Long documentId) {
			this.dModel = dModel;
			this.documentId = documentId;
		}
	}
}
