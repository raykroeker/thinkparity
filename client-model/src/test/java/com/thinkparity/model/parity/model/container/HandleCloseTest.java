/*
 * Created On: Fri May 05 2006 09:36 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import java.util.Calendar;
import java.util.Set;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.api.events.ContainerListener;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * Test the parity document implementation handle close api.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class HandleCloseTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [HANDLE CLOSE TEST]";

    /** The test datum. */
	private Fixture datum;
	
	/** Create HandleCloseTest. */
	public HandleCloseTest() { super(NAME); }

    /** Test the parity container interface handle close api. */ 
	public void testHandleClose() {
        Set<User> team;
        try { datum.cInternalModel.handleClose(datum.containerId, datum.closedBy, datum.closedOn); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        final Container container = datum.cInternalModel.read(datum.containerId);
        assertNotNull(NAME, container);
        assertEquals(NAME + " [CONTAINER STATE DOES NOT MATCH EXPECTATION]", container.getState(), ArtifactState.CLOSED);

        team = datum.aModel.readTeam(datum.containerId);
        assertNotNull(NAME, team);
        assertEquals(NAME + " [TEAM SIZE DOES NOT MATCH EXPECTATION]", datum.eTeamSize.intValue(), team.size());
	}

	/**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
        login();
		final ContainerModel cModel = getContainerModel();
        final ArtifactModel aModel = getArtifactModel();
        final ModelTestUser jUnitX = ModelTestUser.getX();

        final Container container = createContainer(NAME);
        final Document document = addDocument(container, getInputFiles()[0]);
        addTeam(container);
        modifyDocument(document);
        cModel.publish(container.getId());
        aModel.sendKey(container.getId(), jUnitX.getJabberId());
        datum = new Fixture(aModel, jUnitX.getJabberId(), currentDateTime(),
                getInternalContainerModel(), container.getId(),
                aModel.readTeam(container.getId()).size());
	}

    /**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		getContainerModel().removeListener(datum);
		datum = null;
        logout();
		super.tearDown();
	}

	private class Fixture implements ContainerListener {
        private final ArtifactModel aModel;
        private final InternalContainerModel cInternalModel;
        private final JabberId closedBy;
		private final Calendar closedOn;
		private final Long containerId;
        private final Integer eTeamSize;
		private Fixture(final ArtifactModel aModel, final JabberId closedBy,
                final Calendar closedOn,
                final InternalContainerModel cInternalModel,
                final Long containerId, final Integer eTeamSize) {
            this.aModel = aModel;
            this.closedBy = closedBy;
            this.closedOn = closedOn;
			this.cInternalModel = cInternalModel;
			this.containerId = containerId;
            this.eTeamSize = eTeamSize;
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
            fail(NAME + " [CONTAINER REACTIVATED EVENT FIRED]");
        }
        public void documentAdded(final ContainerEvent e) {
            fail(NAME + " [DOCUMENT ADDED EVENT FIRED]");
        }
        public void documentRemoved(final ContainerEvent e) {
            fail(NAME + " [DOCUMENT REMOVED EVENT FIRED]");
        }
	}
}
