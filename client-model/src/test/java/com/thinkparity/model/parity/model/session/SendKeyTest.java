/*
 * Feb 13, 2006
 */
package com.thinkparity.model.parity.model.session;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendKeyTest extends ModelTestCase {

	private List<Fixture> data;

	/**
	 * Create a SendKeyTest.
	 * 
	 */
	public SendKeyTest() { super("Send Key"); }

	public void testSendKey() {
		try {
			for(final Fixture datum : data) {
				datum.sessionModel.sendKeyResponse(
						datum.artifactId, datum.jabberId, datum.keyResponse);
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new LinkedList<Fixture>();
		final SessionModel sessionModel = getSessionModel();

		login();
		final ModelTestUser jUnitTestUser = ModelTestUser.getX();
		final JabberId jabberId = jUnitTestUser.getJabberId();
		Document d;
		for(final File inputFile : getInputFiles()) {
			d = create(inputFile);
			data.add(new Fixture(d.getId(), KeyResponse.ACCEPT, sessionModel, jabberId));
		}
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		data.clear();
		data = null;
		logout();
	}

	private class Fixture {
		private final Long artifactId;
		private final JabberId jabberId;
		private final KeyResponse keyResponse;
		private final SessionModel sessionModel;
		private Fixture(final Long artifactId, final KeyResponse keyResponse,
				final SessionModel sessionModel, final JabberId jabberId) {
			super();
			this.artifactId = artifactId;
			this.keyResponse = keyResponse;
			this.sessionModel = sessionModel;
			this.jabberId = jabberId;
		}
	}
}
