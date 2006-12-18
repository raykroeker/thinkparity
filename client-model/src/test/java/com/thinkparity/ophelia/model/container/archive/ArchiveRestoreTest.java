/*
 * Created On: Sep 27, 2006 8:44:19 AM
 */
package com.thinkparity.ophelia.model.container.archive;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.container.ContainerTestCase;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Container Restore Test<br>
 * <b>Description:</b>Test the container's restore api.
 * <ol>
 * <li>Create.
 * <li>Add documents.
 * <li>Publish to everyone.
 * <li>Archive.
 * <li>Restore.
 * </ol>
 * <br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveRestoreTest extends ArchiveTestCase {

    /** Test name. */
    private static final String NAME = "Restore Test";

    /** Test datum. */
    private Fixture datum;

    /** Create RestoreTest. */
    public ArchiveRestoreTest() {
        super(NAME);
    }

    /** Test the restore api. */
    public void testRestore() {
        final Container c = createContainer(datum.junit_z, NAME);
        addDocuments(datum.junit_z, c.getId());
        publish(datum.junit_z, c.getId(), "JUnit thinkParity", "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        archive(datum.junit_z, c.getId());
        datum.waitForEvents();
        restore(datum.junit_z, c.getUniqueId());

        final Container c_restore = readContainer(datum.junit_z, c.getUniqueId());
        assertNotNull(NAME + " - Container is null.", c);
        final List<ContainerVersion> cv_list_restore = readContainerVersions(datum.junit_z, c_restore.getId());
        assertNotNull(NAME + " - Container versions is null.", cv_list_restore);
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
