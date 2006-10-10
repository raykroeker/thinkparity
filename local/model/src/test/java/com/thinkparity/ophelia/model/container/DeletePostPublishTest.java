/*
 * Created On: Jun 28, 2006 8:29:43 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.document.DocumentModel;
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
        final List<ContainerVersion> versions =
            datum.containerModel.readVersions(datum.container.getId());
        final Map<ContainerVersion, List<Document>> documents = new HashMap<ContainerVersion, List<Document>>();
        for (final ContainerVersion version : versions) {
            documents.put(version,
                    datum.containerModel.readDocuments(datum.container.getId(), version.getVersionId()));
        }

        datum.containerModel.delete(datum.container.getId());
        assertTrue("Container deleted event not fired.", datum.didNotify);

        final Container postDeleteContainer = datum.containerModel.read(datum.container.getId());
        assertNull(NAME + " - Deleted container is not null.", postDeleteContainer);

        final List<ContainerVersion> postDeleteVersions =
            datum.containerModel.readVersions(datum.container.getId());
        assertNotNull(NAME + " - Deleted container versions is null.", postDeleteVersions);
        assertEquals(NAME + " - Deleted versions' size does not match expectation.", 0, postDeleteVersions.size());

        List<Document> postDeleteDocuments;
        Document postDeleteDocument;
        for (final ContainerVersion version : versions) {
            postDeleteDocuments = datum.containerModel.readDocuments(datum.container.getId(), version.getVersionId());   
            assertNotNull(NAME + " - Deleted container documents is null.", postDeleteDocuments);
            assertEquals(NAME + " - Deleted container documents' size does not match expectation.", 0, postDeleteDocuments.size());

            for (final Document document : documents.get(version)) {
                postDeleteDocument = datum.documentModel.get(document.getId());
                assertNull(NAME + " - Deleted document is not null.", postDeleteDocument);
            }
        }
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
        modifyDocuments(OpheliaTestUser.JUNIT, container);
        publish(OpheliaTestUser.JUNIT, container);
        datum = new Fixture(container, containerModel, getDocumentModel(OpheliaTestUser.JUNIT));
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
        private final InternalContainerModel containerModel;
        private Boolean didNotify;
        private final DocumentModel documentModel;
        private Fixture(final Container container,
                final InternalContainerModel containerModel,
                final DocumentModel documentModel) {
            this.containerModel = containerModel;
            this.container = container;
            this.didNotify = Boolean.FALSE;
            this.documentModel = documentModel;
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
