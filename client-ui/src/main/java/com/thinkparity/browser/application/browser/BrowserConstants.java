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
public final class BrowserConstants {

    public static final Float PHI = 1.618033988F;

    private static final String FONT_NAME = "Tahoma";

	public static final Font DefaultFont = new Font(FONT_NAME, Font.PLAIN, 11);

	public static final Font DefaultFontBold = new Font(FONT_NAME, Font.BOLD, 11);

	public static final Font SmallFont = new Font(FONT_NAME, Font.PLAIN, 10);

	public static final Font SmallFontBold = new Font(FONT_NAME, Font.BOLD, 10);

	public static final Font TitleFont = DefaultFont;

	// COLOR WHITE
	public static final Color TitleForeground = Color.WHITE;

	public static final Integer TitlePaneCurvature = 15;
	
	public static final Integer TitlePaneHeight = 30;

	public static final Cursor HandCursor = new Cursor(Cursor.HAND_CURSOR);

	public static final Cursor DefaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

    public static final Color SelectionBackground =
        new Color(215, 231, 244, 255);

    public static final Color SelectionForeground =
        Color.BLACK;

    /** The default dialogue background colour.  A light blue. */
    public static final Color DIALOGUE_BACKGROUND =
        new Color(237, 241, 244, 255);

    public static final class Colours {
        /** COLOR The colour of the separator for the cells in the main list. */
        public static final Color MAIN_CELL_DEFAULT_BORDER1 = Color.WHITE;

        /** COLOR The colour of the border for the cells in the main list. */
        public static final Color MAIN_CELL_DEFAULT_BORDER2 = new Color(107, 111, 115, 255);
    }
}
