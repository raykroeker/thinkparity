/*
 * Created On: Aug 23, 2006 11:07:37 AM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DeleteDraftTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "Delete Draft Test";

    /** The test datum. */
    private Fixture datum;

    /** Create DeleteDraftTest. */
    public DeleteDraftTest() { super(NAME); }

    /**
     * Test the delete draft api.
     * 
     */
    public void testDeleteDraft() {
        final Container c = createContainer(OpheliaTestUser.JUNIT, NAME);
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.txt");
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        datum.addListener(datum.junit);
        deleteDraft(datum.junit, c.getId());
        datum.waitForEvents();
        datum.removeListener(datum.junit);
        assertTrue(datum.didNotify);

        final ContainerDraft cd = readContainerDraft(datum.junit, c.getId());
        assertNull("Container draft is not null for " + datum.junit.getSimpleUsername() + ".", cd);
        
        final Container c_x = readContainer(datum.junit_x, c.getUniqueId());
        final ContainerDraft cd_x = readContainerDraft(datum.junit_x, c_x.getId());
        assertNull("Container draft is not null for " + datum.junit_x.getSimpleUsername() + ".", cd_x);

        final Container c_y = readContainer(datum.junit_y, c.getUniqueId());
        final ContainerDraft cd_y = readContainerDraft(datum.junit_y, c_y.getId());
        assertNull("Container draft is not null for " + datum.junit_y.getSimpleUsername() + ".", cd_y);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X,
                OpheliaTestUser.JUNIT_Y);
        login(datum.junit);
        login(datum.junit_x);
        login(datum.junit_y);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        logout(datum.junit_y);
        datum = null;
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private Boolean didNotify;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            this.didNotify = Boolean.FALSE;
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
        }
        private void addListener(final OpheliaTestUser addAs) {
            getContainerModel(addAs).addListener(this);
        }
        private void removeListener(final OpheliaTestUser removeAs) {
            getContainerModel(removeAs).removeListener(this);
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
