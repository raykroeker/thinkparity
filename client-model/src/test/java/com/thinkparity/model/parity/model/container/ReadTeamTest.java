/*
 * Created On: Aug 2, 2006 2:34:59 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.List;

import com.thinkparity.model.container.Container;
import com.thinkparity.model.parity.model.user.TeamMember;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadTeamTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "TEST READ TEAM";

    /** Test datum. */
    private Fixture datum;

    /** Create ReadTeamTest. */
    public ReadTeamTest() { super(NAME); }

    public void testReadTeam() {
        final List<TeamMember> team = datum.containerModel.readTeam(datum.container.getId());
        assertNotNull(NAME + " [TEAM IS NULL]", team);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final InternalContainerModel containerModel = getInternalContainerModel();
        final Container container = createContainer(NAME);
        datum = new Fixture(container, containerModel);
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
        private final Container container;
        private final InternalContainerModel containerModel;
        private Fixture(final Container container,
                final InternalContainerModel containerModel) {
            this.container = container;
            this.containerModel = containerModel;
        }
    }
    
}
