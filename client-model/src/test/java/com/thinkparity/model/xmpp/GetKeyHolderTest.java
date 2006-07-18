/*
 * Feb 14, 2006
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
public class GetKeyHolderTest extends XMPPTestCase {

	private List<Fixture> data;

	/**
	 * Create a GetKeyHolderTest.
	 * 
	 */
	public GetKeyHolderTest() { super("Get KeyHolder Test"); }

	public void testGetKeyHolder() {
		try {
			User keyHolder;
			for(final Fixture datum : data) {
				keyHolder = datum.session.readArtifactKeyHolder(datum.artifactUniqueId);

				assertNotNull("Key holder is null.", keyHolder);
				assertEquals("Expected key holder does not match actual.",
						keyHolder.getSimpleUsername(),
						datum.expectedKeyHolder.getSimpleUsername());
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

		final ModelTestUser jUnit = ModelTestUser.getJUnit();

		final ModelTestUser userX = ModelTestUser.getX();

		UUID artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, jUnit.getUser(), session));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		data.add(new Fixture(artifactUniqueId, jUnit.getUser(), session));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		session.sendKeyResponse(artifactUniqueId, KeyResponse.ACCEPT, userX.getJabberId());
		data.add(new Fixture(artifactUniqueId, userX.getUser(), session));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		session.sendKeyResponse(artifactUniqueId, KeyResponse.ACCEPT, userX.getJabberId());
		data.add(new Fixture(artifactUniqueId, userX.getUser(), session));

		artifactUniqueId = UUIDGenerator.nextUUID();
		session.createArtifact(artifactUniqueId);
		session.sendKeyResponse(artifactUniqueId, KeyResponse.ACCEPT, userX.getJabberId());
		data.add(new Fixture(artifactUniqueId, userX.getUser(), session));
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
		private final UUID artifactUniqueId;
		private final User expectedKeyHolder;
		private final XMPPSession session;
		private Fixture(final UUID artifactUniqueId, final User expectedKeyHolder, final XMPPSession session) {
			super();
			this.artifactUniqueId = artifactUniqueId;
			this.expectedKeyHolder = expectedKeyHolder;
			this.session = session;
		}
	}
}
