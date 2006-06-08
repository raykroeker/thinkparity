/*
 * Created On: Jun 6, 2006 9:02:27 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.ParityException;

/**
 * Test the document model delete api; where the user is not the key holder.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class DeleteAsNotKeyHolder extends DocumentTestCase {

    private static final String NAME = "[LMODEL] [DOCUMENT MODEL] [DELETE AS NOT KEY HOLDER]";
	/** Test data. */
	private Fixture datum;

	/** Create DeleteAsNotKeyHolder. */
	public DeleteAsNotKeyHolder() { super(NAME); }

	/** Test the parity document interface delete api. */
	public void testDeleteAsNotKeyHolder() {
        testLogger.info(NAME + " [TEST DELETE AS NOT KEY HOLDER]");
        Document d;
        try { datum.dModel.delete(datum.documentId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        d = null;
        try { d = datum.dModel.get(datum.documentId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
        assertNull("[LMODEL] [DOCUMENT] [DELETE TEST] [DOCUMENT NOT NULL]",  d);
	}

	/** @see junit.framework.TestCase#setUp() */
	protected void setUp() throws Exception {
		super.setUp();
		final DocumentModel dModel = getDocumentModel();

        final File file1 = getInputFiles()[1];
		final Document d1 = dModel.create(file1.getName(), file1.getName(), file1);

        addTeamMember(d1, ModelTestUser.getX());
        sendKey(d1);

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
