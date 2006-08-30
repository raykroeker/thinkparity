/*
 * Apr 1, 2006
 */
package com.thinkparity.codebase.email;

/**
 * The error thrown by the EMailBuilder's parse api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * @see EMailBuilder#parse(String)
 */
public class EMailFormatException extends IllegalArgumentException {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

    /**
     * Create a EMailFormatException.
     * 
     * @param email
     *            The malformed e-mail address.
     */
    EMailFormatException(final String email) {
        super(email);
    }

    /**
     * Create a EMailFormatException.
     * 
     * @param error
     *            The error message.
     * @param email
     *            The malformed e-mail address.
     */
    EMailFormatException(final String error, final String email) {
        super(error + ":  "  + email);
    }
}
