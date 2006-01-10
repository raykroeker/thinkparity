/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;

import org.apache.log4j.Logger;

import com.thinkparity.browser.javax.swing.BrowserColorUtil;
import com.thinkparity.browser.javax.swing.BrowserFontUtil;
import com.thinkparity.browser.log4j.BrowserLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentAvatar extends JPanel /*JComponent*/ {

	/**
	 * Used to paint the avatar border.
	 * 
	 */
	private class DocumentAvatarBorder extends AbstractBorder {

		/**
		 * @see java.io.Serializable
		 */
		private static final long serialVersionUID = 1;

		/**
		 * Create a DocumentAvatarBorder.
		 * 
		 */
		private DocumentAvatarBorder() { super(); }

		/**
		 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
		 * 
		 */
		public Insets getBorderInsets(Component c) {
			return new Insets(1, 1, 1, 1);
		}

		/**
		 * @see javax.swing.border.LineBorder#paintBorder(java.awt.Component,
		 *      java.awt.Graphics, int, int, int, int)
		 * 
		 */
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			// outline
			final Graphics g2 = (Graphics2D) g.create();
			g2.setColor(avatarOutlineColor);
			g2.drawRoundRect(x, y, width - 1, height - 1, height / 4, height / 4);
			g2.dispose();
		}
	}

	/**
	 * Font used to draw write the document's name.
	 * 
	 */
	private static final Font nameFont;

	/**
	 * Color of the nameFont use to write the document's name.
	 * 
	 */
	private static final Color nameFontColor;

	/**
	 * The height of the font used to write the document name.
	 * 
	 */
	private static final int nameFontHeight;

	/**
	 * The maximum descent of the font used to write the document name.
	 * 
	 */
	private static final int nameFontMaxDescent;

	/**
	 * The x coordinate to indent the name text by.
	 * 
	 */
	private static final int nameIndentX;

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	static {
		// grab the font info for the name
		nameFont = new Font("Tahoma", Font.PLAIN, 12);
		nameFontColor = BrowserColorUtil.getBlack();
		final FontMetrics nameFontMetrics = BrowserFontUtil.getMetrics(nameFont);
		nameFontHeight = nameFontMetrics.getHeight();
		nameFontMaxDescent = nameFontMetrics.getMaxDescent();

		// set the avatar dimensions\location
		nameIndentX = 10;
	}

	/**
	 * Handle to an apache logger
	 * 
	 */
	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());

	/**
	 * The avatarIndex of the avatar within the list.
	 * 
	 */
	private int avatarIndex;

	/**
	 * The maximum width of the avatar.
	 * 
	 */
	private int avatarMaxWidth;

	/**
	 * Color used to outline the avatar.
	 * 
	 */
	private Color avatarOutlineColor;

	/**
	 * The y coordinate at which the avatar will be drawn.
	 * 
	 * @see #setAvatarIndex(int)
	 * @see #setAvatarRelativeY(int)
	 */
	private int avatarRelativeY;

	/**
	 * The width of the avatar to be drawn.
	 * 
	 * @see #setAvatarMaxWidth(int)
	 * @see #setAvatarWidth(int)
	 */
	private int avatarWidth;

	/**
	 * The document key holder.
	 * 
	 */
	private String keyHolder;

	/**
	 * The document name.
	 * 
	 */
	private String name;

	/**
	 * Create a DocumentAvatar.
	 */
	public DocumentAvatar() {
		super();

		setBorder(new DocumentAvatarBorder());
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(final MouseEvent e) {
				logger.debug("component bounds:" + e.getComponent().getBounds().toString());
			}
		});
	}

	/**
	 * Obtain the avatarIndex of the avatar in the list.
	 * 
	 * @return The avatarIndex of the avatar in the list.
	 */
	public int getAvatarIndex() { return avatarIndex; }

	/**
	 * Obtain the maximum width of the avatar.
	 * 
	 * @return The maximum width of the avatar.
	 */
	public int getAvatarMaxWidth() { return avatarMaxWidth; }

	/**
	 * Obtain the avatar outline color.
	 * 
	 * @return The avatar outline color.
	 */
	public Color getAvatarOutlineColor() { return avatarOutlineColor; }

	/**
	 * Obtain the relative y coordinate of the avatar.
	 * 
	 * @return The relative y coordinate.
	 */
	public int getAvatarRelativeY() { return avatarRelativeY; }

	/**
	 * Obtain the avatar width.
	 * 
	 * @return The avatar width.
	 */
	public int getAvatarWidth() { return avatarWidth; }

	/**
	 * Obtain the document key holder.
	 * 
	 * @return The document key holder.
	 */
	public String getKeyHolder() { return keyHolder; }

	/**
	 * Obtain the document name.
	 * 
	 * @return The document name.
	 */
	public String getName() { return name; }

	/**
	 * Set the avatar outline color.
	 * 
	 * @param avatarOutlineColor
	 *            The avatar outline color.
	 */
	public void setAvatarOutlineColor(Color avatarOutlineColor) {
		this.avatarOutlineColor = avatarOutlineColor;
	}

	/**
	 * Set the document key holder.
	 * 
	 * @param keyHolder
	 *            The key holder.
	 */
	public void setKeyHolder(String keyHolder) { this.keyHolder = keyHolder; }

	/**
	 * Set the name.
	 * 
	 * @param name
	 *            The name.
	 */
	public void setName(String name) { this.name = name; }

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	protected void paintComponent(Graphics g) {
		final Graphics2D g2 = (Graphics2D) g.create();
		// name
		g2.setFont(nameFont);
		g2.setColor(nameFontColor);
		final int nameIndentY = nameFontHeight - nameFontMaxDescent + (getHeight() - nameFontHeight) / 2;
		g2.drawString(name, nameIndentX, nameIndentY);
		g2.dispose();
	}
}
