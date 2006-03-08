/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model;

import com.thinkparity.model.parity.model.index.IndexModel;
import com.thinkparity.model.parity.model.index.InternalIndexModel;

/**
 * An abstract indexor. This class is to be extended when implementing index
 * capability for a concrete artifact model.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractIndexor {

	/**
	 * The parity context.
	 * 
	 */
	private final Context context;

	/**
	 * Create a AbstractIndexor.
	 * 
	 * @param context
	 *            The parity context.
	 */
	protected AbstractIndexor(final Context context) {
		super();
		this.context = context;
	}

	/**
	 * Obtain the internal index model.
	 * 
	 * @return The internal index model.
	 */
	protected InternalIndexModel getInternalIndexModel() {
		return IndexModel.getInternalModel(context);
	}
}
