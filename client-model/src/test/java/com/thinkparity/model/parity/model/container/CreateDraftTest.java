/*
 * Created On: Aug 1, 2006 9:13:19 AM
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerEvent;

/**
 * <b>Title:</b>thinkParity Container Create Draft Test<br>
 * <b>Description:</b>thinkParity Container Create Draft Test
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class CreateDraftTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "[CREATE DRAFT TEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create CreateDraftTest. */
    public CreateDraftTest() { super(NAME); }

    /**
     * Test the container model create api.
     *
     */
    public void testCreateDraft() {
        final ContainerDraft draft =  datum.containerModel.createDraft(datum.containerId);

        assertNotNull(NAME, draft);
        assertEquals(NAME + " [DRAFT ID DOES NOT MATCH EXPECTATION]",
                datum.containerId, draft.getContainerId());
        assertTrue(NAME + " [CONTAINER CREATION EVENT NOT FIRED]", datum.didNotify);

        try {
            assertTrue(NAME + " [USER IS NOT KEY HOLDER]",
                    getSessionModel().isLoggedInUserKeyHolder(datum.containerId));
        }
        catch(final ParityException px) { throw new RuntimeException(px); }
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login();
        final ContainerModel containerModel = getContainerModel();
        final Container container = createContainer(NAME);
        addTeam(container);
        addDocuments(container);
        publish(container);
        datum = new Fixture(containerModel, container.getId());
        containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        logout();
        datum.containerModel.removeListener(datum);
        datum = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final ContainerModel containerModel;
        private final Long containerId;
        private Boolean didNotify;
        private Fixture(final ContainerModel containerModel, final Long containerId) {
            this.containerModel = containerModel;
            this.containerId = containerId;
            this.didNotify = Boolean.FALSE;
        }
        @Override
        public void draftCreated(ContainerEvent e) {
            didNotify = Boolean.TRUE;
            assertTrue(NAME + " [EVENT GENERATED IS NOT LOCAL]", e.isLocal());
            assertTrue(NAME + " [EVENT GENERATED IS REMOTE]", !e.isRemote());
            assertNotNull(NAME, e.getDraft());
            assertNotNull(NAME, e.getContainer());
            assertNull(NAME, e.getDocument());
            assertNull(NAME, e.getTeamMember());
        }
    }
}
