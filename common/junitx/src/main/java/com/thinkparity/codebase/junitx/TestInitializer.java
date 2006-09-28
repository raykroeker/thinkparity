/*
 * Created On: Sep 13, 2006 9:28:47 AM
 */
package com.thinkparity.codebase.junitx;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class TestInitializer {

    /** A singleton instance. */
    private static final TestInitializer SINGLETON;

    static {
        SINGLETON = new TestInitializer();
    }

    /**
     * Initialize the test framework.
     * 
     * @param testSession
     *            The test session.
     */
    static void initialize(final TestSession testSession) {
        SINGLETON.doInitialize(testSession);
    }

    /** Create TestInitializer. */
    private TestInitializer() {
        super();
    }

    /**
     * Initialize the test framework.
     * 
     * @param testSession
     *            The test session.
     */
    private void doInitialize(final TestSession testSession) {
    }
}
