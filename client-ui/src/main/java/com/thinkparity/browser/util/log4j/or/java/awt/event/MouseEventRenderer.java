/*
 * Jan 12, 2006
 */
package com.thinkparity.browser.util.log4j.or.java.awt.event;

import java.awt.event.MouseEvent;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.browser.util.log4j.or.IRendererConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MouseEventRenderer implements ObjectRenderer {

	private static final String BUTTON = ",button:";

	private static final String CLICKCOUNT = ",clickCount:";

	private static final String COMPONENT = ",com.thinkparity.browser.javax.swing.component:";

	private static final String ID = "id:";

	private static final String ISALTDOWN = ",isAltDown:";

	private static final String ISALTGRAPHDOWN = ",isAltDown:";

	private static final String ISCONSUMED = ",isConsumed:";

	private static final String ISCONTROLDOWN = ",isControlDown:";

	private static final String ISMETADOWN = ",isMetaDown:";

	private static final String ISPOPUPTRIGGER = ",isPopupTrigger:";

	private static final String ISSHIFTDOWN = ",isShiftDown:";

	private static final String MODIFIERS = ",modifiers:";

	private static final String PREFIX =
		MouseEvent.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	private static final String SOURCE = ",source:";

	private static final String WHEN = ",when:";

	private static final String X = ",x:";

	private static final String Y = ",y:";

	/**
	 * Create a MouseEventRenderer.
	 */
	public MouseEventRenderer() { super(); }

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
			final MouseEvent e = (MouseEvent) o;
			return new StringBuffer(PREFIX)
				.append(ID).append(e.getID())
				.append(BUTTON).append(e.getButton())
				.append(CLICKCOUNT).append(e.getClickCount())
				.append(COMPONENT).append(e.getComponent().getClass().getName())
				.append(MODIFIERS).append(e.getModifiers())
				.append(X).append(e.getPoint().x)
				.append(Y).append(e.getPoint().y)
				.append(SOURCE).append(e.getSource().getClass().getName())
				.append(WHEN).append(e.getWhen())
				.append(ISALTDOWN).append(e.isAltDown())
				.append(ISALTGRAPHDOWN).append(e.isAltGraphDown())
				.append(ISCONSUMED).append(e.isConsumed())
				.append(ISCONTROLDOWN).append(e.isControlDown())
				.append(ISMETADOWN).append(e.isMetaDown())
				.append(ISPOPUPTRIGGER).append(e.isPopupTrigger())
				.append(ISSHIFTDOWN).append(e.isShiftDown())
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}
}
