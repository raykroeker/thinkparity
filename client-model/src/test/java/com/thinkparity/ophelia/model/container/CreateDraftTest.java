/*
 * Created On: Aug 1, 2006 9:13:19 AM
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.events.ContainerEvent;

/**
 * <b>Title:</b>thinkParity Container Create Draft Test<br>
 * <b>Description:</b>thinkParity Container Create Draft Test
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class CreateDraftTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "Container - Create Draft Test";

    /** Test datum. */
    private Fixture datum;

    /** Create CreateDraftTest. */
    public CreateDraftTest() { super(NAME); }

    /**
     * Test the container model create api.
     *
     */
    public void testCreateDraft() {
        logTrace("{0} - Creating draft for container \"{1}\".", NAME, datum.container.getName());
        final ContainerDraft draft =  datum.containerModel.createDraft(datum.container.getId());

        assertNotNull(NAME, draft);
        assertEquals(NAME + " [DRAFT ID DOES NOT MATCH EXPECTATION]",
                datum.container.getId(), draft.getContainerId());
        assertTrue(NAME + " [CONTAINER CREATION EVENT NOT FIRED]", datum.didNotify);

        final ContainerVersion latestVersion =
                datum.containerModel.readLatestVersion(datum.container.getId());
        final List<Document> latestVersionDocuments =
                datum.containerModel.readDocuments(latestVersion.getArtifactId(), latestVersion.getVersionId());
        assertEquals(
                "LATEST VERSION DOCUMENT LIST SIZE DOES NOT MATCH DRAFT DOCUMENT LIST SIZE",
                latestVersionDocuments.size(), draft.getDocuments().size());
        assertTrue(NAME + " [KEY FLAG IS NOT APPLIED]",
                getArtifactModel(OpheliaTestUser.JUNIT).isFlagApplied(datum.container.getId(), ArtifactFlag.KEY));
        assertTrue(NAME + " [USER IS NOT KEY HOLDER]",
                getSessionModel(OpheliaTestUser.JUNIT).readKeyHolder(datum.loginUser.getId(),
                        datum.container.getUniqueId()).equals(datum.loginUser.getId()));
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        final InternalContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = createContainer(OpheliaTestUser.JUNIT, NAME);
        addDocuments(OpheliaTestUser.JUNIT, container);
        publish(OpheliaTestUser.JUNIT, container);
        datum = new Fixture(container, containerModel, OpheliaTestUser.JUNIT);
        containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        logout(OpheliaTestUser.JUNIT);
        datum.containerModel.removeListener(datum);
        datum = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final InternalContainerModel containerModel;
        private Boolean didNotify;
        private final User loginUser;
        private Fixture(final Container container,
                final InternalContainerModel containerModel, final User loginUser) {
            this.containerModel = containerModel;
            this.container = container;
            this.didNotify = Boolean.FALSE;
            this.loginUser = loginUser;
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
