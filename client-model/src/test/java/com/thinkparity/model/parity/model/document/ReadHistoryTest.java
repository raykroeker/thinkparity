/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.CollectionsUtil;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReadHistoryTest extends DocumentTestCase {

	private List<Fixture> data;

	/**
	 * Create a ReadHistoryTest.
	 * @param name
	 */
	public ReadHistoryTest() { super("Read History Test"); }

	public void testReadHistory() {
		try {
			List<HistoryItem> history;
			HistoryItem hItem;
			for(final Fixture datum : data) {
				history = CollectionsUtil.proxy(datum.dModel.readHistory(datum.documentId));

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
						datum.documentId, hItem.getDocumentId());			}
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
		final SessionModel sModel = getSessionModel();
		final DocumentModel dModel = getDocumentModel();

		Document d;
		for(final File file : getInputFiles()) {
			d = dModel.create(file.getName(), file.getName(), file);
			sModel.send(ModelTestUser.getJUnitBuddy0().getJabberId(), d.getId());
			dModel.close(d.getId());
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
