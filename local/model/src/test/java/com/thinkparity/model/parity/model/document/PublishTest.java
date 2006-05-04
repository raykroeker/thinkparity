/*
 * Mar 3, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.ParityException;

/**
 * Test the document publish api.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class PublishTest extends DocumentTestCase {

    /** The test data. */
	private List<Fixture> data;

	/** Create PublishTest. */
	public PublishTest() { super("Test Archive"); }

    /** Test the publish api. */
	public void testPublish() {
        for(final Fixture datum : data) {
            try { datum.dModel.publish(datum.documentId); }

            catch(final ParityException px) { fail(createFailMessage(px)); }
        }
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
            modifyDocument(d.getId());

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
