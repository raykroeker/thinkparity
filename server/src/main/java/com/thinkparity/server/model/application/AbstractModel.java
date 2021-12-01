/*
 * Nov 28, 2005
 */
package com.thinkparity.desdemona.model;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public abstract class AbstractModel<T extends AbstractModelImpl> extends
        com.thinkparity.codebase.model.AbstractModel<T> {

    /** The implementation synchronization lock. */
    private final Object implLock;

	/**
	 * Create a AbstractModel.
	 */
	protected AbstractModel(final T impl) {
        super(impl);
        this.implLock = new Object();
	}

    /**
     * @see com.thinkparity.codebase.model.AbstractModel#getImplLock()
     */
    @Override
    protected Object getImplLock() {
        return implLock;
    }
}
