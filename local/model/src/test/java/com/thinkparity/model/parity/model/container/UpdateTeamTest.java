/*
 * Created On: Aug 2, 2006 2:34:59 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.ArrayList;

import com.thinkparity.model.xmpp.user.User;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UpdateTeamTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "[MODEL] [CONTAINER] [TEST UPDATE TEAM]";

    /** Test datum. */
    private Fixture datum;

    /** Create UpdateTeamTest. */
    public UpdateTeamTest() { super(NAME); }

    public void testUpdateTeam() {
        datum.cModel.updateTeam(datum.containerId, new ArrayList<User>());
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final ContainerModel cModel = getContainerModel();
        final Container container = createContainer(NAME);
        datum = new Fixture(cModel, container.getId());
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
    private class Fixture {
        private final ContainerModel cModel;
        private final Long containerId;
        private Fixture(final ContainerModel cModel ,final Long containerId) {
            this.cModel = cModel;
            this.containerId = containerId;
        }
    }
    
}
