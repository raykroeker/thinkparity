/*
 * Created On:  7-Jun-07 11:05:12 AM
 */
package com.thinkparity.service.client;

import com.thinkparity.codebase.junitx.TestCase;

import com.thinkparity.service.ServiceFactory;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ServiceTestCase extends TestCase {

    /**
     * Create ServiceTestCase.
     * 
     * @param name
     *            A test name <code>String</code>.
     */
    protected ServiceTestCase(final String name) {
        super(name);
    }

    protected final ServiceFactory getServiceFactory() {
        return ClientServiceFactory.getInstance();
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
}
