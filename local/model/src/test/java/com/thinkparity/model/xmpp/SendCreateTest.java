/*
 * Feb 13, 2006
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
public class SendCreateTest extends XMPPTestCase {

	private List<Fixture> data;

	/**
	 * Create a SendCreateTest.
	 * 
	 */
	public SendCreateTest() { super("Send Create Test"); }

	public void testSendCreate() {
		try {
			for(final Fixture datum : data) {
				datum.session.createArtifact(datum.artifactUniqueId);
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
		data.add(new Fixture(UUIDGenerator.nextUUID(), getSession()));
		data.add(new Fixture(UUIDGenerator.nextUUID(), getSession()));
		data.add(new Fixture(UUIDGenerator.nextUUID(), getSession()));
		data.add(new Fixture(UUIDGenerator.nextUUID(), getSession()));
		data.add(new Fixture(UUIDGenerator.nextUUID(), getSession()));
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
		private final XMPPSession session;
		private Fixture(final UUID artifactUniqueId, final XMPPSession session) {
			super();
			this.artifactUniqueId  = artifactUniqueId;
			this.session = session;
		}
	}

}
