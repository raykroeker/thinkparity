/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.artifact.ArtifactFlag;
import com.thinkparity.model.container.Container;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

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
        datum.containerModel.publish(
                datum.container.getId(), datum.contacts, datum.teamMembers);
        // ensure the draft published event fired
        assertTrue(NAME + " [DRAFT PUBLISHED EVENT NOT FIRED]", datum.didNotify);
        // ensure the system is the key holder again
        final JabberId keyHolder =
            getInternalArtifactModel().readKeyHolder(datum.container.getId());
        assertEquals(NAME + " KEY HOLDER DOES NOT EQUAL EXPECTATION", User.THINK_PARITY.getId(), keyHolder);
        // ensure the local key flag is not still there
        assertTrue(NAME + " KEY FLAG STILL APPLIED",
                !getArtifactModel().isFlagApplied(datum.container.getId(), ArtifactFlag.KEY));
        // ensure the local and remote team jive
        final List<TeamMember> localTeam =
            getInternalArtifactModel().readTeam2(datum.container.getId());
        final List<JabberId> remoteTeam =
            getInternalSessionModel().readArtifactTeam(datum.container.getUniqueId());
        for (final TeamMember localTeamMember : localTeam) {
            Boolean didFindLocal = Boolean.FALSE;
            for (final JabberId remoteTeamMemberId : remoteTeam) {
                if (localTeamMember.getId().equals(remoteTeamMemberId)) {
                    didFindLocal = Boolean.TRUE;
                    break;
                }
            }
            assertTrue(MessageFormat.format(
                    "COULD NOT FIND LOCAL TEAM MEMBER IN REMOTE TEAM:  {0}",
                    localTeamMember.getId()), didFindLocal);
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
                    "COULD NOT FIND REMOTE TEAM MEMBER IN LOCAL TEAM:  {0}",
                    remoteTeamMemberId), didFindRemote);
        }
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login();
        final ContainerModel containerModel = getContainerModel();
        final Container container = createContainer(NAME);
        addDocuments(container);
        final List<Contact> contacts = readContacts();
        datum = new Fixture(contacts, container, containerModel, new ArrayList<TeamMember>(0));
        containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        logout();
        super.tearDown();
    }

    /** Test datumn fixture. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final List<Contact> contacts;
        private final Container container;
        private final ContainerModel containerModel;
        private Boolean didNotify;
        private final List<TeamMember> teamMembers;
        private Fixture(final List<Contact> contacts,
                final Container container, final ContainerModel containerModel,
                final List<TeamMember> teamMembers) {
            this.contacts = contacts;
            this.container = container;
            this.containerModel = containerModel;
            this.didNotify = Boolean.FALSE;
            this.teamMembers = teamMembers;
        }
        @Override
        public void draftPublished(ContainerEvent e) {
            didNotify = Boolean.TRUE;
        }
    }
}
