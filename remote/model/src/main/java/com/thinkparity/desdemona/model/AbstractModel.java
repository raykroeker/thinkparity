/*
 * Nov 28, 2005
 */
package com.thinkparity.desdemona.model;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModel<T extends AbstractModelImpl> extends
        com.thinkparity.codebase.model.AbstractModel<T> {

    private static final Object IMPL_LOCK;

    static {
        IMPL_LOCK = new Object();
    }

	/**
	 * Create a AbstractModel.
	 */
	protected AbstractModel(final T impl) {
        super(impl);
	}

    /**
     * @see com.thinkparity.codebase.model.AbstractModel#getImplLock()
     */
    @Override
    protected Object getImplLock() {
        return IMPL_LOCK;
    }
}
