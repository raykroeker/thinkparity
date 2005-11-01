/*
 * 18-Oct-2005
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
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.xmpp.user.User;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class SessionModelTest extends ModelTestCase {

	private class GetRosterEntriesData {
		private final int rosterEntriesSize;
		private final Collection<String> rosterEntryUsernames;
		private final SessionModel sessionModel;
		private GetRosterEntriesData(final int rosterEntriesSize,
				final Collection<String> rosterEntryUsernames,
				final SessionModel sessionModel) {
			this.rosterEntriesSize = rosterEntriesSize;
			this.rosterEntryUsernames = rosterEntryUsernames;
			this.sessionModel = sessionModel;
		}
	}
	private class LoginData {
		private final String password;
		private final SessionModel sessionModel;
		private final String username;
		private LoginData(final String password,
				final SessionModel sessionModel, final String username) {
			this.password = password;
			this.sessionModel = sessionModel;
			this.username = username;
		}
	}
	private class SendData {
		private final Document document;
		private final SessionModel sessionModel;
		private final Collection<User> users;
		private SendData(final Document document,
				final SessionModel sessionModel, final Collection<User> users) {
			this.document = document;
			this.sessionModel = sessionModel;
			this.users = users;
		}
	}

	private Collection<GetRosterEntriesData> getRosterEntriesData;
	private Collection<LoginData> loginData;
	private Collection<SendData> sendData;

	/**
	 * Create a SessionModelTest.
	 */
	public SessionModelTest() { super("Session model"); }

	public void testGetRosterEntries() {
		try {
			Collection<User> rosterEntries;
			for(GetRosterEntriesData data : getRosterEntriesData) {
				rosterEntries = data.sessionModel.getRosterEntries();
				SessionModelTest.assertNotNull(rosterEntries);
				SessionModelTest.assertEquals(data.rosterEntriesSize, rosterEntries.size());
				for(User rosterEntry : rosterEntries) {
					SessionModelTest.assertNotNull(rosterEntry);
					SessionModelTest.assertTrue(data.rosterEntryUsernames.contains(rosterEntry.getUsername()));
					data.rosterEntryUsernames.remove(rosterEntry.getUsername());
				}
				SessionModelTest.assertEquals(0, data.rosterEntryUsernames.size());
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}
	public void testLogin() {
		try {
			for(LoginData data : loginData) {
				data.sessionModel.login(data.username, data.password);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	public void testSend() {
		try {
			for(SendData data : sendData) {
				data.sessionModel.send(data.users, data.document);
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setUpGetRosterEntries();
		setUpLogin();
		setUpSend();
	}

	protected void setUpGetRosterEntries() throws Exception {
		final ModelTestUser modelTestUser = getModelTestUser();
		final SessionModel sessionModel = getSessionModel();
		final Preferences preferences = getPreferences();
		getRosterEntriesData = new Vector<GetRosterEntriesData>(1);
		Collection<String> usernames;

		sessionModel.login(
				modelTestUser.getUsername(), modelTestUser.getPassword());
		usernames = new Vector<String>(2);
		usernames.add("junit.buddy.0@" + preferences.getServerHost());
		usernames.add("junit.buddy.1@" + preferences.getServerHost());
		getRosterEntriesData.add(
				new GetRosterEntriesData(2, usernames, sessionModel));
	}

	protected void setUpLogin() throws Exception {
		final SessionModel sessionModel = getSessionModel();
		final ModelTestUser testUser = getModelTestUser();
		loginData = new Vector<LoginData>(1);
		loginData.add(new LoginData(
				testUser.getPassword(), sessionModel, testUser.getUsername()));
	}
	
	protected void setUpSend() throws Exception {
		sendData = new Vector<SendData>(4);
		final Project testProject = createTestProject("testSend");
		final DocumentModel documentModel = getDocumentModel();
		final SessionModel sessionModel = getSessionModel();
		Document document;
		String name, description;

		final ModelTestUser testUser = getModelTestUser();
		sessionModel.login(testUser.getUsername(), testUser.getPassword());
		final Collection<User> users = sessionModel.getRosterEntries();

		for(ModelTestFile testFile : getJUnitTestFiles()) {
			name = testFile.getName();
			description = name;
			document = documentModel.create(testProject, name, description, testFile.getFile());

			sendData.add(new SendData(document, sessionModel, users));
		}
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownLogin();
		tearDownSend();
	}

	protected void tearDownLogin() throws Exception {
		loginData.clear();
		loginData = null;
	}

	protected void tearDownSend() throws Exception {
		sendData.clear();
		sendData = null;
	}
}
