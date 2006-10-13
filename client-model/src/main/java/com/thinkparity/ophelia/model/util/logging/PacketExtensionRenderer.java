/*
 * 19-Oct-2005
 */
package com.thinkparity.ophelia.model.util.logging;

import org.apache.log4j.or.ObjectRenderer;

import org.jivesoftware.smack.packet.PacketExtension;


/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class PacketExtensionRenderer implements ObjectRenderer {

	private static final String ELEMENT_NAME = ",elementName:";
	private static final String NAMESPACE = "namespace:";
	private static final String PREFIX =
		PacketExtension.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	/**
	 * Create a PacketExtensionRenderer.
	 */
	public PacketExtensionRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(Object o) {
		if(null == o) {
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.NULL)
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
		else {
			final PacketExtension pe = (PacketExtension) o;
			return new StringBuffer(PREFIX)
				.append(NAMESPACE).append(pe.getNamespace())
				.append(ELEMENT_NAME).append(pe.getElementName())
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}
}
