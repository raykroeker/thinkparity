/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.ui.component;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class LabelFactory {

	public enum TextAlignment {

		CENTER(SwingConstants.CENTER),
		LEADING(SwingConstants.LEADING),
		LEFT(SwingConstants.LEFT),
		RIGHT(SwingConstants.RIGHT),
		TRAILING(SwingConstants.TRAILING);

		private final Integer swingConstant;

		private TextAlignment(final Integer swingConstant) {
			this.swingConstant = swingConstant;
		}

		public Integer getSwingConstant() { return swingConstant; }

	}

	/**
	 * Singleton instance.
	 * 
	 */
	private static final LabelFactory singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new LabelFactory();
		singletonLock = new Object();
	}

	public static JLabel create() {
		synchronized(singletonLock) { return singleton.doCreate(); }
	}

	public static JLabel create(final Font font) {
		synchronized(singletonLock) { return singleton.doCreate(font); }
	}

	public static JLabel create(final Font font, final String text) {
		synchronized(singletonLock) { return singleton.doCreate(font, text); }
	}

	public static JLabel create(final Font font, final String text,
			final Icon icon, final TextAlignment textAlignment) {
		synchronized(singletonLock) {
			return singleton.doCreate(font, text, icon, textAlignment);
		}
	}

	/**
	 * Create a ButtonFactory.
	 */
	private LabelFactory() { super(); }

	/**
	 * Apply a font to a component.
	 * 
	 * @param component
	 *            The component.
	 * @param font
	 *            The font.
	 */
	private void applyFont(final Component component, final Font font) {
		if(null != font) { component.setFont(font); }
	}

	private JLabel doCreate() { return new JLabel(); }

	private JLabel doCreate(final Font font) { return doCreate(font, ""); }

	private JLabel doCreate(final Font font, final String text) {
		// draw the button as a bottom button.
		final JLabel jLabel = new JLabel(null == text ? "" : text);
		jLabel.setFont(font);
		return jLabel;		
	}

	private JLabel doCreate(final Font font, final String text,
			final Icon icon, final TextAlignment textAlignment) {
		final JLabel jLabel = new JLabel(text, icon, textAlignment.getSwingConstant());
		applyFont(jLabel, font);
		return jLabel;
	}
}
