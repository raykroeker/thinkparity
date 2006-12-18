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
import com.thinkparity.ophelia.model.events.ContainerEvent;

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
public class ArchiveTest extends ArchiveTestCase {

    /** The test name <code>String</code>. */
    private static final String NAME = "4004: Package: Archive";

    /** Test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create ArchiveTest.
     * 
     */
    public ArchiveTest() {
        super(NAME);
    }

    /**
     * Test the archive api.
     * 
     */
    public void testArchive() {
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
        // archive
        datum.addListener(datum.junit_z);
        archive(datum.junit_z, c.getId());
        datum.removeListener(datum.junit_z);
        datum.waitForEvents();
        // postconditions
        assertTrue("Archive event not fired.", datum.didNotify);
        // ensure deleted locally
        assertNull("Container has not been properly deleted.", readContainer(datum.junit_z, c.getUniqueId()));
        assertNull("Document has not been properly deleted.", readDocument(datum.junit_z, d_doc.getUniqueId()));
        assertNull("Document has not been properly deleted.", readDocument(datum.junit_z, d_pdf.getUniqueId()));
        assertNull("Document has not been properly deleted.", readDocument(datum.junit_z, d_png.getUniqueId()));
        assertEquals("Container versions have not been properly deleted.", 0, readContainerVersions(datum.junit_z, c.getId()).size());
        // ensure archived remotely
        final Container c_archive = getArchiveModel(datum.junit_z).readContainer(c.getUniqueId());
        assertNotNull("Container has not been properly archived.", c_archive);
        final List<ContainerVersion> cv_list_archive = getArchiveModel(datum.junit_z).readContainerVersions(c.getUniqueId());
        Map<User, ArtifactReceipt> pt_archive;
        List<TeamMember> t_archive;
        List<JabberId> t_id_list_archive;
        List<Document> d_list_archive;
        List<DocumentVersion> dv_list_archive;
        for (final ContainerVersion cv_archive : cv_list_archive) {
            t_archive = getArchiveModel(datum.junit_z).readTeam(c.getUniqueId());
            for (final TeamMember t_member_archive : t_archive) {
                assertNotNull("Team member has not been properly archived.", t_member_archive);
            }
            t_id_list_archive = getArchiveModel(datum.junit_z).readTeamIds(c.getUniqueId());
            for (final JabberId t_id_archive : t_id_list_archive) {
                assertNotNull("Team member id has not been properly archived.", t_id_archive);
            }
            pt_archive = getArchiveModel(datum.junit_z).readPublishedTo(c.getUniqueId(), cv_archive.getVersionId());
            for (final Entry<User, ArtifactReceipt> entry : pt_archive.entrySet()) {
                assertNotNull("Published to user has not been properly archived.", entry.getKey());
                assertNotNull("Published to receipt has not been properly archived.", entry.getValue());
            }
            d_list_archive = getArchiveModel(datum.junit_z).readDocuments(c.getUniqueId(), cv_archive.getVersionId());
            for (final Document d_archive : d_list_archive) {
                assertNotNull("Document has not been properly archived.", d_archive);
            }
            dv_list_archive = getArchiveModel(datum.junit_z).readDocumentVersions(c.getUniqueId(), cv_archive.getVersionId());
            for (final DocumentVersion dv_archive : dv_list_archive) {
                assertNotNull("Document version has not been properly archived.", dv_archive);
            }
        }
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
        private Boolean didNotify;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private final OpheliaTestUser junit_z;
        private Fixture(final OpheliaTestUser junit_x,
                final OpheliaTestUser junit_y, final OpheliaTestUser junit_z) {
            super();
            this.didNotify = Boolean.FALSE;
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
            addQueueHelper(junit_z);
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            this.junit_z = junit_z;
        }
        @Override
        public void containerArchived(final ContainerEvent e) {
            didNotify = Boolean.TRUE;
            assertTrue("Event generated is not local.", e.isLocal());
            assertTrue("Event generated is remote .", !e.isRemote());
            assertNotNull("The archive event's container is null.", e.getContainer());
            assertNull("The archive event's document is not null.", e.getDocument());
            assertNull("The archive event's draft is not null.", e.getDraft());
            assertNull("The archive event's team member is not null.", e.getTeamMember());
            assertNull("The archive event's version is not null.", e.getVersion());
        }
        private void addListener(final OpheliaTestUser testUser) {
            getContainerModel(testUser).addListener(this);
        }
        private void removeListener(final OpheliaTestUser testUser) {
            getContainerModel(testUser).removeListener(this);
        }
    }
}
