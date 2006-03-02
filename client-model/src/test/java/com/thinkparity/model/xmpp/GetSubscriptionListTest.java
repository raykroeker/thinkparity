/*
 * Feb 16, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.LinkedList;
import java.util.List;
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
			List<Contact> artifactContacts;
			for(final Fixture datum : data) {
				artifactContacts =
					datum.session.readArtifactContacts(datum.artifactUniqueId);

				assertNotNull("Returned artifact contacts are null.", artifactContacts);
				assertEquals(
						"Expected artifact contacts size does not match actual.",
						datum.expectedArtifactContacts.size(), artifactContacts.size());
				for(int i = 0; i < datum.expectedArtifactContacts.size(); i++) {
					assertEquals(
							"Expected artifact contact does not match actual.",
							datum.expectedArtifactContacts.get(i),
							artifactContacts.get(i));
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
		final User jUnitUser = session.getUser();

		final List<Contact> expectedArtifactContacts = new LinkedList<Contact>();
		final Contact contact = new Contact();
		contact.setId(jUnitUser.getId());
		expectedArtifactContacts.add(contact);

		UUID artifactUniqueId = UUIDGenerator.nextUUID();
		session.sendCreate(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedArtifactContacts, session));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.sendCreate(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedArtifactContacts, session));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.sendCreate(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedArtifactContacts, session));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.sendCreate(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedArtifactContacts, session));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.sendCreate(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, expectedArtifactContacts, session));
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
		private final List<Contact> expectedArtifactContacts;
		private final XMPPSession session;
		private Fixture(final UUID artifactUniqueId,
				final List<Contact> expectedArtifactContacts,
				final XMPPSession session) {
			this.artifactUniqueId = artifactUniqueId;
			this.expectedArtifactContacts = expectedArtifactContacts;
			this.session = session;
		}
	}
}
