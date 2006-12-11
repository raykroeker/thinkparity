/**
 * Created On: 3-Dec-06 1:53:05 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityBasicMenuItem.MenuStyle;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ThinkParityMenuItemUI extends BasicMenuItemUI {

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
        super.paintBackground(g, menuItem, bgColor);
        if (menuItem instanceof ThinkParityMenuItem) {
            if (((ThinkParityMenuItem)menuItem).getMenuStyle() == MenuStyle.WHITE_SPACE) {
                if (!((ThinkParityMenuItem)menuItem).isLast() && !((ThinkParityMenuItem)menuItem).isSeparatorNext()) {
                    Color oldColor = g.getColor();
                    g.setColor(Colors.Swing.MENU_BETWEEN_ITEMS_BG);
                    int menuWidth = menuItem.getWidth();
                    int menuHeight = menuItem.getHeight();
                    g.drawLine(0, menuHeight-1, menuWidth-1, menuHeight-1);
                    g.setColor(oldColor);
                }
            }
        }
    }   
}
