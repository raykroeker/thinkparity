/*
 * Created On: Aug 2, 2006 2:34:59 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.parity.model.user.TeamMemberState;
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
        final List<User> users = new ArrayList<User>();
        users.add(datum.user);
        datum.containerModel.updateTeam(datum.container.getId(), users);

        assertTrue(NAME + " [ADD TEAM MEMBER EVENT DID NOT FIRE]", datum.didNotify);
        final List<TeamMember> teamMembers = datum.containerModel.readTeam(datum.container.getId());
        Boolean didContainUser = Boolean.FALSE;
        for(final TeamMember teamMember : teamMembers) {
            if(teamMember.getId().equals(datum.user.getId())) {
                didContainUser = Boolean.TRUE;
                assertEquals(NAME + " [TEAM MEMBER DOES NOT MATCH USER]", datum.user, (User) teamMember);
                assertEquals(NAME + " [TEAM MEMBER STATE DOES NOT MATCH EXPECTATION]", TeamMemberState.PENDING, teamMember.getState());
                break;
            }
        }
        assertTrue(NAME + " [TEAM MEMBERS DOES NOT CONTAIN ADDED TEAM MEMBER]", didContainUser);
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
        logout();
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
            assertNotNull(NAME + " [EVENT TEAM MEMBER IS NULL]", e.getTeamMember());
        }
    }
}
