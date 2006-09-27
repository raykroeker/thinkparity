/*
 * Created On: Sep 26, 2006 7:47:30 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.archive.InternalArchiveModel;



/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "Archive Test";

    /**
     * Assert the container versions are not null.
     * 
     * @param assertion
     *            An assertion.
     * @param containers
     *            A list of containers.
     */
    protected static void assertNotNull(final String assertion,
            final List<ContainerVersion> versions) {
        assertNotNull(assertion + " [VERSIONS IS NULL]", (Object) versions);
        for(final ContainerVersion version : versions) {
            assertNotNull(assertion, version);
        }
    }

    /** Test datum. */
    private Fixture datum;

    /** Create ArchiveTest. */
    public ArchiveTest() {
        super(NAME);
    }

    /** Test the archive api. */
    public void testArchive() {
        datum.containerModel.archive(datum.container.getId());

        final Container container = datum.containerModel.read(datum.container.getId());
        assertNull(NAME + " - Container is not null.", container);

        final Container archivedContainer =
            datum.archiveModel.readContainer(datum.container.getUniqueId());
        assertNotNull(NAME + " - Archived container is null.", archivedContainer);
        final List<ContainerVersion> archivedVersions =
            datum.archiveModel.readContainerVersions(datum.container.getUniqueId());
        assertNotNull(NAME + " - Archived container versions are null.", archivedVersions);
    }


    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        login(OpheliaTestUser.JUNIT);
        final InternalArchiveModel archiveModel = getArchiveModel(OpheliaTestUser.JUNIT);
        final ContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = createContainer(OpheliaTestUser.JUNIT, NAME);
        addDocuments(OpheliaTestUser.JUNIT, container);
        publish(OpheliaTestUser.JUNIT, container);
        datum = new Fixture(archiveModel, container, containerModel);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;
        logout(OpheliaTestUser.JUNIT);
    }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final InternalArchiveModel archiveModel;
        private final Container container;
        private final ContainerModel containerModel;
        private Fixture(final InternalArchiveModel archiveModel,
                final Container container, final ContainerModel containerModel) {
            super();
            this.archiveModel = archiveModel;
            this.container = container;
            this.containerModel = containerModel;
        }
    }
}
