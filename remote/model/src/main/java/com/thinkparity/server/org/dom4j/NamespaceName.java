/*
 * Dec 8, 2005
 */
package com.thinkparity.server.org.dom4j;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum NamespaceName {

	IQ_ACCEPT_KEY_REQUEST("acceptkeyrequest"), IQ_ARTIFACT_FLAG("artifactflag"),
	IQ_DENY_KEY_REQUEST("denykeyrequest"), IQ_GET_KEYHOLDER("getkeyholder"),
	IQ_GET_SUBSCRIPTION("getsubscriptions"), IQ_KEY_REQUEST("keyrequest");

	/**
	 * The root parity xml namespace.
	 */
	private static final String ROOT = "jabber:iq:parity:";

	/**
	 * The namespace name.
	 */
	private final String name;

	/**
	 * Create a NamespaceName.
	 * 
	 * @param name
	 *            The name.
	 */
	private NamespaceName(final String name) { this.name = name; }

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return new StringBuffer(ROOT).append(name).toString();
	}
}
