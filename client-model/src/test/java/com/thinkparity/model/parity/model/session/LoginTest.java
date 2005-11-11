/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestUser;

/**
 * Test the session model login api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class LoginTest extends ModelTestCase {

	/**
	 * Test data fixture.
	 * 
	 * @see LoginTest#setUp()
	 * @see LoginTest#tearDown()
	 */
	private class Fixture {
		private final String password;
		private final SessionModel sessionModel;
		private final String username;
		private Fixture(final String password,
				final SessionModel sessionModel, final String username) {
			this.password = password;
			this.sessionModel = sessionModel;
			this.username = username;
		}
	}

	/**
	 * Test data.
	 */
	private Vector<Fixture> data;

	/**
	 * Create a LoginTest.
	 */
	public LoginTest() { super("testLogin"); }

	/**
	 * Test the session model login api.
	 */
	public void testLogin() {
		try {
			for(Fixture datum : data) {
				datum.sessionModel.login(datum.username, datum.password);
			
				assertTrue(datum.sessionModel.isLoggedIn());
				datum.sessionModel.logout();
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		final SessionModel sessionModel = getSessionModel();
		final ModelTestUser testUser = getModelTestUser();
		data = new Vector<Fixture>(1);
		data.add(new Fixture(
				testUser.getPassword(), sessionModel, testUser.getUsername()));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}
}
