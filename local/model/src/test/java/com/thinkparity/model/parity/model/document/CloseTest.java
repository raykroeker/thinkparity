/*
 * Created On: Feb 21, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.xmpp.user.User;

/**
 * Test the parity document interface close api.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CloseTest extends DocumentTestCase {

    /** The test data. */
	private List<Fixture> data;
	
	/** Create CloseTest. */
	public CloseTest() { super("[LMODEL] [DOCUMENT] [CLOSE TEST]"); }

    /** Test the parity document interface close api. */ 
	public void testClose() {
        testLogger.info("[LMODEL] [DOCUMENT] [TEST CLOSE]");
        Document document;
        Set<User> team;
        for(final Fixture datum : data) {
            try { datum.dModel.close(datum.documentId); }
            catch(final ParityException px) { fail(createFailMessage(px)); }

            document = null;
            try { document = datum.dModel.get(datum.documentId); }
            catch(final ParityException px) { fail(createFailMessage(px)); }
            assertNotNull("[LMODEL] [DOCUMENT] [TEST CLOSE] [DOCUMENT IS NULL]", document);
            assertEquals("[LMODEL] [DOCUMENT] [TEST CLOSE] [NON-CLOSED DOCUMENT STATE]", document.getState(), ArtifactState.CLOSED);

            team = datum.aModel.readTeam(datum.documentId);
            assertNotNull("[LMODEL] [DOCUMENT] [TEST CLOSE] [TEAM IS NULL]", team);
            assertEquals("[LMODEL] [DOCUMENT] [TEST CLOSE] [NON-ZERO TEAM SIZE]", team.size(), 0);
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
        final SessionModel sModel = getSessionModel();
        final ModelTestUser userX = ModelTestUser.getX();

        // 2 scenarios
        // 0:  i am the document key holder
        final File file0 = getInputFiles()[0];
        final Document d0 = dModel.create(file0.getName(), file0.getName(), file0);
        addTeam(d0);
        modifyDocument(d0);
        dModel.publish(d0.getId());
        data.add(new Fixture(aModel, dModel, d0.getId()));
        // 1:  i am not the document key holder
        final File file1 = getInputFiles()[1];
        final Document d1 = dModel.create(file1.getName(), file1.getName(), file1);
        addTeam(d1);
        modifyDocument(d1);
        dModel.publish(d1.getId());
        sModel.sendKeyResponse(d1.getId(), userX.getJabberId(), KeyResponse.ACCEPT);
        data.add(new Fixture(aModel, dModel, d1.getId()));
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
		private final DocumentModel dModel;
		private final Long documentId;
		private Fixture(final ArtifactModel aModel, final DocumentModel dModel,
                final Long documentId) {
            this.aModel = aModel;
			this.dModel = dModel;
			this.documentId = documentId;
		}
	}
}
