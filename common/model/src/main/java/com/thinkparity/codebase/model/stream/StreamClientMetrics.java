/*
 * Created On:  21-Feb-07 3:06:37 PM
 */
package com.thinkparity.codebase.model.stream;

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
class StreamClientMetrics {

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
     *            A <code>StreamSession</code> context.
     */
    static void begin(final StreamSession context) {
        MEASURES.put(context, captureMeasure());
    }

    /**
     * End the metrics for the context.
     * 
     * @param context
     *            A <code>Method</code> context.
     */
    static void end(final String httpMethod, final StreamSession context) {
        final Measure begin = MEASURES.remove(context);
        final Measure end = captureMeasure();
        LOGGER.logDebug("{0};{1};{2};{3};{4};{5};{6};{7}",
                httpMethod,                                         // id
                end.currentTimeMillis - begin.currentTimeMillis,    // duration
                end.freeMemory,                                     // free
                end.maxMemory,                                      // max
                end.totalMemory,                                    // total
                end.freeMemory - begin.freeMemory,                  // free mem delta
                end.maxMemory - begin.maxMemory,                    // max mem delta
                end.totalMemory - begin.totalMemory);               // total mem delta
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
    private StreamClientMetrics() {
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
