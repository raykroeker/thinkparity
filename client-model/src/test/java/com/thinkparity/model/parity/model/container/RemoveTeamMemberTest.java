/*
 * Created On: Aug 2, 2006 2:34:59 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.xmpp.user.User;



/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RemoveTeamMemberTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "[MODEL] [CONTAINER] [TEST REMOVE TEAM MEMBER]";

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

    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final ContainerModel containerModel = getContainerModel();
        final Container container = createContainer(NAME);
        final List<User> teamMembers = new ArrayList<User>();
        teamMembers.add(ModelTestUser.getX().getUser());
        teamMembers.add(ModelTestUser.getY().getUser());
        datum = new Fixture(container, containerModel, teamMembers);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final ContainerModel containerModel;
        private final List<User> teamMembers;
        private Boolean didNotifyAdded;
        private Boolean didNotifyRemoved;
        private Fixture(final Container container,
                final ContainerModel containerModel,
                final List<User> teamMembers) {
            this.container = container;
            this.containerModel = containerModel;
            this.didNotifyAdded = Boolean.FALSE;
            this.didNotifyRemoved = Boolean.FALSE;
            this.teamMembers = teamMembers;
        }
        @Override
        public void teamMemberAdded(ContainerEvent e) {
            datum.didNotifyAdded = Boolean.TRUE;
        }
        @Override
        public void teamMemberRemoved(ContainerEvent e) {
            datum.didNotifyRemoved = Boolean.TRUE;
        }
    }
}
