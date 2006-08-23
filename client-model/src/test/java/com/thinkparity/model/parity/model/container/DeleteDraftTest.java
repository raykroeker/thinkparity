/*
 * Created On: Aug 23, 2006 11:07:37 AM
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.api.events.ContainerEvent;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DeleteDraftTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "TEST DELETE DRAFT";

    /** The test datum. */
    private Fixture datum;

    /** Create DeleteDraftTest. */
    public DeleteDraftTest() { super(NAME); }

    /** Test the delete draft api. */
    public void testDeleteDraft() {
        datum.containerModel.deleteDraft(datum.container.getId());
        assertTrue(NAME + " DRAFT DELETED EVENT NOT FIRED", datum.didNotify);
        final ContainerDraft draft = datum.containerModel.readDraft(datum.container.getId());
        assertNull(NAME + " DRAFT CAN STILL BE READ", draft);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login();
        final InternalContainerModel containerModel = getInternalContainerModel();
        final Container container = createContainer(NAME);
        addDocuments(container);
        publish(container);
        createContainerDraft(container);
        datum = new Fixture(container, containerModel);
        datum.containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        logout();
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final InternalContainerModel containerModel;
        private Boolean didNotify;
        private Fixture(final Container container,
                final InternalContainerModel containerModel) {
            this.container = container;
            this.containerModel = containerModel;
            this.didNotify = Boolean.FALSE;
        }
        @Override
        public void draftDeleted(ContainerEvent e) {
            didNotify = Boolean.TRUE;
            assertNotNull(NAME + " EVENT CONTAINER IS NULL", e.getContainer());
            assertNull(NAME + " EVENT DOCUMENT IS NOT NULL", e.getDocument());
            assertNotNull(NAME + " EVENT DRAFT IS NULL", e.getDraft());
            assertNull(NAME + " EVENT TEAM MEMBER IS NOT NULL", e.getTeamMember());
            assertNull(NAME + " EVENT VERSION IS NOT NULL", e.getVersion());
        }
    }
}
