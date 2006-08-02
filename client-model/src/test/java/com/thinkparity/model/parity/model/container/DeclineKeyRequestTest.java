/*
 * Created On: Jul 5, 2006 10:31:56 AM
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.api.events.ContainerListener;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.KeyRequest;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raymond@thinkparity.com
 * @version 
 */
public class DeclineKeyRequestTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [TEST DECLINE KEY REQUEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create DeclineKeyRequestTest. */
    public DeclineKeyRequestTest() { super(NAME); }

    /**
     * Test the send key api.
     *
     */
    public void testDeclineKeyRequest() {
        // send the key
        try { datum.cModel.declineKeyRequest(datum.keyRequestId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
        // ensure key holder is not updated
        JabberId keyHolder = null;
        try { keyHolder = datum.aModel.readKeyHolder(datum.containerId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
        assertNotNull(NAME, keyHolder);
        assertEquals(NAME, datum.eKeyHolder, keyHolder);
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
        final ModelTestUser jUnit = ModelTestUser.getJUnit();
        final ModelTestUser jUnitY = ModelTestUser.getY();
        final Container container = createContainer(NAME);
        addDocument(container, getInputFiles()[0]);
        addTeam(container);
        final KeyRequest keyRequest = requestKey(container, jUnitY);
        datum = new Fixture(aModel, cModel, container.getId(),
                jUnit.getJabberId(), keyRequest.getId());
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
        private final JabberId eKeyHolder;
        private final Long keyRequestId;
        private Fixture(final ArtifactModel aModel,
                final ContainerModel cModel, final Long containerId,
                final JabberId eKeyHolder, final Long keyRequestId) {
            this.aModel = aModel;
            this.cModel = cModel;
            this.containerId = containerId;
            this.eKeyHolder = eKeyHolder;
            this.keyRequestId = keyRequestId;
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
