/*
 * Created On:  28-Nov-06 2:50:00 PM
 */
package com.thinkparity.ophelia.model.container.backup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.CollectionsUtil;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerTestCase;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Backup Test<br>
 * <b>Description:</b><a
 * href="http://thinkparity.dyndns.org/trac/parity/wiki/TestCase_1000">Test
 * Case: 1000 - Backup - Backup</a><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class BackupTest extends BackupTestCase {

    private static final String NAME = "Test Case:  1001 - Backup - Backup";

    private Fixture datum;

    /**
     * Create Restore.
     *
     * @param name
     */
    public BackupTest() {
        super(NAME);
    }

    /**
     * Test the restore api.
     *
     */
    public void testBackup() {
        // create package
        final Container c = createContainer(datum.junit_z, "Backup Test: Backup 1");
        addDocument(datum.junit_z, c.getId(), "JUnitTestFramework.doc");
        addDocument(datum.junit_z, c.getId(), "JUnitTestFramework.pdf");
        addDocument(datum.junit_z, c.getId(), "JUnitTestFramework.png");
        publish(datum.junit_z, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        // ensure created
        final Container c_local = readContainer(datum.junit_z, c.getUniqueId());
        final List<ContainerVersion> cv_list_local = readContainerVersions(datum.junit_z, c_local.getId());
        ContainerVersion cv_local;
        List<JabberId> t_id_list_local;
        JabberId t_id_local;
        Map<User, ArtifactReceipt> pt_map_local;
        List<User> pt_list_keys_local;
        User pt_user_local;
        ArtifactReceipt pt_receipt_local;
        List<Document> d_list_local;
        Document d_local;
        List<DocumentVersion> dv_list_local;
        DocumentVersion dv_local;
        InputStream dv_stream_local;
        // ensure backed up
        logger.logInfo("Validating container \"{0}\"", c.getName());
        final Container c_backup = getBackupModel(datum.junit_z).readContainer(c.getUniqueId());
        assertNotNull("Container has not been properly backed up.", c_backup);
        assertEquals("Container has not been properly backed up.", c_local, c_backup);
        ContainerVersion cv_backup;
        List<JabberId> t_id_list_backup;
        JabberId t_id_backup;
        Map<User, ArtifactReceipt> pt_map_backup;
        List<User> pt_list_keys_backup;
        User pt_user_backup;
        ArtifactReceipt pt_receipt_backup;
        List<Document> d_list_backup;
        Document d_backup;
        List<DocumentVersion> dv_list_backup;
        DocumentVersion dv_backup;
        InputStream dv_stream_backup;
        logger.logInfo("Validating container \"{0}\" versions.", c.getName());
        final List<ContainerVersion> cv_list_backup = getBackupModel(datum.junit_z).readContainerVersions(c.getUniqueId());
        assertEquals("Container versions have not been properly backed up.", cv_list_local.size(), cv_list_backup.size());
        for (int i = 0; i < cv_list_backup.size(); i++) {
            cv_backup = cv_list_backup.get(i);
            assertNotNull("Container version has not been properly backed up.", cv_backup);
            logger.logInfo("Validating container \"{0}\" version \"{1}\".", c_backup.getName(), cv_backup.getVersionId());
            cv_local = cv_list_local.get(i);
            assertEquals("Container verison has not been properly backed up.", cv_local, cv_backup);
            logger.logInfo("Validating container \"{0}\" version \"{1}\" team.", c_backup.getName(), cv_backup.getVersionId());
            t_id_list_local = readTeamIds(datum.junit_z, c_local.getId());
            t_id_list_backup = getBackupModel(datum.junit_z).readTeamIds(c.getUniqueId());
            assertEquals("Team member ids have not been properly backed up.", t_id_list_local.size(), t_id_list_backup.size());
            for (int j = 0; j < t_id_list_backup.size(); j++) {
                t_id_backup = t_id_list_backup.get(j);
                assertNotNull("Team member id has not been properly backed up.", t_id_backup);
                logger.logInfo("Validating container \"{0}\" version \"{1}\" team \"{2}\".", c_backup.getName(), cv_backup.getVersionId(), t_id_backup);
                t_id_local= t_id_list_local.get(j);
                assertEquals("Team member id has not been properly backed up.", t_id_local, t_id_backup);
            }
            logger.logInfo("Validating container \"{0}\" version \"{1}\" published to.", c_backup.getName(), cv_backup.getVersionId());
            pt_map_backup = getBackupModel(datum.junit_z).readPublishedTo(c.getUniqueId(), cv_backup.getVersionId());
            assertNotNull("Published to list has not been properly backed up.", pt_map_backup);
            pt_map_local = readPublishedTo(datum.junit_z, cv_local.getArtifactId(), cv_local.getVersionId());
            assertEquals("Published to list has not been properly backed up.", pt_map_local.size(), pt_map_backup.size());
            pt_list_keys_backup = CollectionsUtil.proxy(pt_map_backup.keySet());
            pt_list_keys_local =  CollectionsUtil.proxy(pt_map_local.keySet());
            for (int k = 0; k < pt_list_keys_backup.size(); k++) {
                pt_user_backup = pt_list_keys_backup.get(k);
                assertNotNull("Published to user has not been properly backed up.", pt_user_backup);
                logger.logInfo("Validating container \"{0}\" version \"{1}\" published to \"{2}\".", c_backup.getName(), cv_backup.getVersionId(), pt_user_backup);
                pt_user_local = pt_list_keys_local.get(k);
                assertEquals("Published to user has not been properly backed up.", pt_user_local, pt_user_backup);
                pt_receipt_backup = pt_map_backup.get(pt_user_backup);
                assertNotNull("Published to receipt has not been properly backed up.", pt_receipt_backup);
                pt_receipt_local = pt_map_local.get(pt_user_local);
                assertEquals("Published to receipt has not been properly backed up.", pt_receipt_local, pt_receipt_backup);
            }
            d_list_backup = getBackupModel(datum.junit_z).readDocuments(c.getUniqueId(), cv_backup.getVersionId());
            assertNotNull("Document list has not been properly backed up.", d_list_backup);
            logger.logInfo("Validating container \"{0}\" version \"{1}\" documents.", c_backup.getName(), cv_backup.getVersionId());
            d_list_local = readContainerVersionDocuments(datum.junit_z, cv_local.getArtifactId(), cv_local.getVersionId());
            assertEquals("Document list has not been properly backed up.", d_list_local.size(), d_list_backup.size());
            for (int m = 0; m < d_list_backup.size(); m++) {
                d_backup = d_list_backup.get(m);
                assertNotNull("Document has not been properly backed up.", d_backup);
                logger.logInfo("Validating container \"{0}\" version \"{1}\" document \"{2}\".", c_backup.getName(), cv_backup.getVersionId(), d_backup.getName());
                d_local = d_list_local.get(m);
                assertEquals("Document has not been properly backed up.", d_local, d_backup);
            }
            logger.logInfo("Validating container \"{0}\" version \"{1}\" document versions.", c_backup.getName(), cv_backup.getVersionId());
            dv_list_backup = getBackupModel(datum.junit_z).readDocumentVersions(c.getUniqueId(), cv_backup.getVersionId());
            assertNotNull("Document version list has not been properly backed up.", dv_list_backup);
            dv_list_local = readContainerVersionDocumentVersions(datum.junit_z, cv_local.getArtifactId(), cv_local.getVersionId());
            assertEquals("Document version list has not been properly backed up.", dv_list_local.size(), dv_list_backup.size());
            for (int n = 0; n < dv_list_backup.size(); n++) {
                dv_backup = dv_list_backup.get(n);
                assertNotNull("Document version has not been properly backed up.", dv_backup);
                logger.logInfo("Validating container \"{0}\" version \"{1}\" document \"{2}\" version \"{3}\".", c_backup.getName(), cv_backup.getVersionId(), dv_backup.getName(), dv_backup.getVersionId());
                dv_local = dv_list_local.get(n);
                assertEquals("Document version has not been properly backed up.", dv_local, dv_backup);
                
                logger.logInfo("Validating container \"{0}\" version \"{1}\" document \"{2}\" version \"{3}\" stream.", c_backup.getName(), cv_backup.getVersionId(), dv_backup.getName(), dv_backup.getVersionId());
                dv_stream_backup = getBackupModel(datum.junit_z).openDocumentVersion(dv_backup.getArtifactUniqueId(), dv_backup.getVersionId());
                dv_stream_local = getDocumentModel(datum.junit_z).openVersion(dv_local.getArtifactId(), dv_local.getVersionId());
                try {
                    assertEquals("Document version content has not been properly backed up.", dv_stream_local, dv_stream_backup);
                } catch (final IOException iox) {
                    fail(createFailMessage(iox));
                }
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#getOutputDirectory()
     *
     */
    @Override
    public File getOutputDirectory() {
        final File parentFile = new File(super.getOutputDirectory(), "BackupTest");
        final File outputDirectory = new File(parentFile, getName());
        if (!outputDirectory.exists())
            assertTrue(outputDirectory.mkdirs());
        return outputDirectory; 
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
