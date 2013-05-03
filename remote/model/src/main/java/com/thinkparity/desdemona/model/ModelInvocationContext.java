/*
 * Created On:  13-Jul-07 2:45:48 PM
 */
package com.thinkparity.desdemona.model;

import java.lang.reflect.Method;

import com.thinkparity.codebase.JVMUniqueId;

/**
 * <b>Title:</b>thinKParity Desdemona Model Invocation Context<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ModelInvocationContext {

    /** The unique id . */
    private JVMUniqueId id;

    /** The method to invoke. */
    private Method method;

    /** The method parameter types. */
    private Class<?>[] parameterTypes;

    /**
     * Create ModelInvocationContext.
     *
     */
    public ModelInvocationContext() {
        super();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj)
            return false;
        if (this == obj)
            return true;
        if (getClass() == obj.getClass()) {
            return ((ModelInvocationContext) obj).id.equals(id);
        } else {
            return false;
        }
    }

    /**
     * Obtain id.
     *
     * @return A JVMUniqueId.
     */
    public final JVMUniqueId getId() {
        return id;
    }

    /**
     * Obtain method.
     *
     * @return A Method.
     */
    public final Method getMethod() {
        return method;
    }

    /**
     * Obtain args.
     *
     * @return A Object[].
     */
    public final Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Set id.
     *
     * @param id
     *		A JVMUniqueId.
     */
    public final void setId(final JVMUniqueId id) {
        this.id = id;
    }

    /**
     * Set method.
     *
     * @param method
     *		A Method.
     */
    public final void setMethod(final Method method) {
        this.method = method;
    }

    /**
     * Set args.
     *
     * @param args
     *		A Object[].
     */
    public final void setParameterTypes(final Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    /**
     * @see java.lang.Object#toString()
     *
     */
    @Override
    public String toString() {
        final StringBuilder buffer =
            new StringBuilder(method.getDeclaringClass().getSimpleName())
            .append('#').append(method.getName())
            .append('(');
        for (int i = 0; i < parameterTypes.length; i++) {
            if (0 < i) {
                buffer.append(',');
            }
            buffer.append(parameterTypes[i].getSimpleName());
        }
        buffer.append(')');
        return buffer.toString();
    }
}
