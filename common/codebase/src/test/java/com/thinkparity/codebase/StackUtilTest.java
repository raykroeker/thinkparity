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
        final StackTraceElement executionPoint = StackUtil.getExecutionPoint();
        final String executionPointClassAndMethodName = StackUtil.getExecutionPointClassAndMethodName();
        final String executionPointClassName = StackUtil.getExecutionPointClassName();
        final int executionPointLineNumber = StackUtil.getExecutionPointLineNumber();
        final String executionPointMethodName = StackUtil.getExecutionPointMethodName();
        final String executionPointFormattedText = StackUtil.formatExecutionPoint();
        assertEquals("doTestGetCallerMethod", executionPoint.getMethodName());
        assertEquals("com.thinkparity.codebase.StackUtilTest.doTestGetCallerMethod", executionPointClassAndMethodName);
        assertEquals("com.thinkparity.codebase.StackUtilTest", executionPointClassName);
        assertEquals(47, executionPointLineNumber);
        assertEquals("doTestGetCallerMethod", executionPointMethodName);
        assertEquals("com.thinkparity.codebase.StackUtilTest.doTestGetCallerMethod(StackUtilTest.java:49)", executionPointFormattedText);

        final StackTraceElement caller = StackUtil.getCaller();
        final String callerClassAndMethodName = StackUtil.getCallerClassAndMethodName();
        final String callerClassName = StackUtil.getCallerClassName();
        final int callerLineNumber = StackUtil.getCallerLineNumber();
        final String callerMethodName = StackUtil.getCallerMethodName();
        final String callerFormattedText = StackUtil.formatCaller();
        assertEquals("testGetCallerMethod", caller.getMethodName());
        assertEquals("com.thinkparity.codebase.StackUtilTest.testGetCallerMethod", callerClassAndMethodName);
        assertEquals("com.thinkparity.codebase.StackUtilTest", callerClassName);
        assertEquals(23, callerLineNumber);
        assertEquals("testGetCallerMethod", callerMethodName);
        assertEquals("com.thinkparity.codebase.StackUtilTest.testGetCallerMethod(StackUtilTest.java:23)", callerFormattedText);
	}
}
