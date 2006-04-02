/*
 * Apr 1, 2006
 */
package com.thinkparity.codebase.email;

/**
 * A singleton builder object user to create EMail objects by parsing text.
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
		if(null == s) { throw new NullPointerException("[CODEBASE] [EMAIL] [PARSE] [IS NULL]"); }
		final int indexOfAt = s.indexOf('@');
		if(-1 == indexOfAt) { throw new EMailFormatException("[CODEBASE] [EMAIL] [PARSE] [DOES NOT CONTAIN '@']", s); }
		final int indexOfDot = s.indexOf('.', indexOfAt);
		if(-1 == indexOfDot) { throw new EMailFormatException("[CODEBASE] [EMAIL] [PARSE] [DOES NOT CONTAIN '.']", s); }
		final EMail eMail = new EMail();
		eMail.setDomain(s.substring(indexOfAt + 1, indexOfDot));
		eMail.setUsername(s.substring(0, indexOfAt));
		return eMail;
	}
}
