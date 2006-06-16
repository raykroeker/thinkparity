/*
 * Created On: Feb 21, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Set;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.xmpp.user.User;

/**
 * Test the parity document interface close api.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CloseTest extends DocumentTestCase {

    /** The test data. */
	private Fixture datum;
	
	/** Create CloseTest. */
	public CloseTest() { super("[LMODEL] [DOCUMENT] [CLOSE TEST]"); }

    /** Test the parity document interface close api. */ 
	public void testClose() {
        testLogger.info("[LMODEL] [DOCUMENT] [TEST CLOSE]");
        try { datum.dModel.close(datum.documentId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }

        // the local document remains intact
        Document document = null;
        try { document = datum.dModel.get(datum.documentId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
        assertNotNull("[LMODEL] [DOCUMENT] [TEST CLOSE] [DOCUMENT IS NULL]", document);
        assertEquals("[LMODEL] [DOCUMENT] [TEST CLOSE] [NON-CLOSED DOCUMENT STATE]", document.getState(), ArtifactState.CLOSED);

        // the local team remains intact
        final Set<User> team = datum.aModel.readTeam(datum.documentId);
        assertNotNull("[LMODEL] [DOCUMENT] [TEST CLOSE] [TEAM IS NULL]", team);
        assertEquals("[LMODEL] [DOCUMENT] [TEST CLOSE] [NON-ZERO TEAM SIZE]", datum.eTeamSize.intValue(), team.size());
	}

	/**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
        final ArtifactModel aModel = getArtifactModel();
		final DocumentModel dModel = getDocumentModel();

        final File file0 = getInputFiles()[0];
        final Document d0 = dModel.create(file0.getName(), file0.getName(), file0);
        addTeam(d0);
        modifyDocument(d0);
        dModel.publish(d0.getId());
        datum = new Fixture(aModel, dModel, d0.getId(), aModel.readTeam(d0.getId()).size());
	}

	/**
	 * @see com.thinkparity.model.parity.model.document.DocumentTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		datum = null;
		super.tearDown();
	}

	private class Fixture {
        private final ArtifactModel aModel;
		private final DocumentModel dModel;
		private final Long documentId;
        private final Integer eTeamSize;
		private Fixture(final ArtifactModel aModel, final DocumentModel dModel,
                final Long documentId, final Integer eTeamSize) {
            this.aModel = aModel;
			this.dModel = dModel;
			this.documentId = documentId;
            this.eTeamSize = eTeamSize;
		}
	}
}
