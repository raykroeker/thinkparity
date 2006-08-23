/*
 * Created On: Aug 23, 2006 12:05:03 PM
 */
package com.thinkparity.model.parity.model.container;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadVersionPostPublishTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "TEST READ VERSION POST PUBLISH";

    /** Test datum. */
    private Fixture datum;

    public void testReadVersion() {
        final ContainerVersion version =
            datum.containerModel.readVersion(datum.container.getId(), datum.version.getVersionId());
        assertNotNull(NAME + " VERSION IS NULL", version);
        assertEquals(NAME + " VERSION DOES NOT MATCH EXPECTATION", datum.version, version);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final InternalContainerModel containerModel = getInternalContainerModel();
        final Container container = createContainer(NAME);
        addDocuments(container);
        login();
        publish(container);
        logout();
        final ContainerVersion version = containerModel.readLatestVersion(container.getId());
        datum = new Fixture(container, containerModel, version);
        datum.containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        super.tearDown();
    }

    /** Create ReadVersionPostPublishTest. */
    public ReadVersionPostPublishTest() { super(NAME); }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final InternalContainerModel containerModel;
        private final Container container;
        private final ContainerVersion version;
        private Fixture(final Container container,
                final InternalContainerModel containerModel,
                final ContainerVersion version) {
            this.container = container;
            this.containerModel = containerModel;
            this.version = version;
        }
    }
}
