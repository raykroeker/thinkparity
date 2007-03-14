/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.test.ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.Constants.Versioning;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.events.ContainerAdapter;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.ContainerListener;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Ticket 442 Test<br>
 * <b>Description:</b>A test case causing the workflow described in ticket 442
 * to fail.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Ticket442Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 442";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket442Test.
     *
     */
    public Ticket442Test() {
        super(NAME);
    }

    /**
     * {@link http://thinkparity.dyndns.org/trac/parity/ticket/442 Ticket 442}
     * 
     */
    public void testTicket() {
        // create a container; add a document; publish
        final Container c_initial = createContainer(datum.junit, NAME);
        final Document d_txt = addDocument(datum.junit, c_initial.getId(), "JUnitTestFramework.txt");
        publish(datum.junit, c_initial.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        // create a draft; modify a document publish
        createDraft(datum.junit, c_initial.getId());
        datum.waitForEvents();
        modifyDocument(datum.junit, d_txt.getId());
        addContainerListener(datum.junit_x, datum.listener_x);
        publish(datum.junit, c_initial.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        removeContainerListener(datum.junit_x, datum.listener_x);
        assertTrue("Draft deleted event was never fired.", datum.notify_x);
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
        private final ContainerListener listener_x;
        private boolean notify_x;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x) {
            this.junit = junit;
            this.junit_x = junit_x;
            this.listener_x = new ContainerAdapter() {
                @Override
                public void draftDeleted(final ContainerEvent e) {
                    try {
                        final Container c_x = readContainer(datum.junit_x, e.getContainer().getId());
                        final ContainerDraft cd_x = readContainerDraft(datum.junit_x, c_x.getId());
                        DocumentVersion dv_start_x;
                        if (null != cd_x) {
                            for (final Document d_cd_x : cd_x.getDocuments()) {
                                dv_start_x = readDocumentVersion(datum.junit_x, d_cd_x.getId(), Versioning.START);
                                if (null != dv_start_x)
                                    assertNotNull("Starting document version is null.", dv_start_x);
                            }
                        }
                        if (null != cd_x && c_x.isLocalDraft()) {
                            for (final Document d_cd_x : cd_x.getDocuments()) {
                                assertNotNull("Draft document is null.", d_cd_x);
                            }
                        }
                        final List<ContainerVersion> cv_list_x = readContainerVersions(datum.junit_x, c_x.getId());
                        final Map<ContainerVersion, List<ArtifactReceipt>> cv_pt_map_x =
                            new HashMap<ContainerVersion, List<ArtifactReceipt>>(cv_list_x.size(), 1.0F);
                        final Map<ContainerVersion, User> cv_pb_map_x = new HashMap<ContainerVersion, User>(cv_list_x.size(), 1.0F);
                        for (final ContainerVersion cv_x : cv_list_x) {
                            final ContainerVersion cv_previous_x = readContainerPreviousVersion(datum.junit_x, c_x.getId(), cv_x.getVersionId());
                            final Map<DocumentVersion, Delta> cv_delta_x;
                            if (null == cv_previous_x) {
                                cv_delta_x = readContainerVersionDeltas(datum.junit_x, c_x.getId(), cv_x.getVersionId());
                            } else {
                                cv_delta_x = readContainerVersionDeltas(datum.junit_x, c_x.getId(), cv_x.getVersionId(), cv_previous_x.getVersionId());
                            }
                            assertNotNull("Container version delta is null.", cv_delta_x);
                            DocumentVersion dv_earliest_x;
                            for (final Entry<DocumentVersion, Delta> cv_delta_entry_x : cv_delta_x.entrySet()) {
                                assertNotNull("Document version is null.", cv_delta_entry_x.getKey());
                                assertNotNull("Document version delta is null.", cv_delta_entry_x.getValue());
                                dv_earliest_x = readDocumentEarliestVersion(datum.junit_x, cv_delta_entry_x.getKey().getArtifactId());
                                assertNotNull("Earliest document version is null.", dv_earliest_x);
                                assertNotNull("Earliest document version created on date is null.", dv_earliest_x.getCreatedOn());
                            }
                            cv_pt_map_x.put(cv_x, readPublishedTo(datum.junit_x, cv_x.getArtifactId(), cv_x.getVersionId()));
                            cv_pb_map_x.put(cv_x, readUser(datum.junit_x, cv_x.getUpdatedBy()));
                        }
                        notify_x = true;
                    } catch (final Throwable t) {
                        logger.logError(t, "Could not handle draft deleted event.  See test log file for details.");
                    }
                }
            };
            this.notify_x = false;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
        }
    }
}
