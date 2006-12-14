/*
 * Created On: Aug 1, 2006 9:13:19 AM
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Container Create Draft Test<br>
 * <b>Description:</b>thinkParity Container Create Draft Test
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class CreateDraftTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "Create Draft Test";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create CreateDraftTest.
     * 
     */
    public CreateDraftTest() {
        super(NAME);
    }

    /**
     * Test the container model create draft api.
     * 
     */
    public void testCreateDraft() {
        final Container c = createContainer(datum.junit, NAME);
        addDocuments(datum.junit, c.getId());
        publish(OpheliaTestUser.JUNIT, c.getId());
        datum.waitForEvents();
        final Container c_x = readContainer(datum.junit_x, c.getUniqueId());
        final Container c_y = readContainer(datum.junit_x, c.getUniqueId());
        datum.addListener(datum.junit);
        ContainerDraft draftCreate = getContainerModel(datum.junit).createDraft(c.getId());
        datum.removeListener(datum.junit);
        datum.waitForEvents();

        ContainerDraft draftRead = readContainerDraft(datum.junit, c.getId());
        assertNotNull("Draft for " + datum.junit.getSimpleUsername() + " is null.", draftRead);
        ContainerDraft draft_x = readContainerDraft(datum.junit_x, c_x.getId());
        assertNotNull("Draft for " + datum.junit_x.getSimpleUsername() + " is null.", draft_x);
        ContainerDraft draft_y = readContainerDraft(datum.junit_y, c_y.getId());
        assertNotNull("Draft for " + datum.junit_y.getSimpleUsername() + " is null.", draft_y);

        assertEquals("Draft created does not match draft read.", draftCreate, draftRead);
        assertTrue("Draft created event not fired.", datum.didNotify);

        final ContainerVersion latestVersion =
            getContainerModel(datum.junit).readLatestVersion(c.getId());
        final List<Document> latestVersionDocuments =
            getContainerModel(datum.junit).readDocuments(
                    latestVersion.getArtifactId(),
                    latestVersion.getVersionId());
        assertEquals(
                "LATEST VERSION DOCUMENT LIST SIZE DOES NOT MATCH DRAFT DOCUMENT LIST SIZE",
                latestVersionDocuments.size(), draftRead.getDocuments().size());
        assertTrue(NAME + " [KEY FLAG IS NOT APPLIED]", getArtifactModel(
                OpheliaTestUser.JUNIT).isFlagApplied(c.getId(),
                ArtifactFlag.KEY));
        assertTrue(NAME + " [USER IS NOT KEY HOLDER]",
                getSessionModel(datum.junit).readKeyHolder(datum.junit.getId(),
                c.getUniqueId()).equals(datum.junit.getId()));
        final InternalDocumentModel documentModel = getDocumentModel(datum.junit);
        for (final Document d : draftRead.getDocuments()) {
            assertTrue("Draft for document \"" + d.getName() + "\" for user \""
                    + datum.junit.getSimpleUsername() + "\" does not exist.",
                    documentModel.doesExistDraft(d.getId()));
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        login(OpheliaTestUser.JUNIT_X);
        login(OpheliaTestUser.JUNIT_Y);
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X,
                OpheliaTestUser.JUNIT_Y);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        logout(datum.junit_y);
        datum = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private Boolean didNotify;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            this.didNotify = Boolean.FALSE;
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
