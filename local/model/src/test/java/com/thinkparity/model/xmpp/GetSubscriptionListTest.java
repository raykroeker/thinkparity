/*
 * Feb 16, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.thinkparity.model.parity.util.UUIDGenerator;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GetSubscriptionListTest extends XMPPTestCase {

	private List<Fixture> data;

	/**
	 * Create a GetSubscriptionListTest.
	 * 
	 */
	public GetSubscriptionListTest() { super("Get Subscription Test"); }

	public void testGetSubscription() {
		try {
			List<User> subscription;
			for(final Fixture datum : data) {
				subscription =
					datum.session.getArtifactSubscription(datum.artifactUniqueId);

				assertNotNull("Returned subscription is null.", subscription);
				assertEquals(
						"Expected subscription size does not match actual.",
						datum.expectedSubscription.size(), subscription.size());
				for(int i = 0; i < datum.expectedSubscription.size(); i++) {
					assertEquals(
							"Expected subscription user does not match actual.",
							datum.expectedSubscription.get(i).getSimpleUsername(),
							subscription.get(i).getSimpleUsername());
				}
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
//		final ModelTestUser jUnitBuddy0 = ModelTestUser.getJUnitBuddy0();
//
		final XMPPSession session = getSession();
		final User jUnitUser = session.getUser();
//		User jUnitBuddy0User = null;
//		for(final User rosterEntry : session.getRosterEntries()) {
//			if(rosterEntry.getSimpleUsername().equals(jUnitBuddy0.getUsername())) {
//				jUnitBuddy0User = rosterEntry;
//				break;
//			}
//		}

//		session.logout();
//		session.login(jUnitBuddy0.getServerHost(), jUnitBuddy0.getServerPort(),
//				jUnitBuddy0.getUsername(), jUnitBuddy0.getPassword());

		final List<User> expectedSubscription = new LinkedList<User>();
//		expectedSubscription.add(jUnitBuddy0User);
		expectedSubscription.add(jUnitUser);

		UUID artifactUniqueId = UUIDGenerator.nextUUID();
//		session.subscribe(artifactUniqueId);
		session.sendCreate(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedSubscription, session));

		artifactUniqueId = UUIDGenerator.nextUUID();
//		session.subscribe(artifactUniqueId);
		session.sendCreate(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedSubscription, session));

		artifactUniqueId = UUIDGenerator.nextUUID();
//		session.subscribe(artifactUniqueId);
		session.sendCreate(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedSubscription, session));

		artifactUniqueId = UUIDGenerator.nextUUID();
//		session.subscribe(artifactUniqueId);
		session.sendCreate(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedSubscription, session));

		artifactUniqueId = UUIDGenerator.nextUUID();
//		session.subscribe(artifactUniqueId);
		session.sendCreate(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedSubscription, session));
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private class Fixture {
		private final UUID artifactUniqueId;
		private final List<User> expectedSubscription;
		private final XMPPSession session;
		private Fixture(final UUID artifactUniqueId,
				final List<User> expectedSubscription,
				final XMPPSession session) {
			this.artifactUniqueId = artifactUniqueId;
			this.expectedSubscription = expectedSubscription;
			this.session = session;
		}
	}
}
