/*
 * Dec 1, 2005
 */
package com.thinkparity.server.handler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum ElementName {

	ACTION("action"), FLAG("flag"), UUID("uuid");

	private final String elementName;

	private ElementName(final String elementName) {
		this.elementName = elementName;
	}

	public String getElementName() { return elementName; }
}
