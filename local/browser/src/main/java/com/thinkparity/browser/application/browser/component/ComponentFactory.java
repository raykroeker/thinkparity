/*
 * Jan 28, 2006
 */
package com.thinkparity.browser.application.browser.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.UIConstants;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class ComponentFactory {

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * Create a ComponentFactory.
	 * 
	 */
	protected ComponentFactory() {
		super();
		this.logger = LoggerFactory.getLogger(getClass());
	}

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
			final Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
			final Cursor originalCursor = component.getCursor();
			public void mouseEntered(final MouseEvent e) {
				component.setCursor(handCursor);
			}
			public void mouseExited(final MouseEvent e) {
				component.setCursor(originalCursor);
			}
		});
	}
}
