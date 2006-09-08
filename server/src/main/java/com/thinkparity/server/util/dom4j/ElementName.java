/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.util.dom4j;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum ElementName {

	ACTION("action"),
    BYTES("bytes"),
    CONTACT("contact"),
    CONTACTS("contacts"),
    FLAG("flag"),
	JID("jid"),
    JIDS("jids"),
    KEYHOLDER("keyholder"),
	KEYRESPONSE("keyresponse"),
    NAME("name"),
    QUERY("query"),
    REACTIVATEDBY("reactivatedby"),
    REACTIVATEDON("reactivatedon"),
    USER("user"),
	USERNAME("username"),
    USERS("users"),
    UUID("uuid"),
    UUIDS("uuids"),
    VCARD("vcard"),
    VERSIONID("versionid");

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
