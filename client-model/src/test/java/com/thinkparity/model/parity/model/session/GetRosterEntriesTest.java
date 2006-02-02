/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.util.Collection;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.xmpp.user.User;

/**
 * Test the session model getRosterEntries api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class GetRosterEntriesTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see GetRosterEntriesTest#setUp()
	 * @see GetRosterEntriesTest#tearDown()
	 */
	private class Fixture {
		private final int rosterEntriesSize;
		private final Collection<String> rosterSimpleUsernames;
		private final Collection<String> rosterUsernames;
		private final SessionModel sessionModel;
		private Fixture(final int rosterEntriesSize,
				final Collection<String> rosterSimpleUsernames,
				final Collection<String> rosterUsernames,
				final SessionModel sessionModel) {
			this.rosterEntriesSize = rosterEntriesSize;
			this.rosterSimpleUsernames = rosterSimpleUsernames;
			this.rosterUsernames = rosterUsernames;
			this.sessionModel = sessionModel;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a GetRosterEntriesTest.
	 */
	public GetRosterEntriesTest() { super("testGetRosterEntries"); }

	/**
	 * Test the session model getRosterEntries api.
	 */
	public void testGetRosterEntries() {
		try {
			Collection<User> rosterEntries;
			for(Fixture datum : data) {
				rosterEntries = datum.sessionModel.getRosterEntries();
				
				assertNotNull(rosterEntries);
				assertEquals(datum.rosterEntriesSize, rosterEntries.size());
				for(User rosterEntry : rosterEntries) {
					assertNotNull(rosterEntry);
					assertTrue(datum.rosterUsernames.contains(rosterEntry.getUsername()));
					assertTrue(datum.rosterSimpleUsernames.contains(rosterEntry.getSimpleUsername()));

					datum.rosterUsernames.remove(rosterEntry.getUsername());
					datum.rosterSimpleUsernames.remove(rosterEntry.getSimpleUsername());
				}
				assertEquals(0, datum.rosterUsernames.size());
			}
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		final ModelTestUser modelTestUser = getModelTestUser();
		final SessionModel sessionModel = getSessionModel();
		final Preferences preferences = getPreferences();
		data = new Vector<Fixture>(1);
		Collection<String> usernames;
		Collection<String> simpleUsernames;

		sessionModel.login(
				modelTestUser.getUsername(), modelTestUser.getPassword());

		simpleUsernames = new Vector<String>(2);
		simpleUsernames.add("junit.buddy.0");
		simpleUsernames.add("junit.buddy.1");

		usernames = new Vector<String>(2);
		usernames.add("junit.buddy.0@" + preferences.getServerHost());
		usernames.add("junit.buddy.1@" + preferences.getServerHost());

		data.add(new Fixture(2, simpleUsernames, usernames, sessionModel));
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}

	
}
