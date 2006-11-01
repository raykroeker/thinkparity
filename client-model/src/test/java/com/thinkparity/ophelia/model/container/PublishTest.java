/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.container;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.user.TeamMember;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class PublishTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "TEST PUBLISH";

    /** Test datum. */
    private Fixture datum;

    /** Create PublishTest. */
    public PublishTest() { super(NAME); }

    /** Test the publish api. */
    public void testPublish() {
        logger.logTraceId();
        datum.containerModel.publish(
                datum.container.getId(), datum.contacts, datum.teamMembers);
        logger.logTraceId();
        assertTrue("The draft published event was not fired.", datum.didNotify);

        // ensure the system is the key holder again
        final JabberId keyHolder =
            getArtifactModel(OpheliaTestUser.JUNIT).readKeyHolder(datum.container.getId());
        assertEquals("Local artifact key holder does not match expectation.", User.THINK_PARITY.getId(), keyHolder);
        assertTrue("Local key flag is still mistakenly applied.",
                !getArtifactModel(OpheliaTestUser.JUNIT).isFlagApplied(datum.container.getId(), ArtifactFlag.KEY));
        // ensure the local and remote team jive
        final List<TeamMember> localTeam =
            getArtifactModel(OpheliaTestUser.JUNIT).readTeam2(datum.container.getId());
        final List<JabberId> remoteTeam =
            getSessionModel(OpheliaTestUser.JUNIT).readArtifactTeamIds(datum.container.getUniqueId());
        for (final TeamMember localTeamMember : localTeam) {
            Boolean didFindLocal = Boolean.FALSE;
            for (final JabberId remoteTeamMemberId : remoteTeam) {
                if (localTeamMember.getId().equals(remoteTeamMemberId)) {
                    didFindLocal = Boolean.TRUE;
                    break;
                }
            }
            assertTrue(MessageFormat.format(
                    "Could not find local team member {0} in remote team.",
                    localTeamMember), didFindLocal);
        }
        for (final JabberId remoteTeamMemberId : remoteTeam) {
            Boolean didFindRemote = Boolean.FALSE;
            for (final TeamMember localTeamMember : localTeam) {
                if (remoteTeamMemberId.equals(localTeamMember.getId())) {
                    didFindRemote = Boolean.TRUE;
                    break;
                }
            }
            assertTrue(MessageFormat.format(
                    "Could not find remote team member {0} in local team.",
                    remoteTeamMemberId), didFindRemote);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        final ContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = createContainer(OpheliaTestUser.JUNIT, NAME);
        addDocuments(OpheliaTestUser.JUNIT, container);
        final List<Contact> contacts = readContacts(OpheliaTestUser.JUNIT);
        datum = new Fixture(contacts, container, containerModel);
        containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        logger.logTraceId();
        datum.containerModel.removeListener(datum);
        datum = null;
        logger.logTraceId();
        logout(OpheliaTestUser.JUNIT);
        logger.logTraceId();
        super.tearDown();
        logger.logTraceId();
    }

    /** Test datumn fixture. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final List<Contact> contacts;
        private final Container container;
        private final ContainerModel containerModel;
        private Boolean didNotify;
        private final List<TeamMember> teamMembers;
        private Fixture(final List<Contact> contacts,
                final Container container, final ContainerModel containerModel) {
            this.contacts = contacts;
            this.container = container;
            this.containerModel = containerModel;
            this.didNotify = Boolean.FALSE;
            this.teamMembers = Collections.emptyList();
        }
        @Override
        public void draftPublished(ContainerEvent e) {
            didNotify = Boolean.TRUE;
        }
    }
}
