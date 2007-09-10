/*
 * Created On:  8-Sep-07 4:14:51 PM
 */
package com.thinkparity.desdemona.model.ticket;

import com.thinkparity.desdemona.model.ModelTestCase;

/**
 * <b>Title:</b>thinkParity Desdemona Model Ticket Test Case<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class TicketTestCase extends ModelTestCase {

    /**
     * Create TicketTestCase.
     *
     */
    public TicketTestCase(final String name) {
        super(name);

    }

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** <b>Title:</b>Ticket Test Fixture<br> */
    protected class Fixture extends ModelTestCase.Fixture {}
}
