/*
 * Created On: Jun 28, 2006 8:29:43 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.model.document.Document;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.4.5
 */
public class DeleteTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [DELETE TEST]";

    /** Test data. */
    private Map<String, Fixture> data;

    /** Create DeleteTest. */
    public DeleteTest() { super(NAME); }

    /**
     * Test the delete api.
     *
     */
    public void testDelete() { testDelete(data.get("testDelete")); }

    /**
     * Test the delete api on a container that has documents.
     *
     */
    public void testDeleteWithDocuments() { testDelete(data.get("testDeleteWithDocuments")); }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login();

        data = new HashMap<String, Fixture>(2, 1.0F);
        final ContainerModel cModel = getContainerModel();
        Container container = null;
        Document document = null;

        // a vanilla container
        container = cModel.create(NAME + ".0");
        data.put("testDelete", new Fixture(cModel, container.getId()));

        // a container with documents
        container = cModel.create(NAME + ".1");
        data.put("testDeleteWithDocuments", new Fixture(cModel, container.getId()));
        document = create(getInputFiles()[0]);
        cModel.addDocument(container.getId(), document.getId());

        // register listeners
        cModel.addListener(data.get("testDelete"));
        cModel.addListener(data.get("testDeleteWithDocuments"));
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        final ContainerModel cModel = getContainerModel();
        for(final Fixture datum : data.values()) { cModel.removeListener(datum); }
        data.clear();
        data = null;
        logout();
        super.tearDown();
    }

    /**
     * Test the delete api on the given datum.
     * 
     * @param datum
     *            The test datum.
     */
    private void testDelete(final Fixture datum) {
        datum.cModel.delete(datum.containerId);
        
        final Container container = datum.cModel.read(datum.containerId);
        assertNull(NAME + " [CONTAINER IS NOT NULL]", container);
        assertTrue(NAME + " [CONTAINER DELETION EVENT NOT FIRED]", datum.didNotify);
    }

    /** Test data definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final ContainerModel cModel;
        private final Long containerId;
        private Boolean didNotify;
        private Fixture(final ContainerModel cModel, final Long containerId) {
            this.cModel = cModel;
            this.containerId = containerId;
            this.didNotify = Boolean.FALSE;
        }
        @Override
        public void containerDeleted(ContainerEvent e) {
            didNotify = Boolean.TRUE;
            assertTrue(NAME + " [EVENT GENERATED IS NOT LOCAL]", e.isLocal());
            assertTrue(NAME + " [EVENT GENERATED IS REMOTE]", !e.isRemote());
            assertNull(NAME + " [CONTAINER IS NOT NULL]", e.getContainer());
            assertNull(NAME + " [TEAM MEMBER IS NOT NULL]", e.getTeamMember());
        }
    }
}
