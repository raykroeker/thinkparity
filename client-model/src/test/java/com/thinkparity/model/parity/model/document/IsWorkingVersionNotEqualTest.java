/*
 * Apr 7, 2006
 */
package com.thinkparity.model.parity.model.document;


/**
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class IsWorkingVersionNotEqualTest extends DocumentTestCase {

    /** Test name. */
    private static final String NAME = "[LMODEL] [DOCUMENT] [TEST IS WORKING VERSION EQUAL]";

    /** Fixture datum. */
    private Fixture datum;

    /** Create IsWorkingVersionEqualTest. */
    public IsWorkingVersionNotEqualTest() { super(NAME); }

    /**
     * Test the IsWorkingVersionEqualTest api.
     * 
     */
    public void testIsWorkingVersionEqualTest() {
        final Boolean isDraftModified = datum.dModel.isDraftModified(datum.documentId);
        assertEquals(NAME + " [IS EQUAL DOES NOT MATCH EXPECTATION]", isDraftModified, datum.isDraftModified);
    }

    /**
     * Setup the test fixture data.
     * 
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        final Document document = createDocument(getInputFiles()[0]);
        modifyDocument(document);
        datum = new Fixture(getDocumentModel(), document.getId(), Boolean.TRUE);
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
