/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.container;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamOpener;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.document.InternalDocumentModel;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity OpheliaModel Publish Container Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.23
 */
public class PublishTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "Publish Test";

    /** Test datum. */
    private Fixture datum;

    /** Create PublishTest. */
    public PublishTest() { super(NAME); }

    /**
     * Test the publish api.
     * 
     */
    public void testPublish() {
        Container c = createContainer(datum.junit, getName());
        final List<Document> documents = addDocuments(datum.junit, c.getId());
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();

        c  = readContainer(datum.junit, c.getUniqueId());
        assertNotNull("Container for user \"" + datum.junit.getSimpleUsername() + "\" is null.", c);
        assertTrue(c.isLatest(),
                "Container \"{0}\" is not flagged as latest for user \"{1}\".",
                c.getName(), datum.junit);

        final Container c_x = readContainer(datum.junit_x, c.getUniqueId());
        assertNotNull("Container for user \"" + datum.junit_x.getSimpleUsername() + "\" is null.", c_x);
        assertTrue(c_x.isLatest(),
                "Container \"{0}\" is not flagged as latest for user \"{1}\".",
                c_x.getName(), datum.junit_x);

        final Container c_y = readContainer(datum.junit_y, c.getUniqueId());
        assertNotNull("Container for user \"" + datum.junit_y.getSimpleUsername() + "\" is null.", c_y);
        assertTrue(c_y.isLatest(),
                "Container \"{0}\" is not flagged as latest for user \"{1}\".",
                c_y.getName(), datum.junit_y);

        final ContainerDraft draft = readContainerDraft(datum.junit, c.getId());
        assertNull("Draft for container " + c.getName() + " for user " + datum.junit.getSimpleUsername() + " is not null.", draft);
        final ContainerDraft draft_x = readContainerDraft(datum.junit_x, c_x.getId());
        assertNull("Draft for container " + c_x.getName() + " for user " + datum.junit_y.getSimpleUsername() + " is not null.", draft_x);
        final ContainerDraft draft_y = readContainerDraft(datum.junit_y, c_x.getId());
        assertNull("Draft for container " + c_y.getName() + " for user " + datum.junit_y.getSimpleUsername() + " is not null.", draft_y);

        final JabberId keyHolder = getSessionModel(datum.junit).readKeyHolder(c.getUniqueId());
        assertEquals("Local artifact key holder does not match expectation.",
                User.THINKPARITY.getId(), keyHolder);
        assertTrue("Local key flag is still mistakenly applied.",
                !getArtifactModel(datum.junit).isFlagApplied(c.getId(), ArtifactFlag.KEY));
        InternalDocumentModel documentModel;
        Document d_other;
        DocumentVersion dv, dv_x, dv_y;
        String d_checksum, d_checksum_x, d_checksum_y;
        Long d_size, d_size_x, d_size_y;
        for (final Document d : documents) {
            documentModel = getDocumentModel(datum.junit);
            assertFalse("Document \"" + d.getName() + "\" for user \"" + datum.junit.getSimpleUsername() + "\" still maintains draft.",
                    documentModel.doesExistDraft(d.getId()));
            dv = documentModel.readLatestVersion(d.getId());
            d_checksum = dv.getChecksum();
            d_size = dv.getSize();

            documentModel = getDocumentModel(datum.junit_x);
            d_other = readDocument(datum.junit_x, d.getUniqueId());
            assertFalse("Document \"" + d_other.getName() + "\" for user \"" + datum.junit_x.getSimpleUsername() + "\" still maintains draft.",
                    documentModel.doesExistDraft(d_other.getId()));
            dv_x = documentModel.readLatestVersion(d_other.getId());
            d_checksum_x = dv_x.getChecksum();
            d_size_x = dv_x.getSize();

            documentModel = getDocumentModel(datum.junit_y);
            d_other = readDocument(datum.junit_y, d.getUniqueId());
            assertFalse("Document \"" + d_other.getName() + "\" for user \"" + datum.junit_y.getSimpleUsername() + "\" still maintains draft.",
                    documentModel.doesExistDraft(d_other.getId()));
            dv_y = documentModel.readLatestVersion(d_other.getId());
            d_checksum_y = dv_y.getChecksum();
            d_size_y = dv_y.getSize();

            assertEquals(d_size, d_size_x);
            assertEquals(d_size, d_size_y);
            assertEquals(d_checksum, d_checksum_x);
            assertEquals(d_checksum, d_checksum_y);
        }

        final ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        final List<ArtifactReceipt> pt = readPublishedTo(datum.junit, c.getId(), cv_latest.getVersionId());
        assertNotNull("Published to list is null.", pt);
        for (final ArtifactReceipt receipt : pt) {
            assertNotNull("Published to receipt is null.", receipt);
            assertNotNull("Published to receipt user is null.", receipt.getUser());
            assertNotNull("Published to receipt received on is null.", receipt.getReceivedOn());
        }

        final ContainerVersion cv_x_latest = readContainerLatestVersion(datum.junit_x, c_x.getId());
        final List<ArtifactReceipt> pt_x = readPublishedTo(datum.junit_x, c_x.getId(), cv_x_latest.getVersionId());
        assertNotNull("Published to list is null.", pt_x);
        for (final ArtifactReceipt receipt_x : pt_x) {
            assertNotNull("Published to receipt is null.", receipt_x);
            assertNotNull("Published to receipt user is null.", receipt_x.getUser());
            assertNotNull("Published to receipt received on is null.", receipt_x.getReceivedOn());
        }

        final ContainerVersion cv_y_latest = readContainerLatestVersion(datum.junit_y, c_y.getId());
        final List<ArtifactReceipt> pt_y = readPublishedTo(datum.junit_y, c_y.getId(), cv_y_latest.getVersionId());
        assertNotNull("Published to list is null.", pt_y);
        for (final ArtifactReceipt receipt_y : pt_y) {
            assertNotNull("Published to receipt is null.", receipt_y);
            assertNotNull("Published to receipt user is null.", receipt_y.getUser());
            assertNotNull("Published to receipt received on is null.", receipt_y.getReceivedOn());
        }
    }

    /**
     * Test the publish to e-mail address api.
     * 
     */
    public void testPublishEMail() {
        Container c = createContainer(datum.junit, getName());
        addDocuments(datum.junit, c.getId());
        final String[] emailAddresses = new String[2];
        // non-user e-mail
        emailAddresses[0] = "junit+" + System.currentTimeMillis() + "@thinkparity.com";
        // non-contact user e-mail
        emailAddresses[1] = "junit.w@thinkparity.com";
        publishToEMails(datum.junit, c.getId(), emailAddresses);
        datum.waitForEvents();
    }

    /**
     * Create a package; add a series of documents (>1); publish it to 2 users;
     * modify a single document and publish to the same users.
     * 
     * The validation is about ensuring that the document state and content for
     * the published to users is the same as for the publishing user.
     */
    public void testPublishPostMod() {
        final Container c_initial = createContainer(datum.junit, getName());
        final List<Document> d_list_initial = addDocuments(datum.junit, c_initial.getId());
        publishToUsers(datum.junit, c_initial.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        createDraft(datum.junit, c_initial.getId());
        datum.waitForEvents();
        modifyDocument(datum.junit, d_list_initial.get(0).getId());
        saveDraft(datum.junit, c_initial.getId());
        publishToUsers(datum.junit, c_initial.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();

        final Container c = readContainer(datum.junit, c_initial.getUniqueId());
        final ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        final ContainerVersion cv_previous = readContainerPreviousVersion(datum.junit, c.getId(), cv_latest.getVersionId());
        final List<Document> d_list = readContainerVersionDocuments(datum.junit, c.getId(), cv_latest.getVersionId());

        final Container c_x = readContainer(datum.junit_x, c_initial.getUniqueId());
        final ContainerVersion cv_latest_x = readContainerLatestVersion(datum.junit_x, c_x.getId());
        final ContainerVersion cv_previous_x = readContainerPreviousVersion(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());
        final List<Document> d_list_x = readContainerVersionDocuments(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());

        final Container c_y = readContainer(datum.junit_y, c_initial.getUniqueId());
        final ContainerVersion cv_latest_y = readContainerLatestVersion(datum.junit_y, c_y.getId());
        final ContainerVersion cv_previous_y = readContainerPreviousVersion(datum.junit_y, c_y.getId(), cv_latest.getVersionId());
        final List<Document> d_list_y = readContainerVersionDocuments(datum.junit_y, c_y.getId(), cv_latest_y.getVersionId());

        assertSimilar("Container does not match expectation.", c, c_x);
        assertSimilar("Container does not match expectation.", c, c_y);

        assertSimilar("Container version does not match expectation.", cv_latest, cv_latest_x);
        assertSimilar("Container version does not match expectation.", cv_latest, cv_latest_y);
        
        assertEquals("Document list size does not match expectation.", d_list.size(), d_list_x.size());
        assertEquals("Document list size does not match expectation.", d_list.size(), d_list_y.size());

        Document d, d_x, d_y;
        for (int i = 0; i < d_list.size(); i++) {
            d = d_list.get(i);
            d_x = d_list_x.get(i);
            d_y = d_list_y.get(i);

            assertSimilar("Document does not match expectation.", d, d_x);
            assertSimilar("Document does not match expectation.", d, d_y);

            final DocumentVersion dv_latest = getDocumentModel(datum.junit).readLatestVersion(d.getId());
            final DocumentVersion dv_latest_x = getDocumentModel(datum.junit_x).readLatestVersion(d_x.getId());
            final DocumentVersion dv_latest_y = getDocumentModel(datum.junit_y).readLatestVersion(d_y.getId());

            assertSimilar("Document version does not match expectation.", dv_latest, dv_latest_x);
            assertSimilar("Document version does not match expectation.", dv_latest, dv_latest_y);

            getDocumentModel(datum.junit).openVersion(d.getId(), dv_latest.getVersionId(), new StreamOpener() {
                public void open(final InputStream stream) throws IOException {
                    final File file = getOutputFile(dv_latest);
                    final OutputStream outputStream = new FileOutputStream(file);
                    try {
                        StreamUtil.copy(stream, outputStream, getDefaultBuffer());
                    } finally {
                        outputStream.close();
                    }
                }
            });
            getDocumentModel(datum.junit_x).openVersion(d_x.getId(), dv_latest_x.getVersionId(), new StreamOpener() {
                public void open(final InputStream stream) throws IOException {
                    final File file = getOutputFile(dv_latest_x);
                    final OutputStream outputStream = new FileOutputStream(file);
                    try {
                        StreamUtil.copy(stream, outputStream, getDefaultBuffer());
                    } finally {
                        outputStream.close();
                    }
                }
            });
            try {
                assertEquals("Document version content does not match expectation.", getOutputFile(dv_latest), getOutputFile(dv_latest_x));
            } catch (final IOException iox) {
                fail(createFailMessage(iox));
            }

            getDocumentModel(datum.junit).openVersion(d.getId(), dv_latest.getVersionId(), new StreamOpener() {
                public void open(final InputStream stream) throws IOException {
                    final File file = getOutputFile(dv_latest);
                    final OutputStream outputStream = new FileOutputStream(file);
                    try {
                        StreamUtil.copy(stream, outputStream, getDefaultBuffer());
                    } finally {
                        outputStream.close();
                    }
                }
            });
            getDocumentModel(datum.junit_y).openVersion(d_y.getId(), dv_latest_y.getVersionId(), new StreamOpener() {
                public void open(final InputStream stream) throws IOException {
                    final File file = getOutputFile(dv_latest_y);
                    final OutputStream outputStream = new FileOutputStream(file);
                    try {
                        StreamUtil.copy(stream, outputStream, getDefaultBuffer());
                    } finally {
                        outputStream.close();
                    }
                }
            });
            try {
                assertEquals("Document version content does not match expectation.", getOutputFile(dv_latest), getOutputFile(dv_latest_y));
            } catch (final IOException iox) {
                fail(createFailMessage(iox));
            }
        }


        final List<DocumentVersion> dv_list = readContainerVersionDocumentVersions(datum.junit, c.getId(), cv_latest.getVersionId());
        final Map<DocumentVersion, Delta> delta = getContainerModel(datum.junit).readDocumentVersionDeltas(c.getId(), cv_latest.getVersionId(), cv_previous.getVersionId());
        final List<DocumentVersion> dv_list_x = readContainerVersionDocumentVersions(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());
        final Map<DocumentVersion, Delta> delta_x = getContainerModel(datum.junit_x).readDocumentVersionDeltas(c_x.getId(), cv_latest_x.getVersionId(), cv_previous_x.getVersionId());
        final List<DocumentVersion> dv_list_y = readContainerVersionDocumentVersions(datum.junit_y, c_y.getId(), cv_latest_y.getVersionId());
        final Map<DocumentVersion, Delta> delta_y = getContainerModel(datum.junit_y).readDocumentVersionDeltas(c_y.getId(), cv_latest_y.getVersionId(), cv_previous_y.getVersionId());

        
        assertEquals("Differences between versions do not match expectation.", dv_list.size(), dv_list_x.size());
        assertEquals("Differences between versions do not match expectation.", dv_list.size(), dv_list_y.size());

        assertEquals("Differences between versions do not match expectation.", delta.size(), delta_x.size());
        assertEquals("Differences between versions do not match expectation.", delta.size(), delta_y.size());

        DocumentVersion dv, dv_x, dv_y;
        Delta del, del_x, del_y;
        for (int i = 0; i < dv_list.size(); i++) {
            dv = dv_list.get(i);
            dv_x = dv_list_x.get(i);
            dv_y = dv_list_y.get(i);

            assertSimilar("Document version does not match expectation.", dv, dv_x);
            assertSimilar("Document version does not match expectation.", dv, dv_y);
            
            del = delta.get(dv);
            del_x = delta_x.get(dv_x);
            del_y = delta_y.get(dv_y);

            assertEquals("Document version delta info does not match expectation.", del, del_x);
            assertEquals("Document version delta info does not match expectation.", del, del_y);
        }
    }

    /**
     * Test the publish api but include a comment.
     *
     */
    public void testPublishWithComment() {
        Container c = createContainer(datum.junit, getName());
        final List<Document> documents = addDocuments(datum.junit, c.getId());
        publishToUsersWithComment(datum.junit, c.getId(), NAME, "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();

        c  = readContainer(datum.junit, c.getUniqueId());
        assertNotNull("Container for user \"" + datum.junit.getSimpleUsername() + "\" is null.", c);
        assertTrue(c.isLatest(),
                "Container \"{0}\" is not flagged as latest for user \"{1}\".",
                c.getName(), datum.junit);

        final Container c_x = readContainer(datum.junit_x, c.getUniqueId());
        assertNotNull("Container for user \"" + datum.junit_x.getSimpleUsername() + "\" is null.", c_x);
        assertTrue(c_x.isLatest(),
                "Container \"{0}\" is not flagged as latest for user \"{1}\".",
                c_x.getName(), datum.junit_x);

        final Container c_y = readContainer(datum.junit_y, c.getUniqueId());
        assertNotNull("Container for user \"" + datum.junit_y.getSimpleUsername() + "\" is null.", c_y);
        assertTrue(c_y.isLatest(),
                "Container \"{0}\" is not flagged as latest for user \"{1}\".",
                c_y.getName(), datum.junit_y);

        final ContainerDraft draft = readContainerDraft(datum.junit, c.getId());
        assertNull("Draft for container " + c.getName() + " for user " + datum.junit.getSimpleUsername() + " is not null.", draft);
        final ContainerDraft draft_x = readContainerDraft(datum.junit_x, c_x.getId());
        assertNull("Draft for container " + c_x.getName() + " for user " + datum.junit_y.getSimpleUsername() + " is not null.", draft_x);
        final ContainerDraft draft_y = readContainerDraft(datum.junit_y, c_x.getId());
        assertNull("Draft for container " + c_y.getName() + " for user " + datum.junit_y.getSimpleUsername() + " is not null.", draft_y);

        final JabberId keyHolder = getSessionModel(datum.junit).readKeyHolder(c.getUniqueId());
        assertEquals("Local artifact key holder does not match expectation.",
                User.THINKPARITY.getId(), keyHolder);
        assertTrue("Local key flag is still mistakenly applied.",
                !getArtifactModel(datum.junit).isFlagApplied(c.getId(), ArtifactFlag.KEY));
        InternalDocumentModel documentModel;
        Document d_other;
        for (final Document d : documents) {
            documentModel = getDocumentModel(datum.junit);
            assertFalse("Document \"" + d.getName() + "\" for user \"" + datum.junit.getSimpleUsername() + "\" still maintains draft.",
                    documentModel.doesExistDraft(d.getId()));

            documentModel = getDocumentModel(datum.junit_x);
            d_other = readDocument(datum.junit_x, d.getUniqueId());
            assertFalse("Document \"" + d_other.getName() + "\" for user \"" + datum.junit_x.getSimpleUsername() + "\" still maintains draft.",
                    documentModel.doesExistDraft(d_other.getId()));

            documentModel = getDocumentModel(datum.junit_y);
            d_other = readDocument(datum.junit_y, d.getUniqueId());
            assertFalse("Document \"" + d_other.getName() + "\" for user \"" + datum.junit_y.getSimpleUsername() + "\" still maintains draft.",
                    documentModel.doesExistDraft(d_other.getId()));
        }

        final ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        assertEquals(NAME, cv_latest.getComment());
        final List<ArtifactReceipt> pt = readPublishedTo(datum.junit, c.getId(), cv_latest.getVersionId());
        assertNotNull("Published to list is null.", pt);
        for (final ArtifactReceipt receipt : pt) {
            assertNotNull("Published to receipt is null.", receipt);
            assertNotNull("Published to receipt user is null.", receipt.getUser());
            assertNotNull("Published to receipt received on is null.", receipt.getReceivedOn());
        }

        final ContainerVersion cv_x_latest = readContainerLatestVersion(datum.junit_x, c_x.getId());
        assertEquals(NAME, cv_x_latest.getComment());
        final List<ArtifactReceipt> pt_x = readPublishedTo(datum.junit_x, c_x.getId(), cv_x_latest.getVersionId());
        assertNotNull("Published to list is null.", pt_x);
        for (final ArtifactReceipt receipt_x : pt_x) {
            assertNotNull("Published to receipt is null.", receipt_x);
            assertNotNull("Published to receipt user is null.", receipt_x.getUser());
            assertNotNull("Published to receipt received on is null.", receipt_x.getReceivedOn());
        }

        final ContainerVersion cv_y_latest = readContainerLatestVersion(datum.junit_y, c_y.getId());
        assertEquals(NAME, cv_y_latest.getComment());
        final List<ArtifactReceipt> pt_y = readPublishedTo(datum.junit_y, c_y.getId(), cv_y_latest.getVersionId());
        assertNotNull("Published to list is null.", pt_y);
        for (final ArtifactReceipt receipt_y : pt_y) {
            assertNotNull("Published to receipt is null.", receipt_y);
            assertNotNull("Published to receipt user is null.", receipt_y.getUser());
            assertNotNull("Published to receipt received on is null.", receipt_y.getReceivedOn());
        }
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

    /** Test datumn fixture. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
        }
    }
}
