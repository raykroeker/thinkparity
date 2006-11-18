/*
 * Created On: Jun 3, 2006 2:16:48 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util;


import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

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

        defaults.put("List.selectionBackground", Colors.Swing.DEFAULT_LIST_SELECTION_BG);
        defaults.put("List.selectionForeground", Colors.Swing.DEFAULT_LIST_SELECTION_FG);
        defaults.put("Menu.font", BrowserConstants.Fonts.DefaultFont);
        defaults.put("Menu.selectionBackground", Colors.Swing.MENU_SELECTION_BG);
        defaults.put("Menu.selectionForeground", Colors.Swing.MENU_SELECTION_FG);
        defaults.put("Menu.background", Colors.Swing.MENU_BG);
        defaults.put("Menu.foreground", Colors.Swing.MENU_FG);
        defaults.put("MenuItem.font", BrowserConstants.Fonts.DefaultFont);
        defaults.put("MenuItem.selectionBackground", Colors.Swing.MENU_ITEM_SELECTION_BG);
        defaults.put("MenuItem.selectionForeground", Colors.Swing.MENU_ITEM_SELECTION_FG);
        defaults.put("MenuItem.background", Colors.Swing.MENU_ITEM_BG);
        defaults.put("MenuItem.foreground", Colors.Swing.MENU_ITEM_FG);

        try {
        	final LookAndFeel laf;
        	switch (OSUtil.getOS()) {
        	case WINDOWS_XP:
        		laf = new WindowsLookAndFeel();
        		break;
        	case LINUX:
        		laf = new MetalLookAndFeel();
        		break;
    		default:
    			throw Assert.createUnreachable("UNSUPPORTED OPERATING SYSTEM");
        	}
        	UIManager.setLookAndFeel(laf);
        }
        catch(final Throwable t) { throw new RuntimeException(t); }
    }

    /** Create Swing. */
    private Swing() { super(); }
}
