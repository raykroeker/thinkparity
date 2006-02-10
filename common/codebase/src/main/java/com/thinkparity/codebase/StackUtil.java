/*
 * Feb 9, 2006
 */
package com.thinkparity.codebase;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class StackUtil {

	/**
	 * Obtain the name of the immediate caller from the stack.
	 * 
	 * @return The name of the immediate caller.
	 */
	public static String getCallerMethodName() {
		return getCallerMethodName(3);
	}

	/**
	 * Obtain the class name "." the method name.
	 * 
	 * @return The class name "." the method name.
	 */
	public static String getCallerClassAndMethodName() {
		return getCallerClassAndMethodName(3);
	}

	/**
	 * Obtain the class name "." the method name at the given level.
	 * 
	 * @param level
	 *            The level.
	 * @return The class name "." the method name.
	 */
	public static String getCallerClassAndMethodName(final Integer level) {
		final StackTraceElement[] stack = new Throwable().getStackTrace();
		if(null == stack) { return null; }
		else {
			if(stack.length > level) {
				return new StringBuffer(stack[level].getClassName())
					.append(".")
					.append(stack[level].getMethodName())
					.toString();
			}
			else { return null; }
		}
	}

	/**
	 * Obtain the caller method name at the given level.
	 * 
	 * @param level
	 *            The level.
	 * @return The caller method name.
	 */
	public static String getCallerMethodName(final Integer level) {
		final StackTraceElement[] stack = new Throwable().getStackTrace();
		if(null == stack) { return null; }
		else {
			if(stack.length > level) { return stack[level].getMethodName(); }
			else { return null; }
		}
	}

	/**
	 * Create a StackUtil.
	 * 
	 */
	private StackUtil() { super(); }
}
