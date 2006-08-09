/*
 * Created On: Aug 2, 2006 2:34:59 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class AddTeamMemberTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "[MODEL] [CONTAINER] [TEST ADD TEAM MEMBER]";

    /** Test datum. */
    private Fixture datum;

    /** Create AddTeamMemberTest. */
    public AddTeamMemberTest() { super(NAME); }

    /**
     * Test the update team api.
     *
     */
    public void testAddTeamMember() {
        final List<TeamMember> team = datum.containerModel.readTeam(datum.container.getId());
        final List<User> newTeam = new ArrayList<User>(team.size());
        newTeam.addAll(team);
        newTeam.add(datum.user);
        datum.containerModel.updateTeam(datum.container.getId(), newTeam);

        assertTrue(NAME + " [ADD TEAM MEMBER EVENT DID NOT FIRE]", datum.didNotify);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final ContainerModel containerModel = getContainerModel();
        final Container container = createContainer(NAME);
        final User user = ModelTestUser.getX().readUser();
        datum = new Fixture(container, containerModel, user);
        containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final ContainerModel containerModel;
        private Boolean didNotify;
        private final User user;
        private Fixture(final Container container,
                final ContainerModel containerModel, final User user) {
            this.container = container;
            this.containerModel = containerModel;
            this.didNotify = Boolean.FALSE;
            this.user = user;
        }
        @Override
        public void teamMemberAdded(ContainerEvent e) {
            datum.didNotify = Boolean.TRUE;
            assertEquals(NAME + " [EVENT CONTAINER DOES NOT MATCH EXPECTATION]", datum.container, e.getContainer());
            assertNotNull(NAME + " [EVENT CONTAINER IS NULL]", e.getContainer());
            assertNull(NAME + " [EVENT DOCUMENT IS NOT NULL]", e.getDocument());
            assertNull(NAME + " [EVENT DRAFT IS NOT NULL]", e.getDraft());
            assertNotNull(NAME + " [EVENT TEAM MEMBER IS NULL]", e.getTeamMember());
            final TeamMember actual = e.getTeamMember();
            assertEquals(NAME + " [EVENT TEAM MEMBER JABBER ID DOES NOT MATCH EXPECTATION]", user.getId(), actual.getId());
            assertEquals(NAME + " [EVENT TEAM MEMBER USER ID DOES NOT MATCH EXPECTATION]", user.getLocalId(), actual.getLocalId());
            assertEquals(NAME + " [EVENT TEAM MEMBER NAME DOES NOT MATCH EXPECTATION]", user.getName(), actual.getName());
            assertEquals(NAME + " [EVENT TEAM MEMBER ORGANIZATION DOES NOT MATCH EXPECTATION]", user.getOrganization(), actual.getOrganization());
            assertEquals(NAME + " [EVENT TEAM MEMBER SIMPLE USERNAME DOES NOT MATCH EXPECTATION]", user.getSimpleUsername(), actual.getSimpleUsername());
            assertEquals(NAME + " [EVENT TEAM MEMBER USERNAME DOES NOT MATCH EXPECTATION]", user.getUsername(), actual.getUsername());
        }
    }
}
