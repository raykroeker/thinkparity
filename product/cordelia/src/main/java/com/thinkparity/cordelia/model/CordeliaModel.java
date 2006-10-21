/**
 * 
 */
package com.thinkparity.cordelia.model;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.AbstractModel;

/**
 * @author raymond
 */
public abstract class CordeliaModel<T extends CordeliaModelImpl> extends
        AbstractModel<T> {

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

    /** The implementation synchronization lock. */
    private final Object lock;

    /** An apache logger wrapper. */
    private final Log4JWrapper logger;

    /**
     * Create CordeliaModel.
     * 
     * @param impl
     *            A cordelia model implementation.
     */
    protected CordeliaModel(final T impl) {
        super(impl);
        this.lock = new Object();
        this.logger = new Log4JWrapper();
    }

    /**
     * @see com.thinkparity.codebase.model.AbstractModel#getImpl()
     * 
     */
    @Override
    protected T getImpl() {
        logger.logApiId(STACK_FILTER);
        return super.getImpl();
    }

    /**
     * @see com.thinkparity.codebase.model.AbstractModel#getImplLock()
     * 
     */
    @Override
    protected Object getImplLock() {
        return lock;
    }
}
