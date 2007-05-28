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
public final class ReadContentTest extends HelpTestCase {

    /** A test case name <code>String</code>. */
    private static final String NAME = "Help read test";

    /** The test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create TestRead.
     *
     */
    public ReadContentTest() {
        super(NAME);
    }

    /**
     * Test the help read api.
     *
     */
    public void testReadTopics() {
        final List<HelpTopic> topics = getHelpModel(datum.junit).readTopics();
        assertNotNull("Help topics is null.", topics);
        assertEquals("Help topics size does not match expectation.", 1, topics.size());
        HelpContent content;
        for (final HelpTopic topic : topics) {
            content = getHelpModel(datum.junit).readContent(topic.getId());
            assertNotNull("Help topic content is null.", content);
        }
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
