/*
 * Created On:  23-May-07 4:30:23 PM
 */
package com.thinkparity.ophelia.model.help;

import java.util.List;

import com.thinkparity.ophelia.OpheliaTestUser;


/**
 * <b>Title:</b>thinkParity OpheliaModel ReadTest<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadTest extends HelpTestCase {

    /** A test case name <code>String</code>. */
    private static final String NAME = "Help read test";

    /** The test datum <code>Fixture</code>. */
    private Fixture datum;

    /** An instance of <code>InternalHelpModel</code>. */
    private InternalHelpModel helpModel;

    /**
     * Create TestRead.
     *
     */
    public ReadTest() {
        super(NAME);
    }

    /**
     * Test the help read api.
     *
     */
    public void testRead() {
        final List<HelpTopic> helpTopics = getHelpModel(datum.junit).readHelpTopics();
    }

    /**
     * @see com.thinkparity.ophelia.model.help.HelpTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture(OpheliaTestUser.JUNIT);
    }

    /**
     * @see com.thinkparity.ophelia.model.help.HelpTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <b>Title:</b>Help Read Test Fixture<br>
     * <b>Description:</b><br>
     * 
     */
    private class Fixture extends HelpTestCase.Fixture {

        /** An <code>OpheliaTestUser</code>. */
        private final OpheliaTestUser junit;

        /**
         * Create Fixture.
         * 
         * @param junit
         *            An <code>OpheliaTestUser</code>.
         */
        private Fixture(final OpheliaTestUser junit) {
            super();
            this.junit = junit;
        }
    }
}
