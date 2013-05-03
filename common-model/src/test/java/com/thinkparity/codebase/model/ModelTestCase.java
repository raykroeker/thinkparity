package com.thinkparity.codebase.model;

import com.thinkparity.codebase.junitx.TestCase;

public abstract class ModelTestCase extends TestCase {

    protected ModelTestCase(final String name) {
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

    protected static abstract class Fixture {}
}