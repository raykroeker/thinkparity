/*
 * Created On: Aug 23, 2006 12:05:03 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.List;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadVersionsPostPublishTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "TEST READ VERSION POST PUBLISH";

    /** Test datum. */
    private Fixture datum;

    public void testReadVersions() {
        final List<ContainerVersion> versions =
            datum.containerModel.readVersions(datum.container.getId());
        assertNotNull(NAME + " VERSIONS IS NULL", versions);
        assertEquals(NAME + " VERSIONS DOES NOT MATCH EXPECTATION", datum.versions, versions);
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
        final List<ContainerVersion> versions = containerModel.readVersions(container.getId());
        datum = new Fixture(container, containerModel, versions);
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
    public ReadVersionsPostPublishTest() { super(NAME); }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final InternalContainerModel containerModel;
        private final Container container;
        private final List<ContainerVersion> versions;
        private Fixture(final Container container,
                final InternalContainerModel containerModel,
                final List<ContainerVersion> versions) {
            this.container = container;
            this.containerModel = containerModel;
            this.versions = versions;
        }
    }
}
