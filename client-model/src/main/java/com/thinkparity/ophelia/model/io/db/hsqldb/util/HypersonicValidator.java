/*
 * Feb 8, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.util;

import com.thinkparity.codebase.StackUtil;

import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.migrator.hsqldb.HypersonicMigrator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicValidator {

	/**
     * Create HypersonicValidator.
     *
	 */
	public HypersonicValidator() {
		super();
	}

    /**
     * Validate the hypersonic schema.
     *
     */
	public void validate() {
		validateSession();
		new HypersonicMigrator().migrate();
	}

    /**
     * Validate that a session can be established.
     * 
     */
	private void validateSession() {
		new SessionManager().openSession(StackUtil.getExecutionPoint()).close();
	}
}
