/*
 * Created On: Sep 27, 2006 8:44:19 AM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.events.ContainerEvent;

/**
 * <b>Title:</b>thinkParity Container Restore Test<br>
 * <b>Description:</b>Test the container's restore api.
 * <ol>
 * <li>Create.
 * <li>Add documents.
 * <li>Publish to everyone.
 * <li>Archive.
 * <li>Restore.
 * </ol>
 * <br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RestoreTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "Restore Test";

    /** Test datum. */
    private Fixture datum;

    /** Create RestoreTest. */
    public RestoreTest() {
        super(NAME);
    }

    /** Test the restore api. */
    public void testRestore() {
        datum.containerModel.restore(datum.container.getUniqueId());
        assertTrue("Restore event did not fire.", datum.didNotify);

        final Long containerId = datum.artifactModel.readId(datum.container.getUniqueId());
        final Container container = datum.containerModel.read(containerId);
        assertNotNull(NAME + " - Container is null.", container);
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        final InternalArtifactModel artifactModel = getArtifactModel(OpheliaTestUser.JUNIT);
        final ContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = createContainer(OpheliaTestUser.JUNIT, NAME);
        addDocuments(OpheliaTestUser.JUNIT, container);
        publish(OpheliaTestUser.JUNIT, container);
        containerModel.archive(container.getId());
        datum = new Fixture(artifactModel, container, containerModel);
        datum.containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        logout(OpheliaTestUser.JUNIT);
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final InternalArtifactModel artifactModel;
        private final Container container;
        private final ContainerModel containerModel;
        private Boolean didNotify;
        private Fixture(final InternalArtifactModel artifactModel,
                final Container container, final ContainerModel containerModel) {
            super();
            this.artifactModel = artifactModel;
            this.container = container;
            this.containerModel = containerModel;
            this.didNotify = Boolean.FALSE;
        }
        @Override
        public void containerRestored(final ContainerEvent e) {
            didNotify = Boolean.TRUE;
            assertTrue("Event generated is not local.", e.isLocal());
            assertTrue("Event generated is remote .", !e.isRemote());
            assertNotNull("The archive event's container is null.", e.getContainer());
            assertNull("The archive event's document is not null.", e.getDocument());
            assertNull("The archive event's draft is not null.", e.getDraft());
            assertNull("The archive event's team member is not null.", e.getTeamMember());
            assertNull("The archive event's version is not null.", e.getVersion());
        }
    }
}
