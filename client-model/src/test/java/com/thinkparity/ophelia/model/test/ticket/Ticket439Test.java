/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.test.ticket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.io.StreamOpener;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Ticket 439 Test<br>
 * <b>Description:</b>A test case causing the workflow described in ticket 439
 * to fail.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Ticket439Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 439";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket439Test.
     *
     */
    public Ticket439Test() {
        super(NAME);
    }

    /**
     * @see com.thinkparity.ophelia.OpheliaTestCase#getOutputDirectory()
     *
     */
    @Override
    public File getOutputDirectory() {
        final File outputDirectory = new File(super.getOutputDirectory(), getName());
        if (!outputDirectory.exists())
            assertTrue(outputDirectory.mkdirs());
        return outputDirectory;
    }

    /**
     * {@link http://thinkparity.dyndns.org/trac/parity/ticket/439 Ticket 439}
     * 
     */
    public void testTicket() {
        // create a container; add a document; publish to first user
        final Container c_initial = createContainer(datum.junit, NAME);
        final Document d_initial = addDocument(datum.junit, c_initial.getId(), "JUnitTestFramework.doc");
        publishToUsers(datum.junit, c_initial.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        // ensure the container; the container version; the documents; the document streams; the document versions; the document version streams; the delta info and the published to info are similar
        Container c = readContainer(datum.junit, c_initial.getUniqueId());
        ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        ContainerVersion cv_previous = readContainerPreviousVersion(datum.junit, c.getId(), cv_latest.getVersionId());
        List<Document> cv_latest_d_list = readContainerVersionDocuments(datum.junit, c.getId(), cv_latest.getVersionId());
        List<DocumentVersion> cv_latest_dv_list = readContainerVersionDocumentVersions(datum.junit, c.getId(), cv_latest.getVersionId());
        Map<DocumentVersion, Delta> cv_latest_delta = readContainerVersionDeltas(datum.junit, c.getId(), cv_latest.getVersionId());
        List<ArtifactReceipt> cv_latest_pt = readPublishedTo(datum.junit, c.getId(), cv_latest.getVersionId());
        List<TeamMember> tm_list = readTeam(datum.junit, c.getId());

        Container c_x = readContainer(datum.junit_x, c_initial.getUniqueId());
        ContainerVersion cv_latest_x = readContainerLatestVersion(datum.junit_x, c_x.getId());
        ContainerVersion cv_previous_x = readContainerPreviousVersion(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());
        List<Document> cv_latest_d_list_x = readContainerVersionDocuments(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());
        List<DocumentVersion> cv_latest_dv_list_x = readContainerVersionDocumentVersions(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());
        Map<DocumentVersion, Delta> cv_latest_delta_x = readContainerVersionDeltas(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());
        List<ArtifactReceipt> cv_latest_pt_x = readPublishedTo(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());
        List<TeamMember> tm_list_x = readTeam(datum.junit_x, c_x.getId());

        assertSimilar("Container does not match expectation.", c, c_x);
        assertSimilar("Latest container version does not match expectation.", cv_latest, cv_latest_x);
        assertNull("Previous version is not null.", cv_previous);
        assertNull("Previous version is not null.", cv_previous_x);
        assertEquals("Document list size does not match expectation.", cv_latest_d_list.size(), cv_latest_d_list_x.size());
        for (int i = 0; i < cv_latest_d_list.size(); i++) {
            assertSimilar("Document does not match expectation.", cv_latest_d_list.get(i), cv_latest_d_list_x.get(i));
        }
        assertEquals("Document version list size does not match expectation.", cv_latest_d_list.size(), cv_latest_d_list_x.size());
        for (int i = 0; i < cv_latest_dv_list.size(); i++) {
            final DocumentVersion cv_latest_dv = cv_latest_dv_list.get(i);
            final DocumentVersion cv_latest_dv_x = cv_latest_dv_list_x.get(i);
            assertSimilar("Document version does not match expectation.", cv_latest_dv, cv_latest_dv_x);
            getDocumentModel(datum.junit).openVersion(cv_latest_dv.getArtifactId(), cv_latest_dv.getVersionId(), new StreamOpener() {
                public void open(final InputStream stream) throws IOException {
                    streamToFile(stream, getOutputFile(cv_latest_dv));
                }
            });
            getDocumentModel(datum.junit_x).openVersion(cv_latest_dv_x.getArtifactId(), cv_latest_dv_x.getVersionId(), new StreamOpener() {
                public void open(final InputStream stream) throws IOException {
                    streamToFile(stream, getOutputFile(cv_latest_dv_x));
                }
            });
            try {
                assertEquals("Document version content does not match expectation.", getOutputFile(cv_latest_dv), getOutputFile(cv_latest_dv_x));
            } catch (final IOException iox) {
                fail(iox, "Could not compare input stream is with is_x.");
            }
        }
        assertEquals("Document version delta list does not match expectation.", cv_latest_delta.size(), cv_latest_delta_x.size());
        for (int i = 0; i < cv_latest_dv_list.size(); i++) {
            final DocumentVersion cv_latest_dv = cv_latest_dv_list.get(i);
            final DocumentVersion cv_latest_dv_x = cv_latest_dv_list_x.get(i);
            assertEquals("Document version delta does not match expectation.", Delta.ADDED, cv_latest_delta.get(cv_latest_dv));
            assertEquals("Document version delta does not match expectation.", cv_latest_delta.get(cv_latest_dv), cv_latest_delta_x.get(cv_latest_dv_x));
        }
        assertEquals("Published to list does not match expectation.", 1, cv_latest_pt.size());
        assertEquals("Published to list does not match expectation.", cv_latest_pt.size(), cv_latest_pt_x.size());
        for (int i = 0; i < cv_latest_pt.size(); i++) {
            assertSimilar("Published to artifact receipt does not match expectation.", cv_latest_pt.get(i), cv_latest_pt_x.get(i));
        }
        assertEquals("Team list does not match expectation.", 2, tm_list.size());
        assertEquals("Team list does not match expectation.", tm_list.size(), tm_list_x.size());
        for (int i = 0; i < tm_list.size(); i++) {
            assertSimilar("Team member does not match expectation.", tm_list.get(i), tm_list_x.get(i));
        }

        // create a draft modfiy document publish to first user
        createDraft(datum.junit, c_initial.getId());
        datum.waitForEvents();
        modifyDocument(datum.junit, d_initial.getId());
        publishToUsers(datum.junit, c_initial.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        // ensure the container; the container version; the documents; the document streams; the document versions; the document version streams and the delta information are similar
        c = readContainer(datum.junit, c_initial.getUniqueId());
        cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        cv_previous = readContainerPreviousVersion(datum.junit, c.getId(), cv_latest.getVersionId());
        cv_latest_d_list = readContainerVersionDocuments(datum.junit, c.getId(), cv_latest.getVersionId());
        cv_latest_dv_list = readContainerVersionDocumentVersions(datum.junit, c.getId(), cv_latest.getVersionId());
        cv_latest_delta = readContainerVersionDeltas(datum.junit, c.getId(), cv_latest.getVersionId(), cv_previous.getVersionId());
        cv_latest_pt = readPublishedTo(datum.junit, c.getId(), cv_latest.getVersionId());
        tm_list = readTeam(datum.junit, c.getId());

        c_x = readContainer(datum.junit_x, c_initial.getUniqueId());
        cv_latest_x = readContainerLatestVersion(datum.junit_x, c_x.getId());
        cv_previous_x = readContainerPreviousVersion(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());
        cv_latest_d_list_x = readContainerVersionDocuments(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());
        cv_latest_dv_list_x = readContainerVersionDocumentVersions(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());
        cv_latest_delta_x = readContainerVersionDeltas(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId(), cv_previous_x.getVersionId());
        cv_latest_pt_x = readPublishedTo(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());
        tm_list_x = readTeam(datum.junit_x, c_x.getId());

        assertSimilar("Container does not match expectation.", c, c_x);
        assertSimilar("Latest container version does not match expectation.", cv_latest, cv_latest_x);
        assertSimilar("Previous container version does not match expectation.", cv_previous, cv_previous_x);
        assertEquals("Document list size does not match expectation.", cv_latest_d_list.size(), cv_latest_d_list_x.size());
        for (int i = 0; i < cv_latest_d_list.size(); i++) {
            assertSimilar("Document does not match expectation.", cv_latest_d_list.get(i), cv_latest_d_list_x.get(i));
        }
        assertEquals("Document version list size does not match expectation.", cv_latest_d_list.size(), cv_latest_d_list_x.size());
        for (int i = 0; i < cv_latest_dv_list.size(); i++) {
            final DocumentVersion cv_latest_dv = cv_latest_dv_list.get(i);
            final DocumentVersion cv_latest_dv_x = cv_latest_dv_list_x.get(i);
            assertSimilar("Document version does not match expectation.", cv_latest_dv, cv_latest_dv_x);
            getDocumentModel(datum.junit).openVersion(cv_latest_dv.getArtifactId(), cv_latest_dv.getVersionId(), new StreamOpener() {
                public void open(final InputStream stream) throws IOException {
                    streamToFile(stream, getOutputFile(cv_latest_dv_x));
                }
            });
            getDocumentModel(datum.junit_x).openVersion(cv_latest_dv_x.getArtifactId(), cv_latest_dv_x.getVersionId(), new StreamOpener() {
                public void open(final InputStream stream) throws IOException {
                    streamToFile(stream, getOutputFile(cv_latest_dv_x));
                }
            });
            final InputStream is;
            final InputStream is_x;
            try {
                is = new FileInputStream(getOutputFile(cv_latest_dv));
                try {
                    is_x = new FileInputStream(getOutputFile(cv_latest_dv_x));
                    try {
                        assertEquals("Document version content does not match expectation.", is, is_x);
                    } finally {
                        try {
                            is_x.close();
                        } catch (final IOException iox) {
                            fail(iox, "Could not close input stream is_x.");
                        }
                    }
                } finally {
                    try {
                        is.close();
                    } catch (final IOException iox) {
                        fail(iox, "Could not close input stream is.");
                    }
                }
            } catch (final IOException iox) {
                fail(iox, "Could not compare input stream is with is_x.");
            }
        }
        assertEquals("Document version delta list does not match expectation.", cv_latest_delta.size(), cv_latest_delta_x.size());
        for (int i = 0; i < cv_latest_dv_list.size(); i++) {
            final DocumentVersion cv_latest_dv = cv_latest_dv_list.get(i);
            final DocumentVersion cv_latest_dv_x = cv_latest_dv_list_x.get(i);
            assertEquals("Document version delta does not match expectation.", Delta.MODIFIED, cv_latest_delta.get(cv_latest_dv));
            assertEquals("Document version delta does not match expectation.", cv_latest_delta.get(cv_latest_dv), cv_latest_delta_x.get(cv_latest_dv_x));
        }
        assertEquals("Published to list does not match expectation.", 1, cv_latest_pt.size());
        assertEquals("Published to list does not match expectation.", cv_latest_pt.size(), cv_latest_pt_x.size());
        for (int i = 0; i < cv_latest_pt.size(); i++) {
            assertSimilar("Published to artifact receipt does not match expectation.", cv_latest_pt.get(i), cv_latest_pt_x.get(i));
        }
        assertEquals("Team list does not match expectation.", 2, tm_list.size());
        assertEquals("Team list does not match expectation.", tm_list.size(), tm_list_x.size());
        for (int i = 0; i < tm_list.size(); i++) {
            assertSimilar("Team member does not match expectation.", tm_list.get(i), tm_list_x.get(i));
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X);
        login(datum.junit);
        login(datum.junit_x);
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        datum = null;
        super.tearDown();
    }

    /** Test datum fixture. */
    private class Fixture extends TicketTestCase.Fixture {
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x) {
            this.junit = junit;
            this.junit_x = junit_x;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
        }
    }
}
