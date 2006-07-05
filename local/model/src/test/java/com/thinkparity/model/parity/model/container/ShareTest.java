/*
 * Created On: Jun 28, 2006 8:29:43 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.api.events.ContainerListener;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ShareTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [SHARE TEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create DeleteTest. */
    public ShareTest() { super(NAME); }

    /**
     * Test the delete api on the given datum.
     * 
     * @param datum
     *            The test datum.
     */
    public void testShare() {
        try { datum.cModel.share(datum.containerId, datum.jabberId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login();
    
        final ModelTestUser jUnitX = ModelTestUser.getX();
        final ContainerModel cModel = getContainerModel();
    
        // a container with documents
        final Container container = cModel.create(NAME);
        final Document document = create(getInputFiles()[0]);
        cModel.addDocument(container.getId(), document.getId());
        datum = new Fixture(cModel, container.getId(), 1L, 1L, jUnitX.getJabberId());
    
        // register listeners
        cModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        getContainerModel().removeListener(datum);
        datum = null;
        logout();
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture implements ContainerListener {
        private final ContainerModel cModel;
        private final Long containerId;
        private final JabberId jabberId;
        private Fixture(final ContainerModel cModel, final Long containerId,
                final Long eContainerVersionId, final Long eDocumentVersionId,
                final JabberId jabberId) {
            this.cModel = cModel;
            this.containerId = containerId;
            this.jabberId = jabberId;
        }
        public void containerClosed(final ContainerEvent e) {
            fail(NAME + " [CONTAINER CLOSED EVENT WAS FIRED]");
        }
        public void containerCreated(final ContainerEvent e) {
            fail(NAME + " [CONTAINER CREATED EVENT WAS FIRED]");
        }
        public void containerDeleted(final ContainerEvent e) {
            fail(NAME + " [CONTAINER DELETED EVENT WAS FIRED]");
        }
        public void containerReactivated(final ContainerEvent e) {
            fail(NAME + " [CONTAINER REACTIVATED EVENT WAS FIRED]");
        }
        public void documentAdded(final ContainerEvent e) {
            fail(NAME + " [DOCUMENT ADDED EVENT WAS FIRED]");
        }
        public void documentRemoved(final ContainerEvent e) {
            fail(NAME + " [DOCUMENT REMOVED EVENT WAS FIRED]");
        }
    }
}