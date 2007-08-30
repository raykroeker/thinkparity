/*
 * Created On:  23-May-07 4:28:32 PM
 */
package com.thinkparity.ophelia.model.help;

import com.thinkparity.ophelia.model.ModelTestCase;

/**
 * <b>Title:</b>thinkParity OpheliaModel Help Test Case Abstraction<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class HelpTestCase extends ModelTestCase {

    protected static void assertNotNull(final String assertion, final HelpTopic topic) {
        assertNotNull(assertion + " (reference)", (Object) topic);
        assertNotNull(assertion + " (id)", topic.getId());
        assertNotNull(assertion + " (movie)", topic.getMovies());
        assertNotNull(assertion + " (name)", topic.getName());
    }

    protected static void assertNotNull(final String assertion, final HelpContent content) {
        assertNotNull(assertion + " (reference)", (Object) content);
        assertNotNull(assertion + " (id)", content.getId());
        assertNotNull(assertion + " (content)", content.getContent());
    }

    /**
     * Create HelpTestCase.
     * 
     * @param name
     *            A test name <code>String</code>.
     */
    public HelpTestCase(final String name) {
        super(name);
    }

    /**
     * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <b>Title:</b>Help Test Case Fixture Abstraction<br>
     * <b>Description:</b><br>
     * 
     */
    protected abstract class Fixture extends ModelTestCase.Fixture {

        /**
         * Create Fixture.
         *
         */
        protected Fixture() {
            super();
        }
    }
}
