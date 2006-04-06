/*
 * Feb 1, 2006
 */
package com.raykroeker.junitx;

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

	public static String getProvider() { return "raykroeker.com"; }

	public static String getShortName() { return "jUnitX"; }

	public static String getVersion() { return "1.0.0"; }

	/**
	 * Create a JUnitX [Singelton]
	 * 
	 */
	private JUnitX() { super(); }

}
