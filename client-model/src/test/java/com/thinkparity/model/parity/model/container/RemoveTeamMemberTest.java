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
public class RemoveTeamMemberTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "[MODEL] [CONTAINER] [TEST REMOVE TEAM MEMBER]";

    /**
     * Determine if the list of team members contains the user.
     * 
     * @param team
     *            A list of team members.
     * @param user
     *            A user.
     * @return True if the id of the user matches one of the team members.
     */
    protected static Boolean contains(final List<TeamMember> team, final User user) {
        return -1 != indexOf(team, user);
    }

    /**
     * Determine if the list of users contains the team member.
     * 
     * @param users
     *            A list of users.
     * @param teamMember
     *            A team member.
     * @return True if the id of the team member matches one of the users.
     */
    protected static Boolean contains(final List<User> users, final TeamMember teamMember) {
        return -1 != indexOf(users, teamMember);
    }

    /**
     * Obtain the index of the user in the team.
     * 
     * @param team
     *            The team.
     * @param user
     *            A user.
     * @return The index of the user in the team; or -1 if the user does not
     *         exist in the list.
     */
    private static int indexOf(final List<TeamMember> team, final User user) {
        for(int i = 0; i < team.size(); i++)
            if(team.get(i).getId().equals(user.getId())) { return i; }
        return -1;
    }

    /**
     * Obtain the index of a team member in a user list.
     * 
     * @param users
     *            A user list.
     * @param teamMember
     *            A team member.
     * @return The index of the team member in the users list or -1 if the team
     *         member does not exist in the list.
     */
    private static int indexOf(final List<User> users, final TeamMember teamMember) {
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).getId().equals(teamMember.getId())) { return i; }
        }
        return -1;
    }

    /** Test datum. */
    private Fixture datum;

    /** Create UpdateTeamTest. */
    public RemoveTeamMemberTest() { super(NAME); }

    /**
     * Test the update team api.
     *
     */
    public void testUpdateTeam() {
        datum.containerModel.updateTeam(datum.container.getId(), datum.teamMembers);

        assertTrue(NAME + " [TEAM MEMBER REMOVED NOT FIRED]", datum.didNotify);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final ContainerModel containerModel = getContainerModel();
        final Container container = createContainer(NAME);
        addTeam(container);
        final List<User> newTeam = new ArrayList<User>();
        newTeam.add(ModelTestUser.getJUnit().readUser());
        newTeam.add(ModelTestUser.getX().readUser());
        newTeam.add(ModelTestUser.getY().readUser());
        datum = new Fixture(container, containerModel, newTeam);
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
        private final List<User> teamMembers;
        private Fixture(final Container container,
                final ContainerModel containerModel,
                final List<User> teamMembers) {
            this.container = container;
            this.containerModel = containerModel;
            this.didNotify = Boolean.FALSE;
            this.teamMembers = teamMembers;
        }
        @Override
        public void teamMemberRemoved(ContainerEvent e) {
            datum.didNotify = Boolean.TRUE;
            assertNotNull(NAME + " [EVENT CONTAINER IS NULL]", e.getContainer());
            assertNull(NAME + " [EVENT DOCUMENT IS NOT NULL]", e.getDocument());
            assertNull(NAME + " [EVENT DRAFT IS NOT IS NULL]", e.getDraft());
            assertNotNull(NAME + " [EVENT TEAM MEMBER IS NULL]", e.getTeamMember());
            final TeamMember actual = e.getTeamMember();
            final User expected = ModelTestUser.getZ().readUser();
            assertEquals(NAME + " [EVENT TEAM MEMBER JABBER ID DOES NOT MATCH EXPECTATION]", expected.getId(), actual.getId());
            assertEquals(NAME + " [EVENT TEAM MEMBER USER ID DOES NOT MATCH EXPECTATION]", expected.getLocalId(), actual.getLocalId());
            assertEquals(NAME + " [EVENT TEAM MEMBER NAME DOES NOT MATCH EXPECTATION]", expected.getName(), actual.getName());
            assertEquals(NAME + " [EVENT TEAM MEMBER ORGANIZATION DOES NOT MATCH EXPECTATION]", expected.getOrganization(), actual.getOrganization());
            assertEquals(NAME + " [EVENT TEAM MEMBER SIMPLE USERNAME DOES NOT MATCH EXPECTATION]", expected.getSimpleUsername(), actual.getSimpleUsername());
            assertEquals(NAME + " [EVENT TEAM MEMBER USERNAME DOES NOT MATCH EXPECTATION]", expected.getUsername(), actual.getUsername());
        }
    }
}
