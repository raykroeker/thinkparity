/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.document.InternalDocumentModel;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
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
        Container c = createContainer(datum.junit, NAME);
        final List<Document> documents = addDocuments(datum.junit, c.getId());
        publish(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
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

        final JabberId keyHolder = getArtifactModel(datum.junit).readKeyHolder(c.getId());
        assertEquals("Local artifact key holder does not match expectation.",
                User.THINK_PARITY.getId(), keyHolder);
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
        final Map<User, ArtifactReceipt> pt = readPublishedTo(datum.junit, c.getId(), cv_latest.getVersionId());
        assertNotNull("Published to list is null.", pt);
        for (final Entry<User, ArtifactReceipt> entry : pt.entrySet()) {
            assertNotNull("Published to user is null.", entry.getKey());
            assertNotNull("Published to receipt is null.", entry.getValue());
            assertNotNull("Published to receipt received on is null.", entry.getValue().getReceivedOn());
        }

        final ContainerVersion cv_x_latest = readContainerLatestVersion(datum.junit_x, c_x.getId());
        final Map<User, ArtifactReceipt> pt_x = readPublishedTo(datum.junit_x, c_x.getId(), cv_x_latest.getVersionId());
        assertNotNull("Published to list is null.", pt_x);
        for (final Entry<User, ArtifactReceipt> entry_x : pt_x.entrySet()) {
            assertNotNull("Published to user is null.", entry_x.getKey());
            assertNotNull("Published to receipt is null.", entry_x.getValue());
            assertNotNull("Published to receipt received on is null.", entry_x.getValue().getReceivedOn());
        }

        final ContainerVersion cv_y_latest = readContainerLatestVersion(datum.junit_y, c_y.getId());
        final Map<User, ArtifactReceipt> pt_y = readPublishedTo(datum.junit_y, c_y.getId(), cv_y_latest.getVersionId());
        assertNotNull("Published to list is null.", pt_y);
        for (final Entry<User, ArtifactReceipt> entry_y : pt_y.entrySet()) {
            assertNotNull("Published to user is null.", entry_y.getKey());
            assertNotNull("Published to receipt is null.", entry_y.getValue());
            assertNotNull("Published to receipt received on is null.", entry_y.getValue().getReceivedOn());
        }
    }

    /**
     * Test the publish api but include a comment.
     *
     */
    public void testPublishWithComment() {
        Container c = createContainer(datum.junit, NAME);
        final List<Document> documents = addDocuments(datum.junit, c.getId());
        publishWithComment(datum.junit, c.getId(), NAME, "JUnit.X thinkParity", "JUnit.Y thinkParity");
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

        final JabberId keyHolder = getArtifactModel(datum.junit).readKeyHolder(c.getId());
        assertEquals("Local artifact key holder does not match expectation.",
                User.THINK_PARITY.getId(), keyHolder);
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
        final Map<User, ArtifactReceipt> pt = readPublishedTo(datum.junit, c.getId(), cv_latest.getVersionId());
        assertNotNull("Published to list is null.", pt);
        for (final Entry<User, ArtifactReceipt> entry : pt.entrySet()) {
            assertNotNull("Published to user is null.", entry.getKey());
            assertNotNull("Published to receipt is null.", entry.getValue());
            assertNotNull("Published to receipt received on is null.", entry.getValue().getReceivedOn());
        }

        final ContainerVersion cv_x_latest = readContainerLatestVersion(datum.junit_x, c_x.getId());
        assertEquals(NAME, cv_x_latest.getComment());
        final Map<User, ArtifactReceipt> pt_x = readPublishedTo(datum.junit_x, c_x.getId(), cv_x_latest.getVersionId());
        assertNotNull("Published to list is null.", pt_x);
        for (final Entry<User, ArtifactReceipt> entry_x : pt_x.entrySet()) {
            assertNotNull("Published to user is null.", entry_x.getKey());
            assertNotNull("Published to receipt is null.", entry_x.getValue());
            assertNotNull("Published to receipt received on is null.", entry_x.getValue().getReceivedOn());
        }

        final ContainerVersion cv_y_latest = readContainerLatestVersion(datum.junit_y, c_y.getId());
        assertEquals(NAME, cv_y_latest.getComment());
        final Map<User, ArtifactReceipt> pt_y = readPublishedTo(datum.junit_y, c_y.getId(), cv_y_latest.getVersionId());
        assertNotNull("Published to list is null.", pt_y);
        for (final Entry<User, ArtifactReceipt> entry_y : pt_y.entrySet()) {
            assertNotNull("Published to user is null.", entry_y.getKey());
            assertNotNull("Published to receipt is null.", entry_y.getValue());
            assertNotNull("Published to receipt received on is null.", entry_y.getValue().getReceivedOn());
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
