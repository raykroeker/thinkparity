/*
 * Created On: Jun 27, 2006 4:00:45 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;


/**
 * <b>Title:</b>thinkParity Container Read Latest Version Test<br>
 * <b>Description:</b>thinkParity Container Read Latest Version Test
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadLatestVersionTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [READ LATEST VERSION TEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create ReadLatestVersionTest. */
    public ReadLatestVersionTest() { super(NAME); }

    /**
     * Test the container model's read api.
     *
     */
    public void testReadLatestVersion() {
        final ContainerVersion version = datum.cModel.readLatestVersion(datum.containerId);

        assertNotNull(NAME, version);
        assertEquals(NAME, datum.eVersion, version);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login();

        final InternalContainerModel cModel = getInternalContainerModel();
        final Container eContainer = cModel.create(NAME);
        final ContainerVersion eVersion = cModel.createVersion(eContainer.getId());

        datum = new Fixture(cModel, eContainer.getId(), eVersion);
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
        private final ContainerModel cModel;
        private final Long containerId;
        private final ContainerVersion eVersion;
        private Fixture(final ContainerModel cModel, final Long containerId,
                final ContainerVersion eVersion) {
            this.cModel = cModel;
            this.containerId = containerId;
            this.eVersion = eVersion;
        }
    }
}
