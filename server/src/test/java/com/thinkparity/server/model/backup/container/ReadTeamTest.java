/*
 * Created On: Aug 2, 2006 2:34:59 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.user.TeamMember;

import com.thinkparity.ophelia.OpheliaTestUser;


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
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final InternalContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = createContainer(OpheliaTestUser.JUNIT, NAME);
        datum = new Fixture(container, containerModel);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
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
