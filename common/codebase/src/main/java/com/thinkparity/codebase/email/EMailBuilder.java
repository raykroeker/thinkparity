/*
 * Apr 1, 2006
 */
package com.thinkparity.codebase.email;

/**
 * A singleton builder object user to create EMail objects by parsing text.  The
 * basis for parsing is {@link http://tools.ietf.org/html/rfc2822 RFC2822}.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * @see EMail
 */
public class EMailBuilder {

	private static final EMailBuilder SINGLETON;

	static { SINGLETON = new EMailBuilder(); }

	/**
     * Parses the string as an e-mail address.
     * 
     * @param s
     *            The <code>String</code> to parse.
     * @return An <code>EMail</code>.
     * @throws EMailFormatException
     */
	public static EMail parse(final String s) throws EMailFormatException {
		return SINGLETON.doParse(s);
	}

	/**
	 * Create a EMailBuilder.
	 * 
	 */
	private EMailBuilder() { super(); }

	/**
     * Parses the string as an e-mail address.
     * 
     * @param s
     *            The <code>String</code> to parse.
     * @return An <code>EMail</code>.
     * @throws EMailFormatException
     */
	private EMail doParse(final String s) throws EMailFormatException {
		if (null == s) {
            throw new NullPointerException("CODEBASE EMAIL IS NULL");
		}
		final int indexOfAt = s.indexOf('@');
		if (-1 == indexOfAt) {
            throw new EMailFormatException("CODEBASE EMAIL DOES NOT CONTAIN '@'", s);
		}
		final int indexOfDot = s.indexOf('.', indexOfAt);
		if (-1 == indexOfDot) {
            throw new EMailFormatException("CODEBASE EMAIL DOES NOT CONTAIN '.'", s);
		}
        int indexOfWhitespace = s.length() - 1;
        for (int i = indexOfDot; i < s.length(); i++) {
            if (Character.isWhitespace(s.charAt(i))) {
                indexOfWhitespace = i;
                break;
            }
        }
		final EMail email = new EMail();
        email.setDomain(s.substring(indexOfAt + 1, indexOfWhitespace + 1));
        email.setUsername(s.substring(0, indexOfAt));
		return email;
	}
}
