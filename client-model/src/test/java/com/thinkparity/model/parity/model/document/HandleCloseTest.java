/*
 * Created On: Fri May 05 2006 09:36 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * Test the parity document implementation handle close api.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HandleCloseTest extends DocumentTestCase {

    /** The test data. */
	private List<Fixture> data;
	
	/** Create HandleCloseTest. */
	public HandleCloseTest() { super("[LMODEL] [DOCUMENT] [HANDLE CLOSE TEST]"); }

    /** Test the parity document interface close api. */ 
	public void testHandleClose() {
        testLogger.info("[LMODEL] [DOCUMENT] [TEST HANDLE CLOSE]");
        Document document;
        Set<User> team;
        for(final Fixture datum : data) {
            try { datum.dMImpl.handleClose(datum.documentId, datum.closedBy); }
            catch(final ParityException px) { fail(createFailMessage(px)); }

            document = null;
            try { document = datum.dModel.get(datum.documentId); }
            catch(final ParityException px) { fail(createFailMessage(px)); }
            assertNotNull("[LMODEL] [DOCUMENT] [TEST CLOSE] [DOCUMENT IS NULL]", document);
            assertEquals("[LMODEL] [DOCUMENT] [TEST CLOSE] [NON-CLOSED DOCUMENT STATE]", document.getState(), ArtifactState.CLOSED);

            team = datum.aModel.readTeam(datum.documentId);
            assertNotNull("[LMODEL] [DOCUMENT] [TEST CLOSE] [TEAM IS NULL]", team);
            assertEquals("[LMODEL] [DOCUMENT] [TEST CLOSE] [NON-ZERO TEAM SIZE]", datum.eTeamSize.intValue(), team.size());
        }
	}

	/**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new LinkedList<Fixture>();
        final ArtifactModel aModel = getArtifactModel();
		final DocumentModel dModel = getDocumentModel();
		final DocumentModelImpl dMImpl = getImpl();
        final SessionModel sModel = getSessionModel();
        final ModelTestUser userX = ModelTestUser.getX();

        // 1 scenario
        final File file0 = getInputFiles()[0];
        final Document d0 = dModel.create(file0.getName(), file0.getName(), file0);
        addTeam(d0);
        modifyDocument(d0);
        dModel.publish(d0.getId());
        sModel.sendKeyResponse(d0.getId(), userX.getJabberId(), KeyResponse.ACCEPT);
        data.add(new Fixture(aModel, userX.getJabberId(), dMImpl, dModel, d0.getId(), aModel.readTeam(d0.getId()).size()));
	}

	/**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
		super.tearDown();
	}

	private class Fixture {
        private final ArtifactModel aModel;
        private final JabberId closedBy;
		private final DocumentModelImpl dMImpl;
        private final DocumentModel dModel;
		private final Long documentId;
        private final Integer eTeamSize;
		private Fixture(final ArtifactModel aModel, final JabberId closedBy,
                final DocumentModelImpl dMImpl, final DocumentModel dModel,
                final Long documentId, final Integer eTeamSize) {
            this.aModel = aModel;
            this.closedBy = closedBy;
			this.dMImpl = dMImpl;
            this.dModel = dModel;
			this.documentId = documentId;
            this.eTeamSize = eTeamSize;
		}
	}
}
