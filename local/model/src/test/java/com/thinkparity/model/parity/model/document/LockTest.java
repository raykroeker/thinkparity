/*
 * Dec 12, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Vector;

import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class LockTest extends ModelTestCase {

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
			Document document;
			for(Fixture datum : data) {
				datum.iDocumentModel.lock(datum.documentId);

				document = datum.iDocumentModel.get(datum.documentId);
				LockTest.assertFalse(document.contains(ArtifactFlag.KEY));
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

		String description, name;
		Document document;
		for(File testFile : getInputFiles()) {
			name = testFile.getName();
			description = name;
			document = iDocumentModel.create(name, description, testFile);
			data.add(new Fixture(document.getId(), iDocumentModel));
		}
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
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
