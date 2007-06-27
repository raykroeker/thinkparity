/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.test.ticket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.StreamUtil;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.io.StreamOpener;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Ticket406Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 406";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket406Test.
     *
     */
    public Ticket406Test() {
        super(NAME);
    }

    /**
     * Create a package; add documents publish; create a draft; modify a
     * document; then publish again. Here we are specifically looking for the
     * documents that exist when the package arrives at the second and third
     * users. The bug was that document that were not being modified between the
     * first and second publish were being flagged as removed.
     */
    public void testTicket() {
        final Container c = createContainer(datum.junit, NAME);
        final List<Document> d_list_initial = addDocuments(datum.junit, c.getId());
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        modifyDocument(datum.junit, d_list_initial.get(0).getId());
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();

        final ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        final ContainerVersion cv_previous = readContainerPreviousVersion(datum.junit, c.getId(), cv_latest.getVersionId());
        final List<Document> d_list = readContainerVersionDocuments(datum.junit, c.getId(), cv_latest.getVersionId());

        final Container c_x = readContainer(datum.junit_x, c.getUniqueId());
        final ContainerVersion cv_latest_x = readContainerLatestVersion(datum.junit_x, c_x.getId());
        final ContainerVersion cv_previous_x = readContainerPreviousVersion(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());
        final List<Document> d_list_x = readContainerVersionDocuments(datum.junit_x, c_x.getId(), cv_latest_x.getVersionId());

        final Container c_y = readContainer(datum.junit_y, c.getUniqueId());
        final ContainerVersion cv_latest_y = readContainerLatestVersion(datum.junit_y, c_y.getId());
        final ContainerVersion cv_previous_y = readContainerPreviousVersion(datum.junit_y, c_y.getId(), cv_latest_y.getVersionId());
        final List<Document> d_list_y = readContainerVersionDocuments(datum.junit_y, c_y.getId(), cv_latest_y.getVersionId());

        assertSimilar("Container does not match expectation.", c, c_x);
        assertSimilar("Container does not match expectation.", c, c_y);

        assertSimilar("Container version does not match expectation.", cv_latest, cv_latest_x);
        assertSimilar("Container version does not match expectation.", cv_latest, cv_latest_y);
        assertSimilar("Container version does not match expectation.", cv_previous, cv_previous_x);
        assertSimilar("Container version does not match expectation.", cv_previous, cv_previous_y);
        
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
