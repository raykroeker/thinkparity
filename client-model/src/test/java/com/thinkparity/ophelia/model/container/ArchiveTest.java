/*
 * Created On: Sep 26, 2006 7:47:30 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;
import java.io.File;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.events.ContainerAdapter;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.ContainerListener;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Container Archive Test<br>
 * <b>Description:</b><a
 * href="http://thinkparity.dyndns.org/trac/parity/wiki/TestCase_4004">Test Case
 * 4004</a><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.5
 */
public class ArchiveTest extends ContainerTestCase {

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
     * As a user with the backup feature:
     * <ol>
     * <li>Create a package.
     * <li>Add documents to the package.
     * <li>Publish the package.
     * <li>Archive the package.
     * <li>Verify the result:
     * <ol>
     * <li>Archive event is fired.
     * <li>Package exists in the backup.
     * <li>Package is archived in the backup.
     * <li>Package team does not contain the user in the backup.
     * <li>Package exists locally.
     * <li>Package is archived locally.
     * <li>Package team does not contain the user.
     */
    public void testArchive() {
        // create a package; add documents to the package; publish the package
        final Container c_initial_z = createContainer(datum.junit_z, getName());
        addDocuments(datum.junit_z, c_initial_z.getId());
        publish(datum.junit_z, c_initial_z.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        // archive the package
        addContainerListener(datum.junit_z, datum.cl_archive_z);
        archive(datum.junit_z, c_initial_z.getId());
        removeContainerListener(datum.junit_z, datum.cl_archive_z);
        datum.waitForEvents();

        // verify the archive event is fired
        assertTrue("Archive event not fired.", datum.cl_archive_notify_z);

        // verify the package exists in the backup; the package is archived in the backup
        final Container c_backup_z = getBackupModel(datum.junit_z).readContainer(c_initial_z.getUniqueId());
        assertNotNull("Remote container is null.", c_backup_z);
        assertTrue("Remote container is not archived.", c_backup_z.isArchived());

        // verify the package team does not contain the archive user in the backup
        final List<JabberId> t_id_list_backup_z = getBackupModel(datum.junit_z).readTeamIds(c_backup_z.getUniqueId());
        assertEquals("Backup container team list does not match expectation.", 1, t_id_list_backup_z.size());
        assertFalse("Backup container team list contains archive user.", t_id_list_backup_z.contains(datum.junit_z.getId()));
        assertTrue("Backup container team list does not contain publish to user.", t_id_list_backup_z.contains(datum.junit_x.getId()));

        // verify the package exists locally; the package is archived locally
        final Container c_z = readContainer(datum.junit_z, c_initial_z.getUniqueId());
        assertNotNull("Local container is null.", c_z);
        assertTrue("Local container is not archived.", c_z.isArchived());

        // verify the package team does not contain the archive user
        final List<JabberId> t_id_list_z = readTeamIds(datum.junit_z, c_z.getId());
        assertEquals("Local container team list does not match expectation.", 1, t_id_list_z.size());
        assertFalse("Local container team list contains archive user.", t_id_list_z.contains(datum.junit_z.getId()));
        assertTrue("Local container team list does not contain publish to user.", t_id_list_z.contains(datum.junit_x.getId()));

        // verify the package can be exported
        final File exportFile = getContainerModel(datum.junit_z).export(getOutputDirectory(), c_z.getId());
        assertNotNull("Export file is null.", exportFile);
        assertTrue("Export file does not exist.", exportFile.exists());
    }

    /**
     * As a user with the backup feature:
     * <ol>
     * <li>Create a package.
     * <li>Add documents to the package.
     * <li>Publish the package.
     * <li>Archive the package.
     * <li>Restore the package.
     * <li>Verify the result:
     * <ol>
     * <li>Restore event is fired.
     * <li>Package exists in the backup.
     * <li>Package is not archived in the backup.
     * <li>Package team contains the user in the backup.
     * <li>Package exists locally.
     * <li>Package is not archived locally.
     * <li>Package team contains the user.
     */
    public void testArchiveRestore() {
        // create a package; add documents to the package; publish the package
        final Container c_initial_z = createContainer(datum.junit_z, getName());
        addDocuments(datum.junit_z, c_initial_z.getId());
        publish(datum.junit_z, c_initial_z.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        // archive the package
        archive(datum.junit_z, c_initial_z.getId());
        datum.waitForEvents();

        // restore the package
        addContainerListener(datum.junit_z, datum.cl_restore_z);
        restore(datum.junit_z, c_initial_z.getId());
        removeContainerListener(datum.junit_z, datum.cl_restore_z);
        datum.waitForEvents();

        // verify the restore event is fired
        assertTrue("Restore event not fired.", datum.cl_restore_notify_z);

        // verify the package exists in the backup; the package is not archived in the backup
        final Container c_backup_z = getBackupModel(datum.junit_z).readContainer(c_initial_z.getUniqueId());
        assertNotNull("Remote container is null.", c_backup_z);
        assertFalse("Remote container is archived.", c_backup_z.isArchived());

        // verify the package team contains the archive user in the backup
        final List<JabberId> t_id_list_backup_z = getBackupModel(datum.junit_z).readTeamIds(c_backup_z.getUniqueId());
        assertEquals("Backup container team list does not match expectation.", 2, t_id_list_backup_z.size());
        assertTrue("Backup container team list does not contain archive user.", t_id_list_backup_z.contains(datum.junit_z.getId()));
        assertTrue("Backup container team list does not contain publish to user.", t_id_list_backup_z.contains(datum.junit_x.getId()));

        // verify the package exists locally; the package is archived locally
        final Container c_z = readContainer(datum.junit_z, c_initial_z.getUniqueId());
        assertNotNull("Local container is null.", c_z);
        assertFalse("Local container is archived.", c_z.isArchived());

        // verify the package team does not contain the archive user
        final List<JabberId> t_id_list_z = readTeamIds(datum.junit_z, c_z.getId());
        assertEquals("Local container team list does not match expectation.", 2, t_id_list_z.size());
        assertTrue("Local container team list does not contain archive user.", t_id_list_z.contains(datum.junit_z.getId()));
        assertTrue("Local container team list does not contain publish to user.", t_id_list_z.contains(datum.junit_x.getId()));
    }

    /**
     * As a user with the backup feature:
     * <ol>
     * <li>Create a package.
     * <li>Add documents to the package.
     * <li>Publish the package.
     * <li>Archive the package.
     * <li>Restore the package.
     * <li>Archive the package.
     * <li>Verify the result:
     * <ol>
     * <li>Archive event is fired.
     * <li>Package exists in the backup.
     * <li>Package is archived in the backup.
     * <li>Package team does not contain the user in the backup.
     * <li>Package exists locally.
     * <li>Package is archived locally.
     * <li>Package team does not contain the user.
     */
    public void testArchiveRestoreArchive() {
        // create a package; add documents to the package; publish the package
        final Container c_initial_z = createContainer(datum.junit_z, getName());
        addDocuments(datum.junit_z, c_initial_z.getId());
        publish(datum.junit_z, c_initial_z.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        // archive the package
        archive(datum.junit_z, c_initial_z.getId());
        datum.waitForEvents();

        // restore the package
        restore(datum.junit_z, c_initial_z.getId());
        datum.waitForEvents();

        // archive the package
        addContainerListener(datum.junit_z, datum.cl_archive_z);
        archive(datum.junit_z, c_initial_z.getId());
        removeContainerListener(datum.junit_z, datum.cl_archive_z);
        datum.waitForEvents();

        // verify the archive event is fired
        assertTrue("Archive event not fired.", datum.cl_archive_notify_z);

        // verify the package exists in the backup; the package is archived in the backup
        final Container c_backup_z = getBackupModel(datum.junit_z).readContainer(c_initial_z.getUniqueId());
        assertNotNull("Remote container is null.", c_backup_z);
        assertTrue("Remote container is not archived.", c_backup_z.isArchived());

        // verify the package team does not contain the archive user in the backup
        final List<JabberId> t_id_list_backup_z = getBackupModel(datum.junit_z).readTeamIds(c_backup_z.getUniqueId());
        logger.logVariable("t_id_list_backup_z", t_id_list_backup_z);
        assertEquals("Backup container team list does not match expectation.", 1, t_id_list_backup_z.size());
        assertFalse("Backup container team list contains archive user.", t_id_list_backup_z.contains(datum.junit_z.getId()));
        assertTrue("Backup container team list does not contain publish to user.", t_id_list_backup_z.contains(datum.junit_x.getId()));

        // verify the package exists locally; the package is archived locally
        final Container c_z = readContainer(datum.junit_z, c_initial_z.getUniqueId());
        assertNotNull("Local container is null.", c_z);
        assertTrue("Local container is not archived.", c_z.isArchived());

        // verify the package team does not contain the archive user
        final List<JabberId> t_id_list_z = readTeamIds(datum.junit_z, c_z.getId());
        assertEquals("Local container team list does not match expectation.", 1, t_id_list_z.size());
        assertFalse("Local container team list contains archive user.", t_id_list_z.contains(datum.junit_z.getId()));
        assertTrue("Local container team list does not contain publish to user.", t_id_list_z.contains(datum.junit_x.getId()));
    }

    /**
     * As a user with the backup feature:
     * <ol>
     * <li>Create a package.
     * <li>Add documents to the package.
     * <li>Publish the package.
     * <li>Archive the package.
     * <li>Restore the package.
     * <li>Archive the package.
     * <li>Restore the package.
     * <li>Verify the result:
     * <ol>
     * <li>Restore event is fired.
     * <li>Package exists in the backup.
     * <li>Package is not archived in the backup.
     * <li>Package team contains the user in the backup.
     * <li>Package exists locally.
     * <li>Package is not archived locally.
     * <li>Package team contains the user.
     */
    public void testArchiveRestoreArchiveRestore() {
        // create a package; add documents to the package; publish the package
        final Container c_initial_z = createContainer(datum.junit_z, getName());
        addDocuments(datum.junit_z, c_initial_z.getId());
        publish(datum.junit_z, c_initial_z.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        // archive the package
        archive(datum.junit_z, c_initial_z.getId());
        datum.waitForEvents();

        // restore the package
        restore(datum.junit_z, c_initial_z.getId());
        datum.waitForEvents();

        // archive the package
        archive(datum.junit_z, c_initial_z.getId());
        datum.waitForEvents();

        // restore the package
        addContainerListener(datum.junit_z, datum.cl_restore_z);
        restore(datum.junit_z, c_initial_z.getId());
        removeContainerListener(datum.junit_z, datum.cl_restore_z);
        datum.waitForEvents();

        // verify the restore event is fired
        assertTrue("Restore event not fired.", datum.cl_restore_notify_z);

        // verify the package exists in the backup; the package is not archived in the backup
        final Container c_backup_z = getBackupModel(datum.junit_z).readContainer(c_initial_z.getUniqueId());
        assertNotNull("Remote container is null.", c_backup_z);
        assertFalse("Remote container is archived.", c_backup_z.isArchived());

        // verify the package team contains the archive user in the backup
        final List<JabberId> t_id_list_backup_z = getBackupModel(datum.junit_z).readTeamIds(c_backup_z.getUniqueId());
        assertEquals("Backup container team list does not match expectation.", 2, t_id_list_backup_z.size());
        assertTrue("Backup container team list does not contain archive user.", t_id_list_backup_z.contains(datum.junit_z.getId()));
        assertTrue("Backup container team list does not contain publish to user.", t_id_list_backup_z.contains(datum.junit_x.getId()));

        // verify the package exists locally; the package is archived locally
        final Container c_z = readContainer(datum.junit_z, c_initial_z.getUniqueId());
        assertNotNull("Local container is null.", c_z);
        assertFalse("Local container is archived.", c_z.isArchived());

        // verify the package team does not contain the archive user
        final List<JabberId> t_id_list_z = readTeamIds(datum.junit_z, c_z.getId());
        assertEquals("Local container team list does not match expectation.", 2, t_id_list_z.size());
        assertTrue("Local container team list does not contain archive user.", t_id_list_z.contains(datum.junit_z.getId()));
        assertTrue("Local container team list does not contain publish to user.", t_id_list_z.contains(datum.junit_x.getId()));
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
        private boolean cl_archive_notify_z;
        private final ContainerListener cl_archive_z;
        private boolean cl_restore_notify_z;
        private final ContainerListener cl_restore_z;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private final OpheliaTestUser junit_z;
        private Fixture(final OpheliaTestUser junit_x,
                final OpheliaTestUser junit_y, final OpheliaTestUser junit_z) {
            super();
            this.cl_archive_notify_z = this.cl_restore_notify_z = false;
            this.cl_archive_z = new ContainerAdapter() {
                @Override
                public void containerArchived(final ContainerEvent e) {
                    assertTrue("Archive event is not local.", e.isLocal());
                    assertTrue("Archive event is remote .", !e.isRemote());
                    assertNotNull("Archive event's container is null.", e.getContainer());
                    assertTrue("Archive event's container is not archived.", e.getContainer().isArchived());
                    assertNull("Archive event's document is not null.", e.getDocument());
                    assertNull("Archive event's draft is not null.", e.getDraft());
                    assertNull("Archive event's team member is not null.", e.getTeamMember());
                    assertNull("Archive event's version is not null.", e.getVersion());
                    cl_archive_notify_z = true;
                }
            };
            this.cl_restore_z = new ContainerAdapter() {
                @Override
                public void containerRestored(final ContainerEvent e) {
                    assertTrue("Restore event is not local.", e.isLocal());
                    assertTrue("Restore event is remote .", !e.isRemote());
                    assertNotNull("Restore event's container is null.", e.getContainer());
                    assertFalse("Restore event's container is archived.", e.getContainer().isArchived());
                    assertNull("Restore event's document is not null.", e.getDocument());
                    assertNull("Restore event's draft is not null.", e.getDraft());
                    assertNull("Restore event's team member is not null.", e.getTeamMember());
                    assertNull("Restore event's version is not null.", e.getVersion());
                    cl_restore_notify_z = true;
                }
            };
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
            addQueueHelper(junit_z);
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            this.junit_z = junit_z;
        }
    }
}
