/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.apache.log4j.Logger;

import com.thinkparity.browser.javax.swing.BrowserColorUtil;
import com.thinkparity.browser.javax.swing.BrowserFontUtil;
import com.thinkparity.browser.log4j.BrowserLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentAvatar {

	/**
	 * The height of the document avatar.
	 * 
	 */
	private static final int avatarHeight;

	/**
	 * The x corrdinate to indent the avatar by.
	 * 
	 */
	private static final int avatarIndentX;

	/**
	 * The y coordinate to indent the avatar by.
	 * 
	 */
	private static final int avatarOffsetY;

	/**
	 * The number of pixels to separate the avatars by.
	 * 
	 */
	private static final int avatarSpacer;

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

	static {
		// grab the font info for the name
		nameFont = new Font("Tahoma", Font.PLAIN, 12);
		nameFontColor = BrowserColorUtil.getBlack();
		final FontMetrics nameFontMetrics = BrowserFontUtil.getMetrics(nameFont);
		nameFontHeight = nameFontMetrics.getHeight();
		nameFontMaxDescent = nameFontMetrics.getMaxDescent();

		// set the avatar dimensions\location
		nameIndentX = 10;
		avatarHeight = 25;
		avatarIndentX = avatarOffsetY = 12;
		avatarSpacer = 10;
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
	 * Y coordinate of the name text.
	 * 
	 * @see #setNameY(int)
	 * @see #setAvatarRelativeY(int)
	 */
	private int nameY;

	/**
	 * Create a DocumentAvatar.
	 */
	public DocumentAvatar() { super(); }

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
	 * Obtain the name y coordinate.
	 * 
	 * @return The name y coordinate.
	 */
	public int getNameY() { return nameY; }

	/**
	 * Set the avatarIndex of the avatar.
	 * 
	 * @param avatarIndex
	 *            The avatarIndex.
	 */
	public void setAvatarIndex(int index) {
		this.avatarIndex = index;
		// NOTE Calulate the relative y position using the height and spacer
		// constants in conjunction with the avatarIndex variable
		setAvatarRelativeY(avatarOffsetY + index * (avatarHeight + avatarSpacer));
	}

	/**
	 * Set the maximum width of the avatar.
	 * 
	 * @param avatarMaxWidth
	 *            The avatarMaxWidth to set.
	 */
	public void setAvatarMaxWidth(int maxWidth) {
		this.avatarMaxWidth = maxWidth;
		setAvatarWidth(maxWidth - 2 * avatarIndentX);
	}

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
	 * Set relative y coordinate.
	 * 
	 * @param avatarRelativeY
	 *            The relative y coordinate.
	 */
	public void setAvatarRelativeY(int relativeY) {
		this.avatarRelativeY = relativeY;
		setNameY(relativeY + nameFontHeight - nameFontMaxDescent + (avatarHeight - nameFontHeight) / 2);
	}

	/**
	 * Set the width of the avatar.
	 * 
	 * @param avatarWidth
	 *            The avatar width.
	 */
	public void setAvatarWidth(int avatarWidth) {
		this.avatarWidth = avatarWidth;
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
	 * Set the y coordinate at which to start drawing the name text.
	 * 
	 * @param nameY
	 *            The y coordinate.
	 */
	public void setNameY(int nameY) { this.nameY = nameY; }

	/**
	 * Paint the document avatar. Note that we do not need to dispose of the
	 * graphics object; nor restore it to its previous state.
	 * 
	 * @see DocumentShuffler#paintComponent(Graphics)
	 */
	void paint(final Graphics2D g) {
		// outline
		g.setColor(avatarOutlineColor);
		g.drawRoundRect(avatarIndentX, avatarRelativeY, avatarWidth, avatarHeight, avatarHeight / 4, avatarHeight / 4);
		// name
		g.setFont(nameFont);
		g.setColor(nameFontColor);
		g.drawString(name, avatarIndentX + nameIndentX, nameY);
	}
}
