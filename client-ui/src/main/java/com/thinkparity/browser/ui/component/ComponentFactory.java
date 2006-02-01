/*
 * Jan 28, 2006
 */
package com.thinkparity.browser.ui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.thinkparity.browser.ui.UIConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class ComponentFactory {

	/**
	 * Create a ComponentFactory.
	 * 
	 */
	protected ComponentFactory() { super(); }

	/**
	 * Apply the default font to a component.
	 * 
	 * @param component
	 *            The component.
	 */
	protected void applyDefaultFont(final Component component) {
		applyFont(component, UIConstants.DefaultFont);
	}

	/**
	 * Apply a font to a component.
	 * 
	 * @param component
	 *            The component.
	 * @param font
	 *            The font.
	 */
	protected void applyFont(final Component component, final Font font) {
		component.setFont(font);
	}

	/**
	 * Apply a foreground color to a component.
	 * 
	 * @param component
	 *            The component.
	 * @param foreground
	 *            The foreground color.
	 */
	protected void applyForeground(final Component component, final Color foreground) {
		component.setForeground(foreground);
	}

	/**
	 * Add a mouse listener to adjust the cursor to a hand with the component.
	 * 
	 * @param comopnent
	 *            The component.
	 */
	protected void applyHandCursor(final Component component) {
		component.addMouseListener(new MouseAdapter() {
			final Cursor originalCursor = component.getCursor();
			final Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
			public void mouseEntered(final MouseEvent e) {
				component.setCursor(handCursor);
			}
			public void mouseExited(final MouseEvent e) {
				component.setCursor(originalCursor);
			}
		});
	}
}
