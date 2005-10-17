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
	 * Create an XFilter. All x filters will filter on the
	 * <code>ISmackXConstants#NAMESPACE</code> namespace.
	 * 
	 * @param elementName
	 *            The xml element name of the packet extension to filter.
	 */
	protected XFilter(final String elementName) {
		super(elementName, ISmackXConstants.NAMESPACE);
	}
}
