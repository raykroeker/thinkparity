/*
 * Nov 5, 2003
 */
package com.thinkparity.codebase;

import com.thinkparity.codebase.junitx.TestCase;

/**
 * Codebase abstract test case.
 * 
 * @author raymond@raykroeker.com
 * @version 1.3.2.6
 */
public abstract class CodebaseTestCase extends TestCase {

    protected static abstract class Fixture {}

	/**
     * Assert that two byte arrays are equal.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected byte array.
     * @param actual
     *            The actual byte array.
     */
    protected static void assertEquals(final String assertion,
            final byte[] expected, final byte[] actual) {
        assertEquals(new StringBuffer(assertion).append(" [BYTE ARRAY LENGTH DOES NOT MATCH EXPECTATION]").toString(), expected.length, actual.length);
        for(int i = 0; i < expected.length; i++) {
            assertEquals(new StringBuffer(assertion).append(" [BYTE AT POSITION ").append(i).append(" DOES NOT MATCH EXPECTATION]").toString(), expected[i], actual[i]);
        }
    }

    /**
	 * Create a new CodebaseTestCase
	 * 
	 * @param name
	 *            The test name.
	 */
	protected CodebaseTestCase(String name) { super(name); }
}
