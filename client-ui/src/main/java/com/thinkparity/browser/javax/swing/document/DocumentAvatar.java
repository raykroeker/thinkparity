/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.apache.log4j.Logger;

import com.thinkparity.browser.javax.swing.BrowserColorUtil;
import com.thinkparity.browser.log4j.BrowserLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentAvatar {

	private static final int nameIndentX = 10;

	private static final int avatarHeight = 60;

	private static final int avatarWidth = 216;

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

	private static final int offsetX = 12;

	private static final int offsetY = offsetX;

	/**
	 * Color used to ouline the avatar.
	 * 
	 */
	private static final Color outlineColor;

	/**
	 * Font of the state text.
	 */
	private static final Font stateFont;

	/**
	 * Color of the state text.
	 * 
	 */
	private static final Color stateFontColor;
	static {
		nameFont = new Font("Tahoma", Font.PLAIN, 14);
		nameFontColor = BrowserColorUtil.getWhite();

		stateFont = new Font("Tahoma", Font.PLAIN, 10);
		stateFontColor = nameFontColor;

		outlineColor = BrowserColorUtil.getParitySplashBlue();
	}
	/**
	 * Handle to an apache logger
	 * 
	 */
	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());
	private int index;

	private String info;

	private String keyHolder;

	private String name;

	private String state;

	/**
	 * Create a DocumentAvatar.
	 */
	public DocumentAvatar() { super(); }

	/**
	 * @return Returns the index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return Returns the keyHolder.
	 */
	public String getKeyHolder() { return keyHolder; }

	/**
	 * @return Returns the name.
	 */
	public String getName() { return name; }

	/**
	 * @return Returns the state.
	 */
	public String getState() { return state; }

	/**
	 * @param index The index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @param keyHolder The keyHolder to set.
	 */
	public void setKeyHolder(String keyHolder) {
		this.keyHolder = keyHolder;
		setInfo();
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) { this.name = name; }

	/**
	 * @param state The state to set.
	 */
	public void setState(String state) {
		this.state = state;
		setInfo();
	}

	private static final int avatarSpacer = 10;

	/**
	 * Paint the document avatar. Note that we do not need to dispose of the
	 * graphics object; nor restore it to its previous state.
	 * 
	 * @see DocumentShuffler#paintComponent(Graphics)
	 */
	void paint(final Graphics2D g) {
		logger.debug(name);
		logger.debug(index);
		logger.debug(offsetY + index * (avatarWidth + avatarSpacer));
		final int relativeY = offsetY + index * (avatarHeight + avatarSpacer);

		// outline
		g.setColor(outlineColor);
		g.drawRoundRect(offsetX, relativeY, avatarWidth, avatarHeight, 12, 12);
		// name
		g.setFont(nameFont);
		g.setColor(nameFontColor);
		g.drawString(name, offsetX + nameIndentX, relativeY + 20);
//		// info
		final int nameFontHeight = g.getFontMetrics().getHeight();
		g.setFont(stateFont);
		g.setColor(stateFontColor);
		g.drawString(info, offsetX + 17, relativeY + nameFontHeight + 20);
	}

	private void paintAvatar_v1(final Graphics2D g) {
		// outline
		final int scale = 12;
		g.setColor(outlineColor);
		g.drawRoundRect(1 * scale, 1 * scale, 18 * scale, 5 * scale, scale, scale);
		// name
		g.setFont(nameFont);
		g.setColor(nameFontColor);
		g.drawString(name, 1 * scale + offsetX, 1 * scale + offsetY);
		// info
		final int nameFontHeight = g.getFontMetrics().getHeight();
		g.setFont(stateFont);
		g.setColor(stateFontColor);
		g.drawString(info, 1 * scale + offsetX + 7, 1 * scale + offsetY + nameFontHeight);
	}

	private void paintOutline_v2(final Graphics2D g) {
		final int scale = 20;
		g.setColor(outlineColor);
		int[] outlineX = {1 * scale, 1 * scale, 12 * scale, 12 * scale, 3 * scale };
		int[] outlineY = {3 * scale, 1 * scale, 1 * scale, 5 * scale, 5 * scale };
		final int outlinePoints = outlineX.length;
		g.drawPolyline(outlineX, outlineY, outlinePoints);
//		g.drawOval(1 * scale, 3 * scale, 2 * scale, 2 * scale);
		g.drawArc(1 * scale, 1 * scale, 4 * scale, 4 * scale, 180, 90);
	}

	private void paintOutline_v1(final Graphics2D g) {
		// outline
		final int scale = 20;
		g.setColor(outlineColor);
		int[] outlineX = {1 * scale, 1 * scale, 12 * scale, 12 * scale, 2 * scale };
		int[] outlineY = {4 * scale, 1 * scale, 1 * scale, 5 * scale, 5 * scale };
		final int outlinePoints = outlineX.length;
		g.drawPolyline(outlineX, outlineY, outlinePoints);
//		g.drawOval(1 * scale, 3 * scale, 2 * scale, 2 * scale);
		g.drawArc(1 * scale, 3 * scale, 2 * scale, 2 * scale, 180, 90);
	}

	private void setInfo() {
		this.info = new StringBuffer("- ")
			.append(state)
			.append(" - ")
			.append(keyHolder).toString();
	}
}
