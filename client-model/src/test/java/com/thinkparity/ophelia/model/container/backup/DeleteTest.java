/*
 * Created On:  11-Dec-06 2:17:50 PM
 */
package com.thinkparity.ophelia.model.container.backup;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Test Case: 1002 - Backup - Delete<br>
 * <b>Description:</b><a
 * href="http://thinkparity.dyndns.org/trac/parity/wiki/TestCase_1002"></a><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeleteTest extends BackupTestCase {

    /** The test name <code>String</code>. */
    private static final String NAME = "thinkParity Test Case: 1002 - Backup - Delete";

    private Fixture datum;

    /**
     * Create DeleteTest.
     *
     * @param name
     */
    public DeleteTest() {
        super(NAME);
    }

    /**
     * Test the delete api.
     *
     */
    public void testDelete() {
        // test
        final Container c = createContainer(datum.junit_z, "Backup Test: Delete backup 1");
        addDocument(datum.junit_z, c.getId(), "JUnitTestFramework.doc");
        addDocument(datum.junit_z, c.getId(), "JUnitTestFramework.pdf");
        addDocument(datum.junit_z, c.getId(), "JUnitTestFramework.png");
        publishToUsers(datum.junit_z, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        deleteContainer(datum.junit_z, c.getId());
        datum.waitForEvents();
        // postconditions - ensure deleted remotely
        final Container c_backup = getBackupModel(datum.junit_z).readContainer(c.getUniqueId());
        assertNull("Container has not been properly deleted.", c_backup);
        final List<ContainerVersion> cv_list_backup = getBackupModel(datum.junit_z).readContainerVersions(c.getUniqueId());
        assertEquals("Container versions have not been properly deleted.", 0, cv_list_backup.size());
    }

    /**
     * @see com.thinkparity.ophelia.model.container.backup.BackupTestCase#setUp()
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
     * @see com.thinkparity.ophelia.model.container.backup.BackupTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit_x);
        logout(datum.junit_z);
        datum = null;
        super.tearDown();
    }

    private final class Fixture extends BackupTestCase.Fixture {
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_z;
        private Fixture(final OpheliaTestUser junit_x, final OpheliaTestUser junit_z) {
            super();
            this.junit_x = junit_x;
            this.junit_z = junit_z;
            addQueueHelper(junit_x);
            addQueueHelper(junit_z);
        }
    }
}

