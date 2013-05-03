/*
 * Created On:  2-Aug-07 1:54:35 PM
 */
package com.thinkparity.desdemona;

import com.thinkparity.codebase.junitx.TestCase;

/**
 * <b>Title:</b>thinkParity Desdemona Test Case<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class DesdemonaTestCase extends TestCase {

    /**
     * Create DesdemonaTestCase.
     *
     */
    protected DesdemonaTestCase(final String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
