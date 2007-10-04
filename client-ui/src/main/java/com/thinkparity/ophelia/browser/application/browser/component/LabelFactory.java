/*
 * Jan 13, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.thinkparity.ophelia.browser.Constants.Colors;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class LabelFactory extends ComponentFactory {

    /** A singleton instance of <code>LabelFactory</code>. */
	private static final LabelFactory singleton;

	/** The singleton instance synchronization lock <code>Object</code>. */
	private static final Object singletonLock;

	static {
		singleton = new LabelFactory();
		singletonLock = new Object();
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

    public static JLabel create(final Font font, final Color foreground) {
        synchronized (singletonLock) {
            return singleton.doCreate(font, foreground);
        }
    }

    public static JLabel create(final Icon icon) {
		synchronized (singletonLock) {
			return singleton.doCreate(icon);
		}
	}

	public static JLabel create(final String text) {
		synchronized(singletonLock) { return singleton.doCreate(text); }
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
	 * Create a Label Factory.
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

    /**
     * Create a JLabel with a Font applied.
     * 
     * @param font
     *            The font to apply.
     * @param foreground
     *            A <code>Color</code>.
     * @return The JLabel.
     */
    private JLabel doCreate(final Font font, final Color foreground) {
        final JLabel jLabel = doCreate(font);
        applyForeground(jLabel, foreground);
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
	 * @return The JLabel.
	 */
	private JLabel doCreateLink(final String text, final Font font) {
        final JLabel jLabel = new UnderlinedJLabel();
        applyText(jLabel, text);
        applyFont(jLabel, font);
        applyForeground(jLabel, Colors.Browser.Link.LINK_FOREGROUND);
		applyHandCursor(jLabel);
		return jLabel;
	}

	/**
	 * Text alignment when using an image and text in a label.
	 * 
	 */
	public enum TextAlignment {

		CENTER(SwingConstants.CENTER), LEADING(SwingConstants.LEADING),
		LEFT(SwingConstants.LEFT), RIGHT(SwingConstants.RIGHT),
		TRAILING(SwingConstants.TRAILING);

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

	/**
     * <b>Title:</b>thinkParity OpheliaUI Underline JLabel<br>
     * <b>Description:</b>A label that displays its text with a graphical
     * underline.<br>
     */
    private static class UnderlinedJLabel extends JLabel {
        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            if (getText().length() > 0) {
                final int iconWidth = (null == getIcon() ? 0 : getIcon().getIconWidth() + getIconTextGap());
                final FontMetrics fontMetrics = getFontMetrics(getFont());
                final int y = fontMetrics.getMaxAscent() + 1;
                g.drawLine(0, y, fontMetrics.stringWidth(getText()) + iconWidth, y);
            }
        }
    }
}
