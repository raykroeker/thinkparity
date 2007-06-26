/*
 * Created On: Sep 16, 2006 1:54:18 PM
 */
package com.thinkparity.codebase.model;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractModelImpl {

    /** An apache logger wrapper. */
    protected final Log4JWrapper logger;

    private Context context;

    /**
     * Create AbstractModelImpl.
     * 
     * 
     */
    protected AbstractModelImpl() {
        super();
        this.logger = new Log4JWrapper(getClass());
    }

    /**
     * Set context.
     *
     * @param context The Context.
     */
    public final void setContext(final Context context) {
        this.context = context;
    }

    /**
     * Obtain the context
     *
     * @return The Context.
     */
    protected final Context getContext() {
        return context;
    }

    /**
     * Panic. Nothing can be done about the error that has been generated. An
     * appropriate error is constructed suitable for throwing beyond the model
     * interface.
     * 
     * @param t
     *            A <code>Throwable</code>.
     * @return A <code>RuntimeException</code>.
     */
    protected ThinkParityException panic(final Throwable t) {
        if (ThinkParityException.class.isAssignableFrom(t.getClass())) {
            return (ThinkParityException) t;
        } else {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, "{0}", errorId);
            return new ThinkParityException(errorId.toString(), t);
        }
    }
}
