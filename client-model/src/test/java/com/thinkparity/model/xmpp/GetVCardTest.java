/*
 * Feb 25, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.xmpp.user.UserVCard;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GetVCardTest extends XMPPTestCase {

	private List<Fixture> data;

	/**
	 * Create a GetVCardTest.
	 * 
	 */
	public GetVCardTest() { super("Get VCard Test"); }

	public void testGetVCard() {
		try {
			UserVCard userVCard;
			for(final Fixture datum : data) {
				userVCard = datum.session.readVCard(datum.jabberId);

				assertNotNull("User's vcard is null.", userVCard);
				assertEquals("User's vcard does not match expectation.",
						userVCard, datum.expectedUserVCard);
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new LinkedList<Fixture>();
		final XMPPSession session = getSession();

		final JabberId jabberId = ModelTestUser.getJUnit().getJabberId();

		final UserVCard userVCard = session.readVCard(jabberId);
		userVCard.setFirstName(System.currentTimeMillis() + "");
		userVCard.setLastName(System.currentTimeMillis() + "");
		userVCard.setOrganization(System.currentTimeMillis() + "");
		session.saveVCard(userVCard);
		data.add(new Fixture(session, userVCard, jabberId));
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private class Fixture {
		private final UserVCard expectedUserVCard;
		private final JabberId jabberId;
		private final XMPPSession session;
		private Fixture(final XMPPSession session, final UserVCard expectedUserVCard, final JabberId jabberId) {
			this.session = session;
			this.expectedUserVCard = expectedUserVCard;
			this.jabberId = jabberId;
		}
	}
}
