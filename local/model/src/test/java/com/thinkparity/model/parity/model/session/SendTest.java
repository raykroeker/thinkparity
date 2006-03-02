/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.io.File;
import java.util.List;
import java.util.Vector;

import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
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
					datum.sessionModel.send(contact.getId(), datum.message);
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
		final DocumentModel documentModel = getDocumentModel();
		final SessionModel sessionModel = getSessionModel();
		Document document;
		String name, description;
		String message;

		login();
		final List<Contact> contacts = sessionModel.readContacts();

		for(File testFile : getInputFiles()) {
			name = testFile.getName();
			description = name;
			document =
				documentModel.create(name, description, testFile);
			message = getTestText(250);

			data.add(new Fixture(document, message, sessionModel, contacts));
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
		private final List<Contact> contacts;
		private final Document document;
		private final String message;
		private final SessionModel sessionModel;
		private Fixture(final Document document, final String message,
				final SessionModel sessionModel, final List<Contact> contacts) {
			this.document = document;
			this.message = message;
			this.sessionModel = sessionModel;
			this.contacts = contacts;
		}
	}
}
