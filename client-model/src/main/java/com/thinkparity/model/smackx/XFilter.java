/*
 * Jun 6, 2005
 */
package com.thinkparity.model.smackx;

import org.jivesoftware.smack.filter.PacketExtensionFilter;

/**
 * XFilter
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class XFilter extends PacketExtensionFilter {

	/**
	 * Create a XFilter
	 * @param elementName
	 * @param namespace
	 */
	protected XFilter(final String elementName, final String namespace) {
		super(elementName, namespace);
	}
}
