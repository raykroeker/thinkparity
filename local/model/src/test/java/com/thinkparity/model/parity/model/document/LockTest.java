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
public class LockTest extends DocumentTestCase {

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a LockTest.
	 */
	public LockTest() { super("testLock"); }


	/**
	 * Test the document model lock api.
	 *
	 */
	public void testLock() {
		try {
			for(final Fixture datum : data) {
				datum.iDocumentModel.lock(datum.documentId);
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
	 * @see LockTest#setUp()
	 * @see LockTest#tearDown()
	 */
	private class Fixture {
		private final Long documentId;
		private final InternalDocumentModel iDocumentModel;
		private Fixture(final Long documentId,
				final InternalDocumentModel iDocumentModel) {
			super();
			this.documentId = documentId;
			this.iDocumentModel = iDocumentModel;
		}
	}
}
