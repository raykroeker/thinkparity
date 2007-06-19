/**
 * Created On: 3-Dec-06 1:53:05 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

import com.thinkparity.ophelia.browser.util.ImageIOUtil;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityPopupMenu.MenuBackgroundType;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ThinkParityMenuItemUI extends BasicMenuItemUI {

    /** The <code>Color</code> for the background of row 3 and below. */
    private static final Color BACKGROUND_COLOR_ROW_N;

    /** The <code>BufferedImage</code> for the background, row 0. */
    private static final BufferedImage BACKGROUND_ROW_0;

    /** The <code>BufferedImage</code> for the background, row 1. */
    private static final BufferedImage BACKGROUND_ROW_1;

    /** The <code>BufferedImage</code> for the background, row 2. */
    private static final BufferedImage BACKGROUND_ROW_2;

    static {
        BACKGROUND_ROW_0 = ImageIOUtil.read("MenuItemBackgroundRow0.png");
        BACKGROUND_ROW_1 = ImageIOUtil.read("MenuItemBackgroundRow1.png");
        BACKGROUND_ROW_2 = ImageIOUtil.read("MenuItemBackgroundRow2.png");
        BACKGROUND_COLOR_ROW_N = new Color(228, 235, 243, 255);
    }

    /**
     * Create a thinkParity menu item ui.
     * 
     * @param c
     *            A <code>JComponent</code>.
     * @return A <code>ComponentUI</code>.
     */
    public static ComponentUI createUI(final JComponent c) {
        return new ThinkParityMenuItemUI();
    }

    /**
     * Create ThinkParityScrollBarUI.
     * 
     */
    public ThinkParityMenuItemUI() {
        super();
    }

    /**
     * @see javax.swing.plaf.basic.BasicMenuItemUI#paintBackground(java.awt.Graphics, javax.swing.JMenuItem, java.awt.Color)
     */
    @Override
    protected void paintBackground(final Graphics g, final JMenuItem menuItem, final Color bgColor) {
        ButtonModel model = menuItem.getModel();
        Color oldColor = g.getColor();
        int menuWidth = menuItem.getWidth();
        int menuHeight = menuItem.getHeight();

        if (menuItem.isOpaque()) {
            if (model.isArmed() || (menuItem instanceof JMenu && model.isSelected())) {
                g.setColor(bgColor);
                g.fillRect(0,0, menuWidth, menuHeight);
            } else if (isGradientBackground(menuItem)) {
                // draw gradient image in the background.
                // NOTE To keep it simple and fast, no image scaling is done for now
                // since we know the provided images are wider than the widest menu item.
                // The images are clipped by g.drawImage().
                // Image scaling can be added if a more general solution is required.
                final int index = getMenuItemIndex(menuItem);
                if (index == 0) {
                    g.drawImage(BACKGROUND_ROW_0, 0, 0, null);
                } else if (index == 1) {
                    g.drawImage(BACKGROUND_ROW_1, 0, 0, null);
                } else if (index == 2) {
                    g.drawImage(BACKGROUND_ROW_2, 0, 0, null);
                } else {
                    g.setColor(BACKGROUND_COLOR_ROW_N);
                    g.fillRect(0,0, menuWidth, menuHeight);
                }
            } else {
                g.setColor(menuItem.getBackground());
                g.fillRect(0,0, menuWidth, menuHeight);
            }
            g.setColor(oldColor);
        } else if (model.isArmed() || (menuItem instanceof JMenu && model.isSelected())) {
            g.setColor(bgColor);
            g.fillRect(0,0, menuWidth, menuHeight);
            g.setColor(oldColor);
        }
    }

    /**
     * Get the index of the menu item.
     * 
     * @param menuItem
     *            A <code>JMenuItem</code>.
     * @return The <code>int</code> index of the menu item, or -1 if unknown.
     */
    private int getMenuItemIndex(final JMenuItem menuItem) {
        final Container parent = menuItem.getParent();
        if (parent instanceof JPopupMenu) {
            return ((JPopupMenu)parent).getComponentIndex(menuItem);
        } else if (parent instanceof JMenu) {
            return ((JMenu)parent).getPopupMenu().getComponentIndex(menuItem);
        } else {
            return -1;
        }
    }

    /**
     * Determine if there is a gradient background.
     * 
     * @param menuItem
     *            A <code>JMenuItem</code>.
     * @return true if there is a gradient background.
     */
    private boolean isGradientBackground(final JMenuItem menuItem) {
        final Container parent = menuItem.getParent();
        if (parent instanceof ThinkParityPopupMenu) {
            return (MenuBackgroundType.GRADIENT == ((ThinkParityPopupMenu)parent).getMenuBackgroundType());
        } else {
            return false;
        }
    }
}
