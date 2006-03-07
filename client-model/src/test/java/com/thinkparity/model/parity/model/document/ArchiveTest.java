/*
 * Mar 3, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArchiveTest extends DocumentTestCase {

	private List<Fixture> data;

	/**
	 * Create a ArchiveTest.
	 * 
	 */
	public ArchiveTest() { super("Test Archive"); }

	public void testArchive() {
		try {
			File archive;
			for(final Fixture datum : data) {
				archive = datum.dModel.archive(datum.documentId);

				assertNotNull("Generated archive is null.", archive);
				assertTrue("Generated archive is of zero length.", archive.length() > 0);
				try { Thread.sleep(1000); }
				catch(final InterruptedException ix) { fail(createFailMessage(ix)); }
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new LinkedList<Fixture>();
		final DocumentModel dModel = getDocumentModel();

		Document d;
		for(final File file : getInputFiles()) {
			d = dModel.create(file.getName(), file.getName(), file);
			dModel.createVersion(d.getId());
			dModel.createVersion(d.getId());
			dModel.createVersion(d.getId());
			data.add(new Fixture(dModel, d.getId()));
		}
	}

	/**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
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
