/*
 * Created On: Jun 27, 2006 4:00:45 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.container.ContainerModel;


/**
 * <b>Title:</b>thinkParity Container Read Test<br>
 * <b>Description:</b>thinkParity Container Read Test
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [READ TEST]";

    /** Test datum. */
    private Map<String, Fixture> data;

    /** Create ReadTest. */
    public ReadTest() { super(NAME); }

    /**
     * Test the container model's read api.
     *
     */
    public void testReadContainer() {
        final Fixture datum = data.get("testReadContainer");
        final Container container = datum.cModel.read(datum.containerId);

        assertNotNull(NAME, container);
        assertEquals(NAME, datum.eContainer, container);
    }

    /**
     * Test the container model's read api.
     *
     */
    public void testReadContainers() {
        final Fixture datum = data.get("testReadContainers");
        final List<Container> containers = datum.cModel.read();

        assertNotNull(NAME, containers);
        for(final Container eContainer : datum.eContainers) {
            assertTrue(NAME + " [ACTUAL CONTAINERS DOES NOT CONTAIN EXPECTATION]", containers.contains(eContainer));
        }
        for(final Container container : containers) {
            assertTrue(NAME + " [EXPECTED CONTAINERS DOES NOT CONTAIN ACTUAL]", datum.eContainers.contains(container));
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login();

        data = new HashMap<String, Fixture>(2, 1.0F);

        final ContainerModel containerModel = getContainerModel();
        final Container container = containerModel.create(NAME + ".0");
        data.put("testReadContainer", new Fixture(containerModel, container.getId(), container));

        final List<Container> containers = containerModel.read();
        containers.add(containerModel.create(NAME + ".1"));
        data.put("testReadContainers", new Fixture(containerModel, container.getId(), containers));
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        logout();
        data.clear();
        data = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture {
        private final ContainerModel cModel;
        private final Long containerId;
        private final Container eContainer;
        private final List<Container> eContainers;
        private Fixture(final ContainerModel cModel, final Long containerId,
                final Container eContainer) {
            this.cModel = cModel;
            this.containerId = containerId;
            this.eContainer = eContainer;
            this.eContainers = null;
        }
        private Fixture(final ContainerModel cModel, final Long containerId,
                final List<Container> eContainers) {
            this.cModel = cModel;
            this.containerId = containerId;
            this.eContainer = null;
            this.eContainers = eContainers;
        }
    }
}
