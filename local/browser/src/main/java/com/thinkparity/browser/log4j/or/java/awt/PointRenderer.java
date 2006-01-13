/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.log4j.or.java.awt;

import java.awt.Point;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.browser.log4j.or.IRendererConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class PointRenderer implements ObjectRenderer {

	private static final String PREFIX =
		Point.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	private static final String X = "x:";

	private static final String Y = "y:";

	/**
	 * Create a PointRenderer.
	 * 
	 */
	public PointRenderer() { super(); }

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
			final Point p = (Point) o;
			return new StringBuffer(PREFIX)
				.append(X).append(p.x)
				.append(Y).append(p.y)
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}

}
