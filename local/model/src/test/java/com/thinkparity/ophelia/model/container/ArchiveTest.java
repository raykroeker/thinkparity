/*
 * Created On: Sep 26, 2006 7:47:30 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.archive.InternalArchiveModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.events.ContainerEvent;

/**
 * <b>Title:</b>thinkParity Container Archive Test<br>
 * <b>Description:</b>Test the archive api.
 * <ol>
 * <li>Create.
 * <li>Add documents.
 * <li>Publish to everyone.
 * <li>Archive.
 * </ol>
 * <br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "Archive Test";

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

    /**
     * Assert the container versions are not null.
     * 
     * @param assertion
     *            An assertion.
     * @param containers
     *            A list of containers.
     */
    protected static void assertNotNull(final String assertion,
            final List<ContainerVersion> versions) {
        assertNotNull(assertion + " [VERSIONS IS NULL]", (Object) versions);
        for(final ContainerVersion version : versions) {
            assertNotNull(assertion, version);
        }
    }

    /** Test datum. */
    private Fixture datum;


    /** Create ArchiveTest. */
    public ArchiveTest() {
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
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        final InternalArchiveModel archiveModel = getArchiveModel(OpheliaTestUser.JUNIT);
        final InternalContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = createContainer(OpheliaTestUser.JUNIT, NAME);
        addDocuments(OpheliaTestUser.JUNIT, container);
        publishToContacts(OpheliaTestUser.JUNIT, container);
        datum = new Fixture(archiveModel, container, containerModel, getDocumentModel(OpheliaTestUser.JUNIT));
        datum.containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
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
        private final InternalArchiveModel archiveModel;
        private final Container container;
        private final InternalContainerModel containerModel;
        private Boolean didNotify;
        private final DocumentModel documentModel;
        private Fixture(final InternalArchiveModel archiveModel,
                final Container container, final InternalContainerModel containerModel,
                final DocumentModel documentModel) {
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
