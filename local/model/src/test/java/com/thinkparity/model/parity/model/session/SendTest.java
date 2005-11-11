/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.util.Collection;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestFile;
import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.xmpp.user.User;

/**
 * Test the session model send api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class SendTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see SendTest#setUp()
	 * @see SendTest#tearDown()
	 */
	private class SendData {
		private final Document document;
		private final String message;
		private final SessionModel sessionModel;
		private final Collection<User> users;
		private SendData(final Document document, final String message,
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
	private Vector<SendData> sendData;

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
			for(SendData datum : sendData) {
				datum.sessionModel.send(datum.users, datum.document);
				datum.sessionModel.send(datum.users, datum.message);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		sendData = new Vector<SendData>(4);
		final Project testProject = createTestProject("testSend");
		final DocumentModel documentModel = getDocumentModel();
		final SessionModel sessionModel = getSessionModel();
		Document document;
		String name, description;
		String message;

		final ModelTestUser testUser = getModelTestUser();
		sessionModel.login(testUser.getUsername(), testUser.getPassword());
		final Collection<User> users = sessionModel.getRosterEntries();

		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			document =
				documentModel.create(testProject, name, description, testFile.getFile());
			message = getJUnitTestText(250);

			sendData.add(new SendData(document, message, sessionModel, users));
		}
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		for(SendData datum : sendData) {
			datum.sessionModel.logout();
		}
		sendData.clear();
		sendData = null;
	}
}
