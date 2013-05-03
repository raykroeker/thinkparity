/*
 * Created On:  9-Oct-07 1:48:38 PM
 */
package com.thinkparity.desdemona.model.admin.report;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdmona Model Admin Report Generate Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class GenerateTest extends ReportTestCase {

    /** A test name. */
    private static final String NAME = "Admin report generate test";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create GenerateTest.
     *
     */
    public GenerateTest() {
        super(NAME);
    }

    /**
     * Test the generate functionality.
     * 
     */
    public void testGenerate() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test report generate.");
        setUpGenerate();
        try {
            datum.newReportModel(datum.generate).generate();
        } finally {
            tearDownGenerate();
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
        datum.generate = datum.login(User.THINKPARITY.getSimpleUsername());
    }

    /**
     * Set up create.
     * 
     */
    protected void setUpGenerate() {
    }

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum.generate = null;

        super.tearDown();
    }

    /**
     * Tear down create.
     * 
     */
    protected void tearDownGenerate() {
    }

    /** <b>Title:</b>Generate Test Fixture<br> */
    private class Fixture extends ReportTestCase.Fixture {
        /** The authentication token to generate as. */
        private AuthToken generate;
    }
}
