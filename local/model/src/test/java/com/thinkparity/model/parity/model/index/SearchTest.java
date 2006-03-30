/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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
		try {
			List<IndexHit> indexHits;
			for(final Fixture datum : data) {
				indexHits = datum.iModel.search(datum.d.getName());
				assertNotNull("Index hits is null.", indexHits);

				Document searchHit;
				for(final IndexHit indexHit : indexHits) {
					searchHit = datum.dModel.get(indexHit.getId());
					assertEquals(searchHit.getName(), datum.d.getName());
				}
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
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
		Document d;
		for(final File inputFile : getInputFiles()) {
			d = dModel.create(inputFile.getName(), inputFile.getName(), inputFile);
			data.add(new Fixture(d, dModel, 1, iModel));
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
		private final Document d;
		private final DocumentModel dModel;
		private final int hitsExpectedSize;
		private final IndexModel iModel;
		private Fixture(final Document d, final DocumentModel dModel,
				final Integer hitsExpectedSize, final IndexModel iModel) {
			this.d = d;
			this.dModel = dModel;
			this.hitsExpectedSize = hitsExpectedSize;
			this.iModel = iModel;
		}
	}

	
}
