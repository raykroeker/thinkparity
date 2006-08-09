/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index;

import java.util.List;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.container.ContainerModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SearchDocumentsTest extends IndexTestCase {

    /** Test name. */
    private static final String NAME = "[LMODEL] [INDEX] [TEST SEARCH DOCUMENTS]";

	/** Test datum. */
	private Fixture datum;

	/** Create SearchDocumentsTest. */
	public SearchDocumentsTest() { super(NAME); }

    /**
     * Test the search documents api.
     *
     */
    public void testSearchDocuments() {
        List<IndexHit> indexHits = null;
        try { indexHits = datum.iModel.searchDocuments(datum.expression); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        assertNotNull(NAME, indexHits);
        assertTrue(NAME + " [NO DOCUMENTS RETURNED]", indexHits.size() > 0);
        Document document = null;
        for(final IndexHit indexHit : indexHits) {
            document = datum.dModel.get(indexHit.getId());
            assertNotNull(NAME, document);
        }
	}

	/**
	 * @see com.thinkparity.model.parity.model.index.IndexTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
        login();
        final ContainerModel containerModel = getContainerModel();
        final DocumentModel documentModel = getDocumentModel();
        final IndexModel indexModel = getIndexModel();

        final Container container = createContainer(NAME);
        final Document document = createDocument(getInputFiles()[0]);
        containerModel.addDocument(container.getId(), document.getId());
		datum = new Fixture(documentModel, document.getName(), indexModel);
	}

	/**
	 * @see com.thinkparity.model.parity.model.index.IndexTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		datum = null;
		logout();
		super.tearDown();
	}

    /** Test datum definition. */
	private class Fixture {
        private final DocumentModel dModel;
		private final String expression;
		private final IndexModel iModel;
		private Fixture(final DocumentModel dModel, final String expression,
                final IndexModel iModel) {
            this.dModel = dModel;
			this.expression = expression;
			this.iModel = iModel;
		}
	}
}
