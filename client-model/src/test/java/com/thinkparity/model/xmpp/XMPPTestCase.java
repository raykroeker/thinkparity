/*
 * Feb 13, 2006
 */
package com.thinkparity.model.xmpp;


import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.ModelTestUser;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class XMPPTestCase extends ModelTestCase {

	private XMPPSession session;

	/**
	 * Create a XMPPTestCase.
	 * 
	 * @param name
	 *            The test name.
	 */
	protected XMPPTestCase(final String name) { super(name); }

	protected XMPPSession getSession() { return session; }

	/**
	 * @see junit.framework.TestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		session = XMPPSessionFactory.createSession();
		final ModelTestUser modelTestUser = ModelTestUser.getJUnit();
		session.login(modelTestUser.getServerHost(),
				modelTestUser.getServerPort(), modelTestUser.getUsername(),
				modelTestUser.getPassword());
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		session.logout();
		session = null;

		super.tearDown();
	}
}
