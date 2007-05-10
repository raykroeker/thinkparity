/*
 * Created On:  10-Dec-06 3:00:13 PM
 */
package com.thinkparity.ophelia.model.test.ticket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.stream.StreamOpener;

import com.thinkparity.ophelia.model.util.Opener;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Ticket298Test extends TicketTestCase {

    /** Test name <code>String</code>. */
    private static final String NAME = "Test ticket 298";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket298Test.
     *
     * @param name
     */
    public Ticket298Test() {
        super(NAME);
    }

    /**
     * Test restoring a document post-publish.
     *
     */
    public void testRestore() {
        // create a container; add two documents and publish
        final Container c = createContainer(datum.junit, NAME);
        final Document d_odt = addDocument(datum.junit, c.getId(), "JUnitTestFramework.odt");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.txt");
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        // create draft; remove first document; revert first document
        createDraft(datum.junit, c.getId());
        removeDocument(datum.junit, c.getId(), d_odt.getId());
        revertDocument(datum.junit, c.getId(), d_odt.getId());

        // ensure the contents are equal
        final DocumentVersion dv_odt_latest = readDocumentLatestVersion(datum.junit, d_odt.getId());
        getDocumentModel(datum.junit).openVersion(d_odt.getId(), dv_odt_latest.getVersionId(), new StreamOpener() {
            public void open(final InputStream stream) throws IOException {
                streamToFile(stream, getOutputFile(dv_odt_latest));
            }
        });
        getDocumentModel(datum.junit).open(d_odt.getId(), new Opener() {
            public void open(final File file) {
                try {
                    final InputStream d_odt_stream = new FileInputStream(file);
                    assertEquals("Reverted document's content does not match expectation.",
                            d_odt_stream, getOutputFile(dv_odt_latest));
                } catch (final IOException iox) {
                    fail(createFailMessage(iox));
                }
            }
        });
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        login(OpheliaTestUser.JUNIT_X);
        login(OpheliaTestUser.JUNIT_Y);
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X,
                OpheliaTestUser.JUNIT_Y);
    }

    @Override
    protected void tearDown() throws Exception {
        logout(OpheliaTestUser.JUNIT);
        logout(OpheliaTestUser.JUNIT_X);
        logout(OpheliaTestUser.JUNIT_Y);
        datum = null;
        super.tearDown();
    }

    /**
     * <b>Title:</b><br>
     * <b>Description:</b><br>
     * @author raymond@thinkparity.com
     * @version 1.1.2.1
     */
    private final class Fixture extends TicketTestCase.Fixture {
        private final OpheliaTestUser junit;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            super();
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
            this.junit = junit;
        }
    }
}
