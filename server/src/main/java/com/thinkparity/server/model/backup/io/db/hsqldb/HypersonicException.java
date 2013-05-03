/*
 * Feb 8, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicException extends RuntimeException {

    /**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

    /**
     * Translate an error into a hypersonic error.
     * 
     * @param errorId
     *            An error id.
     * @param cause
     *            The cause of the error.
     * @return A hypersonic error.
     */
    public static HypersonicException translate(final String errorId,
            final Throwable cause) {
        return new HypersonicException(errorId, cause);
    }

    /**
     * Translate an error into a hypersonic error.
     * 
     * @param errorId
     *            An error id.
     * @param cause
     *            The cause of the error.
     * @return A hypersonic error.
     */
    public static HypersonicException translate(final String errorId) {
        return new HypersonicException(errorId);
    }

	/**
	 * Create a HypersonicException.
	 * @param message The error message.
	 */
	public HypersonicException(final String message) { super(message); }

	/**
	 * Create a HypersonicException.
	 * 
	 * @param cause
	 *            The cause of the error.
	 */
	public HypersonicException(final Throwable cause) { super(cause); }

    /**
     * Create HypersonicException.
     * 
     * @param errorId
     *            An error id <code>String</code>.
     * @param cause
     *            The error cause <code>Throwable</code>.
     */
    private HypersonicException(final String errorId, final Throwable cause) {
        super(errorId, cause);
    }
}
