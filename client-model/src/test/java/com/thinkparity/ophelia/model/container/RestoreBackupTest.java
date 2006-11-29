/*
 * Created On:  28-Nov-06 2:50:00 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RestoreBackupTest extends ContainerTestCase {

    private static final String NAME = "Restore Backup Test";

    private Fixture datum;

    /**
     * Create Restore.
     *
     * @param name
     */
    public RestoreBackupTest() {
        super(NAME);
    }

    /**
     * Test the restore backup api.
     *
     */
    public void testRestoreBackup() {
        final Container c = createContainer(datum.junit, NAME + ":  1");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.pdf");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.png");
        publishToContacts(datum.junit, c.getId(), "JUnit.Z thinkParity");
        datum.waitForEvents();

        final Container c_two = createContainer(datum.junit, NAME + ":  2");
        final Document d = addDocument(datum.junit, c_two.getId(), "JUnitTestFramework.txt");
        publishToContacts(datum.junit, c_two.getId(), "JUnit.Z thinkParity");
        createDraft(datum.junit, c_two.getId());
        modifyDocument(datum.junit, d.getId());
        publishToTeam(datum.junit, c_two.getId());
        datum.waitForEvents();

        getContainerModel(datum.junit_z).restoreBackup();
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_Z);
        login(datum.junit);
        login(datum.junit_z);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_z);
        datum = null;
        super.tearDown();
    }

    private class Fixture extends ContainerTestCase.Fixture {
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_z;
        private Fixture(final OpheliaTestUser junit, final OpheliaTestUser junit_z) {
            this.junit = junit;
            this.junit_z = junit_z;
            addQueueHelper(junit);
            addQueueHelper(junit_z);
        }
    }

}
