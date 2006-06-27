/*
 * Dec 12, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Vector;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UnlockTest extends DocumentTestCase {

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create an UnlockTest.
	 */
	public UnlockTest() { super("testUnlock"); }


	/**
	 * Test the document model lock api.
	 *
	 */
	public void testUnlock() {
		try {
			for(final Fixture datum : data) {
				datum.iDocumentModel.unlock(datum.documentId);
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new Vector<Fixture>(getInputFilesLength());
		final InternalDocumentModel iDocumentModel = getInternalDocumentModel();

		Document document;
		for(File testFile : getInputFiles()) {
			document = create(testFile);
			// in order to unlock the document it must first be locked
			// since it's initial state is not locked after creation
			iDocumentModel.lock(document.getId());
			data.add(new Fixture(document.getId(), iDocumentModel));
		}
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		super.tearDown();
	}

	/**
	 * Test data fixture.
	 * 
	 * @see UnlockTest#setUp()
	 * @see UnlockTest#tearDown()
	 */
	private class Fixture {
		private final Long documentId;
		private final InternalDocumentModel iDocumentModel;
		private Fixture(final Long documentId, final InternalDocumentModel iDocumentModel) {
			super();
			this.documentId = documentId;
			this.iDocumentModel = iDocumentModel;
		}
	}
}
