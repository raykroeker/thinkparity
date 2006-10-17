/*
 * Nov 10, 2005
 */
package com.thinkparity.ophelia.model.session;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.ModelTestCase;

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
		datum.sessionModel.login(datum.monitor, datum.testUser.getCredentials());
			
		assertTrue(datum.sessionModel.isLoggedIn());
        datum.sessionModel.logout();
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
        final OpheliaTestUser testUser = OpheliaTestUser.JUNIT;
		final SessionModel sessionModel = getSessionModel(testUser);
		datum = new Fixture(sessionModel, testUser);
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
		private final LoginMonitor monitor;
		private final SessionModel sessionModel;
        private final OpheliaTestUser testUser;
		private Fixture(final SessionModel sessionModel,
                final OpheliaTestUser testUser) {
            this.monitor = new DefaultLoginMonitor();
			this.sessionModel = sessionModel;
			this.testUser = testUser;
		}
	}
}
