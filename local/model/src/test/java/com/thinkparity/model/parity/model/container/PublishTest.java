/*
 * Created On: Jul 3, 2006 2:05:41 PM
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
public class PublishTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [TEST PUBLISH]";

    /** Test datum. */
    private Fixture datum;

    /** Create PublishTest. */
    public PublishTest() { super(NAME); }

    /** Test the publish api. */
    public void testPublish() {
        try { datum.cModel.publish(datum.containerId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        final ContainerVersion cVersion = datum.cModel.readLatestVersion(datum.containerId);
        assertEquals(NAME + " [CONTAINER VERSION ID DOES NOT MATCH EXPECTATION]", datum.eVersionId, cVersion.getVersionId());
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login();
        final ContainerModel cModel = getContainerModel();
        final Container container = createContainer(NAME);
        final Document document = create(getInputFiles()[0]);
        cModel.addDocument(container.getId(), document.getId());
        modifyDocument(document);
        datum = new Fixture(cModel, container.getId(), 2L);

        cModel.addListener(datum);
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

    /** Test datumn fixture. */
    private class Fixture implements ContainerListener {
        private final ContainerModel cModel;
        private final Long containerId;
        private final Long eVersionId;
        private Fixture(final ContainerModel cModel, final Long containerId,
                final Long eVersionId) {
            this.cModel = cModel;
            this.containerId = containerId;
            this.eVersionId = eVersionId;
        }
        public void draftCreated(ContainerEvent e) {
            fail(NAME + " [DRAFT CREATED EVENT FIRED]");
        }
        public void teamMemberAdded(ContainerEvent e) {
            fail(NAME + " [TEAM MEMBER ADDED EVENT FIRED]");
        }
        public void teamMemberRemoved(ContainerEvent e) {
            fail(NAME + " [TEAM MEMBER REMOVED EVENT FIRED]");
        }
        public void containerClosed(final ContainerEvent e) {
            fail(NAME + " [CONTAINER CREATED EVENT FIRED]");
        }
        public void containerCreated(final ContainerEvent e) {
            fail(NAME + " [CONTAINER CREATED EVENT FIRED]");
        }
        public void containerDeleted(final ContainerEvent e) {
            fail(NAME + " [CONTAINER DELETED EVENT FIRED]");
        }
        public void containerReactivated(final ContainerEvent e) {
            fail(NAME + " [CONTAINER REACTIVATED EVENT WAS FIRED]");
        }
        public void documentAdded(final ContainerEvent e) {
            fail(NAME + " [DOCUMENT ADDED EVENT FIRED]");
        }
        public void documentRemoved(final ContainerEvent e) {
            fail(NAME + " [DOCUMENT REMOVED EVENT FIRED]");
        }
    }

}
