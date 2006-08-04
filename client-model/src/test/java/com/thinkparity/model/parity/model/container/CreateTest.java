/*
 * Created On: Jun 27, 2006 4:00:45 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerEvent;

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
        assertTrue(NAME + " [CONTAINER CREATION EVENT NOT FIRED]", datum.didNotifyContainerCreated);
        assertTrue(NAME + " [CONTAINER CREATION EVENT NOT FIRED]", datum.didNotifyDraftCreated);

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
    private class Fixture extends ContainerTestCase.Fixture {
        private final ContainerModel cModel;
        private final String containerName;
        private Boolean didNotifyContainerCreated;
        private Boolean didNotifyDraftCreated;
        private Fixture(final ContainerModel cModel, final String containerName) {
            this.cModel = cModel;
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
