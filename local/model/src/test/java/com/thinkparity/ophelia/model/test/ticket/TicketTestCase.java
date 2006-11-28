/*
 * Created On:  24-Nov-06 6:19:39 PM
 */
package com.thinkparity.ophelia.model.test.ticket;

import com.thinkparity.ophelia.model.ModelTestCase;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class TicketTestCase extends ModelTestCase {

    /**
     * Create TicketTestCase.
     *
     * @param name
     */
    public TicketTestCase(final String name) {
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
     * <b>Title:</b>thinkParity Scenario Test Case Fixture<br>
     * <b>Description:</b>Provides no functionality.<br>
     * 
     * @author raymond@thinkparity.com
     * @version 1.1.2.1
     */
    protected abstract class Fixture extends ModelTestCase.Fixture {}
}
