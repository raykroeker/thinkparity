/*
 * Created On: Jul 6, 2006 8:50:37 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.List;

import com.thinkparity.model.parity.model.document.Document;

/**
 * <b>Title:</b>thinkParity Container Read History Test<br>
 * <b>Description:</b>A test for the container read history api.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class ReadHistoryTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [TEST READ HISTORY]";

    /** Test datum. */
	private Fixture datum;

	/** Create ReadHistoryTest. */
	public ReadHistoryTest() { super(NAME); }

	public void testReadHistory() {
        final List<ContainerHistoryItem> history = datum.cModel.readHistory(datum.containerId);
        ContainerHistoryItem item;

		assertNotNull(NAME, history);
        item = history.get(0);
		assertNotNull(NAME, item);
		assertEquals(NAME + " [PUBLISH HISTORY ITEM CONATINER ID DOES NOT MATCH EXPECTATION]",
				datum.containerId, item.getContainerId());

        item = history.get(1);
		assertNotNull(NAME + " [PUBLISH HISTORY ITEM IS NULL]", item);
		assertEquals(NAME + " [PUBLISH HISTORY ITEM CONTAINER ID DOES NOT MATCH EXPECTATION]",
                datum.containerId, item.getContainerId());
	}

	/**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
        login();
        final ContainerModel cModel = getContainerModel();
        final Container container = createContainer(NAME);
        final Document document = addDocument(container, getInputFiles()[0]);
        modifyDocument(document);
        getContainerModel().publish(container.getId());
        getContainerModel().close(container.getId());
		datum = new Fixture(cModel, container.getId());
	}

	/**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#tearDown()
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
		private final Long containerId;
		private Fixture(final ContainerModel cModel, final Long containerId) {
			this.cModel = cModel;
			this.containerId = containerId;
		}
	}
}
