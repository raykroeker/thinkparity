/*
 * Created On: Sep 27, 2006 8:44:19 AM
 */
package com.thinkparity.ophelia.model.container.archive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.archive.InternalArchiveModel;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerTestCase;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Container Restore Test<br>
 * <b>Description:</b>Test the container's restore api.
 * <ol>
 * <li>Create.
 * <li>Add documents.
 * <li>Publish to everyone.
 * <li>Archive.
 * <li>Restore.
 * <li>Archive.
 * </ol>
 * <br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveRestoreArchiveTest extends ArchiveTestCase {

    /** Test name. */
    private static final String NAME = "Restore Test";

    /**
     * Assert the container is not null.
     * 
     * @param assertion
     *            An assertion.
     * @param container
     *            A container.
     */
    protected static void assertNotNull(final String assertion, final Container container) {
        assertNotNull(assertion + " [CONTAINER IS NULL]", (Object) container);
        assertNotNull(assertion + " [CONTAINER'S CREATED BY IS NULL]", container.getCreatedBy());
        assertNotNull(assertion + " [CONTAINER'S CREATED ON IS NULL]", container.getCreatedOn());
        assertNotNull(assertion + " [CONTAINER'S FLAGS IS NULL]", container.getFlags());
        assertNotNull(assertion + " [CONTAINER'S NAME IS NULL]", container.getName());
        assertNotNull(assertion + " [CONTAINER'S REMOTE INFO IS NULL]", container.getRemoteInfo());
        assertNotNull(assertion + " [CONTAINER'S UPDATED BY REMOTE INFO IS NULL]", container.getRemoteInfo().getUpdatedBy());
        assertNotNull(assertion + " [CONTAINER'S UPDATED ON REMOTE INFO IS NULL]", container.getRemoteInfo().getUpdatedOn());
        assertNotNull(assertion + " [CONTAINER'S STATE IS NULL]", container.getState());
        assertNotNull(assertion + " [CONTAINER'S TYPE IS NULL]", container.getType());
        assertNotNull(assertion + " [CONTAINER'S UNIQUE ID IS NULL]", container.getUniqueId());
        assertNotNull(assertion + " [CONTAINER'S UPDATED BY IS NULL]", container.getUpdatedBy());
        assertNotNull(assertion + " [CONTAINER'S UPDATED ON IS NULL]", container.getUpdatedOn());
    }

    /**
     * Assert that a container version is not null.
     * 
     * @param assertion
     *            The assertion.
     * @param version
     *            The container version.
     */
    protected static void assertNotNull(final String assertion, final ContainerVersion version) {
        assertNotNull(assertion + " [CONTAINER VERSION IS NULL]", (Object) version);
        assertNotNull(assertion + " [CONTAINER VERSION'S ARTIFACT TYPE IS NULL]", version.getArtifactType());
        assertNotNull(assertion + " [CONTAINER VERSION'S UNIQUE ID IS NULL]", version.getArtifactUniqueId());
        assertNotNull(assertion + " [CONTAINER VERSION'S CREATED BY IS NULL]", version.getCreatedBy());
        assertNotNull(assertion + " [CONTAINER VERSION'S CREATED ON IS NULL]", version.getCreatedOn());
        assertNotNull(assertion + " [CONTAINER VERSION'S NAME IS NULL]", version.getName());
        assertNotNull(assertion + " [CONTAINER VERSION'S UPDATED BY IS NULL]", version.getUpdatedBy());
        assertNotNull(assertion + " [CONTAINER VERSION'S UPDATED ON IS NULL]", version.getUpdatedOn());
        assertNotNull(assertion + " [CONTAINER VERSION'S VERSION ID IS NULL]", version.getVersionId());
    }

    /** Test datum. */
    private Fixture datum;

    /** Create RestoreTest. */
    public ArchiveRestoreArchiveTest() {
        super(NAME);
    }

    /** Test the archive api. */
    public void testArchive() {
        final List<ContainerVersion> versions =
            datum.containerModel.readVersions(datum.container.getId());
        final Map<ContainerVersion, List<Document>> documents = new HashMap<ContainerVersion, List<Document>>();
        for (final ContainerVersion version : versions) {
            documents.put(version,
                    datum.containerModel.readDocuments(datum.container.getId(), version.getVersionId()));
        }

        datum.containerModel.archive(datum.container.getId());
        assertTrue("The archive event was not fired.", datum.didNotify);

        final Container archivedContainer =
            datum.archiveModel.readContainer(datum.container.getUniqueId());
        assertNotNull(NAME + " - Archived container is null.", archivedContainer);
        final List<ContainerVersion> archivedVersions =
            datum.archiveModel.readContainerVersions(datum.container.getUniqueId());
        assertNotNull(NAME + " - Archived container versions is null.", archivedVersions);
        assertEquals(NAME + " - Archived container versions size is wrong.", 1, archivedVersions.size());

        final Container postArchiveContainer = datum.containerModel.read(datum.container.getId());
        assertNull(NAME + " - Archived container is not null.", postArchiveContainer);

        final List<ContainerVersion> postArchiveVersions =
            datum.containerModel.readVersions(datum.container.getId());
        assertNotNull(NAME + " - Archived container versions is null.", postArchiveVersions);
        assertEquals(NAME + " - Archived versions' size does not match expectation.", 0, postArchiveVersions.size());

        List<Document> postArchiveDocuments;
        Document postArchiveDocument;
        for (final ContainerVersion version : versions) {
            postArchiveDocuments = datum.containerModel.readDocuments(datum.container.getId(), version.getVersionId());   
            assertNotNull(NAME + " - Archived container documents is null.", postArchiveDocuments);
            assertEquals(NAME + " - Archived container documents' size does not match expectation.", 0, postArchiveDocuments.size());

            for (final Document document : documents.get(version)) {
                postArchiveDocument = datum.documentModel.get(document.getId());
                assertNull(NAME + " - Archived document is not null.", postArchiveDocument);
            }
        }
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT_Z);
        final InternalArtifactModel artifactModel = getArtifactModel(OpheliaTestUser.JUNIT_Z);
        final InternalContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT_Z);
        final Container container = createContainer(OpheliaTestUser.JUNIT_Z, NAME);
        addDocuments(OpheliaTestUser.JUNIT_Z, container.getId());
        publish(OpheliaTestUser.JUNIT_Z, container.getId());
        containerModel.archive(container.getId());
        containerModel.restore(container.getUniqueId());

        final Long containerId = artifactModel.readId(container.getUniqueId());
        datum = new Fixture(getArchiveModel(OpheliaTestUser.JUNIT_Z),
                artifactModel, containerModel.read(containerId),
                containerModel, getDocumentModel(OpheliaTestUser.JUNIT_Z));
        datum.containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        logout(OpheliaTestUser.JUNIT_Z);
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final InternalArchiveModel archiveModel;
        private final Container container;
        private final InternalContainerModel containerModel;
        private Boolean didNotify;
        private final InternalDocumentModel documentModel;
        private Fixture(final InternalArchiveModel archiveModel,
                final InternalArtifactModel artifactModel,
                final Container container, final InternalContainerModel containerModel,
                final InternalDocumentModel documentModel) {
            super();
            this.archiveModel = archiveModel;
            this.container = container;
            this.containerModel = containerModel;
            this.didNotify = Boolean.FALSE;
            this.documentModel = documentModel;
        }
        @Override
        public void containerArchived(final ContainerEvent e) {
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
