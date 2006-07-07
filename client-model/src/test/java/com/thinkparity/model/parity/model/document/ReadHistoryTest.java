/*
 * Created On: Feb 22, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.util.List;

import com.thinkparity.model.parity.model.container.Container;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReadHistoryTest extends DocumentTestCase {

    /** Test name. */
    private static final String NAME = "[LMODEL] [DOCUMENT] [TEST READ HISTORY]";

    /** Test datum. */
	private Fixture datum;

	/** Create ReadHistoryTest. */
	public ReadHistoryTest() { super(NAME); }

	public void testReadHistory() {
		final List<DocumentHistoryItem> history =
            datum.dModel.readHistory(datum.documentId);

		assertNotNull("Null history returned.", history);
		DocumentHistoryItem item = history.get(0);
		assertNotNull("Close history item is null.", item);
		assertNotNull("Close history item is null.", item.getEvent());
		assertEquals("Close history item document id does not match expectation.",
				datum.documentId, item.getDocumentId());

        item = history.get(1);
		assertNotNull(NAME + " [PUBLISH HISTORY ITEM IS NULL]", item);
		assertEquals(NAME + " [PUBLISH HISTORY ITEM DOCUMENT ID DOES NOT MATCH EXPECTATION]",
                datum.documentId, item.getDocumentId());
	}

	/**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final DocumentModel dModel = getDocumentModel();
        final Container container = createContainer(NAME);
        final Document document = addDocument(container, getInputFiles()[0]);
        modifyDocument(document);
        getContainerModel().publish(container.getId());
        getContainerModel().close(container.getId());
		datum = new Fixture(dModel, document.getId());
	}

	/**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		datum = null;
		super.tearDown();
	}

    /** Test datum definition. */
	private class Fixture {
		private final DocumentModel dModel;
		private final Long documentId;
		private Fixture(final DocumentModel dModel, final Long documentId) {
			this.dModel = dModel;
			this.documentId = documentId;
		}
	}
}
