/*
 * Created On:  21-Feb-07 3:06:37 PM
 */
package com.thinkparity.ophelia.model;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

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
        final long begin = System.currentTimeMillis();
        MEASURES.put(context, new Measure(begin));
    }

    /**
     * End the metrics for the context.
     * 
     * @param context
     *            A <code>Method</code> context.
     */
    static void end(final Method context) {
        final long end = System.currentTimeMillis();
        final Measure measure = MEASURES.get(context);
        LOGGER.logDebug("{0}#{1} {2} ms",
                context.getDeclaringClass().getSimpleName(), context.getName(),
                end - measure.begin);
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
        final long begin;
        private Measure(final long begin) {
            super();
            this.begin = begin;
        }
    }
}
