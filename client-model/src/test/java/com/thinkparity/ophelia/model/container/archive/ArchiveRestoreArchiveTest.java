/*
 * Created On: Sep 27, 2006 8:44:19 AM
 */
package com.thinkparity.ophelia.model.container.archive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;

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
 * <li>Archive.
 * </ol>
 * <br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveRestoreArchiveTest extends ArchiveTestCase {

    /** Test name. */
    private static final String NAME = "Restore Test";

    /**
     * Assert the container is not null.
     * 
     * @param assertion
     *            An assertion.
     * @param container
     *            A container.
     */
    protected static void assertNotNull(final String assertion, final Container container) {
        assertNotNull(assertion + " [CONTAINER IS NULL]", (Object) container);
        assertNotNull(assertion + " [CONTAINER'S CREATED BY IS NULL]", container.getCreatedBy());
        assertNotNull(assertion + " [CONTAINER'S CREATED ON IS NULL]", container.getCreatedOn());
        assertNotNull(assertion + " [CONTAINER'S FLAGS IS NULL]", container.getFlags());
        assertNotNull(assertion + " [CONTAINER'S NAME IS NULL]", container.getName());
        assertNotNull(assertion + " [CONTAINER'S REMOTE INFO IS NULL]", container.getRemoteInfo());
        assertNotNull(assertion + " [CONTAINER'S UPDATED BY REMOTE INFO IS NULL]", container.getRemoteInfo().getUpdatedBy());
        assertNotNull(assertion + " [CONTAINER'S UPDATED ON REMOTE INFO IS NULL]", container.getRemoteInfo().getUpdatedOn());
        assertNotNull(assertion + " [CONTAINER'S STATE IS NULL]", container.getState());
        assertNotNull(assertion + " [CONTAINER'S TYPE IS NULL]", container.getType());
        assertNotNull(assertion + " [CONTAINER'S UNIQUE ID IS NULL]", container.getUniqueId());
        assertNotNull(assertion + " [CONTAINER'S UPDATED BY IS NULL]", container.getUpdatedBy());
        assertNotNull(assertion + " [CONTAINER'S UPDATED ON IS NULL]", container.getUpdatedOn());
    }

    /**
     * Assert that a container version is not null.
     * 
     * @param assertion
     *            The assertion.
     * @param version
     *            The container version.
     */
    protected static void assertNotNull(final String assertion, final ContainerVersion version) {
        assertNotNull(assertion + " [CONTAINER VERSION IS NULL]", (Object) version);
        assertNotNull(assertion + " [CONTAINER VERSION'S ARTIFACT TYPE IS NULL]", version.getArtifactType());
        assertNotNull(assertion + " [CONTAINER VERSION'S UNIQUE ID IS NULL]", version.getArtifactUniqueId());
        assertNotNull(assertion + " [CONTAINER VERSION'S CREATED BY IS NULL]", version.getCreatedBy());
        assertNotNull(assertion + " [CONTAINER VERSION'S CREATED ON IS NULL]", version.getCreatedOn());
        assertNotNull(assertion + " [CONTAINER VERSION'S NAME IS NULL]", version.getName());
        assertNotNull(assertion + " [CONTAINER VERSION'S UPDATED BY IS NULL]", version.getUpdatedBy());
        assertNotNull(assertion + " [CONTAINER VERSION'S UPDATED ON IS NULL]", version.getUpdatedOn());
        assertNotNull(assertion + " [CONTAINER VERSION'S VERSION ID IS NULL]", version.getVersionId());
    }

    /** Test datum. */
    private Fixture datum;

    /** Create RestoreTest. */
    public ArchiveRestoreArchiveTest() {
        super(NAME);
    }

    /** Test the archive api. */
    public void testArchive() {
        final Container c = createContainer(OpheliaTestUser.JUNIT_Z, NAME);
        addDocuments(datum.junit_z, c.getId());
        publish(datum.junit_z, c.getId(), "JUnit thinkParity", "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();

        archive(datum.junit_z, c.getId());
        datum.waitForEvents();
        restore(datum.junit_z, c.getId());
        datum.waitForEvents();

        final Container c_restore = readContainer(datum.junit_z, c.getUniqueId());
        final List<ContainerVersion> c_v_restore_list = readContainerVersions(datum.junit_z, c.getId());
        final Map<ContainerVersion, List<Document>> cv_d_restore_map = new HashMap<ContainerVersion, List<Document>>();
        for (final ContainerVersion c_v_restore : c_v_restore_list) {
            cv_d_restore_map.put(c_v_restore, readContainerVersionDocuments(
                    datum.junit_z, c_restore.getId(), c_v_restore.getVersionId()));
        }

        archive(datum.junit_z, c_restore.getId());

        final Container c_archive = getContainerModel(datum.junit_z).read(c.getId());
        assertNotNull(NAME + " - Archived container is null.", c_archive);
        final List<ContainerVersion> cv_archive_list =
            getContainerModel(datum.junit_z).readVersions(c.getId());
        assertNotNull(NAME + " - Archived container versions is null.", cv_archive_list);
        assertEquals(NAME + " - Archived container versions size is wrong.", 1, cv_archive_list.size());
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
