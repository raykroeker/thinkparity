/*
 * Feb 20, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.thinkparity.model.parity.util.UUIDGenerator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendDeleteTest extends XMPPTestCase {

	private List<Fixture> data;

	/**
	 * Create a SendDeleteTest.
	 * 
	 */
	public SendDeleteTest() { super("Send Delete Test"); }

	public void testSendDelete() {
		try {
			for(final Fixture datum : data) {
				datum.session.removeArtifactTeamMember(datum.artifactUniqueId);
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new LinkedList<Fixture>();

		UUID artifactUniqueId;
		final XMPPSession session = getSession();
		for(int i = 0, count = getInputFiles().length; i < count; i++) {
			artifactUniqueId = UUIDGenerator.nextUUID();
			session.createArtifact(artifactUniqueId);
			session.closeArtifact(artifactUniqueId);
			data.add(new Fixture(artifactUniqueId, session));
		}
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
		private final XMPPSession session;
		private Fixture(final UUID artifactUniqueId, final XMPPSession session) {
			super();
			this.artifactUniqueId = artifactUniqueId;
			this.session = session;
		}
	}
}
