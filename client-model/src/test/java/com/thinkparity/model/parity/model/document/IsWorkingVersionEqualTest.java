/*
 * Apr 7, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IsWorkingVersionEqualTest extends DocumentTestCase {

    /** Fixture data. */
    private Set<Fixture> data;

    /**
     * Create a IsWorkingVersionEqualTest.
     * 
     */
    public IsWorkingVersionEqualTest() {
        super("IsWorkingVersionEqualTest");
    }

    /**
     * Test the IsWorkingVersionEqualTest api.
     * 
     */
    public void testIsWorkingVersionEqualTest() {
        Boolean isEqual;
        for(final Fixture datum : data) {
            try {
                isEqual = datum.dModel.isWorkingVersionEqual(datum.dId);
                assertEquals(
                        "[LMODEL] [JUNIT] [DOCUMENT] [IS WORKING VERSION EQUAL]",
                        isEqual, datum.expectedIsEqual);
            }
            catch(final ParityException px) {
                fail(createFailMessage(px));
            }
        }
    }

    /**
     * Setup the test fixture data.
     * 
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        final DocumentModel dModel = getDocumentModel();
        final SessionModel sModel = getSessionModel();
        data = new HashSet<Fixture>();

        Document d;
        final File[] inputFiles = getInputFiles();
        final File[] modFiles = getModFiles();
        Boolean isEqual;
        for(int i = 0; i < inputFiles.length; i++) {
            d = dModel.create(inputFiles[i].getName(), null, inputFiles[i]);
            if(0 == (i % 2)) {
                dModel.updateWorkingVersion(d.getId(), modFiles[i]);
                isEqual = Boolean.FALSE;
            }
            else {
                sModel.send(ModelTestUser.getJUnitX().getJabberId(), d.getId());
                isEqual = Boolean.TRUE;
            }
            data.add(new Fixture(d.getId(), dModel, isEqual));
        }
    }

    /**
     * Tear down the fixture data.
     *
     * @throws Exception
     */
    protected void tearDown() throws Exception {
        data.clear();
        data = null;
        super.tearDown();
    }

    /** Fixture definition. */
    private class Fixture {
        private final Long dId;
        private final DocumentModel dModel;
        private final Boolean expectedIsEqual;
        private Fixture(final Long dId, final DocumentModel dModel, final Boolean expectedIsEqual) {
            this.dId = dId;
            this.dModel = dModel;
            this.expectedIsEqual = expectedIsEqual;
        }
    }
}
