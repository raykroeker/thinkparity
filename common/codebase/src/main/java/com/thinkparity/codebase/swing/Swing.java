/*
 * Created On: Jun 3, 2006 2:16:48 PM
 * $Id$
 */
package com.thinkparity.codebase.swing;

import java.awt.Color;

import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

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

        defaults.put("List.selectionBackground", List.LIST_SELECTION_BG);
        defaults.put("List.selectionForeground", List.LIST_SELECTION_FG);
        defaults.put("Menu.background", Menu.BG);
        defaults.put("Menu.font", Menu.Font);
        defaults.put("Menu.foreground", Menu.FG);
        defaults.put("Menu.selectionBackground", Menu.SELECTION_BG);
        defaults.put("Menu.selectionForeground", Menu.SELECTION_FG);
        defaults.put("MenuItem.background", MenuItem.BG);
        defaults.put("MenuItem.font", MenuItem.Font);
        defaults.put("MenuItem.foreground", MenuItem.FG);
        defaults.put("MenuItem.selectionBackground", MenuItem.SELECTION_BG);
        defaults.put("MenuItem.selectionForeground", MenuItem.SELECTION_FG);

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

    private static final class List {
        private static final Color LIST_SELECTION_BG = new Color(87, 136, 206, 255);
        private static final Color LIST_SELECTION_FG = Color.WHITE;
    }

    private static final class Menu {
        private static final Color BG = Color.WHITE;
        private static final Color FG = Color.BLACK;
        private static final java.awt.Font Font = new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11);
        private static final Color SELECTION_BG = new Color(39, 102, 192, 255);
        private static final Color SELECTION_FG = Color.WHITE;
    }

    private static final class MenuItem {
        private static final Color BG = Color.WHITE;
        private static final Color FG = Color.BLACK;
        private static final java.awt.Font Font = Menu.Font;
        private static final Color SELECTION_BG = Menu.SELECTION_BG;
        private static final Color SELECTION_FG = Color.WHITE;
    }
}
