/*
 * Feb 16, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.model.parity.util.UUIDGenerator;
import com.thinkparity.model.xmpp.contact.Contact;
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
			Set<User> artifactTeam;
			for(final Fixture datum : data) {
                artifactTeam =
					datum.session.readArtifactTeam(datum.artifactUniqueId);

				assertNotNull("Returned artifact contacts are null.", artifactTeam);
				assertEquals(
						"Expected artifact contacts size does not match actual.",
						datum.expectedTeam.size(), artifactTeam.size());
				for(final User teamMember : artifactTeam) {
                    assertTrue(
                            "Expected artifact contact does not match actual.",
                            datum.expectedTeam.contains(teamMember));
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
		final XMPPSession session = getSession();
		final User jUnitUser = session.readCurrentUser();

		final Set<User> expectedTeam = new HashSet<User>();

        final User teamMember = new Contact();
        teamMember.setId(jUnitUser.getId());
		expectedTeam.add(teamMember);

		UUID artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedTeam, session));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedTeam, session));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedTeam, session));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedTeam, session));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedTeam, session));
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
		private final Set<User> expectedTeam;
		private final XMPPSession session;
		private Fixture(final UUID artifactUniqueId,
                final Set<User> expectedTeam, final XMPPSession session) {
			this.artifactUniqueId = artifactUniqueId;
			this.expectedTeam = expectedTeam;
			this.session = session;
		}
	}
}
