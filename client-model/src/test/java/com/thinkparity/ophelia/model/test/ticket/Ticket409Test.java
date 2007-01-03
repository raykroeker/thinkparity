/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.test.ticket;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerDraft.ArtifactState;
import com.thinkparity.ophelia.model.events.ContainerAdapter;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.ContainerListener;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Trac Ticket #409<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Ticket409Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 409";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket406Test.
     *
     */
    public Ticket409Test() {
        super(NAME);
    }

    /**
     * Create a package; add documents publish; create a draft; remove a
     * document; undelete a document.
     * 
     */
    public void testTicket() {
        final Container c = createContainer(datum.junit, "No such test case.");
        final List<Document> d_list_initial = addDocuments(datum.junit, c.getId());
        publish(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        addContainerListener(datum.junit, datum.containerListener);
        final Document d_removed = removeDocument(datum.junit, c.getId(), d_list_initial.get(0).getId());
        assertTrue("Document removed event not fired.", datum.didFireRemoved);
        removeContainerListener(datum.junit, datum.containerListener);
        final ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        final List<Document> d_list = readContainerVersionDocuments(datum.junit, c.getId(), cv_latest.getVersionId());

        /* ensure the draft state is consistent; ie the removed document's state
         * is removed; all the rest are none */
        ContainerDraft draft = readContainerDraft(datum.junit, c.getId());
        List<Document> d_list_draft = draft.getDocuments();
        assertEquals("Document list size does not match expectation.", d_list.size(), d_list_draft.size());
        for (int i = 0; i < d_list.size(); i++) {
            assertEquals("Document does not match expectation.", d_list.get(i), d_list_draft.get(i));
        }
        ArtifactState state;
        for (int i = 0; i < d_list.size(); i++) {
            state = draft.getState(d_list.get(i));
            if (d_removed.equals(d_list.get(i))) {
                assertEquals("State info does not match expectation.", ArtifactState.REMOVED,  state);
            } else {
                assertEquals("State info does not match expectation.", ArtifactState.NONE, state);
            }
        }

        addContainerListener(datum.junit, datum.containerListener);
        revertDocument(datum.junit, c.getId(), d_removed.getId());
        assertTrue("Document reverted event not fired.", datum.didFireReverted);
        removeContainerListener(datum.junit, datum.containerListener);

        /* ensure the draft state is consistent; ie the documents' state
         * is none */
        draft = readContainerDraft(datum.junit, c.getId());
        d_list_draft = draft.getDocuments();
        assertEquals("Document list size does not match expectation.", d_list.size(), d_list_draft.size());
        for (int i = 0; i < d_list.size(); i++) {
            assertEquals("Document does not match expectation.", d_list.get(i), d_list_draft.get(i));
        }
        for (int i = 0; i < d_list.size(); i++) {
            state = draft.getState(d_list.get(i));
            assertEquals("State info does not match expectation.", ArtifactState.NONE, state);
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
        private boolean didFireRemoved;
        private boolean didFireReverted;
        private final ContainerListener containerListener;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
            this.didFireRemoved = false;
            this.didFireReverted = false;
            this.containerListener = new ContainerAdapter() {
                @Override
                public void documentRemoved(final ContainerEvent e) {
                    didFireRemoved = true;
                }
                @Override
                public void documentReverted(final ContainerEvent e) {
                    didFireReverted = true;
                }
            };
        }
    }
}
