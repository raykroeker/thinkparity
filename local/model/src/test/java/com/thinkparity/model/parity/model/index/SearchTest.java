/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SearchTest extends IndexTestCase {

	private List<Fixture> data;

	/**
	 * Create a SearchTest.
	 * 
	 */
	public SearchTest() { super("Test Search"); }

	public void testSearch() {
		List<IndexHit> indexHits;
		for(final Fixture datum : data) {
			try { indexHits = datum.iModel.searchArtifact(datum.criteria); }
			catch(final ParityException px) { throw new RuntimeException(px); }
			assertNotNull("Index hits is null.", indexHits);

			Document searchHit;
			for(final IndexHit indexHit : indexHits) {
				try { searchHit = datum.dModel.get(indexHit.getId()); }
				catch(final ParityException px) { throw new RuntimeException(px); }
				assertNotNull(searchHit);
			}
		}
	}

	/**
	 * @see com.thinkparity.model.parity.model.index.IndexTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final DocumentModel dModel = getDocumentModel();
		final IndexModel iModel = getIndexModel();

		data = new LinkedList<Fixture>();
		for(final File inputFile : getInputFiles()) {
			create(inputFile);
			data.add(new Fixture("JUnit", dModel, iModel));
		}
	}

	/**
	 * @see com.thinkparity.model.parity.model.index.IndexTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;

		super.tearDown();
	}

	private class Fixture {
		private final String criteria;
		private final DocumentModel dModel;
		private final IndexModel iModel;
		private Fixture(final String criteria, final DocumentModel dModel,
				final IndexModel iModel) {
			this.criteria = criteria;
			this.dModel = dModel;
			this.iModel = iModel;
		}
	}

	
}
