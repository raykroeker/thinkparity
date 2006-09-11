/*
 * Nov 10, 2005
 */
package com.thinkparity.ophelia.model.session;


import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.ModelTestCase;
import com.thinkparity.ophelia.model.session.SessionModel;

/**
 * Test the session model login api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class LoginTest extends ModelTestCase {

	/** Test datum. */
	private Fixture datum;

	/**
	 * Create a LoginTest.
	 */
	public LoginTest() { super("testLogin"); }

	/**
	 * Test the session model login api.
	 */
	public void testLogin() {
		datum.sessionModel.login(datum.credentials);
			
		assertTrue(datum.sessionModel.isLoggedIn());
        datum.sessionModel.logout();
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final SessionModel sessionModel = getSessionModel();
		final OpheliaTestUser testUser = getModelTestUser();
		datum = new Fixture(testUser.getCredentials(), sessionModel);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		datum = null;
        super.tearDown();
	}

	/**
	 * Test data fixture.
	 * 
	 * @see LoginTest#setUp()
	 * @see LoginTest#tearDown()
	 */
	private class Fixture {
		private final Credentials credentials;
		private final SessionModel sessionModel;
		private Fixture(final Credentials credentials,
                final SessionModel sessionModel) {
			this.credentials = credentials;
			this.sessionModel = sessionModel;
		}
	}
}
