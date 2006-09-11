/*
 * Created On: Aug 23, 2006 12:04:52 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.container.InternalContainerModel;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadVersionsPrePublishTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "READ VERSIIONS PRE PUBLISH";

    /** Test datum. */
    private Fixture datum;

    /** Create ReadVersionsPrePublishTest. */
    public ReadVersionsPrePublishTest() { super(NAME); }

    /** Test the read version api. */
    public void testReadVersions() {
        final List<ContainerVersion> versions =
            datum.containerModel.readVersions(datum.container.getId());
        assertNotNull(NAME + " VERSIONS IS NULL", versions);
        assertEquals(NAME + " VERSIONS SIZE DOES NOT MATCH EXPECTATION", 0, versions.size());
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final InternalContainerModel containerModel = getInternalContainerModel();
        final Container container = createContainer(NAME);
        datum = new Fixture(container, containerModel);
        datum.containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final InternalContainerModel containerModel;
        private Fixture(final Container container,
                final InternalContainerModel containerModel) {
            this.container = container;
            this.containerModel = containerModel;
        }
    }
}
