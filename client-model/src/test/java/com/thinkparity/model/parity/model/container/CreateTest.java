/*
 * Created On: Jun 27, 2006 4:00:45 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.api.events.ContainerListener;

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
        Container container = null;
        try { container = datum.cModel.create(datum.containerName); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        assertNotNull(NAME, container);
        assertEquals(NAME + " [CONTAINER NAME DOES NOT MATCH EXPECTATION]",
                datum.containerName, container.getName());
        assertTrue(NAME + " [CONTAINER CREATION EVENT NOT FIRED]", datum.didNotify);

        final ContainerVersion latestVersion = datum.cModel.readLatestVersion(container.getId());
        assertNotNull(NAME, latestVersion);
        assertEquals(NAME, container, latestVersion);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        final ContainerModel cModel = getContainerModel();
        datum = new Fixture(cModel, NAME);
        cModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        getContainerModel().removeListener(datum);
        datum = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture implements ContainerListener {
        private final ContainerModel cModel;
        private final String containerName;
        private Boolean didNotify;
        private Fixture(final ContainerModel cModel, final String containerName) {
            this.cModel = cModel;
            this.containerName = containerName;
            this.didNotify = Boolean.FALSE;
        }
        public void draftCreated(ContainerEvent e) {
            fail(NAME + " [DRAFT CREATED EVENT FIRED]");
        }
        public void teamMemberAdded(ContainerEvent e) {
            fail(NAME + " [TEAM MEMBER ADDED EVENT FIRED]");
        }
        public void teamMemberRemoved(ContainerEvent e) {
            fail(NAME + " [TEAM MEMBER REMOVED EVENT FIRED]");
        }
        public void containerClosed(ContainerEvent e) {
            fail(NAME + " [CONTAINER CLOSED EVENT FIRED]");
        }
        public void containerCreated(final ContainerEvent e) {
            datum.didNotify = Boolean.TRUE;
            assertTrue(NAME + " [EVENT GENERATED IS NOT LOCAL]", e.isLocal());
            assertTrue(NAME + " [EVENT GENERATED IS REMOTE]", !e.isRemote());
            assertNotNull(NAME, e.getContainer());
            assertNull(NAME, e.getTeamMember());
            assertNull(NAME, e.getDocument());
        }
        public void containerDeleted(final ContainerEvent e) {
            fail(NAME + " [CONTAINER DELETED EVENT FIRED]");
        }
        public void containerReactivated(final ContainerEvent e) {
            fail(NAME + " [CONTAINER REACTIVATED EVENT WAS FIRED]");
        }
        public void documentAdded(final ContainerEvent e) {
            fail(NAME + " [DOCUMENT ADDED EVENT FIRED]");
        }
        public void documentRemoved(final ContainerEvent e) {
            fail(NAME + " [DOCUMENT REMOVED EVENT WAS FIRED]");
        }
    }
}
