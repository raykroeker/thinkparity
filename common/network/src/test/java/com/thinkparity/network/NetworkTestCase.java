/*
 * Created On:  20-Aug-07 8:55:50 AM
 */
package com.thinkparity.network;

import com.thinkparity.codebase.junitx.TestCase;

/**
 * <b>Title:</b>thinkParity Network Test Case<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class NetworkTestCase extends TestCase {

    /**
     * Create NetworkTestCase.
     * 
     * @param name
     *            A test name <code>String</code>.
     */
    public NetworkTestCase(final String name) {
        super(name);
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** <b>Title:</b>Network Test Case Fixture<br> */
    protected abstract class Fixture {}
}
