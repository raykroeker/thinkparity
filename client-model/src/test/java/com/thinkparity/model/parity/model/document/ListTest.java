/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.filter.FilterChain;
import com.thinkparity.model.parity.model.filter.artifact.Active;
import com.thinkparity.model.parity.model.filter.artifact.Closed;
import com.thinkparity.model.parity.model.filter.artifact.IsKeyHolder;
import com.thinkparity.model.parity.model.filter.artifact.IsNotKeyHolder;

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

	public void testListWithFilter() {
		try {
			FilterChain<Artifact> filterChain;
			Collection<Document> documents;
			for(final Fixture datum : data) {
				// look for closed documents
				filterChain = new FilterChain<Artifact>(new Closed());
				documents = datum.documentModel.list(filterChain);
				for(final Document document : documents) {
					assertTrue(document.getState() == ArtifactState.CLOSED);
				}
				// look for active documents
				filterChain = new FilterChain<Artifact>(new Active());
				documents = datum.documentModel.list(filterChain);
				for(final Document document : documents) {
					assertTrue(document.getState() == ArtifactState.ACTIVE);
				}
				// look for documents with the key
				filterChain = new FilterChain<Artifact>(new IsKeyHolder());
				documents = datum.documentModel.list(filterChain);
				for(final Document document : documents) {
					assertTrue(document.contains(ArtifactFlag.KEY));
				}
				// look for documents without the key
				filterChain = new FilterChain<Artifact>(new IsNotKeyHolder());
				documents = datum.documentModel.list(filterChain);
				for(final Document document : documents) {
					assertTrue(!document.contains(ArtifactFlag.KEY));
				}
				// look for active documents with the key
				filterChain = new FilterChain<Artifact>(new Active());
				filterChain.addFilter(new IsKeyHolder());
				documents = datum.documentModel.list(filterChain);
				for(final Document document : documents) {
					assertTrue(document.getState() == ArtifactState.ACTIVE);
					assertTrue(document.contains(ArtifactFlag.KEY));
				}
				// look for closed documents with the key
				filterChain = new FilterChain<Artifact>(new Closed());
				filterChain.addFilter(new IsKeyHolder());
				documents = datum.documentModel.list(filterChain);
				for(final Document document : documents) {
					assertTrue(document.getState() == ArtifactState.CLOSED);
					assertTrue(document.contains(ArtifactFlag.KEY));
				}
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
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
		String name;
		Document document;

		documentList = new Vector<Document>(getInputFilesLength());
		final File[] inputFiles = getInputFiles();
		for(int i = 0; i < inputFiles.length; i++) {
			name = inputFiles[i].getName();
			document = documentModel.create(name, null, inputFiles[i]);
			if(0 == i) { documentModel.close(document.getId()); }
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
