/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model;

import com.thinkparity.ophelia.model.audit.AuditModel;
import com.thinkparity.ophelia.model.audit.InternalAuditModel;

/**
 * An abstract auditor. This class is to be extended when implementing audit
 * capability for a concrete model.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AbstractAuditor {

	/**
	 * The parity context.
	 * 
	 */
	private final Context context;

	/**
	 * Create a AbstractAuditor.
	 * 
	 * @param context
	 *            The parity context.
	 */
	protected AbstractAuditor(final Context context) {
		super();
		this.context = context;
	}

	/**
	 * Obtain the internal audit model.
	 * 
	 * @return The internal audit model.
	 */
	protected InternalAuditModel getInternalAuditModel() {
		return AuditModel.getInternalModel(context);
	}
}
