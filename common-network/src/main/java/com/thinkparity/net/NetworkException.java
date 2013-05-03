/*
 * Created On:  19-Aug-07 7:09:48 PM
 */
package com.thinkparity.net;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NetworkException extends Exception {

    /**
     * Extract the network exception.
     * 
     * @param error
     *            A <code>Throwable</code>.
     * @return A <code>NetworkException</code>.
     */
    public static NetworkException getNetworkException(final Throwable error) {
        if (InvocationTargetException.class.isAssignableFrom(error.getClass())) {
            return getNetworkException(((InvocationTargetException) error).getTargetException());
        }
        if (UndeclaredThrowableException.class.isAssignableFrom(error.getClass())) {
            return getNetworkException(((UndeclaredThrowableException) error).getUndeclaredThrowable());
        }
        return (NetworkException) error;
    }

    /**
     * Determine if a network exception is assignable from the error.
     * 
     * @param error
     *            A <code>Throwable</code>.
     * @return True if the error is:
     *         <ul>
     *         <li>An invocation target exception where the target is a network
     *         exception.
     *         <li>An undeclared throwable exception where the undeclared
     *         throwable is a network exception.
     *         <li>A network exception.
     */
    public static Boolean isAssignableFrom(final Throwable error) {
        if (InvocationTargetException.class.isAssignableFrom(error.getClass())) {
            return isAssignableFrom(((InvocationTargetException) error).getTargetException());
        }
        if (UndeclaredThrowableException.class.isAssignableFrom(error.getClass())) {
            return isAssignableFrom(((UndeclaredThrowableException) error).getUndeclaredThrowable());
        }
        return Boolean.valueOf(NetworkException.class.isAssignableFrom(error.getClass()));
    }

    /**
     * Create NetworkException.
     * 
     * @param message
     *            A <code>String</code>.
     */
    public NetworkException(final String message) {
        super(message);
    }

    /**
     * Create NetworkException.
     * 
     * @param cause
     *            A cause <code>Throwable</code>.
     */
    public NetworkException(final Throwable cause) {
        super(cause);
    }
}
