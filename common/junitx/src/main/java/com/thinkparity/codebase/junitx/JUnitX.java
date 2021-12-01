/*
 * Feb 1, 2006
 */
package com.thinkparity.codebase.junitx;

/**
 * @author raymond@raykroeker.com
 */
public class JUnitX {

	static final String MESSAGE_INIT;

	static {
		MESSAGE_INIT = new StringBuffer(getName())
			.append(" - ")
			.append(getVersion())
			.append(" - ")
			.append(getProvider())
            .toString();
	}

	public static String getName() { return "JUnit eXtensions"; }

	public static String getProvider() { return "thinkParity Solutions Inc."; }

	public static String getShortName() { return "JUnitX"; }

	public static String getVersion() { return "2006.11"; }

	/**
	 * Create a JUnitX [Singelton]
	 * 
	 */
	private JUnitX() { super(); }

}
