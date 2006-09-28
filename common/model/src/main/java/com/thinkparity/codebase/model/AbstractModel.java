/*
 * Created On: Sep 16, 2006 1:53:22 PM
 */
package com.thinkparity.codebase.model;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractModel<T extends AbstractModelImpl> {

    /** A stack filter for the logger. */
    private static final StackUtil.Filter STACK_FILTER;

    static {
        STACK_FILTER = new StackUtil.Filter() {
            public Boolean accept(final StackTraceElement stackElement) {
                return !"getImpl".equals(stackElement.getMethodName()) &&
                    !"getImplLock".equals(stackElement.getMethodName());
            }
        };
    }

    /** A thinkParity model context. */
    private final Context context;

    /** A thinkParity model implementation. */
    private final T impl;

    /** An apache logger. */
    private final Log4JWrapper logger;

    /** Create AbstractModel. */
    protected AbstractModel(final T impl) {
        super();
        this.context = new Context(this);
        this.context.assertIsValid();
        this.impl = impl;
        this.impl.setContext(context);
        this.logger = new Log4JWrapper();
    }

    /**
     * Obtain the implementation.
     * 
     * @return A thinkParity model implementation.
     */
    protected final T getImpl() {
        logger.logApiId(STACK_FILTER);
        return impl;
    }

    /**
     * Obtain the implementation synchronization lock.
     * 
     * @return An implementation synchronization lock.
     */
    protected abstract Object getImplLock();
}
