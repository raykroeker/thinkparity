/*
 * Created On: Jun 3, 2006 2:16:48 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util;

import java.awt.image.BufferedImage;

import javax.swing.UIDefaults;
import javax.swing.UIManager;


import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;

/**
 * thinkParity Swing
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Swing {

    /**
     * Initialize thinkParity Swing.
     *
     */
    public static void init() {
        System.setProperty("sun.awt.noerasebackground", "true");

        final UIDefaults defaults = UIManager.getDefaults();

        defaults.put("List.selectionBackground", Colors.Swing.LIST_SELECTION_BG);
        defaults.put("List.selectionForeground", Colors.Swing.LIST_SELECTION_FG);
        defaults.put("Menu.font", BrowserConstants.Fonts.DefaultFont);
        defaults.put("Menu.selectionBackground", Colors.Swing.MENU_SELECTION_BG);
        defaults.put("Menu.selectionForeground", Colors.Swing.MENU_SELECTION_FG);
        defaults.put("MenuItem.font", BrowserConstants.Fonts.DefaultFont);
        defaults.put("MenuItem.selectionBackground", Colors.Swing.MENU_ITEM_SELECTION_BG);
        defaults.put("MenuItem.selectionForeground", Colors.Swing.MENU_ITEM_SELECTION_FG);

        try { UIManager.setLookAndFeel(new WindowsLookAndFeel()); }
        catch(final Throwable t) { throw new RuntimeException(t); }
    }

    /** Create Swing. */
    private Swing() { super(); }

    public static final class Constants {
        public static final class Images {
            public static final BufferedImage WINDOW_ICON_IMAGE = ImageIOUtil.read("ThinkParity32x32.png");
        }
    }
}
