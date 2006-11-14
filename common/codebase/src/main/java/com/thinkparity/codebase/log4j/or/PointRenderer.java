/*
 * Jan 13, 2006
 */
package com.thinkparity.codebase.log4j.or;

import java.awt.Point;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.StringUtil.Separator;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b>thinkParity Log4J Point Renderer<br>
 * <b>Description:</b>A Log4J renderer for a java awt point.<br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PointRenderer implements ObjectRenderer {

	/**
	 * Create PointRenderer.
	 * 
	 */
	public PointRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(final Object o) {
		if(null == o) {
			return Separator.Null.toString();
		}
		else {
			final Point p = (Point) o;
			return StringUtil.toString(Point.class, "x", p.x, "y", p.y);
		}
	}

}
