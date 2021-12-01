/*
 * Apr 1, 2006
 */
package com.thinkparity.codebase.email;

/**
 * A singleton builder object user to create EMail objects by parsing text.  The
 * basis for parsing is {@link http://tools.ietf.org/html/rfc2822 RFC2822}.
 * 
 * @author raymond@raykroeker.com
 * @version 1.1
 * 
 * @see EMail
 */
public class EMailBuilder {

    /** Allowed sub level domain characters */
    private static final String ALLOWED_SUB_DOMAIN_CHARACTERS;

    /** Allowed top level domain characters */
    private static final String ALLOWED_TOP_DOMAIN_CHARACTERS;

    /** Allowed username characters (before the @) */
    private static final String ALLOWED_USERNAME_CHARACTERS;

    /** Maximum length for a domain */
    private static final int MAX_LENGTH_DOMAIN;

	protected static final EMailBuilder SINGLETON;

	static {
        ALLOWED_SUB_DOMAIN_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789-.";
        ALLOWED_TOP_DOMAIN_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
        ALLOWED_USERNAME_CHARACTERS =  "abcdefghijklmnopqrstuvwxyz0123456789-._+";
        MAX_LENGTH_DOMAIN = 255;

        SINGLETON = new EMailBuilder();
    }

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

        // handle emails of the form Joe(joe@abc.com)
        int startIndex = s.indexOf('(');
        int endIndex = s.indexOf(')');
        if (startIndex >= 0 && endIndex > startIndex+1) {
            return doParse(s.substring(startIndex+1, endIndex));
        }

        // handle emails of the form Joe[joe@abc.com]
        startIndex = s.indexOf('[');
        endIndex = s.indexOf(']');
        if (startIndex >= 0 && endIndex > startIndex+1) {
            return doParse(s.substring(startIndex+1, endIndex));
        }

        // check the username up to the @
		final int indexOfAt = s.indexOf('@');
		if (-1 == indexOfAt)
            throw new EMailFormatException("EMail does not contain '@'", s);
        if (0 == indexOfAt)
            throw new EMailFormatException("EMail does not contain username.", s);
        final int lastIndexOfAt = s.lastIndexOf('@');
        if (indexOfAt != lastIndexOfAt)
            throw new EMailFormatException("EMail contains an extra '@'.", s);
        final String username = s.substring(0, indexOfAt).toLowerCase();
        if (!validString(username, ALLOWED_USERNAME_CHARACTERS))
            throw new EMailFormatException("EMail contains invalid characters in the username.", s);

        // check the domain
        if (indexOfAt + 1 == s.length())
            throw new EMailFormatException("EMail does not contain domain.", s);
        final String domain = s.substring(indexOfAt + 1).toLowerCase();
        final int indexOfDot = domain.indexOf('.');
        if (-1 == indexOfDot)
            throw new EMailFormatException("EMail domain does not contain '.'", s);
        if (0 == indexOfDot || -1 != domain.indexOf(".."))
            throw new EMailFormatException("EMail contains a zero length sub level domain.", s);
        final int lastIndexOfDot = domain.lastIndexOf('.');
        if (lastIndexOfDot + 1 == domain.length())
            throw new EMailFormatException("EMail does not contain top level domain.", s);
        final String subDomain = domain.substring(0, lastIndexOfDot);
        final String topDomain = domain.substring(lastIndexOfDot + 1);
        if (topDomain.length() < 2 || topDomain.length() > 4)
            throw new EMailFormatException("EMail top level domain must be between 2 and 4 characters.", s);
        if (isLongDomain(subDomain))
            throw new EMailFormatException("EMail contains a domain that is too long.");
        if (!validString(subDomain, ALLOWED_SUB_DOMAIN_CHARACTERS))
            throw new EMailFormatException("EMail contains invalid characters in the sub level domain.", s);
        if (!validString(topDomain, ALLOWED_TOP_DOMAIN_CHARACTERS))
            throw new EMailFormatException("EMail contains invalid characters in the top level domain.", s);

        // build the email
		final EMail email = new EMail();
        // NOTE e-mail addresses are all lower case
        email.setDomain(domain.toLowerCase());
        email.setUsername(username.toLowerCase());
		return email;
	}

    /**
     * Determine if any of the domains in the string are too long.
     * 
     * @param domains
     *            A domains <code>String</code>.
     */
    private boolean isLongDomain(final String domains) {
        int fromIndex = 0;
        while (true) {
            final int indexOfDot = domains.indexOf('.', fromIndex);
            if (-1 == indexOfDot) {
                return (domains.length() - fromIndex > MAX_LENGTH_DOMAIN);
            } else if (indexOfDot - fromIndex > MAX_LENGTH_DOMAIN) {
                return true;
            }
            fromIndex = indexOfDot + 1;
        }
    }

    /**
     * Determines if the provided text string contains only the allowed characters.
     * 
     * @param text
     *            A text <code>String</code>.
     * @param allowedCharacters
     *            A list of allowed characters <code>String</code>.
     * @return A <code>boolean</code>, true if the text string contains only the allowed characters.
     */
    private boolean validString(final String text, final String allowedCharacters) {
        for (int index = 0; index < text.length(); index++) {
            if (-1 == allowedCharacters.indexOf(text.charAt(index))) {
                return false;
            }
        }
        return true;
    }
}
