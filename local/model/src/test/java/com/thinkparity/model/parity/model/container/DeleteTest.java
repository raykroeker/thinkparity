/*
 * Created On: Jun 28, 2006 8:29:43 PM
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.api.events.ContainerEvent;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.4.5
 */
public class DeleteTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "[TEST DELETE]";

    /** Test datum. */
    private Fixture datum;

    /** Create DeleteTest. */
    public DeleteTest() { super(NAME); }

    /** Test the delete api. */
    public void testDelete() {
        datum.containerModel.delete(datum.container.getId());

        final Container container = datum.containerModel.read(datum.container.getId());
        assertNull(NAME + " [CONTAINER IS NOT NULL]", container);
        assertTrue(NAME + " [CONTAINER DELETION EVENT NOT FIRED]", datum.didNotify);
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
        datum = new Fixture(container, containerModel);
        datum.containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        logout();
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final ContainerModel containerModel;
        private Boolean didNotify;
        private Fixture(final Container container,
                final ContainerModel containerModel) {
            this.containerModel = containerModel;
            this.container = container;
            this.didNotify = Boolean.FALSE;
        }
        @Override
        public void containerDeleted(ContainerEvent e) {
            didNotify = Boolean.TRUE;
            assertTrue(NAME + " [EVENT GENERATED IS NOT LOCAL]", e.isLocal());
            assertTrue(NAME + " [EVENT GENERATED IS REMOTE]", !e.isRemote());
            assertNull(NAME + " [EVENT CONTAINER IS NOT NULL]", e.getContainer());
            assertNull(NAME + " [EVENT DOCUMENT IS NOT NULL]", e.getDocument());
            assertNull(NAME + " [EVENT DRAFT IS NOT NULL]", e.getDraft());
            assertNull(NAME + " [EVENT TEAM MEMBER IS NOT NULL]", e.getTeamMember());
            assertNull(NAME + " [EVENT VERSION NOT NULL]", e.getVersion());
        }
    }
}
