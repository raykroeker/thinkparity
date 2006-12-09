/*
 * Created On: Jun 28, 2006 8:29:43 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.4.5
 */
public final class DeleteTest extends ContainerTestCase {

    /** The test name <code>String</code>. */
    private static final String NAME = "Delete Test";

    /** Test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create DeleteTest.
     * 
     */
    public DeleteTest() { super(NAME); }

    /**
     * Test the delete api.
     * 
     */
    public void testDelete() {
        final Container c = createContainer(datum.junit, NAME);
        getContainerModel(datum.junit).addListener(datum);
        logger.logInfo("Deleting container \"{0}\" as \"{1}.\"", c.getName(),
                datum.junit_x.getSimpleUsername());
        getContainerModel(datum.junit).delete(c.getId());
        getContainerModel(datum.junit).removeListener(datum);

        assertDeleted(datum.junit, c);
    }

    /**
     * Test the delete api after adding a document.
     *
     */
    public void testDeletePostAdd() {
        final Container c = createContainer(datum.junit, NAME);
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.odt");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.pdf");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.txt");
        final ContainerDraft cd = readContainerDraft(datum.junit, c.getId());
        final List<Document> d = cd.getDocuments();
        getContainerModel(datum.junit).addListener(datum);
        logger.logInfo("Deleting container \"{0}\" as \"{1}.\"", c.getName(),
                datum.junit_x.getSimpleUsername());
        getContainerModel(datum.junit).delete(c.getId());
        getContainerModel(datum.junit).removeListener(datum);

        assertDeleted(datum.junit, c, d);
    }

    /**
     * Test the delete api after multiple publishes.
     *
     */
    public void testDeletePostMultiPublish() {
        final Container c = createContainer(datum.junit, NAME);
        final Document d_doc = addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        final Document d_odt = addDocument(datum.junit, c.getId(), "JUnitTestFramework.odt");
        final Document d_pdf = addDocument(datum.junit, c.getId(), "JUnitTestFramework.pdf");
        final Document d_txt = addDocument(datum.junit, c.getId(), "JUnitTestFramework.txt");
        publishToContacts(datum.junit, c.getId(), "JUnit.X thinkParity",
                "JUnit.Y thinkParity");
        datum.waitForEvents();
        ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        final List<Document> d = readContainerVersionDocuments(datum.junit, c.getId(), cv_latest.getVersionId());
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        modifyDocument(datum.junit, d_doc.getId());
        modifyDocument(datum.junit, d_odt.getId());
        modifyDocument(datum.junit, d_pdf.getId());
        modifyDocument(datum.junit, d_txt.getId());
        publishToTeam(datum.junit, c.getId());
        datum.waitForEvents();
        cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        d.addAll(readContainerVersionDocuments(datum.junit, c.getId(), cv_latest.getVersionId()));
        getContainerModel(datum.junit).addListener(datum);
        logger.logInfo("Deleting container \"{0}\" as \"{1}.\"", c.getName(),
                datum.junit.getSimpleUsername());
        getContainerModel(datum.junit).delete(c.getId());
        getContainerModel(datum.junit).removeListener(datum);
        datum.waitForEvents();

        assertDeleted(datum.junit, c, d);
    }

    /**
     * Test the delete api after multiple publishes.
     *
     */
    public void testDeletePostMultiPublishWithOpen() {
        final Container c = createContainer(datum.junit, NAME);
        final Document d_doc = addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        final Document d_odt = addDocument(datum.junit, c.getId(), "JUnitTestFramework.odt");
        final Document d_pdf = addDocument(datum.junit, c.getId(), "JUnitTestFramework.pdf");
        final Document d_txt = addDocument(datum.junit, c.getId(), "JUnitTestFramework.txt");
        publishToContacts(datum.junit, c.getId(), "JUnit.X thinkParity",
                "JUnit.Y thinkParity");
        datum.waitForEvents();
        ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        final List<Document> d = readContainerVersionDocuments(datum.junit, c.getId(), cv_latest.getVersionId());
        final List<DocumentVersion> dv = readContainerVersionDocumentVersions(datum.junit, c.getId(), cv_latest.getVersionId());
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        modifyDocument(datum.junit, d_doc.getId());
        modifyDocument(datum.junit, d_odt.getId());
        modifyDocument(datum.junit, d_pdf.getId());
        modifyDocument(datum.junit, d_txt.getId());
        publishToTeam(datum.junit, c.getId());
        datum.waitForEvents();
        cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        d.addAll(readContainerVersionDocuments(datum.junit, c.getId(), cv_latest.getVersionId()));
        dv.addAll(readContainerVersionDocumentVersions(datum.junit, c.getId(), cv_latest.getVersionId()));
        getContainerModel(datum.junit).addListener(datum);
        for (final Document document : d)
            openDocument(datum.junit, document.getId());
        for (final DocumentVersion documentVersion : dv)
            openDocumentVersion(datum.junit, documentVersion.getArtifactId(),
                    documentVersion.getVersionId());
        logger.logInfo("Deleting container \"{0}\" as \"{1}.\"", c.getName(),
                datum.junit.getSimpleUsername());
        getContainerModel(datum.junit).delete(c.getId());
        getContainerModel(datum.junit).removeListener(datum);
        datum.waitForEvents();

        assertDeleted(datum.junit, c, d);
    }

    /**
     * Test the delete api after adding a document.
     *
     */
    public void testDeletePostPublish() {
        final Container c = createContainer(datum.junit, NAME);
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.odt");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.pdf");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.txt");
        publishToContacts(datum.junit, c.getId(), "JUnit.X thinkParity",
                "JUnit.Y thinkParity");
        datum.waitForEvents();
        final ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        final List<Document> d = readContainerVersionDocuments(datum.junit, c.getId(), cv_latest.getVersionId());
        getContainerModel(datum.junit).addListener(datum);
        logger.logInfo("Deleting container \"{0}\" as \"{1}.\"", c.getName(),
                datum.junit.getSimpleUsername());
        getContainerModel(datum.junit).delete(c.getId());
        getContainerModel(datum.junit).removeListener(datum);
        datum.waitForEvents();

        assertDeleted(datum.junit, c, d);
    }

    /**
     * Test the delete api after receiving a container.
     *
     */
    public void testDeletePostPublishAsRecipient() {
        final Container c = createContainer(datum.junit, NAME);
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.odt");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.pdf");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.txt");
        publishToContacts(datum.junit, c.getId(), "JUnit.X thinkParity",
                "JUnit.Y thinkParity");
        datum.waitForEvents();
        final Container c_x = readContainer(datum.junit_x, c.getUniqueId());
        final ContainerVersion cv_x_latest = readContainerLatestVersion(datum.junit_x, c_x.getId());
        final List<Document> d_x = readContainerVersionDocuments(datum.junit_x, c_x.getId(), cv_x_latest.getVersionId());
        getContainerModel(datum.junit_x).addListener(datum);
        logger.logInfo("Deleting container \"{0}\" as \"{1}.\"", c_x.getName(),
                datum.junit_x.getSimpleUsername());
        getContainerModel(datum.junit_x).delete(c_x.getId());
        getContainerModel(datum.junit_x).removeListener(datum);
        datum.waitForEvents();

        assertDeleted(datum.junit_x, c_x, d_x);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
     */
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
     * 
     */
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        logout(datum.junit_y);
        datum = null;
        super.tearDown();
    }

    /**
     * Assert that the container has been deleted. Also assert the datum's
     * didNotify flag.
     * 
     * @param assertAs
     *            The user to check/assert for.
     * @param container
     *            The container.
     */
    private void assertDeleted(final OpheliaTestUser assertAs,
            final Container container) {
        final List<Document> documents = Collections.emptyList();
        assertDeleted(assertAs, container, documents);
    }

    /**
     * Assert that the container and the documents have been deleted. Also
     * assert the datum's did notify flag.
     * 
     * @param assertAs
     *            The user to check/assert for.
     * @param container
     *            The container.
     * @param documents
     *            The documents.
     */
    private void assertDeleted(final OpheliaTestUser assertAs,
            final Container container, final List<Document> documents) {
        final Container cRead = getContainerModel(assertAs).read(container.getId());
        assertNull("Container \"" + container.getName() + "\" was not deleted.",
                cRead);
        assertTrue("Container deleted event was not fired for container \""
                + container.getName() + ".\"", datum.didNotify);
        Document dRead;
        for (final Document document : documents) {
            dRead = getDocumentModel(assertAs).read(document.getId());
            assertNull("Document \"" + document.getName() + "\" was not deleted.",
                    dRead);
        }
    }

    /** Test data definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private Boolean didNotify;
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
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
        @Override
        public void containerDeleted(ContainerEvent e) {
            didNotify = Boolean.TRUE;
            assertTrue(NAME + " [EVENT GENERATED IS NOT LOCAL]", e.isLocal());
            assertTrue(NAME + " [EVENT GENERATED IS REMOTE]", !e.isRemote());
            assertNotNull(NAME + " EVENT CONTAINER IS NULL", e.getContainer());
            assertNull(NAME + " [EVENT DOCUMENT IS NOT NULL]", e.getDocument());
            assertNull(NAME + " [EVENT DRAFT IS NOT NULL]", e.getDraft());
            assertNull(NAME + " [EVENT TEAM MEMBER IS NOT NULL]", e.getTeamMember());
            assertNull(NAME + " [EVENT VERSION NOT NULL]", e.getVersion());
        }
    }
}
