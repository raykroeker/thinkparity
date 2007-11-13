/*
 * Created On:  13-Nov-07 11:47:42 AM
 */
package com.thinkparity.desdemona.model.session;

import com.thinkparity.desdemona.model.ModelTestCase;

/**
 * <b>Title:</b>thinkParity Desdemona Model Session Test Case<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class SessionTestCase extends ModelTestCase {

    /**
     * Create SessionTestCase.
     *
     * @param name
     */
    public SessionTestCase(final String name) {
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

    /** <b>Title:</b>Session Test Fixture<br> */
    protected class Fixture extends ModelTestCase.Fixture {}
}
