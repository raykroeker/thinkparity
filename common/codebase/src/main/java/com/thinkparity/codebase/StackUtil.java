/*
 * Created On:  Feb 9, 2006
 * $Id$
 */
package com.thinkparity.codebase;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


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

    /** A filter to remove StackUtil. */
    private static final Filter STACK_UTIL_FILTER = new Filter() {
        public Boolean accept(final StackTraceElement stackElement) {
            return !stackElement.getClassName().equals(StackUtil.class.getName());
        }};

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
     * Obtain the caller's file name.
     * 
     * @return A file name <code>String</code>.
     */
    public static String getCallerFileName() {
        return getFrameFileName(CALLER_DEPTH);
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
    public static StackTraceElement getExecutionPoint() {
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
     * Obtain a filtered stack frame.
     * 
     * @param filters
     *            A list of stack filters.
     * @return A stack trace element.
     */
    public static StackTraceElement getFrame(final List<StackUtil.Filter> filters) {
        final ArrayList<Filter> internalFilters =
            new ArrayList<Filter>(filters.size() + 1);
        internalFilters.add(STACK_UTIL_FILTER);
        internalFilters.addAll(filters);
        final StackTraceElement[] stack = createFilteredStack(
                internalFilters.toArray(new Filter[] {}));
        return 0 < stack.length ? stack[0] : null;
    }

    /**
     * Obtain a filtered stack frame.
     * 
     * @param filter
     *            A <code>StackUtil.Filter</code>.
     * @return A stack trace element.
     */
    public static StackTraceElement getFrame(final StackUtil.Filter filter) {
        final List<StackUtil.Filter> filters = new ArrayList<StackUtil.Filter>(1);
        filters.add(filter);
        return getFrame(filters);
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
     * Obtain the frame's file name.
     * 
     * @param depth
     *            The depth of the frame.
     * @return The file name; or null if the frame does not exist.
     */
    public static String getFrameFileName(final Integer depth) {
        final StackTraceElement frame = getFrame(depth);
        return null == frame ? null : frame.getFileName();
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
        return createFilteredStack(new Filter[] {STACK_UTIL_FILTER});
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
        Boolean doFilter;
        for (final StackTraceElement stackElement : stack) {
            // if any filter rejects the element; do not include it in the list
            doFilter = Boolean.FALSE;
            for (final Filter filter : filters) {
                if (!filter.accept(stackElement)) {
                    doFilter = Boolean.TRUE;
                    break;
                }
            }
            if (!doFilter) {
                filteredStack.add(stackElement);
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
    public interface Filter {

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
