/*
 * Created On:  9-Oct-07 1:48:38 PM
 */
package com.thinkparity.desdemona.model.admin.derby;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.service.AuthToken;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ArchiveTest extends DerbyTestCase {

    /** A test name. */
    private static final String NAME = "Admin derby archive test";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create ArchiveTest.
     *
     */
    public ArchiveTest() {
        super(NAME);
    }

    /**
     * Test the archive functionality.
     * 
     */
    public void testArchive() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test derby archive.");
        setUpArchive();
        try {
            datum.newDerbyModel(datum.archive).archive();
        } finally {
            tearDownArchive();
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture();
        datum.archive = datum.login(User.THINKPARITY.getSimpleUsername());
    }

    /**
     * Set up create.
     * 
     */
    protected void setUpArchive() {
    }

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum.archive = null;

        super.tearDown();
    }

    /**
     * Tear down create.
     * 
     */
    protected void tearDownArchive() {
    }

    /** <b>Title:</b>Archive Test Fixture<br> */
    private class Fixture extends DerbyTestCase.Fixture {
        /** The authentication token to archive as. */
        private AuthToken archive;
    }
}
