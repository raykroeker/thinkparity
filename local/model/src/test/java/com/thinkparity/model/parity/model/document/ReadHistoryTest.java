/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.CollectionsUtil;

import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.parity.model.document.history.HistoryItemEvent;
import com.thinkparity.model.parity.model.document.history.SendHistoryItem;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.user.User;

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
			SendHistoryItem sHItem;
			List<User> sentTo;
			for(final Fixture datum : data) {
				history = CollectionsUtil.proxy(datum.dModel.readHistory(datum.documentId));

				assertNotNull("Null history returned.", history);
				hItem = history.get(0);
				assertNotNull("Close history item is null.", hItem);
				assertEquals("Close history item event does not match expectation.",
						HistoryItemEvent.CLOSE, hItem.getEvent());
				assertEquals("Close history item document id does not match expectation.",
						datum.documentId, hItem.getDocumentId());

				hItem = history.get(1);
				assertNotNull("Send history item is null.", hItem);
				assertEquals("Send history item event does not match expectation.",
						HistoryItemEvent.SEND, hItem.getEvent());
				assertEquals("Send history item document id does not match expectation.",
						datum.documentId, hItem.getDocumentId());
				sHItem = (SendHistoryItem) hItem;
				sentTo = sHItem.getSentTo();
				assertNotNull("Send history item's sent to list is null.", sentTo);
				assertEquals("Sent history items' sent to list does not match expectation.",
						datum.sendSentTo, sentTo.get(0));
				assertEquals("Sent history item's version id does not match expectation.",
						datum.sendVersionId, sHItem.getVersionId());

				hItem = history.get(2);
				assertNotNull("Create history item is null.", hItem);
				assertEquals("Create history item event does not match expectation.",
						HistoryItemEvent.CREATE, hItem.getEvent());
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
		final User user = findUser("junit.buddy.0");
		final Collection<User> users = new LinkedList<User>();
		users.add(user);
		Document d;
		for(final File file : getInputFiles()) {
			d = dModel.create(file.getName(), file.getName(), file);
			sModel.send(users, d.getId());
			dModel.close(d.getId());
			data.add(new Fixture(dModel, d.getId(), user, 1L));
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
		private final User sendSentTo;
		private final Long sendVersionId;
		private Fixture(final DocumentModel dModel, final Long documentId,
				final User sendSentTo, final Long sendVersionId) {
			this.dModel = dModel;
			this.documentId = documentId;
			this.sendSentTo = sendSentTo;
			this.sendVersionId = sendVersionId;
		}
	}
}
