/*
 * Created On: Jun 28, 2006 8:29:43 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.events.ContainerEvent;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.4.5
 */
public class DeletePostPublishTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "TEST DELETE POST PUBLISH";

    /** Test datum. */
    private Fixture datum;

    /** Create DeletePostPublishTest. */
    public DeletePostPublishTest() { super(NAME); }

    /** Test the delete api. */
    public void testDelete() {
        datum.containerModel.delete(datum.container.getId());

        final Container container = datum.containerModel.read(datum.container.getId());
        assertNull(NAME + " [CONTAINER IS NOT NULL]", container);
        assertTrue(NAME + " [CONTAINER DELETION EVENT NOT FIRED]", datum.didNotify);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        final ContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = createContainer(OpheliaTestUser.JUNIT, NAME);
        addDocuments(OpheliaTestUser.JUNIT, container);
        modifyDocuments(OpheliaTestUser.JUNIT, container);
        publish(OpheliaTestUser.JUNIT, container);
        datum = new Fixture(container, containerModel);
        datum.containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        logout(OpheliaTestUser.JUNIT);
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
            assertNotNull(NAME + " EVENT CONTAINER IS NOT NULL", e.getContainer());
            assertNull(NAME + " [EVENT DOCUMENT IS NOT NULL]", e.getDocument());
            assertNull(NAME + " [EVENT DRAFT IS NOT NULL]", e.getDraft());
            assertNull(NAME + " [EVENT TEAM MEMBER IS NOT NULL]", e.getTeamMember());
            assertNull(NAME + " [EVENT VERSION NOT NULL]", e.getVersion());
        }
    }
}
