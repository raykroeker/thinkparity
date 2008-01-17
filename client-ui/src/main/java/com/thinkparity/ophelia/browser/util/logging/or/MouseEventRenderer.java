/*
 * Jan 12, 2006
 */
package com.thinkparity.ophelia.browser.util.logging.or;

import java.awt.event.MouseEvent;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;


import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b>thinkParity Log4J Mouse Event Renderer<br>
 * <b>Description:</b>A Log4J renderer for a java mouse event.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MouseEventRenderer implements ObjectRenderer {

	/**
	 * Create MouseEventRenderer.
     * 
	 */
	public MouseEventRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     * 
	 */
	public String doRender(final Object o) {
		if(null == o) {
			return Separator.Null.toString();
		}
		else {
			final MouseEvent e = (MouseEvent) o;
            return StringUtil.toString(MouseEvent.class,
                    "getID()", e.getID(),
                    "getButton()", e.getButton(),
                    "getClickCount()", e.getClickCount(),
                    "getComponent().getClass().getName()", e.getComponent().getClass().getName(),
                    "getModifiers()", e.getModifiers(),
                    "getPoint().x", e.getPoint().x,
                    "getPoint().y", e.getPoint().y,
                    "getSource().getClass().getName()", e.getSource().getClass().getName(),
                    "getWhen()", e.getWhen(),
                    "isAltDown()", e.isAltDown(),
                    "isAltGraphDown()", e.isAltGraphDown(),
                    "isConsumed()", e.isConsumed(),
                    "isControlDown()", e.isControlDown(),
                    "isMetaDown()", e.isMetaDown(),
                    "isPopupTrigger()", e.isPopupTrigger(),
                    "isShiftDown()", e.isShiftDown());
		}
	}
}
