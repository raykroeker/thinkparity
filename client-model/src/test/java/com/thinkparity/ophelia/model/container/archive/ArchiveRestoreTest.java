/*
 * Created On: Sep 27, 2006 8:44:19 AM
 */
package com.thinkparity.ophelia.model.container.archive;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

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
        final List<Document> d_list = addDocuments(datum.junit_z, c.getId());
        publish(datum.junit_z, c.getId(), "JUnit thinkParity", "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();

        final List<ContainerVersion> cv_list =
            readContainerVersions(datum.junit_z, c.getId());
        final ContainerVersion cv_latest =
            readContainerLatestVersion(datum.junit_z, c.getId());
        final List<DocumentVersion> dv_list =
            readContainerVersionDocumentVersions(datum.junit_z, c.getId(),
                    cv_latest.getVersionId());

        archive(datum.junit_z, c.getId());
        datum.waitForEvents();
        restore(datum.junit_z, c.getId());
        datum.waitForEvents();

        final Container c_restore = readContainer(datum.junit_z, c.getUniqueId());
        assertNotNull("Restored container is null.", c);
        assertEquals("Restored container does not match expectation.", c, c_restore);

        final List<ContainerVersion> cv_list_restore = readContainerVersions(datum.junit_z, c_restore.getId());
        assertNotNull("Restored container versions is null.", cv_list_restore);
        for (int i = 0; i < cv_list_restore.size(); i++) {
            assertEquals("Restored container version does not match expectaion.",
                    cv_list_restore.get(i), cv_list.get(i));
        }

        final ContainerVersion cv_latest_restore = readContainerLatestVersion(datum.junit_z, c_restore.getId());
        final List<Document> d_list_restore = readContainerVersionDocuments(
                datum.junit_z, c_restore.getId(), cv_latest_restore.getVersionId());
        for (int i = 0; i < d_list_restore.size(); i++) {
            assertEquals("Restored document does not match expectation.",
                    d_list_restore.get(i), d_list.get(i));
        }
        final List<DocumentVersion> dv_list_restore = readContainerVersionDocumentVersions(
                datum.junit_z, c_restore.getId(), cv_latest_restore.getVersionId());
        for (int i = 0; i < dv_list_restore.size(); i++) {
            assertEquals("Restored document version does not match expectation.",
                    dv_list_restore.get(i), dv_list.get(i));
        }
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
