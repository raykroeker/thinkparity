/*
 * Created On: Sep 16, 2006 1:53:22 PM
 */
package com.thinkparity.codebase.model;

/**
 * <b>Title:</b>thinkParity Model<br>
 * <b>Description:</b>A thinkParity model defines the proxy interface to a
 * business logic implementation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @param <T>
 *            A typed proxy implementation.
 */
public abstract class AbstractModel<T extends AbstractModelImpl> {

    /** A thinkParity model context. */
    private final Context context;

    /** A thinkParity model implementation. */
    private final T impl;

    /**
     * Create AbstractModel.
     * 
     * @param impl
     *            The abstract model implmentation.
     */
    protected AbstractModel(final T impl) {
        super();
        this.context = new Context();
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
