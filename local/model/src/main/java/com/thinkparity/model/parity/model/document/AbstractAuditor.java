/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.document;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.audit.AuditModel;
import com.thinkparity.model.parity.model.audit.InternalAuditModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AbstractAuditor {

	private final Context context;

	/**
	 * Create a AbstractAuditor.
	 */
	protected AbstractAuditor(final Context context) {
		super();
		this.context = context;
	}

	protected InternalAuditModel getInternalAuditModel() {
		final InternalAuditModel iAModel = AuditModel.getInternalModel(context);
		return iAModel;
	}
}
