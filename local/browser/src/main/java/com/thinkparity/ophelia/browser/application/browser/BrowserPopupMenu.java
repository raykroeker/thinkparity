/**
 * Created On: 4-Dec-06 2:24:56 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.codebase.swing.border.DropShadowBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.Constants.Dimensions;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityPopupMenu;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserPopupMenu extends JPopupMenu implements ThinkParityPopupMenu {

    /** A <code>DropShadowBorder</code>. */
    private final DropShadowBorder dropShadowBorder;

    /** A <code>MenuBackgroundType</code>. */
    private MenuBackgroundType menuBackgroundType;

    /**
     * Create BrowserPopupMenu.
     *
     * @throws AWTException
     */
    public BrowserPopupMenu() throws AWTException {
        super();
        this.menuBackgroundType = MenuBackgroundType.NORMAL;
        this.dropShadowBorder = new DropShadowBorder(Colors.Swing.MENU_BG);
        setBorder(dropShadowBorder);
    }

    /**
     * @see javax.swing.JPopupMenu#add(javax.swing.JMenuItem)
     */
    @Override
    public JMenuItem add(final JMenuItem menuItem) {
        setPreferredWidth(menuItem);
        return super.add(menuItem);
    }

    /**
     * @see com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityPopupMenu#getMenuBackgroundType()
     */
    public MenuBackgroundType getMenuBackgroundType() {
        return menuBackgroundType;
    }

    /**
     * @see com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityPopupMenu#setMenuBackgroundType(com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityPopupMenu.MenuBackgroundType)
     */
    public void setMenuBackgroundType(final MenuBackgroundType menuBackgroundType) {
        this.menuBackgroundType = menuBackgroundType;
    }

    /**
     * @see javax.swing.JPopupMenu#show(java.awt.Component, int, int)
     * 
     */
    @Override
    public void show(final Component invoker, final int x, final int y) {
        final Dimension preferredSize = getPreferredSize();
        dropShadowBorder.paintUnderneathBorder(invoker, x, y,
                preferredSize.width, preferredSize.height);
        super.show(invoker, x, y);
    }

    /**
     * Set the preferred width on the menu item.
     */
    private void setPreferredWidth(final JMenuItem menuItem) {
        final Dimension preferredSize = menuItem.getPreferredSize();
        if (preferredSize.width < Dimensions.PopupMenu.MINIMUM_WIDTH) {
            preferredSize.width = Dimensions.PopupMenu.MINIMUM_WIDTH;
            menuItem.setPreferredSize(preferredSize);     
        }
    }  
}
