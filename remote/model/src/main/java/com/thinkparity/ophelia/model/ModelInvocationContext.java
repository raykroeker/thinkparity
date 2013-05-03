/*
 * Created On:  15-Mar-07 12:49:37 PM
 */
package com.thinkparity.ophelia.model;

import java.lang.reflect.Method;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ModelInvocationContext {

    /**
     * Obtain the invocation method arguments.
     * 
     * @return An <code>Object[]</code>.
     */
    public Object[] getArguments();

    /**
     * Obtain the invocation method.
     * 
     * @return A <code>Method</code>.
     */
    public Method getMethod();
}
