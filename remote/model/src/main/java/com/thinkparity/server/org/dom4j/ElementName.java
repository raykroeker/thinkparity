/*
 * Dec 1, 2005
 */
package com.thinkparity.server.org.dom4j;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum ElementName {

	ACTION("action"), FLAG("flag"), KEYHOLDER("keyholder"),
	USERNAME("username"), QUERY("query"), UUID("uuid");

	/**
	 * The xml element name.
	 */
	private final String name;

	/**
	 * Create a ElementName.
	 * 
	 * @param name
	 *            The xml element name.
	 */
	private ElementName(final String name) { this.name = name; }

	/**
	 * Obtain the xml element name.
	 * 
	 * @return The xml element name.
	 */
	public String getName() { return name; }
}
