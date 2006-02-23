/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.application.browser.component;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class LabelFactory extends ComponentFactory {

	/**
	 * Foreground color of the link.
	 * 
	 */
	private static final Color LINK_FOREGROUND;

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

		// COLOR 49, 102, 148, 255
		LINK_FOREGROUND = new Color(49, 102, 148, 255);
	}

	/**
	 * Create a JLabel.
	 * 
	 * @return The JLabel.
	 */
	public static JLabel create() {
		synchronized (singletonLock) {
			return singleton.doCreate();
		}
	}

	public static JLabel create(final Font font) {
		synchronized (singletonLock) {
			return singleton.doCreate(font);
		}
	}

	public static JLabel create(final Icon icon) {
		synchronized (singletonLock) {
			return singleton.doCreate(icon);
		}
	}

	public static JLabel create(final String text, final Font font) {
		synchronized (singletonLock) {
			return singleton.doCreate(text, font);
		}
	}

	public static JLabel create(final String text, final Font font,
			final Color foreground) {
		synchronized (singletonLock) {
			return singleton.doCreate(text, font, foreground);
		}
	}

	public static JLabel create(final String text, final Icon icon,
			final TextAlignment textAlignment, final Font font) {
		synchronized (singletonLock) {
			return singleton.doCreate(text, icon, textAlignment, font);
		}
	}

	public static JLabel createLink(final String text, final Font font) {
		synchronized(singletonLock) {
			return singleton.doCreateLink(text, font);
		}
	}

	/**
	 * Create a ButtonFactory.
	 */
	private LabelFactory() {
		super();
	}

	/**
	 * Apply an icon to a JLabel.
	 * 
	 * @param jLabel
	 *            The JLabel.
	 * @param icon
	 *            The icon.
	 */
	private void applyIcon(final JLabel jLabel, final Icon icon) {
		jLabel.setIcon(icon);
	}

	/**
	 * Apply the text to the JLabel.
	 * 
	 * @param jLabel
	 *            The JLabel.
	 * @param text
	 *            The text.
	 */
	private void applyText(final JLabel jLabel, final String text) {
		jLabel.setText(text);
	}

	/**
	 * Create a JLabel.
	 * 
	 * @return The JLabel.
	 */
	private JLabel doCreate() {
		final JLabel jLabel = new JLabel();
		applyDefaultFont(jLabel);
		return jLabel;
	}

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

	private JLabel doCreate(final Icon icon) {
		final JLabel jLabel = doCreate();
		applyIcon(jLabel, icon);
		return jLabel;
	}

	/**
	 * Create a JLabel containing text.
	 * 
	 * @param text
	 *            The label text.
	 * @return The JLabel.
	 */
	private JLabel doCreate(final String text) {
		final JLabel jLabel = doCreate();
		applyText(jLabel, text);
		return jLabel;
	}

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
		final JLabel jLabel = new JLabel(text, icon, textAlignment
				.getSwingConstant());
		applyFont(jLabel, font);
		return jLabel;
	}

	/**
	 * Create a JLabel containing text that appears as a hyperlink; with a font
	 * applied.
	 * 
	 * @param text
	 *            The link text.
	 * @param font
	 *            The font.
	 * 
	 * @return The JLabel.
	 */
	private JLabel doCreateLink(final String text, final Font font) {
		final JLabel jLabel = doCreate(text, font, LINK_FOREGROUND);
		applyHandCursor(jLabel);
		return jLabel;
	}

	/**
	 * Text alignment when using an image and text in a label.
	 * 
	 */
	public enum TextAlignment {

		CENTER(SwingConstants.CENTER), LEADING(SwingConstants.LEADING), LEFT(
				SwingConstants.LEFT), RIGHT(SwingConstants.RIGHT), TRAILING(
				SwingConstants.TRAILING);

		/**
		 * The swing constant that the enum wraps.
		 * 
		 * @see SwingConstants
		 */
		private final Integer swingConstant;

		/**
		 * Create a TextAlignment.
		 * 
		 * @param swingConstant
		 *            The wrapped swing constant.
		 * 
		 * @see SwingConstants
		 */
		private TextAlignment(final Integer swingConstant) {
			this.swingConstant = swingConstant;
		}

		/**
		 * Obtain the swing constant this type wraps.
		 * 
		 * @return The swing constant.
		 */
		public Integer getSwingConstant() {
			return swingConstant;
		}
	}
}
