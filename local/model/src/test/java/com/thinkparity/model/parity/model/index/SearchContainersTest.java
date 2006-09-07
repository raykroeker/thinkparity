/*
 * Created On: Jul 4, 2006 6:21:59 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.index;

import java.util.List;

import com.thinkparity.model.container.Container;
import com.thinkparity.model.parity.model.container.ContainerModel;

/**
 * <b>Title:</b>thinkParity Search Containers Test<br>
 * <b>Description:</b>Test of the search containers index api.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class SearchContainersTest extends IndexTestCase {

    /** Test name. */
    private static final String NAME = "TEST SEARCH CONTAINERS";

	/** Test datum. */
	private Fixture datum;

	/** Create SearchContainersTest. */
	public SearchContainersTest() { super(NAME); }

    /**
     * Test search containers.
     *
     */
    public void testSearch() {
        final List<Long> containerIds =
            datum.containerModel.search(datum.container.getName());

        assertNotNull(NAME, containerIds);
        assertTrue(NAME + " [NO CONTAINERS RETURNED]", containerIds.size() > 0);
        Container container;
        for(final Long containerId : containerIds) {
            container = datum.containerModel.read(containerId);
            assertNotNull(NAME, container);
        }
	}

	/**
	 * @see com.thinkparity.model.parity.model.index.IndexTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
        login();
        final ContainerModel containerModel = getContainerModel();

        final String containerName = NAME.replaceAll("\\[|\\]", "");
        final Container container = createContainer(containerName);
		datum = new Fixture(containerModel, container);
	}

	/**
	 * @see com.thinkparity.model.parity.model.index.IndexTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		datum = null;
		logout();
		super.tearDown();
	}

    /** Test datum definition. */
	private class Fixture {
        private final Container container;
		private final ContainerModel containerModel;
		private Fixture(final ContainerModel containerModel,
                final Container container) {
            this.containerModel = containerModel;
			this.container = container;
		}
	}
}
