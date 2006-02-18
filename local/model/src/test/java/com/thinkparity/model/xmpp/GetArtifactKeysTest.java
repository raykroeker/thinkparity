/*
 * Feb 17, 2006
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
public class GetArtifactKeysTest extends XMPPTestCase {

	private List<Fixture> data;

	/**
	 * Create a GetArtifactKeysTest.
	 */
	public GetArtifactKeysTest() { super("Get Artifact Keys"); }

	public void testGetArtifactKeys() {
		try {
			List<UUID> keys;
			for(final Fixture datum : data) {
				keys = datum.session.getArtifactKeys();

				assertNotNull("Artifact keys are null.");
				for(final UUID expectedKey : datum.expectedKeys) {
					assertTrue("Artifact key not found.", keys.contains(expectedKey));
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
		final List<UUID> expectedKeys = new LinkedList<UUID>();

		UUID artifactUniqueId = UUIDGenerator.nextUUID();
		session.sendCreate(artifactUniqueId);
		expectedKeys.add(artifactUniqueId);

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.sendCreate(artifactUniqueId);
		expectedKeys.add(artifactUniqueId);

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.sendCreate(artifactUniqueId);
		expectedKeys.add(artifactUniqueId);

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.sendCreate(artifactUniqueId);
		expectedKeys.add(artifactUniqueId);

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.sendCreate(artifactUniqueId);
		expectedKeys.add(artifactUniqueId);

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.sendCreate(artifactUniqueId);
		expectedKeys.add(artifactUniqueId);
		
		data.add(new Fixture(expectedKeys, session));
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		super.tearDown();
	}

	private class Fixture {
		private final List<UUID> expectedKeys;
		private final XMPPSession session;
		private Fixture(final List<UUID> expectedKeys, final XMPPSession session) {
			super();
			this.expectedKeys = expectedKeys;
			this.session = session;
		}
	}
}
