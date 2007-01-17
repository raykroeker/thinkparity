/*
 * Created On: Sep 26, 2006 7:47:30 PM
 */
package com.thinkparity.ophelia.model.container.archive;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerTestCase;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Container Archive Test<br>
 * <b>Description:</b><a
 * href="http://thinkparity.dyndns.org/trac/parity/wiki/TestCase_4004">Test Case
 * 4004</a><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RestoreTest extends ArchiveTestCase {

    /** The test name <code>String</code>. */
    private static final String NAME = "4004: Package: Archive";

    /** Test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create ArchiveTest.
     * 
     */
    public RestoreTest() {
        super(NAME);
    }

    /**
     * Test the restore api.
     * 
     */
    public void testRestore() {
        final Container c = createContainer(datum.junit_z, "Packages Test: Archive 1");
        final Document d_doc = addDocument(datum.junit_z, c.getId(), "JUnitTestFramework.doc");
        final Document d_pdf = addDocument(datum.junit_z, c.getId(), "JUnitTestFramework.pdf");
        publish(datum.junit_z, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        createDraft(datum.junit_z, c.getId());
        datum.waitForEvents();
        final Document d_png = addDocument(datum.junit_z, c.getId(), "JUnitTestFramework.png");
        publish(datum.junit_z, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        archive(datum.junit_z, c.getId());
        datum.waitForEvents();
        restore(datum.junit_z, c.getUniqueId());
        datum.waitForEvents();
        // ensure exists locally
        final Container c_z = readContainer(datum.junit_z, c.getUniqueId());
        assertNotNull("Container has not been properly restored.", c_z);
        assertNotNull("Document has not been properly restored.", readDocument(datum.junit_z, d_doc.getUniqueId()));
        assertNotNull("Document has not been properly restored.", readDocument(datum.junit_z, d_pdf.getUniqueId()));
        assertNotNull("Document has not been properly restored.", readDocument(datum.junit_z, d_png.getUniqueId()));
        assertEquals("Container versions have not been properly restored.", 2, readContainerVersions(datum.junit_z, c_z.getId()).size());
        // ensure archived remotely
        final Container c_archive = getArchiveModel(datum.junit_z).readContainer(c.getUniqueId());
        assertNull("Container has not been properly restored.", c_archive);
        assertEquals("Container versions have not been properly restored.", 0,
                getArchiveModel(datum.junit_z).readContainerVersions(c.getUniqueId()).size());
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
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
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit_x);
        logout(datum.junit_y);
        logout(datum.junit_z);
        datum = null;
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private final OpheliaTestUser junit_z;
        private Fixture(final OpheliaTestUser junit_x,
                final OpheliaTestUser junit_y, final OpheliaTestUser junit_z) {
            super();
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
            addQueueHelper(junit_z);
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            this.junit_z = junit_z;
        }
    }
}
