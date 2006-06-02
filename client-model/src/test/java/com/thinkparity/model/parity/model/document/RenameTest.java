/*
 * Created On: Thu Jun 01 2006 17:15 PDT
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
 * Test the parity document interface rename api.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class RenameTest extends DocumentTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [DOCUMENT] [RENAME TEST]";

    /** The test data. */
	private List<Fixture> data;
	
	/** Create CloseTest. */
	public RenameTest() { super(NAME); }

    /** Test the parity document interface close api. */ 
	public void testRename() {
        testLogger.info(NAME + " [TEST RENAME]");
        Document document;
        DocumentVersion documentVersion;
        for(final Fixture datum : data) {
            try { datum.dModel.rename(datum.documentId, datum.documentName); }
            catch(final ParityException px) { fail(createFailMessage(px)); }

            document = null;
            documentVersion = null;
            try {
                document = datum.dModel.get(datum.documentId);
                documentVersion = datum.dModel.listVersions(datum.documentId).iterator().next();
            }
            catch(final ParityException px) { fail(createFailMessage(px)); }

            assertEquals(NAME + " [TEST RENAME] [DOCUMENT NAME DOES NOT MATCH EXPECTATION]", datum.documentName, document.getName());
            assertEquals(NAME + " [TEST RENAME] [DOCUMENT VERSION NAME DOES NOT MATCH EXPECTATION]", datum.documentName, documentVersion.getName());
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

        // 0:  a new document with a name
        final File file0 = getInputFiles()[0];
        final Document d0 = dModel.create(file0.getName(), file0.getName(), file0);
        data.add(new Fixture(dModel, d0.getId(), "New " + file0.getName()));
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
		private final DocumentModel dModel;
		private final Long documentId;
        private final String documentName;
		private Fixture(final DocumentModel dModel, final Long documentId,
                final String documentName) {
			this.dModel = dModel;
			this.documentId = documentId;
            this.documentName = documentName;
		}
	}
}
