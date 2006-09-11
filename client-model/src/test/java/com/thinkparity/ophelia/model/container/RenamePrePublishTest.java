/*
 * Created On: Aug 23, 2006 1:04:52 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.events.ContainerEvent;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RenamePrePublishTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "TEST RENAME PRE PUBLISH";

    /** Test datum. */
    private Fixture datum;

    /** Create RenamePrePublishTest. */
    public RenamePrePublishTest() { super(NAME); }

    /** Test the rename api. */
    public void testRename() {
        datum.containerModel.rename(datum.container.getId(), datum.name);
        assertTrue(NAME + " CONTAINER UPDATED EVENT NOT FIRED", datum.didNotify);
        final Container container = datum.containerModel.read(datum.container.getId());
        assertEquals(NAME + " CONTAINER NAME NOT UPDATED", datum.name, container.getName());
    }


    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final InternalContainerModel containerModel = getInternalContainerModel();
        final Container container = createContainer(NAME);
        final String name = NAME + " RENAMED";
        datum = new Fixture(container, containerModel, name);
        datum.containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final InternalContainerModel containerModel;
        private Boolean didNotify;
        private final String name;
        private Fixture(final Container container,
                final InternalContainerModel containerModel, final String name) {
            this.container = container;
            this.containerModel = containerModel;
            this.didNotify = Boolean.FALSE;
            this.name = name;
        }
        @Override
        public void containerUpdated(ContainerEvent e) {
            didNotify = Boolean.TRUE;
            assertNotNull(NAME + " EVENT CONTAINER IS NULL", e.getContainer());
            assertNull(NAME + " EVENT CONTAINER IS NOT NULL", e.getDocument());
            assertNull(NAME + " EVENT CONTAINER IS NOT NULL", e.getDraft());
            assertNull(NAME + " EVENT CONTAINER IS NOT NULL", e.getTeamMember());
            assertNull(NAME + " EVENT CONTAINER IS NOT NULL", e.getVersion());
        }
    }
}
