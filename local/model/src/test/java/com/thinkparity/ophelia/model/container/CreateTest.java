/*
 * Created On: Jun 27, 2006 4:00:45 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.user.TeamMember;

/**
 * <b>Title:</b>thinkParity Container Create Test<br>
 * <b>Description:</b>thinkParity Container Create Test
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class CreateTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [CREATE TEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create CreateTest. */
    public CreateTest() { super(NAME); }

    /**
     * Test the container model create api.
     *
     */
    public void testCreate() {
        final Container container = datum.containerModel.create(datum.containerName);

        assertNotNull(NAME, container);
        assertEquals(NAME + " [CONTAINER NAME DOES NOT MATCH EXPECTATION]",
                datum.containerName, container.getName());
        assertTrue(NAME + " [CONTAINER CREATION EVENT NOT FIRED]", datum.didNotifyContainerCreated);

        final ContainerDraft draft = datum.containerModel.readDraft(container.getId());
        assertNotNull(NAME, draft);
        assertEquals(NAME + " [DRAFT OWNER DOES NOT MATCH EXPECTATION]",
                OpheliaTestUser.JUNIT.getId() , draft.getOwner().getId());
        assertTrue(NAME + " [CONTAINER CREATION EVENT NOT FIRED]", datum.didNotifyDraftCreated);

        assertTrue(
                NAME + " [FLAG KEY NOT APPLIED]",
                getArtifactModel(OpheliaTestUser.JUNIT).isFlagApplied(container.getId(), ArtifactFlag.KEY));

        final List<TeamMember> team = datum.containerModel.readTeam(container.getId());
        assertEquals(NAME + " [TEAM SIZE DOES NOT MATCH EXPECTATION]", 1, team.size());
        assertEquals(NAME + " [TEAM MEMBER NOT MATCH EXPECTATION]",
                OpheliaTestUser.JUNIT.getId(), team.get(0).getId());
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        final ContainerModel cModel = getContainerModel(OpheliaTestUser.JUNIT);
        datum = new Fixture(cModel, NAME);
        cModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        getContainerModel(OpheliaTestUser.JUNIT).removeListener(datum);
        datum = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final ContainerModel containerModel;
        private final String containerName;
        private Boolean didNotifyContainerCreated;
        private Boolean didNotifyDraftCreated;
        private Fixture(final ContainerModel cModel, final String containerName) {
            this.containerModel = cModel;
            this.containerName = containerName;
            this.didNotifyContainerCreated = Boolean.FALSE;
            this.didNotifyDraftCreated = Boolean.FALSE;
        }
        @Override
        public void containerCreated(final ContainerEvent e) {
            datum.didNotifyContainerCreated = Boolean.TRUE;
            assertTrue(NAME + " [EVENT GENERATED IS NOT LOCAL]", e.isLocal());
            assertTrue(NAME + " [EVENT GENERATED IS REMOTE]", !e.isRemote());
            assertNotNull(NAME, e.getContainer());
            assertNull(NAME, e.getDocument());
            assertNull(NAME, e.getDraft());
            assertNull(NAME, e.getTeamMember());
        }
        @Override
        public void draftCreated(ContainerEvent e) {
            datum.didNotifyDraftCreated = Boolean.TRUE;
            assertTrue(NAME + " [EVENT GENERATED IS NOT LOCAL]", e.isLocal());
            assertTrue(NAME + " [EVENT GENERATED IS REMOTE]", !e.isRemote());
            assertNotNull(NAME, e.getContainer());
            assertNull(NAME, e.getDocument());
            assertNotNull(NAME, e.getDraft());
            assertNull(NAME, e.getTeamMember());
        }
    }
}
