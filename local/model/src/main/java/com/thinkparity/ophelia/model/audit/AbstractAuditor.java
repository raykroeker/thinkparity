/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.audit;

import com.thinkparity.ophelia.model.InternalModelFactory;

/**
 * An abstract auditor. This class is to be extended when implementing audit
 * capability for a concrete model.
 * 
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class AbstractAuditor {

    /** A thinkParity <code>InternalModelFactory</code>. */
    private final InternalModelFactory modelFactory;

	/**
     * Create AbstractAuditor.
     * 
     * @param modelFactory
     *            A thinkParity <code>InternalModelFactory</code>.
     */
	protected AbstractAuditor(final InternalModelFactory modelFactory) {
		super();
		this.modelFactory = modelFactory;
	}

    /**
     * Obtain the internal audit model.
     * 
     * @return The internal audit model.
     */
    protected InternalAuditModel getAuditModel() {
        return modelFactory.getAuditModel();
    }

    /**
     * Obtain the internal audit model.
     * 
     * @return The internal audit model.
     */
    protected InternalAuditModel getInternalAuditModel() {
        return getAuditModel();
    }
}
