/*
 * Created On:  21-Feb-07 3:06:37 PM
 */
package com.thinkparity.ophelia.model.util.service;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b>thinkParity Service Client Proxy Metrics<br>
 * <b>Description:</b>Used to record the duration of each invocation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ServiceProxyMetrics {

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
    static Context begin(final Method method, final Integer invocation) {
        final Context context = new Context(method, invocation);
        MEASURES.put(context, captureMeasure());
        return context;
    }

    /**
     * End the metrics for the context.
     * 
     * @param context
     *            A <code>Method</code> context.
     */
    static void end(final Context context) {
        final Measure begin = MEASURES.remove(context);
        final Measure end = captureMeasure();
        final String id = new StringBuilder(context.method.getDeclaringClass().getSimpleName())
            .append('#').append(context.method.getName())
            .toString();
        LOGGER.logDebug("{0};{1};{2};{3};{4};{5};{6};{7}",
                id,                                                 // id
                end.currentTimeMillis - begin.currentTimeMillis,    // duration
                end.freeMemory,                                     // free
                end.maxMemory,                                      // max
                end.totalMemory,                                    // total
                end.freeMemory - begin.freeMemory,                  // free mem delta
                end.maxMemory - begin.maxMemory,                    // max mem delta
                end.totalMemory - begin.totalMemory,                // total mem delta
                context.invocation);                                // invocation
    }

    /**
     * Capture a measure.
     * 
     * @return A <code>Measure</code>.
     */
    private static Measure captureMeasure() {
        final Runtime runtime = Runtime.getRuntime();
        return new Measure(System.currentTimeMillis(), runtime.freeMemory(),
                runtime.maxMemory(), runtime.totalMemory());
    }

    /**
     * Create ServiceProxyMetrics.
     *
     */
    private ServiceProxyMetrics() {
        super();
    }

    /** <b>Title:</b>Metric Context<br> */
    static class Context {

        /** An invocation count. */
        private final Integer invocation;

        /** A method. */
        private final Method method;

        /**
         * Create Context.
         * 
         * @param method
         *            A <code>Method</code>.
         * @param invocation
         *            An <code>Invocation</code>.
         */
        private Context(final Method method, final Integer invocation) {
            super();
            this.method = method;
            this.invocation = invocation;
        }
    }

    /**
     * <b>Title:</b>Service Proxy Metrics Measure<br>
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
