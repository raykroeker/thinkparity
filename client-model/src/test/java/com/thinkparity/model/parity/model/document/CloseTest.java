/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CloseTest extends DocumentTestCase {

	private List<Fixture> data;
	
	/**
	 * Create a CloseTest.
	 * 
	 */
	public CloseTest() { super("Close Test"); }

	public void testClose() {
		try {
			for(final Fixture datum : data) {
				datum.dModel.close(datum.documentId);
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
