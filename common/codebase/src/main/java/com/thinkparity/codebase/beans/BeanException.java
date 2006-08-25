/*
 * Created On: Aug 25, 2006 9:46:40 AM
 */
package com.thinkparity.codebase.beans;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class BeanException extends RuntimeException {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Create BeanException. */
    BeanException(final String message) {
        super(message);
    }

    /** Create BeanException. */
    BeanException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /** Create BeanException. */
    BeanException(final Throwable cause) {
        super(cause);
    }
}
