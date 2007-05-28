/*
 * Created On:  23-May-07 4:30:23 PM
 */
package com.thinkparity.ophelia.model.help;

import com.thinkparity.ophelia.OpheliaTestUser;


/**
 * <b>Title:</b>thinkParity OpheliaModel ReadTest<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadTopicTest extends HelpTestCase {

    /** A test case name <code>String</code>. */
    private static final String NAME = "Help read topic test";

    /** The test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create TestRead.
     *
     */
    public ReadTopicTest() {
        super(NAME);
    }

    /**
     * Test the help read api.
     *
     */
    public void testReadTopic() {
        final HelpTopic topic = getHelpModel(datum.junit).readTopic(1000L);
        assertNotNull("Help topic is null.", topic);

        final HelpTopic nullTopic = getHelpModel(datum.junit).readTopic(-1L);
        assertNull("Help topic is not null.", nullTopic);
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

        datum = null;
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
