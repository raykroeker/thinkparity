/*
 * Created On: Jul 3, 2006 5:48:12 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.api.events.ContainerListener;
import com.thinkparity.model.parity.model.document.Document;


/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReactivateTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [TEST REACTIVATE]";

    /** Test datum. */
    private Fixture datum;

    /** Create ReactivateTest. */
    public ReactivateTest() { super(NAME); }

    public void testReactivate() {
        try { datum.cModel.reactivate(datum.containerId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        assertTrue(NAME + " [CONTAINER REACTIVATE DID NOT FIRE]", datum.didNotify);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login();
        final InternalContainerModel cModel = getInternalContainerModel();
        final Container container = createContainer(NAME);
        final Document document = create(getInputFiles()[0]);
        cModel.addDocument(container.getId(), document.getId());
        cModel.close(container.getId());
        datum = new Fixture(cModel, container.getId());
        getContainerModel().addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        getContainerModel().removeListener(datum);
        datum = null;
        logout();
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture implements ContainerListener {
        private final ContainerModel cModel;
        private final Long containerId;
        private Boolean didNotify;
        private Fixture(final ContainerModel cModel, final Long containerId) {
            this.cModel = cModel;
            this.containerId = containerId;
        }
        public void containerClosed(final ContainerEvent e) {
            fail(NAME + " [CONTAINER CLOSED EVENT FIRED]");
        }
        public void containerCreated(final ContainerEvent e) {
            fail(NAME + " [CONTAINER CREATED EVENT FIRED]");
        }
        public void containerDeleted(final ContainerEvent e) {
            fail(NAME + " [CONTAINER DELETED EVENT FIRED]");
        }
        public void containerReactivated(final ContainerEvent e) {
            didNotify = Boolean.TRUE;
        }
        public void documentAdded(final ContainerEvent e) {
            fail(NAME + " [DOCUMENT ADDED EVENT FIRED]");
        }
        public void documentRemoved(final ContainerEvent e) {
            fail(NAME + " [DOCUMENT REMOVED EVENT FIRED]");
        }
    }

}
