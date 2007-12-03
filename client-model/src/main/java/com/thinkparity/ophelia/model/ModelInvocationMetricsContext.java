/*
 * Created On:  3-Dec-07 2:30:23 PM
 */
package com.thinkparity.ophelia.model;

import java.lang.reflect.Method;

import com.thinkparity.codebase.HashCodeUtil;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ModelInvocationMetricsContext {

    /** A synchronization lock. */
    private final Object lock;

    /** A method. */
    private final Method method;

    /**
     * Create ModelInvocationMetricsContext.
     *
     */
    public ModelInvocationMetricsContext(final Object lock, final Method method) {
        super();
        this.lock = lock;
        this.method = method;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() == obj.getClass()) {
            return ((ModelInvocationMetricsContext) obj).lock.equals(lock)
                && ((ModelInvocationMetricsContext) obj).method.equals(method);
        } else {
            return false;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        return HashCodeUtil.hashCode(lock, method);
    }

    /**
     * @see java.lang.Object#toString()
     *
     */
    @Override
    public String toString() {
        return new StringBuilder(method.getDeclaringClass().getSimpleName())
            .append("#").append(method.getName())
            .toString();
    }
}
