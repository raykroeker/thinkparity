/*
 * Created On:  Feb 9, 2006
 * $Id$
 */
package com.thinkparity.codebase;

import java.text.MessageFormat;
import java.util.ArrayList;


/**
 * Utility methods for accessing stack trace items.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class StackUtil {

    /** The depth of the immediate caller. */
    private static final Integer CALLER_DEPTH = 1;

    /** The depth of the execution point. */
    private static final Integer EXECUTION_POINT_DEPTH = 0;

    /**
     * Obtain the caller's formatted text.
     * 
     * @return The formatted text.
     */
    public static String formatCaller() {
        return formatFrame(CALLER_DEPTH);
    }

    /**
     * Obtain the execution point's formatted text.
     * 
     * @return The formatted text.
     */
    public static String formatExecutionPoint() {
        return formatFrame(EXECUTION_POINT_DEPTH);
    }

    /**
     * Obtain the frame's formatted value.
     * 
     * @param depth
     *            The depth of the frame to retrieve.
     * @return The formatted text.
     */
    public static String formatFrame(final Integer depth) {
        final StackTraceElement frame = getFrame(depth);
        if(null == frame) { return null; }
        else {
            return MessageFormat.format(
                    "{0}.{3}({1}:{2})",
                    new Object[] {frame.getClassName(), frame.getFileName(),
                            frame.getLineNumber(), frame.getMethodName()});
        }
    }

    /**
     * Obtain the caller stack frame.
     * 
     * @return A stack trace element.
     */
    public static StackTraceElement getCaller() {
        return getFrame(CALLER_DEPTH);
    }

    /**
	 * Obtain the caller's class and method name.
	 * 
	 * @return The class and method name.
	 */
	public static String getCallerClassAndMethodName() {
		return getFrameClassAndMethodName(CALLER_DEPTH);
	}

    /**
     * Obtain the caller's class name.
     * 
     * @return The class name.
     */
    public static String getCallerClassName() {
        return getFrameClassName(CALLER_DEPTH);
    }

    /**
     * Obtain the caller's line number.
     * 
     * @return The line number.
     */
    public static int getCallerLineNumber() {
        return getFrameLineNumber(CALLER_DEPTH);
    }

	/**
	 * Obtain the caller's method name.
	 * 
	 * @return The name of the immediate caller.
	 */
	public static String getCallerMethodName() {
		return getFrameMethodName(CALLER_DEPTH);
	}

    /**
     * Obtain the execution point's stack frame.
     * 
     * @return A stack trace element.
     */
    public static StackTraceElement getExcutionPoint() {
        return getFrame(EXECUTION_POINT_DEPTH);
    }

    /**
     * Obtain the execution point's class and method name.
     * 
     * @return The class and method name.
     */
    public static String getExecutionPointClassAndMethodName() {
        return getFrameClassAndMethodName(EXECUTION_POINT_DEPTH);
    }

    /**
     * Obtain the execution point's class name.
     * 
     * @return The class name.
     */
    public static String getExecutionPointClassName() {
        return getFrameClassName(EXECUTION_POINT_DEPTH);
    }

    /**
     * Obtain the execution point's line number.
     * 
     * @return The line number.
     */
    public static int getExecutionPointLineNumber() {
        return getFrameLineNumber(EXECUTION_POINT_DEPTH);
    }

    /**
     * Obtain the execution point's method name.
     * 
     * @return The method name.
     */
    public static String getExecutionPointMethodName() {
        return getFrameMethodName(EXECUTION_POINT_DEPTH);
    }

	/**
     * Obtain a stack frame.
     * 
     * @param depth
     *            The depth of the frame to retrieve.
     * @return A stack trace element.
     */
    public static StackTraceElement getFrame(final Integer depth) {
        final StackTraceElement[] stack = createFilteredStack();
        return depth < stack.length ? stack[depth] : null;
    }

	/**
	 * Obtain the frame's class and method name.
	 * 
	 * @param depth
	 *            The frame depth.
	 * @return The class and method name.
	 */
	public static String getFrameClassAndMethodName(final Integer depth) {
        final StackTraceElement frame = getFrame(depth);
        if(null == frame) { return null; }
        else {
            return new StringBuffer(frame.getClassName())
                    .append(".").append(frame.getMethodName())
                    .toString();
        }
	}

    /**
     * Obtain the frame's class name.
     * 
     * @param depth
     *            The depth of the frame.
     * @return The class name; or null if the frame does not exist.
     */
    public static String getFrameClassName(final Integer depth) {
        final StackTraceElement frame = getFrame(depth);
        return null == frame ? null : frame.getClassName();
    }

	/**
     * Obtain the frame's line number
     * 
     * @param depth
     *            The depth of the frame.
     * @return The line number; or null if the frame does not exist.
     */
    public static int getFrameLineNumber(final Integer depth) {
        final StackTraceElement frame = getFrame(depth);
        return null == frame
                ? null : frame.getLineNumber();
    }

    /**
     * Obtain the frame's method name.
     * 
     * @param depth
     *            The depth of the frame.
     * @return The frame's method name; or null if the depth does not exist.
     */
    public static String getFrameMethodName(final Integer depth) {
        final StackTraceElement frame = getFrame(depth);
        return null == frame
                ? null : frame.getMethodName();
    }

	/**
     * Create a filtered stack. The stack returned will not inlcude any
     * references to StackUtil.
     * 
     * @return A stack trace.
     */
    private static StackTraceElement[] createFilteredStack() {
        return createFilteredStack(new Filter[] {new Filter() {
            public Boolean accept(final StackTraceElement stackElement) {
                return !stackElement.getClassName().equals(StackUtil.class.getName());
            }
        }});
    }

    /**
     * Create a filtered stack.
     * 
     * @param filters
     *            A list of filters to apply to the stack.
     * @return A stack trace.
     */
    private static StackTraceElement[] createFilteredStack(final Filter[] filters) {
        final ArrayList<StackTraceElement> filteredStack = new ArrayList<StackTraceElement>();
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        for(final StackTraceElement stackElement : stack) {
            for(final Filter filter : filters) {
                if(filter.accept(stackElement)) {
                    filteredStack.add(stackElement);
                }
            }
        }
        return filteredStack.toArray(new StackTraceElement[] {});
    }

    /**
	 * Create a StackUtil.
	 * 
	 */
	private StackUtil() { super(); }

    /**
     * An interface used to filter the stack trace.
     * 
     * @author raymond@thinkparity.com
     * @version $Revision$
     */
    private interface Filter {

        /**
         * Determine whether or not to filter the stack trace element.
         * 
         * @param stackElement
         *            A stack trace element.
         * @return True if the element should be accepted.
         */
        Boolean accept(final StackTraceElement stackElement);
    }
}
