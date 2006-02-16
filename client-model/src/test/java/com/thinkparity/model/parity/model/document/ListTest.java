/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * Test the document model list api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class ListTest extends DocumentTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see ListTest#setUp()
	 * @see ListTest#tearDown()
	 */
	private class Fixture {
		private final DocumentModel documentModel;
		private final Collection<Document> expectedDocumentList;
		private Fixture(final DocumentModel documentModel,
				final Collection<Document> expectedDocumentList) {
			this.documentModel = documentModel;
			this.expectedDocumentList = expectedDocumentList;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a ListTest.
	 */
	public ListTest() { super("testList"); }

	/**
	 * Test the document model list api.
	 */
	public void testList() {
		try {
			Collection<Document> documentList;
			for(Fixture datum : data) {
				documentList = datum.documentModel.list();
				
				assertNotNull(documentList);

				final Document actual = getLastInCollection(documentList);
				final Document expected = getLastInCollection(datum.expectedDocumentList);
				assertEquals(actual, expected);
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * Obtain the last document in the collection.
	 * 
	 * @param documents
	 *            The document collection.
	 * @return The last document in the collection.
	 */
	private Document getLastInCollection(final Collection<Document> documents) {
		final Iterator<Document> iDocuments = documents.iterator();
		Document last = null;
		while(iDocuments.hasNext()) { last = iDocuments.next(); }
		return last;
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final Integer scenarioCount = 4;
		data = new Vector<Fixture>(scenarioCount);
		final DocumentModel documentModel = getDocumentModel();
		Collection<Document> documentList;
		String name, description;
		Document document;

		documentList = new Vector<Document>(getInputFilesLength());
		for(final File testFile : getInputFiles()) {
			name = testFile.getName();
			description = "Document:  " + name;
			document = documentModel.create(name, description, testFile);
			documentList.add(document);
		}
		data.add(new Fixture(documentModel, documentList));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		super.tearDown();
	}
}
