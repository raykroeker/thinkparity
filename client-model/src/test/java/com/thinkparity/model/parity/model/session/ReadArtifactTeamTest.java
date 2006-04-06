/*
 * Feb 16, 2006
 */
package com.thinkparity.model.parity.model.session;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReadArtifactTeamTest extends ModelTestCase {

	private List<Fixture> data;

	/**
	 * Create a ReadArtifactTeamTest.
	 */
	public ReadArtifactTeamTest() { super("Read Artifact Contacts Test"); }

	public void testReadArtifactTeam() {
		try {
			Set<User> artifactTeam;
			for(final Fixture datum : data) {
                artifactTeam =
                    datum.sessionModel.readArtifactTeam(datum.artifactId);

				assertNotNull("Artifact subscription is null.", artifactTeam);
				assertEquals(
						"Number of artifact subscriptions does not match expectation.",
						datum.expectedTeam.size(), artifactTeam.size());
				
				for(final User teamMember : artifactTeam) {
					assertTrue(
							"Subscription user does not match expectation.",
							datum.expectedTeam.contains(teamMember));
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
		Set<User> team;
		User teamMember;
		for(final File file : getInputFiles()) {
			d = documentModel.create(file.getName(), file.getName(), file);

            team = new HashSet<User>();
			teamMember = new User();
            teamMember.setId(jUnitUser.getId());
			team.add(teamMember);

			data.add(new Fixture(d.getId(), team, sessionModel));
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
		private final Set<User> expectedTeam;
		private final SessionModel sessionModel;
		private Fixture(final Long artifactId, final Set<User> expectedTeam,
                final SessionModel sessionModel) {
			super();
			this.artifactId = artifactId;
			this.expectedTeam = expectedTeam;
			this.sessionModel = sessionModel;
		}
	}
}
