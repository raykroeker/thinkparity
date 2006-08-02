/*
 * Created On: Jul 5, 2006 10:31:56 AM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import java.util.List;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.api.events.ContainerListener;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.KeyRequest;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class SendKeyTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [TEST SEND KEY]";

    /** Test datum. */
    private Fixture datum;

    /** Create SendKeyTest. */
    public SendKeyTest() { super(NAME); }

    /**
     * Test the send key api.
     *
     */
    public void testSendKey() {
        // send the key
        try { datum.cModel.sendKey(datum.containerId, datum.jabberId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
        // ensure key holder is updated
        JabberId keyHolder = null;
        try { keyHolder = datum.aModel.readKeyHolder(datum.containerId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
        assertNotNull(NAME, keyHolder);
        assertEquals(NAME, datum.jabberId, keyHolder);
        // ensure no pending requests
        final List<KeyRequest> keyRequests = datum.cModel.readKeyRequests(datum.containerId);
        assertEquals(NAME + " [PENDING KEY REQUESTS STILL EXIST]", 0, keyRequests.size());
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     * 
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login();
        final ArtifactModel aModel = getArtifactModel();
        final ContainerModel cModel = getContainerModel();
        final ModelTestUser jUnitX = ModelTestUser.getX();
        final ModelTestUser jUnitY = ModelTestUser.getY();
        final Container container = createContainer(NAME);
        addDocument(container, getInputFiles()[0]);
        addTeam(container);
        requestKey(container, jUnitY);
        datum = new Fixture(aModel, cModel, container.getId(), jUnitX.getJabberId());
        cModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     * 
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
        private final ArtifactModel aModel;
        private final ContainerModel cModel;
        private final Long containerId;
        private final JabberId jabberId;
        private Fixture(final ArtifactModel aModel,
                final ContainerModel cModel, final Long containerId,
                final JabberId jabberId) {
            this.aModel = aModel;
            this.cModel = cModel;
            this.containerId = containerId;
            this.jabberId = jabberId;
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
        public void containerClosed(ContainerEvent e) {
            fail(NAME + " [CONTAINER CLOSED EVENT FIRED]");
        }
        public void containerCreated(ContainerEvent e) {
            fail(NAME + " [CONTAINER CREATED EVENT FIRED]");
        }
        public void containerDeleted(ContainerEvent e) {
            fail(NAME + " [CONTAINER DELETED EVENT FIRED]");
        }
        public void containerReactivated(ContainerEvent e) {
            fail(NAME + " [CONTAINER REACTIVATED EVENT FIRED]");
        }
        public void documentAdded(ContainerEvent e) {
            fail(NAME + " [DOCUMENT ADDED EVENT FIRED]");
        }
        public void documentRemoved(ContainerEvent e) {
            fail(NAME + " [DOCUMENT REMOVED EVENT FIRED]");
        }
    }
}
