/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * Test the session model getRosterEntries api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class ReadContactsTest extends ModelTestCase {

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a ReadContactsTest.
	 */
	public ReadContactsTest() { super("testGetRosterEntries"); }

	/**
	 * Test the session model getRosterEntries api.
	 */
	public void testReadContacts() {
		try {
			List<Contact> contacts;
			for(Fixture datum : data) {
				contacts = datum.sessionModel.readContacts();
				
				assertNotNull("Contacts from session model are null.", contacts);
				assertEquals("Number of contacts from session model; don't match fixture data.", datum.contactsSize, contacts.size());
				for(final Contact contact : contacts) {
					assertNotNull(contact);
					assertTrue(datum.contactIds.contains(contact.getId()));

					datum.contactIds.remove(contact.getId());
				}
				assertEquals(0, datum.contactIds.size());
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final SessionModel sessionModel = getSessionModel();
		data = new Vector<Fixture>(1);
		final List<JabberId> contactIds;

		login();

		contactIds = new LinkedList<JabberId>();

		data.add(new Fixture(contactIds.size(), contactIds, sessionModel));
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;

		logout();
	}

	/**
	 * Test data fixture.
	 * 
	 * @see ReadContactsTest#setUp()
	 * @see ReadContactsTest#tearDown()
	 */
	private class Fixture {
		private final List<JabberId> contactIds;
		private final int contactsSize;
		private final SessionModel sessionModel;
		private Fixture(final int contactsSize,
				final List<JabberId> contactIds,
				final SessionModel sessionModel) {
			this.contactsSize = contactsSize;
			this.contactIds = contactIds;
			this.sessionModel = sessionModel;
		}
	}
}
