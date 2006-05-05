/*
 * Created On: Nov 10, 2005
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.List;
import java.util.LinkedList;

import com.thinkparity.model.parity.ParityException;

/**
 * Test the document model delete api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class DeleteTest extends DocumentTestCase {

	/** Test data. */
	private List<Fixture> data;

	/** Create DeleteTest. */
	public DeleteTest() { super("[LMODEL] [DOCUMENT] [DELETE TEST]"); }

	/** Test the parity document interface delete api. */
	public void testDelete() {
        testLogger.info("[LMODEL] [DOCUMENT] [TEST DELETE]");
        Document d;
        for(Fixture datum : data) {
            try { datum.dModel.delete(datum.documentId); }
            catch(final ParityException px) { fail(createFailMessage(px)); }

            d = null;
            try { d = datum.dModel.get(datum.documentId); }
            catch(final ParityException px) { fail(createFailMessage(px)); }
            assertNull("[LMODEL] [DOCUMENT] [DELETE TEST] [DOCUMENT NOT NULL]",  d);
		}
	}

	/** @see junit.framework.TestCase#setUp() */
	protected void setUp() throws Exception {
		super.setUp();
        data = new LinkedList<Fixture>();
		final DocumentModel dModel = getDocumentModel();

        // 2 scenarios
        // 0:  create'n'delete
        final File file0 = getInputFiles()[0];
		final Document d0 = dModel.create(file0.getName(), file0.getName(), file0);
        dModel.close(d0.getId());
		data.add(new Fixture(dModel, d0.getId()));

        // 1:  create modify'n'delete
        final File file1 = getInputFiles()[1];
		final Document d1 = dModel.create(file1.getName(), file1.getName(), file1);
        modifyDocument(d1);
        dModel.publish(d1.getId());
        dModel.close(d1.getId());
		data.add(new Fixture(dModel, d1.getId()));
	}

	/** @see junit.framework.TestCase#tearDown() */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
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
