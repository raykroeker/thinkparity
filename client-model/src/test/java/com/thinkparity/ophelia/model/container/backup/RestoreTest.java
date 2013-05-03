/*
 * Created On:  28-Nov-06 2:50:00 PM
 */
package com.thinkparity.ophelia.model.container.backup;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.container.ContainerTestCase;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Backup Test<br>
 * <b>Description:</b><a
 * href="http://thinkparity.dyndns.org/trac/parity/wiki/TestCase_1001">Test
 * Case: 1001 - Backup - Restore</a><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RestoreTest extends BackupTestCase {

    private static final String NAME = "Test Case:  1001 - Backup - Restore";

    private Fixture datum;

    /**
     * Create Restore.
     *
     * @param name
     */
    public RestoreTest() {
        super(NAME);
    }

    /**
     * Test the restore api.
     *
     */
    public void testRestore() {
        // test
        final Container c = createContainer(datum.junit_z, "Backup Test: Restore backup 1");
        addDocument(datum.junit_z, c.getId(), "JUnitTestFramework.doc");
        addDocument(datum.junit_z, c.getId(), "JUnitTestFramework.pdf");
        addDocument(datum.junit_z, c.getId(), "JUnitTestFramework.png");
        publishToUsers(datum.junit_z, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        // postconditions
        // ensure backed up remotely
//        final Container c_backup = getBackupModel(datum.junit_z).readContainer(c.getUniqueId());
//        assertNotNull("Container has not been properly backed up.", c_backup);
//        final List<ContainerVersion> cv_list_backup = getBackupModel(datum.junit_z).readContainerVersions(c.getUniqueId());
//        Map<User, ArtifactReceipt> pt_backup;
//        List<JabberId> t_id_list_backup;
//        List<Document> d_list_backup;
//        List<DocumentVersion> dv_list_backup;
//        for (final ContainerVersion cv_backup : cv_list_backup) {
//            t_id_list_backup = getBackupModel(datum.junit_z).readTeamIds(c.getUniqueId());
//            for (final JabberId t_id_backup : t_id_list_backup) {
//                assertNotNull("Team member id has not been properly backed up.", t_id_backup);
//            }
//            pt_backup = getBackupModel(datum.junit_z).readPublishedTo(c.getUniqueId(), cv_backup.getVersionId());
//            for (final Entry<User, ArtifactReceipt> entry : pt_backup.entrySet()) {
//                assertNotNull("Published to user has not been properly backed up.", entry.getKey());
//                assertNotNull("Published to receipt has not been properly backed up.", entry.getValue());
//            }
//            d_list_backup = getBackupModel(datum.junit_z).readDocuments(c.getUniqueId(), cv_backup.getVersionId());
//            for (final Document d_backup : d_list_backup) {
//                assertNotNull("Document has not been properly backed up.", d_backup);
//            }
//            dv_list_backup = getBackupModel(datum.junit_z).readDocumentVersions(c.getUniqueId(), cv_backup.getVersionId());
//            for (final DocumentVersion dv_backup : dv_list_backup) {
//                assertNotNull("Document version has not been properly backed up.", dv_backup);
//            }
//        }
//        getContainerModel(datum.junit_z).restoreBackup();
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT_X, OpheliaTestUser.JUNIT_Z);
        login(datum.junit_x);
        login(datum.junit_z);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit_x);
        logout(datum.junit_z);
        datum = null;
        super.tearDown();
    }

    private class Fixture extends ContainerTestCase.Fixture {
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_z;
        private Fixture(final OpheliaTestUser junit_x, final OpheliaTestUser junit_z) {
            this.junit_x = junit_x;
            this.junit_z = junit_z;
            addQueueHelper(junit_x);
            addQueueHelper(junit_z);
        }
    }
}
