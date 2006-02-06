/*
 * Jan 22, 2006
 */
package com.thinkparity.browser.application.browser;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface UIConstants {

	public static final Font DefaultFont = new Font("Verdana", Font.PLAIN, 11);

	public static final Font DefaultFontBold = new Font("Verdana", Font.BOLD, 11);

	public static final Font SmallFont = new Font("Verdana", Font.PLAIN, 10);

	public static final Font SmallFontBold = new Font("Verdana", Font.BOLD, 10);

	public static final Font TitleFont = DefaultFont;

	// COLOR WHITE
	public static final Color TitleForeground = Color.WHITE;

	public static final Integer TitlePaneCurvature = 15;
	
	public static final Integer TitlePaneHeight = 30;

	public static final Cursor HandCursor = new Cursor(Cursor.HAND_CURSOR);

	public static final Cursor DefaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
}
