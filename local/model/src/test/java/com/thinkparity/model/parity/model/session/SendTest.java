/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.io.File;
import java.util.Collection;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.xmpp.user.User;

/**
 * Test the session model send api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class SendTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see SendTest#setUp()
	 * @see SendTest#tearDown()
	 */
	private class Fixture {
		private final Document document;
		private final String message;
		private final SessionModel sessionModel;
		private final Collection<User> users;
		private Fixture(final Document document, final String message,
				final SessionModel sessionModel, final Collection<User> users) {
			this.document = document;
			this.message = message;
			this.sessionModel = sessionModel;
			this.users = users;
		}
	}

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
				datum.sessionModel.send(datum.users, datum.document.getId());
				datum.sessionModel.send(datum.users, datum.message);
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		data = new Vector<Fixture>(4);
		final Project testProject = createTestProject("testSend");
		final DocumentModel documentModel = getDocumentModel();
		final SessionModel sessionModel = getSessionModel();
		Document document;
		String name, description;
		String message;

		final ModelTestUser testUser = getModelTestUser();
		sessionModel.login(testUser.getUsername(), testUser.getPassword());
		final Collection<User> users = sessionModel.getRosterEntries();

		for(File testFile : getInputFiles()) {
			name = testFile.getName();
			description = name;
			document =
				documentModel.create(testProject.getId(), name, description, testFile);
			message = getTestText(250);

			data.add(new Fixture(document, message, sessionModel, users));
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
