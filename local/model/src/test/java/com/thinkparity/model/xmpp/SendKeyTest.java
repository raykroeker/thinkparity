/*
 * Feb 13, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.parity.util.UUIDGenerator;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendKeyTest extends XMPPTestCase {

	private List<Fixture> data;

	/**
	 * Create a SendKeyTest.
	 * 
	 */
	public SendKeyTest() { super("Send Key Test"); }

	public void testSendKey() {
		try {
			for(final Fixture datum : data) {
				datum.session.sendKeyResponse(datum.artifactUniqueId,
						datum.keyResponse, datum.user);
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		final XMPPSession session = getSession();
		final User user = ModelTestUser.getJUnitBuddy0().getUser();
		data = new LinkedList<Fixture>();

		UUID artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, KeyResponse.ACCEPT, getSession(), user));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, KeyResponse.ACCEPT, getSession(), user));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, KeyResponse.ACCEPT, getSession(), user));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, KeyResponse.ACCEPT, getSession(), user));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, KeyResponse.ACCEPT, getSession(), user));
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		super.tearDown();
	}

	private class Fixture {
		private final UUID artifactUniqueId;
		private final KeyResponse keyResponse;
		private final XMPPSession session;
		private final User user;
		private Fixture(final UUID artifactUniqueId,
				final KeyResponse keyResponse, final XMPPSession session,
				final User user) {
			super();
			this.artifactUniqueId = artifactUniqueId;
			this.keyResponse = keyResponse;
			this.session = session;
			this.user = user;
		}
	}
}
