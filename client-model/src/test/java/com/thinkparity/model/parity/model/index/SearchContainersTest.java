/*
 * Created On: Jul 4, 2006 6:21:59 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.index;

import java.util.List;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.container.Container;
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
    private static final String NAME = "[LMODEL] [INDEX] [TEST SEARCH CONTAINERS]";

	/** Test datum. */
	private Fixture datum;

	/** Create SearchContainersTest. */
	public SearchContainersTest() { super(NAME); }

    /**
     * Test search containers.
     *
     */
    public void testSearch() {
        List<IndexHit> indexHits = null;
        try { indexHits = datum.iModel.searchContainers(datum.expression); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        assertNotNull(NAME, indexHits);
        assertTrue(NAME + " [NO CONTAINERS RETURNED]", indexHits.size() > 0);
        Container container;
        for(final IndexHit indexHit : indexHits) {
            container = datum.cModel.read(indexHit.getId());
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
        final ContainerModel cModel = getContainerModel();
        final IndexModel iModel = getIndexModel();

        final String containerName = NAME.replaceAll("\\[|\\]", "");
        final Container container = createContainer(containerName);
		datum = new Fixture(cModel, container.getName(), iModel);
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
        private final ContainerModel cModel;
		private final String expression;
		private final IndexModel iModel;
		private Fixture(final ContainerModel cModel, final String expression,
                final IndexModel iModel) {
            this.cModel = cModel;
			this.expression = expression;
			this.iModel = iModel;
		}
	}
}
