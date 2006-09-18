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

	/**
	 * Create a AbstractModel.
	 */
	protected AbstractModel(final T impl) {
        super(impl);
	}
}
