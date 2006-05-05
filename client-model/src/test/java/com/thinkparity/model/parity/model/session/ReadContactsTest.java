/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.ParityException;
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
		Set<Contact> contacts = null;
		for(Fixture datum : data) {
			try { contacts = datum.sessionModel.readContacts(); }
            catch(final ParityException px) {
                fail(createFailMessage(px));
            }
			
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

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final SessionModel sessionModel = getSessionModel();
		data = new Vector<Fixture>(1);

		login();

		final Set<JabberId> contactIds = new HashSet<JabberId>();
        contactIds.add(ModelTestUser.getX().getJabberId());

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
		private final Set<JabberId> contactIds;
		private final int contactsSize;
		private final SessionModel sessionModel;
		private Fixture(final int contactsSize,
				final Set<JabberId> contactIds,
				final SessionModel sessionModel) {
			this.contactsSize = contactsSize;
			this.contactIds = contactIds;
			this.sessionModel = sessionModel;
		}
	}
}
