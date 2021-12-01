/*
 * Nov 10, 2005
 */
package com.thinkparity.ophelia.model.session;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.ModelTestCase;

/**
 * Test the session model login api.
 * 
 * @author raymond@raykroeker.com
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
        login(datum.junit);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		datum = new Fixture(OpheliaTestUser.JUNIT_Y);
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
	private class Fixture extends ModelTestCase.Fixture {
		private final OpheliaTestUser junit;
		private Fixture(final OpheliaTestUser junit) {
			this.junit = junit;
            addQueueHelper(junit);
		}
	}
}
