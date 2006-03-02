/*
 * Feb 16, 2006
 */
package com.thinkparity.model.parity.model.session;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.CollectionsUtil;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReadArtifactContactsTest extends ModelTestCase {

	private List<Fixture> data;

	/**
	 * Create a ReadArtifactContactsTest.
	 */
	public ReadArtifactContactsTest() { super("Read Artifact Contacts Test"); }

	public void testReadArtifactContacts() {
		try {
			List<Contact> subscriptions;
			for(final Fixture datum : data) {
				subscriptions =
					CollectionsUtil.proxy(datum.sessionModel.readArtifactContacts(datum.artifactId));

				assertNotNull("Artifact subscription is null.", subscriptions);
				assertEquals(
						"Number of artifact subscriptions does not match expectation.",
						datum.expectedArtifactContacts.size(), subscriptions.size());
				
				for(int i = 0; i < subscriptions.size(); i++) {
					assertEquals(
							"Subscription user does not match expectation.",
							datum.expectedArtifactContacts.get(i).getSimpleUsername(),
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
		List<Contact> artifactContacts;
		Contact contact;
		for(final File file : getInputFiles()) {
			d = documentModel.create(file.getName(), file.getName(), file);
			artifactContacts = new LinkedList<Contact>();
			contact = new Contact();
			contact.setId(jUnitUser.getId());
			artifactContacts.add(contact);
			data.add(new Fixture(d.getId(), artifactContacts, sessionModel));
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
		private final List<Contact> expectedArtifactContacts;
		private final SessionModel sessionModel;
		private Fixture(final Long artifactId,
				final List<Contact> expectedArtifactContacts,
				final SessionModel sessionModel) {
			super();
			this.artifactId = artifactId;
			this.expectedArtifactContacts = expectedArtifactContacts;
			this.sessionModel = sessionModel;
		}
	}
}
