/*
 * Feb 13, 2006
 */
package com.thinkparity.model.parity.model.session;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.xmpp.user.User;

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
						datum.artifactId, datum.user, KeyResponse.ACCEPT);
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
		final DocumentModel documentModel = getDocumentModel();
		final SessionModel sessionModel = getSessionModel();

		login();
		final User user = findUser("junit.buddy.0");
		Document d;
		for(final File inputFile : getInputFiles()) {
			d = documentModel.create(inputFile.getName(), inputFile.getName(), inputFile);
			data.add(new Fixture(d.getId(), sessionModel, user));
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
		private final SessionModel sessionModel;
		private final User user;
		private Fixture(final Long artifactId, final SessionModel sessionModel,
				final User user) {
			super();
			this.artifactId = artifactId;
			this.sessionModel = sessionModel;
			this.user = user;
		}
	}
}
