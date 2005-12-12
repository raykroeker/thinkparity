/*
 * Nov 29, 2005
 */
package com.thinkparity.server.org.apache.log4j.or.org.dom4j;

import org.apache.log4j.or.ObjectRenderer;
import org.dom4j.Element;

import com.thinkparity.server.org.apache.log4j.or.IRendererConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ElementRenderer implements ObjectRenderer {

	private static final String NODECOUNT = "nodeCount:";

	private static final String PREFIX =
		Element.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	private static final String XML = ",xml:";

	/**
	 * Create a ElementRenderer.
	 */
	public ElementRenderer() { super(); }

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
			final Element e = (Element) o;
			return new StringBuffer(PREFIX)
				.append(ElementRenderer.NODECOUNT).append(e.nodeCount())
				.append(ElementRenderer.XML).append(e.asXML())
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}

}
