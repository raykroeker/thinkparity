/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.io.File;
import java.util.Set;
import java.util.Vector;

import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * Test the session model send api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class SendTest extends ModelTestCase {

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a SendTest.
	 */
	public SendTest() { super("testSend"); }

	/**
	 * Test the session model send api.
	 *
	 */
	public void testSend() {
		try {
			for(Fixture datum : data) {
				for(final Contact contact : datum.contacts) {
					datum.sessionModel.send(contact.getId(), datum.document.getId());
				}
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new Vector<Fixture>(4);
		final SessionModel sessionModel = getSessionModel();
		Document document;

		login();
		final Set<Contact> contacts = sessionModel.readContacts();

		for(File testFile : getInputFiles()) {
			document = create(testFile);

			data.add(new Fixture(document, sessionModel, contacts));
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;

		logout();
	}

	/**
	 * Test data fixture.
	 * 
	 * @see SendTest#setUp()
	 * @see SendTest#tearDown()
	 */
	private class Fixture {
		private final Set<Contact> contacts;
		private final Document document;
		private final SessionModel sessionModel;
		private Fixture(final Document document,
                final SessionModel sessionModel, final Set<Contact> contacts) {
			this.document = document;
			this.sessionModel = sessionModel;
			this.contacts = contacts;
		}
	}
}
