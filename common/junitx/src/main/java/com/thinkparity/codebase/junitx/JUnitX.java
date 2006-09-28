/*
 * Feb 1, 2006
 */
package com.thinkparity.codebase.junitx;

/**
 * @author raykroeker@gmail.com
 */
public class JUnitX {

	static final Object MESSAGE_INIT;

	static {
		MESSAGE_INIT = new StringBuffer(getName())
			.append(" - ")
			.append(getVersion())
			.append(" - ")
			.append(getProvider());
	}

	public static String getName() { return "JUnit eXtensions"; }

	public static String getProvider() { return "thinkParity Solutions Inc."; }

	public static String getShortName() { return "jUnitX"; }

	public static String getVersion() { return "2006.09"; }

	/**
	 * Create a JUnitX [Singelton]
	 * 
	 */
	private JUnitX() { super(); }

}
