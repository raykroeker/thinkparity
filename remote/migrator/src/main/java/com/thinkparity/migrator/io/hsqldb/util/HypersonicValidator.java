/*
 * Feb 8, 2006
 */
package com.thinkparity.migrator.io.hsqldb.util;

import com.thinkparity.migrator.io.hsqldb.HypersonicException;
import com.thinkparity.migrator.io.hsqldb.HypersonicSession;
import com.thinkparity.migrator.io.hsqldb.HypersonicSessionManager;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicValidator {

    /**
     * Validate the remote migrator's hypersonic schema.
     * 
     * @throws HypersonicException
     */
	public static void validate() throws HypersonicException {
        new HypersonicValidator().doValidate();
    }

	/**
	 * Create a HypersonicValidator.
	 */
	private HypersonicValidator() { super(); }

    private void doValidate() {
		validateSession();
		new HypersonicMigrator().migrate();
	}

	private void validateSession() {
		final HypersonicSession session = HypersonicSessionManager.openSession();
		session.close();
	}
}
