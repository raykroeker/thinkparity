/*
 * Created On: Aug 1, 2006 9:13:19 AM
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.api.events.ContainerListener;

/**
 * <b>Title:</b>thinkParity Container Create Draft Test<br>
 * <b>Description:</b>thinkParity Container Create Draft Test
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class CreateDraftTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [CREATE DRAFT TEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create CreateDraftTest. */
    public CreateDraftTest() { super(NAME); }

    /**
     * Test the container model create api.
     *
     */
    public void testCreateDraft() {
        final ContainerDraft draft =  datum.cModel.createDraft(datum.containerId);

        assertNotNull(NAME, draft);
        assertEquals(NAME + " [DRAFT ID DOES NOT MATCH EXPECTATION]",
                datum.containerId, draft.getId());
        assertEquals(NAME + " [DRAFT VERSION ID DOES NOT MATCH EXPECTATION]",
                datum.eVersionId, draft.getVersionId());
        assertTrue(NAME + " [CONTAINER CREATION EVENT NOT FIRED]", datum.didNotify);

        final ContainerVersion version = datum.cModel.readVersion(draft.getId(), draft.getVersionId());
        assertNotNull(NAME, version);

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
        final ContainerModel cModel = getContainerModel();
        final Container container = createContainer(NAME);
        final ContainerVersion version = cModel.readLatestVersion(container.getId());
        addTeam(container);
        datum = new Fixture(cModel, container.getId(), version.getVersionId() + 1);
        cModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        logout();
        getContainerModel().removeListener(datum);
        datum = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture implements ContainerListener {
        private final ContainerModel cModel;
        private final Long containerId;
        private Boolean didNotify;
        private final Long eVersionId;
        private Fixture(final ContainerModel cModel, final Long containerId, final Long eVersionId) {
            this.cModel = cModel;
            this.containerId = containerId;
            this.eVersionId = eVersionId;
            this.didNotify = Boolean.FALSE;
        }
        public void containerClosed(ContainerEvent e) {
            fail(NAME + " [CONTAINER CLOSED EVENT FIRED]");
        }
        public void containerCreated(ContainerEvent e) {
            fail(NAME + " [CONTAINER CREATED EVENT FIRED]");
        }
        public void containerDeleted(ContainerEvent e) {
            fail(NAME + " [CONTAINER DELETED EVENT FIRED]");
        }
        public void containerReactivated(ContainerEvent e) {
            fail(NAME + " [CONTAINER REACTIVATED EVENT WAS FIRED]");
        }
        public void documentAdded(ContainerEvent e) {
            fail(NAME + " [DOCUMENT ADDED EVENT FIRED]");
        }
        public void documentRemoved(ContainerEvent e) {
            fail(NAME + " [DOCUMENT REMOVED EVENT WAS FIRED]");
        }
        public void draftCreated(ContainerEvent e) {
            didNotify = Boolean.TRUE;
            assertTrue(NAME + " [EVENT GENERATED IS NOT LOCAL]", e.isLocal());
            assertTrue(NAME + " [EVENT GENERATED IS REMOTE]", !e.isRemote());
            assertNotNull(NAME, e.getDraft());
            assertNull(NAME, e.getContainer());
            assertNull(NAME, e.getDocument());
            assertNull(NAME, e.getUser());
        }
        public void teamMemberAdded(ContainerEvent e) {
            fail(NAME + " [TEAM MEMBER ADDED EVENT FIRED]");
        }
        public void teamMemberRemoved(ContainerEvent e) {
            fail(NAME + " [TEAM MEMBER REMOVED EVENT FIRED]");
        }
    }
}
