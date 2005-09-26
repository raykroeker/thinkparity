/*
 * Aug 6, 2005
 */
package com.thinkparity.codebase;

/**
 * OS
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum OS {

	LINUX("Linux", "2.6.11.4-21.8-smp"), /**
	 * Windows XP
	 */
	WINDOWS_XP("Windows XP", "5.1");

	/**
	 * Obtain the OS enum based upon the name and version system properties.
	 * @param name <code>java.lang.String</code>
	 * @param version <code>java.lang.String</code>
	 * @return <code>OS</code>
	 */
	static synchronized OS valueOf(final String name,
			final String version) {
		if(name.equals("Windows XP") && version.equals("5.1")) { return WINDOWS_XP; }
		else if(name.equals("Linux")) { return LINUX; }
		throw new IllegalArgumentException(
	            "No OS const " + name + ", " + version + ".");
	}

	/**
	 * Operating system name.
	 */
	private String name;

	/**
	 * Operating system version.
	 */
	private String version;

	/**
	 * Create a OS
	 * @param name <code>java.lang.String</code>
	 * @param version <code>java.lang.String</code>
	 */
	private OS(final String name, final String version) {
		this.name = name;
		this.version = version;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() { return "OS:  [" + name + ", " + version + "]"; }
}
