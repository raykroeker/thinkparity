/*
 * Apr 7, 2006
 */
package com.thinkparity.model.parity.model.document;

import com.thinkparity.model.parity.ParityException;

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
        Boolean isEqual = null;
        try { isEqual = datum.dModel.isWorkingVersionEqual(datum.documentId); }
        catch(final ParityException px) { fail(createFailMessage(px)); }
        assertEquals(NAME + " [IS EQUAL DOES NOT MATCH EXPECTATION]", isEqual, datum.eIsEqual);
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
        datum = new Fixture(getDocumentModel(), document.getId(), Boolean.FALSE);
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
        private final Boolean eIsEqual;
        private Fixture(final DocumentModel dModel, final Long documentId,
                final Boolean eIsEqual) {
            this.documentId = documentId;
            this.dModel = dModel;
            this.eIsEqual = eIsEqual;
        }
    }
}
