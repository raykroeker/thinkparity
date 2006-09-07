/*
 * Created On: Jun 27, 2006 4:00:45 PM
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.container.Container;
import com.thinkparity.model.container.ContainerVersion;

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
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login();
        final InternalContainerModel containerModel = getInternalContainerModel();
        final Container container = createContainer(NAME);
        datum = new Fixture(container, containerModel);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        logout();
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
