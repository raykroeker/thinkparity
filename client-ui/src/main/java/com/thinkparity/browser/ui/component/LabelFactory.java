/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.ui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

	public static JLabel create(final String text, final Font font) {
		synchronized(singletonLock) { return singleton.doCreate(text, font); }
	}

	public static JLabel create(final String text, final Font font,
			final Color foreground) {
		synchronized(singletonLock) {
			return singleton.doCreate(text, font, foreground);
		}
	}

	public static JLabel create(final String text, final Icon icon,
			final TextAlignment textAlignment, final Font font) {
		synchronized(singletonLock) {
			return singleton.doCreate(text, icon, textAlignment, font);
		}
	}

	public static JLabel createLink(final Component parent, final String text,
			final Font font) {
		synchronized(singletonLock) {
			return singleton.doCreateLink(parent, text, font);
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

	/**
	 * Apply a foreground color to a component.
	 * 
	 * @param component
	 *            The component.
	 * @param foreground
	 *            The foreground color.
	 */
	private void applyForeground(final Component component,
			final Color foreground) {
		component.setForeground(foreground);
	}

	/**
	 * Create a JLabel.
	 * 
	 * @return The JLabel
	 */
	private JLabel doCreate() { return new JLabel(); }

	/**
	 * Create a JLabel with a Font applied.
	 * 
	 * @param font
	 *            The font to apply.
	 * @return The JLabel.
	 */
	private JLabel doCreate(final Font font) {
		final JLabel jLabel = doCreate();
		applyFont(jLabel, font);
		return jLabel;
	}

	/**
	 * Create a JLabel containing text.
	 * 
	 * @param text
	 *            The label text.
	 * @return The JLabel.
	 */
	private JLabel doCreate(final String text) { return new JLabel(text); }

	/**
	 * Create a JLabel containing text, with a font applied.
	 * 
	 * @param text
	 *            The label text.
	 * @param font
	 *            The font.
	 * @return The JLabel.
	 */
	private JLabel doCreate(final String text, final Font font) {
		final JLabel jLabel = doCreate(text);
		applyFont(jLabel, font);
		return jLabel;		
	}

	/**
	 * Create a JLabel containing text, with a font applied; with a foreground
	 * color applied.
	 * 
	 * @param text
	 *            The label text.
	 * @param font
	 *            The font.
	 * @param foreground
	 *            The foreground color.
	 * @return The JLabel.
	 */
	private JLabel doCreate(final String text, final Font font,
			final Color foreground) {
		final JLabel jLabel = doCreate(text);
		applyFont(jLabel, font);
		applyForeground(jLabel, foreground);
		return jLabel;
	}

	/**
	 * Create a JLabel containing text and an icon, with a font applied.
	 * 
	 * @param text
	 *            The label text.
	 * @param icon
	 *            The label icon.
	 * @param textAlignment
	 *            The text alignment
	 * @param font
	 *            The font.
	 * @return The JLabel.
	 */
	private JLabel doCreate(final String text, final Icon icon,
			final TextAlignment textAlignment, final Font font) {
		final JLabel jLabel =
			new JLabel(text, icon, textAlignment.getSwingConstant());
		applyFont(jLabel, font);
		return jLabel;
	}

	/**
	 * Create a JLabel containing text that appears as a hyperlink; with a font
	 * applied.
	 * 
	 * @param parent
	 *            The parent component on which the link will be placed.
	 * @param text
	 *            The link text.
	 * @param font
	 *            The font.
	 * 
	 * @return The JLabel.
	 */
	private JLabel doCreateLink(final Component parent, final String text,
			final Font font) {
		final JLabel jLabel = doCreate(text, font, Color.BLUE);
		jLabel.addMouseListener(new MouseAdapter() {
			final Cursor HAND = new Cursor(Cursor.HAND_CURSOR);
			final Cursor DEFAULT = new Cursor(Cursor.DEFAULT_CURSOR);
			public void mouseEntered(final MouseEvent e) {
				parent.setCursor(HAND);
			}
			public void mouseExited(final MouseEvent e) {
				parent.setCursor(DEFAULT);
			}
		});
		return jLabel;
	}
}
