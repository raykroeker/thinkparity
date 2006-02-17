/*
 * Feb 16, 2006
 */
package com.thinkparity.model.parity.model.session;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GetSubscriptionsTest extends ModelTestCase {

	private List<Fixture> data;

	/**
	 * Create a GetSubscriptionsTest.
	 */
	public GetSubscriptionsTest() { super("Get Subscriptions Test"); }

	public void testGetSubscriptions() {
		try {
			List<User> subscriptions;
			for(final Fixture datum : data) {
				subscriptions =
					proxy(datum.sessionModel.getSubscriptions(datum.artifactId));

				assertNotNull("Artifact subscription is null.", subscriptions);
				assertEquals(
						"Number of artifact subscriptions does not match expectation.",
						datum.expectedSubscriptions.size(), subscriptions.size());
				
				for(int i = 0; i < subscriptions.size(); i++) {
					assertEquals(
							"Subscription user does not match expectation.",
							datum.expectedSubscriptions.get(i).getSimpleUsername(),
							subscriptions.get(i).getSimpleUsername());
				}
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
		final ModelTestUser jUnit = ModelTestUser.getJUnit();
		final User jUnitUser = jUnit.getUser();

		login();
		Document d;
		List<User> subscriptions;
		for(final File file : getInputFiles()) {
			d = documentModel.create(file.getName(), file.getName(), file);
			subscriptions = new LinkedList<User>();
			subscriptions.add(jUnitUser);
			data.add(new Fixture(d.getId(), subscriptions, sessionModel));
		}
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private class Fixture {
		private final Long artifactId;
		private final List<User> expectedSubscriptions;
		private final SessionModel sessionModel;
		private Fixture(final Long artifactId,
				final List<User> expectedSubscriptions,
				final SessionModel sessionModel) {
			super();
			this.artifactId = artifactId;
			this.expectedSubscriptions = expectedSubscriptions;
			this.sessionModel = sessionModel;
		}
	}
}
