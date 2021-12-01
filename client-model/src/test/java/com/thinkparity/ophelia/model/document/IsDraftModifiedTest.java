/*
 * Apr 7, 2006
 */
package com.thinkparity.ophelia.model.document;

import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;


/**
 * @author raymond@raykroeker.com
 * @version $Revision$
 */
public class IsDraftModifiedTest extends DocumentTestCase {

    /** Test name. */
    private static final String NAME = "[[TEST IS DRAFT MODIFIED]";

    /** Fixture datum. */
    private Fixture datum;

    /** Create IsWorkingVersionEqualTest. */
    public IsDraftModifiedTest() { super(NAME); }

    /**
     * Test the is draft modified api.
     * 
     */
    public void testIsDraftModified() {
        final Boolean isDraftModified = datum.dModel.isDraftModified(datum.documentId);
        assertEquals("[IS DRAFT MODIFIED DOES NOT MATCH EXPECTATION]",
                isDraftModified, datum.isDraftModified);
    }

    /**
     * Setup the test fixture data.
     * 
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        final Document document = createDocument(OpheliaTestUser.JUNIT, getInputFiles()[0]);
        modifyDocument(OpheliaTestUser.JUNIT, document.getId());
        datum = new Fixture(getDocumentModel(OpheliaTestUser.JUNIT), document.getId(), Boolean.TRUE);
    }

    /**
     * Tear down the fixture data.
     *
     * @throws Exception
     */
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }

    /** Fixture definition. */
    private class Fixture {
        private final DocumentModel dModel;
        private final Long documentId;
        private final Boolean isDraftModified;
        private Fixture(final DocumentModel dModel, final Long documentId,
                final Boolean isDraftModified) {
            this.documentId = documentId;
            this.dModel = dModel;
            this.isDraftModified = isDraftModified;
        }
    }
}
