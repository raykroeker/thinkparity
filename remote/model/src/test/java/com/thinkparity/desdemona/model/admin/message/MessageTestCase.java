/*
 * Created On:  21-Nov-07 2:26:45 PM
 */
package com.thinkparity.desdemona.model.admin.message;

import com.thinkparity.desdemona.model.ModelTestCase;

/**
 * <b>Title:</b>thinkParity Desdemona Model Admin Message Test Case<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class MessageTestCase extends ModelTestCase {

    /**
     * Create MessageTestCase.
     * 
     * @param name
     *            A <code>String</code>.
     */
    protected MessageTestCase(String name) {
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

    /** <b>Title:</b>Message Test Fixture<br> */
    protected abstract class Fixture extends ModelTestCase.Fixture {}
}
