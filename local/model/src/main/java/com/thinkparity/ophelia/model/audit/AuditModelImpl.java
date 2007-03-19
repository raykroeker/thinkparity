/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.audit;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Audit Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public final class AuditModelImpl extends Model implements
        AuditModel, InternalAuditModel {

    /**
	 * Create a AuditModelImpl.
	 * 
	 */
	public AuditModelImpl() {
		super();
	}

	/**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
    }
}
