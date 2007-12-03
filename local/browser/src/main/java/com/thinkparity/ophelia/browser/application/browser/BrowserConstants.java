/*
 * Jan 22, 2006
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.Color;
import java.awt.Font;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public final class BrowserConstants {

	/** The default dialogue background colour. */
    public static final Color DIALOGUE_BACKGROUND = new Color(225, 228, 231, 255);  // A colour within the browser gradient

    /** Browser Colours */
    public static final class Colours {

        /** The error text colour. */
        public static final Color DIALOG_ERROR_TEXT_FG = new Color(255, 96, 6, 255);

        /** The page text colour. */
        public static final Color DIALOG_PAGE_TEXT_FG = new Color(123, 109, 89, 255);

        /** The standard text colour. */
        public static final Color DIALOG_TEXT_FG = Color.BLACK;

        /** Transparent colour. */
        public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    }

    /** Browser Fonts */
    public static final class Fonts {

        /** The dialog font family name. */
        private static final String DIALOG_FONT_NAME = "Tahoma";

        /** The font family name. */
        private static final String FONT_NAME = "Tahoma";

        /** The default font. */
        public static final Font DefaultFont =
            new Font(FONT_NAME, Font.PLAIN, 11);

        /** The default bold font. */
        public static final Font DefaultFontBold =
            new Font(FONT_NAME, Font.BOLD, 11);

        /** The dialog font. */
        public static final Font DialogFont =
            new Font(DIALOG_FONT_NAME, Font.PLAIN, 11);

        /** The dialog bold font. */
        public static final Font DialogFontBold =
            new Font(DIALOG_FONT_NAME, Font.BOLD, 11);

        /** The dialog button font. */
        public static final Font DialogButtonFont =
            new Font(DIALOG_FONT_NAME, Font.PLAIN, 11);

        /** The dialog page font (eg. "1 of 3") */
        public static final Font DialogPageFont =
            new Font(DIALOG_FONT_NAME, Font.PLAIN, 10);

        /** The dialog text entry font. */
        public static final Font DialogTextEntryFont =
            new Font(DIALOG_FONT_NAME, Font.PLAIN, 11);

        /** The dialog title font. */
        public static final Font DialogTitle =
            new Font(DIALOG_FONT_NAME, Font.BOLD, 13);

        /** The small font. */
        public static final Font SmallFont =
            new Font(FONT_NAME, Font.PLAIN, 10);

        /** The status bar font. */
        public static final Font StatusBar =
            new Font(FONT_NAME, Font.PLAIN, 11);
    }
}
