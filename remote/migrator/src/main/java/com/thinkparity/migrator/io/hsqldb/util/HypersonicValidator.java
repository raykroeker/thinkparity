/*
 * Feb 8, 2006
 */
package com.thinkparity.migrator.io.hsqldb.util;

import org.apache.log4j.Logger;

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

    /** An apache logger. */
    protected final Logger logger;

	/**
	 * Create a HypersonicValidator.
	 */
	private HypersonicValidator() {
        super();
        this.logger = Logger.getLogger(getClass());
    }

    private void doValidate() {
        logger.info("[VALIDATE]");
		validateSession();
		new HypersonicMigrator().migrate();
	}

	private void validateSession() {
		final HypersonicSession session = HypersonicSessionManager.openSession();
		session.close();
	}
}
