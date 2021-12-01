/*
 * Nov 10, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.io.File;
import java.util.Vector;

import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * Test the document model get api.
 * 
 * @author raymond@raykroeker.com
 * @version 1.1.2.2
 */
public class GetTest extends DocumentTestCase {

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a GetTest.
	 */
	public GetTest() { super("testGet"); }

	/**
	 * Test the document model get api.
	 */
	public void testGet() {
		try {
			Document document;
			for(Fixture datum : data) {
				document = datum.documentModel.get(datum.documentId);

				assertEquals(datum.document, document);
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new Vector<Fixture>(getInputFilesLength());
		final DocumentModel documentModel = getDocumentModel(OpheliaTestUser.JUNIT);
		Document document;
		Long documentId;
		
		for(File testFile : getInputFiles()) {
			document = create(OpheliaTestUser.JUNIT, testFile);
			documentId = document.getId();

			data.add(new Fixture(document, documentModel, documentId));
		}
		// add an element where no document is found
		data.add(new Fixture(null, documentModel, -1L));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		super.tearDown();
	}

	/**
	 * Test data fixture.
	 * 
	 * @see GetTest#setUp()
	 * @see GetTest#tearDown()
	 */
	private static class Fixture {
		private final Document document;
		private final Long documentId;
		private final DocumentModel documentModel;
		private Fixture(final Document document,
				final DocumentModel documentModel, final Long documentId) {
			this.document = document;
			this.documentModel = documentModel;
			this.documentId = documentId;
		}
	}
}
