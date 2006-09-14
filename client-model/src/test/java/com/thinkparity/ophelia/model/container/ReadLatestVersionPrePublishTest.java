/*
 * Created On: Jun 27, 2006 4:00:45 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Container Read Latest Version Test<br>
 * <b>Description:</b>thinkParity Container Read Latest Version Test
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadLatestVersionPrePublishTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [READ LATEST VERSION TEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create ReadLatestVersionTest. */
    public ReadLatestVersionPrePublishTest() { super(NAME); }

    /**
     * Test the container model's read api.
     *
     */
    public void testReadLatestVersion() {
        final ContainerVersion version =
            datum.containerModel.readLatestVersion(datum.container.getId());
        assertNull(NAME + " LATEST VERSION IS NOT NULL", version);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        final InternalContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = createContainer(OpheliaTestUser.JUNIT, NAME);
        datum = new Fixture(container, containerModel);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        logout(OpheliaTestUser.JUNIT);
        datum = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture {
        private final Container container;
        private final ContainerModel containerModel;
        private Fixture(final Container container,
                final ContainerModel containerModel) {
            this.containerModel = containerModel;
            this.container = container;
        }
    }
}
