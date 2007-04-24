/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.test.ticket;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Ticket 452 Test<br>
 * <b>Description:</b>A test case causing the workflow described in ticket 555
 * to fail.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Ticket555Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 555";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket555Test.
     *
     */
    public Ticket555Test() {
        super(NAME);
    }

    /**
     * {@link http://thinkparity.dyndns.org/trac/parity/ticket/555 Ticket 555}
     * 
     */
    public void testTicket() {
        // create package
        final Container c = createContainer(datum.junit, getName());
        final List<Document> d_list = new ArrayList<Document>();
        d_list.add(addDocument(datum.junit, c.getId(), "JUnitTestFramework"));
        // publish to x/y
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity",
                "JUnit.Y thinkParity");
        datum.waitForEvents();
        // create draft
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        // add document
        d_list.add(addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc"));
        // publish to x
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        // publish version to y
        final ContainerVersion cv = readContainerLatestVersion(datum.junit, c.getId());
        publishVersionToUsers(datum.junit, c.getId(), cv.getVersionId(), "JUnit.Y thinkParity");
        datum.waitForEvents();

        // verify result
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
        for (final Document d : d_list) {
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

        assertEquals("Published to size does not match expectation.", pt.size(), pt_x.size());
        assertEquals("Published to size does not match expectation.", pt.size(), pt_y.size());
        for (int i = 0; i < pt.size(); i++) {
            assertEquals("Published to receipt does not match expectation.", pt.get(i), pt_x.get(i));
            assertEquals("Published to receipt does not match expectation.", pt.get(i), pt_y.get(i));
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#setUp()
     *
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
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        logout(datum.junit_y);
        datum = null;
        super.tearDown();
    }

    /** Test datum fixture. */
    private class Fixture extends TicketTestCase.Fixture {
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
