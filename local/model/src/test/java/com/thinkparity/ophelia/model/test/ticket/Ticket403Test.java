/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.test.ticket;

import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Ticket403Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 403";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket403Test.
     *
     */
    public Ticket403Test() {
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
        final Container c_z = createContainer(datum.junit_z, NAME);
        addDocument(datum.junit_z, c_z.getId(), "JUnitTestFramework.doc");
        publishToUsers(datum.junit_z, c_z.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        createDraft(datum.junit_z, c_z.getId());
        datum.waitForEvents();
        addDocument(datum.junit_z, c_z.getId(), "JUnitTestFramework.odt");
        publishToUsers(datum.junit_z, c_z.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        archive(datum.junit_z, c_z.getId());
        datum.waitForEvents();

        final List<ContainerVersion> cv_list_z = getContainerModel(datum.junit_z).readVersions(c_z.getId());
        final ContainerVersion cv_latest_z = cv_list_z.get(0);
        final ContainerVersion cv_previous_z = cv_list_z.get(1);

        final List<DocumentVersion> dv_list_z = getContainerModel(datum.junit_z).readDocumentVersions(cv_latest_z.getArtifactId(), cv_latest_z.getVersionId());
        final Map<DocumentVersion, Delta> delta_z = getContainerModel(datum.junit_z).readDocumentVersionDeltas(c_z.getId(), cv_latest_z.getVersionId(), cv_previous_z.getVersionId());

        final Delta delta_doc_z = delta_z.get(dv_list_z.get(0));
        assertEquals("Document delta does not match expectation.", Delta.NONE, delta_doc_z);
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT_X, OpheliaTestUser.JUNIT_Y,
                OpheliaTestUser.JUNIT_Z);
        login(datum.junit_x);
        login(datum.junit_y);
        login(datum.junit_z);
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit_x);
        logout(datum.junit_y);
        logout(datum.junit_z);
        datum = null;
        super.tearDown();
    }

    /** Test datum fixture. */
    private class Fixture extends TicketTestCase.Fixture {
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private final OpheliaTestUser junit_z;
        private Fixture(final OpheliaTestUser junit_x,
                final OpheliaTestUser junit_y, final OpheliaTestUser junit_z) {
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            this.junit_z = junit_z;
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
            addQueueHelper(junit_z);
        }
    }
}
