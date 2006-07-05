/*
 * Created On: Feb 22, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.util.List;

import com.thinkparity.codebase.CollectionsUtil;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.document.history.HistoryItem;

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
		List<HistoryItem> history = null;
		HistoryItem hItem;
		try {
            history = CollectionsUtil.proxy(datum.dModel.readHistory(datum.documentId));
        }
        catch(final ParityException px) { fail(createFailMessage(px)); }

		assertNotNull("Null history returned.", history);
		hItem = history.get(0);
		assertNotNull("Close history item is null.", hItem);
		assertNotNull("Close history item is null.", hItem.getEvent());
		assertEquals("Close history item document id does not match expectation.",
				datum.documentId, hItem.getDocumentId());

		hItem = history.get(1);
		assertNotNull("Send history item is null.", hItem);
		assertNotNull("Send history item event is null.", hItem.getEvent());
		assertEquals("Send history item document id does not match expectation.",
				datum.documentId, hItem.getDocumentId());

		hItem = history.get(2);
		assertNotNull("Create history item is null.", hItem);
		assertNotNull("Create history item event is null.", hItem.getEvent());
		assertEquals("Create history item document id does not match expectation.",
				datum.documentId, hItem.getDocumentId());
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
