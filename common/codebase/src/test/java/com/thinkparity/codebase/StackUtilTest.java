/*
 * Feb 9, 2006
 */
package com.thinkparity.codebase;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class StackUtilTest extends CodebaseTestCase {

	/**
	 * Create a StackUtilTest.
	 * 
	 */
	public StackUtilTest() { super("Stack Util Test"); }

	/**
	 * Test the StackUtil getCallerMethodName api.
	 *
	 */
	public void testGetCallerMethod() {
		doTestGetCallerMethod();
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {}

	/**
	 * Use the stack util to get the immediate caller which should be the
	 * test method above.
	 *
	 */
	private void doTestGetCallerMethod(){ 
		final String methodName = StackUtil.getCallerMethodName();
		assertEquals("testGetCallerMethod", methodName);
		final String classAndMethodName = StackUtil.getCallerClassAndMethodName();
		assertEquals("com.thinkparity.codebase.StackUtilTest.testGetCallerMethod", classAndMethodName);
	}
}
