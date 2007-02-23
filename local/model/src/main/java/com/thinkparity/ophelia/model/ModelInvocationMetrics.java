/*
 * Created On:  21-Feb-07 3:06:37 PM
 */
package com.thinkparity.ophelia.model;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b>thinkParity OpheliaModel Invocation Metrics<br>
 * <b>Description:</b>Used to record the duration of each invocation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ModelInvocationMetrics {

    /** A metrics <code>Log4JWrapper</code>. */
    private static final Log4JWrapper LOGGER;

    /** A map of all of the context measures. */
    private static final Map<Object, Measure> MEASURES;

    static {
        LOGGER = new Log4JWrapper("METRIX_DEBUGGER");
        MEASURES = new Hashtable<Object, Measure>(1, 1.0F);
    }

    /**
     * Begin the metrics for the context.
     * 
     * @param context
     *            A <code>Method</code> context.
     */
    static void begin(final Method context) {
        if (MEASURES.containsKey(context))
            MEASURES.remove(context);
        MEASURES.put(context, captureMeasure());
    }

    /**
     * End the metrics for the context.
     * 
     * @param context
     *            A <code>Method</code> context.
     */
    static void end(final Method context) {
        final Measure begin = MEASURES.get(context);
        final Measure end = captureMeasure();
        LOGGER.logDebug("{0}#{1}{3}  {2} ms{3}  {4} free{3}  {5} max{3}  {6} total",
                context.getDeclaringClass().getSimpleName(), context.getName(),
                end.currentTimeMillis - begin.currentTimeMillis,
                Separator.SystemNewLine,
                Math.abs(end.freeMemory - begin.freeMemory),
                Math.abs(end.maxMemory - begin.maxMemory),
                Math.abs(end.totalMemory - begin.totalMemory));
    }

    private static Measure captureMeasure() {
        final Runtime runtime = Runtime.getRuntime();
        return new Measure(System.currentTimeMillis(), runtime.freeMemory(),
                runtime.maxMemory(), runtime.totalMemory());
    }

    /**
     * Create ModelInvocationTiming.
     *
     */
    private ModelInvocationMetrics() {
        super();
    }

    /**
     * <b>Title:</b>Model Invocation Metrics Measure<br>
     * <b>Description:</b>A measure used to store metric data.<br>
     */
    private static class Measure {
        private final long currentTimeMillis;
        private final long freeMemory;
        private final long maxMemory;
        private final long totalMemory;
        private Measure(final long currentTimeMillis, final long freeMemory,
                final long maxMemory, final long totalMemory) {
            super();
            this.currentTimeMillis = currentTimeMillis;
            this.freeMemory = freeMemory;
            this.maxMemory = maxMemory;
            this.totalMemory = totalMemory;
        }
    }
}
