/*
 * Jan 22, 2006
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public final class BrowserConstants {

    public static final Cursor DefaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

	/** The default dialogue background colour.  A light blue. */
    public static final Color DIALOGUE_BACKGROUND =
        new Color(237, 241, 244, 255);

	public static final Cursor HandCursor = new Cursor(Cursor.HAND_CURSOR);
	
	public static final Float PHI = 1.618033988F;

    // COLOR WHITE
	public static final Color TitleForeground = Color.WHITE;

    public static final Integer TitlePaneCurvature = 15;

    public static final Integer TitlePaneHeight = 30;

    /** Browser Colours */
    public static final class Colours {

        /** COLOR The colour of the separator for the cells in the main list. */
        public static final Color MAIN_CELL_DEFAULT_BORDER1 = new Color(207, 207, 207, 255);

        /** Darker colour used as a separator. */
        public static final Color MAIN_CELL_DEFAULT_BORDER_GROUP = new Color(150, 150, 150, 255);
    }

    /** Browser Fonts */
    public static final class Fonts {

        /** The font family name. */
        private static final String FONT_NAME = "Tahoma";

        /** The default font. */
        public static final Font DefaultFont =
            new Font(FONT_NAME, Font.PLAIN, 11);

        /** The default bold font. */
        public static final Font DefaultFontBold =
            new Font(FONT_NAME, Font.BOLD, 11);

        /** The small font. */
        public static final Font SmallFont =
            new Font(FONT_NAME, Font.PLAIN, 10);

        /** The status bar font. */
        public static final Font StatusBar =
            new Font(FONT_NAME, Font.PLAIN, 11);

    }
}
