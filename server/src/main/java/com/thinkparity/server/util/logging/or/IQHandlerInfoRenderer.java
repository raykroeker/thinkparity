/*
 * Nov 29, 2005
 */
package com.thinkparity.desdemona.util.logging.or;

import org.apache.log4j.or.ObjectRenderer;

import org.jivesoftware.wildfire.IQHandlerInfo;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQHandlerInfoRenderer implements ObjectRenderer {

	private static final String NAME = ",name:";

	private static final String NAMESPACE = "namespace:";

	private static final String PREFIX =
		IQHandlerInfo.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	/**
	 * Create a IQHandlerInfoRenderer.
	 */
	public IQHandlerInfoRenderer() { super(); }

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
			final IQHandlerInfo iq = (IQHandlerInfo) o;
			return new StringBuffer(PREFIX)
				.append(NAMESPACE).append(iq.getNamespace())
				.append(NAME).append(iq.getName())
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}
}
