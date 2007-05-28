/*
 * Created On:  23-May-07 4:30:23 PM
 */
package com.thinkparity.ophelia.model.help;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.ophelia.OpheliaTestUser;


/**
 * <b>Title:</b>thinkParity OpheliaModel ReadTest<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class IndexTest extends HelpTestCase {

    /** A test case name <code>String</code>. */
    private static final String NAME = "Help rebuild index test";

    /** The test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create TestRead.
     *
     */
    public IndexTest() {
        super(NAME);
    }

    /**
     * Test the help read api.
     *
     */
    public void testReadTopics() {
        getHelpModel(datum.junit).index();

        final List<Long> searchResult = getHelpModel(datum.junit).searchTopics("topic 1000");
        assertNotNull("Help topic search result is null.", searchResult);
        assertEquals("Help topic search result does not match expectation.",
                1, searchResult.size());
        final List<HelpTopic> helpTopics = new ArrayList<HelpTopic>(searchResult.size());
        for (final Long topicId : searchResult) {
            helpTopics.add(getHelpModel(datum.junit).readTopic(topicId));
        }
        assertEquals("Help topic search result size does not match expectation.",
                helpTopics.size(), searchResult.size());
        for (final HelpTopic helpTopic : helpTopics) {
            assertNotNull("Help topic search result was null.", helpTopic);
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
