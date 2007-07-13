/*
 * Created On:  21-Feb-07 3:06:37 PM
 */
package com.thinkparity.ophelia.model;

import java.lang.reflect.Method;

/**
 * <b>Title:</b>thinkParity OpheliaModel Invocation Metrics<br>
 * <b>Description:</b>Used to record the duration of each invocation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ModelInvocationMetrics {

    /**
     * Begin the metrics for the context.
     * 
     * @param context
     *            A <code>Method</code> context.
     */
    static void begin(final Method context) {
    }

    /**
     * End the metrics for the context.
     * 
     * @param context
     *            A <code>Method</code> context.
     */
    static void end(final Method context) {
    }
}
