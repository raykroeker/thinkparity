/*
 * Created On: Jun 6, 2006 9:02:27 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;

import com.thinkparity.codebase.assertion.UnreachableCodeAssertion;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.ParityException;

/**
 * Test the document model delete api; where the user is not the key holder.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class DeleteAsKeyHolderDistributedNotClosed extends DocumentTestCase {

    private static final String NAME = "[LMODEL] [DOCUMENT MODEL] [DELETE AS KEY HOLDER DISTRIBUTED NOT CLOSED]";

    /** Test data. */
	private Fixture datum;

	/** Create DeleteAsKeyHolderDistributedNotClosed. */
	public DeleteAsKeyHolderDistributedNotClosed() { super(NAME); }

	/** Test the parity document interface delete api. */
	public void testDeleteAsKeyHolderDistributedNotClosed() {
        testLogger.info(NAME + " [TEST DELETE AS KEY HOLDER DISTRIBUTED NOT CLOSED]");
        Boolean didAssert = Boolean.FALSE;
        try { datum.dModel.delete(datum.documentId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
        catch(final UnreachableCodeAssertion ucx) { didAssert = Boolean.TRUE; }

        if(!didAssert) { fail("[DID NOT ASSERT]"); }
	}

	/** @see junit.framework.TestCase#setUp() */
	protected void setUp() throws Exception {
		super.setUp();
		final DocumentModel dModel = getDocumentModel();

        final File file1 = getInputFiles()[1];
		final Document d1 = create(file1);
        addTeamMember(d1, ModelTestUser.getX());
        modifyDocument(d1);
        dModel.publish(d1.getId());

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
