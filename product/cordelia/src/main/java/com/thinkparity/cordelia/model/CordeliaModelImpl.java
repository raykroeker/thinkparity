/**
 * 
 */
package com.thinkparity.cordelia.model;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.assertion.Assertion;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.AbstractModelImpl;

import com.thinkparity.cordelia.CordeliaException;

/**
 * @author raymond
 */
public abstract class CordeliaModelImpl extends AbstractModelImpl {

    /** An apache logger wrapper. */
    protected final Log4JWrapper logger;

    /**
     * Create CordeliaModelImpl.
     * 
     */
    protected CordeliaModelImpl() {
        super();
        this.logger = new Log4JWrapper();
    }

    /**
     * Translate an error into a parity unchecked error.
     * 
     * @param t
     *            An error.
     */
    protected RuntimeException translateError(final Throwable t) {
        if (CordeliaException.class.isAssignableFrom(t.getClass())) {
            return (CordeliaException) t;
        } else if (Assertion.class.isAssignableFrom(t.getClass())) {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, "{0}", errorId);
            return (Assertion) t;
        }
        else {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, "{0}", errorId);
            return CordeliaException.translate(getContext(), errorId, t);
        }
    }
}
