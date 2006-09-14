/*
 * Created On: Aug 23, 2006 11:07:37 AM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.assertion.NotTrueAssertion;
import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.events.ContainerEvent;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DeleteFirstDraftTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "TEST DELETE FIRST DRAFT";

    /** The test datum. */
    private Fixture datum;

    /** Create DeleteDraftTest. */
    public DeleteFirstDraftTest() { super(NAME); }

    /** Test the delete draft api. */
    public void testDeleteDraft() {
        try {
            datum.containerModel.deleteDraft(datum.container.getId());
        } catch (final NotTrueAssertion nta) {
            // this assertion should be raised
            if (!nta.getMessage().equals("CANNOT DELETE FIRST DRAFT")) {
                fail(createFailMessage(nta));
            }
        }
        assertTrue(NAME + " DRAFT DELETED EVENT WAS FIRED", !datum.didNotify);
        final ContainerDraft draft = datum.containerModel.readDraft(datum.container.getId());
        assertNotNull(NAME + " DRAFT CANNOT STILL BE READ", draft);
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
        datum.containerModel.addListener(datum);
        login(OpheliaTestUser.JUNIT);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        logout(OpheliaTestUser.JUNIT);
        datum.containerModel.removeListener(datum);
        datum = null;
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
