/*
 * Created On: Sep 16, 2006 1:53:22 PM
 */
package com.thinkparity.codebase.model;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractModel<T extends AbstractModelImpl> {

    /** A thinkParity model context. */
    private final Context context;

    /** A thinkParity model implementation. */
    private final T impl;

    /** Create AbstractModel. */
    protected AbstractModel(final T impl) {
        super();
        this.context = new Context(this);
        this.context.assertIsValid();
        this.impl = impl;
        this.impl.setContext(context);
    }

    /**
     * Obtain the implementation.
     * 
     * @return A thinkParity model implementation.
     */
    protected T getImpl() {
        return impl;
    }

    /**
     * Obtain the implementation synchronization lock.
     * 
     * @return An implementation synchronization lock.
     */
    protected abstract Object getImplLock();
}
