/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.util.logging.or;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.desdemona.wildfire.handler.IQAction;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQActionRenderer implements ObjectRenderer {

	private static final String ACTION = "action:";

	private static final String PREFIX =
		IQAction.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	/**
	 * Create a IQActionRenderer.
	 */
	public IQActionRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(Object o) {
		if(null == o) {
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.NULL)
				.append(IRendererConstants.SUFFIX).toString();
		}
		else {
			final IQAction iqa = (IQAction) o;
			return new StringBuffer(PREFIX)
				.append(ACTION).append(iqa.toString())
				.append(IRendererConstants.SUFFIX).toString();
		}
	}
}
