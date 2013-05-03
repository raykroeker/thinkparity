/*
 * Feb 8, 2006
 */
package com.thinkparity.desdemona.model.io.hsqldb;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Hypersonic Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class HypersonicException extends RuntimeException {

    /** A mapping of the logical error name to the db specific sql state. */
    private static final Map<String, String> SQL_STATE_REGISTRY;

    static {
        /* DERBY - HypersonicException#<cinit> - SQL State Registry
         * REF - http://db.apache.org/derby/docs/10.2/ref/refderby.pdf */
        SQL_STATE_REGISTRY = new HashMap<String, String>(1, 1.0F);
        SQL_STATE_REGISTRY.put("isDuplicateKey", "23505");
    }

    /**
     * Determine if the error is a duplicate key violation.
     * 
     * @param error
     *            A <code>HypersonicException</code>.
     * @return True if the cause is a duplicate key violation.
     */
    public static Boolean isDuplicateKey(final HypersonicException error) {
        final Throwable cause = error.getCause();
        if (null == cause) {
            return Boolean.FALSE;
        } else {
            if (isIntegrityConstraintViolation(cause)) {
                return isStateEqual((SQLException) cause, "isDuplicateKey");
            } else {
                return false;
            }
        }
    }

    /**
     * Determine if the cause is an integrity constraint violation.
     * 
     * @param cause
     *            A <code>Throwable</code>.
     * @return True if it is assignable from
     *         <code>SQLIntegrityConstraintViolationException</code>.
     */
    private static Boolean isIntegrityConstraintViolation(final Throwable cause) {
        return SQLIntegrityConstraintViolationException.class.isAssignableFrom(
                cause.getClass());
    }

    /**
     * Determine if the state of the error is equal.
     * 
     * @param error
     *            A <code>SQLException</code>.
     * @param state
     *            A <code>String</code>.
     * @return True if the states match.
     */
    private static Boolean isStateEqual(final SQLException error, final String state) {
        final String sqlState = SQL_STATE_REGISTRY.get(state);
        return null == sqlState ? Boolean.FALSE : sqlState.equals(error.getSQLState());
    }

	/**
     * Create HypersonicException.
     * 
     * @param message
     *            An error message.
     */
	public HypersonicException(final String message) {
        super(message);
	}

    /**
     * Create HypersonicException.
     * 
     * @param message
     *            An error message.
     * @param messageArguments
     *            An error message arguments.
     */
    public HypersonicException(final String message,
            final Object... messageArguments) {
        super(new MessageFormat(message).format(messageArguments));
    }

    /**
	 * Create a HypersonicException.
	 * 
	 * @param cause
	 *            The cause of the error.
	 */
	public HypersonicException(final Throwable cause) {
        super(cause);
    }

    /**
     * Create HypersonicException.
     * 
     * @param cause
     *            An error cause.
     * @param message
     *            An error message.
     * @param messageArguments
     *            An error message arguments.
     */
    public HypersonicException(final Throwable cause, final String message,
            final Object... messageArguments) {
        super(new MessageFormat(message).format(messageArguments), cause);
    }
}
