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

	protected static final EMailBuilder SINGLETON;

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
	protected EMailBuilder() { super(); }

	/**
     * Parses the string as an e-mail address.
     * 
     * @param s
     *            The <code>String</code> to parse.
     * @return An <code>EMail</code>.
     * @throws EMailFormatException
     */
	protected EMail doParse(final String s) throws EMailFormatException {
		if (null == s)
            throw new EMailFormatException("EMail is null.");
		final int indexOfAt = s.indexOf('@');
		if (-1 == indexOfAt)
            throw new EMailFormatException("EMail does not contain '@'", s);
        if (0 == indexOfAt)
            throw new EMailFormatException("EMail does not contain username.", s);
        final int lastIndexOfAt = s.lastIndexOf('@');
        if (indexOfAt != lastIndexOfAt)
            throw new EMailFormatException("EMail contains an extra '@'.", s);
		final int indexOfDot = s.indexOf('.', indexOfAt);
		if (-1 == indexOfDot)
            throw new EMailFormatException("EMail does not contain '.'", s);
        if (s.length() - 1 == indexOfDot)
            throw new EMailFormatException("EMail does not contain tld.", s);
        final int lastIndexOfDot = s.lastIndexOf('.');
        if (indexOfDot != lastIndexOfDot)
            throw new EMailFormatException("EMail contains an extra '.'", s);
		final String username = s.substring(0, indexOfAt);
		final String domain = s.substring(indexOfAt + 1);
		if (0 == domain.length())
            throw new EMailFormatException("EMail does not contain domain.", s);

		final EMail email = new EMail();
        email.setDomain(domain);
        email.setUsername(username);
		return email;
	}
}
