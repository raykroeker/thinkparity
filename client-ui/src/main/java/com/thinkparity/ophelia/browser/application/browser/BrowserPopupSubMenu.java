/**
 * Created On: 11-Dec-06 12:47:13 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.thinkparity.codebase.swing.border.DropShadowBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.Constants.Dimensions;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityPopupMenu;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityPopupSubMenu;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserPopupSubMenu extends ThinkParityPopupSubMenu implements ThinkParityPopupMenu {

    /** A <code>DropShadowBorder</code>. */
    private final DropShadowBorder dropShadowBorder;

    /** A <code>MenuBackgroundType</code>. */
    private MenuBackgroundType menuBackgroundType;

    /**
     * Create BrowserPopupSubMenu.
     * 
     * @param text
     *          Menu text.
     */
    public BrowserPopupSubMenu(final String text) throws AWTException {
        super(text);
        this.menuBackgroundType = MenuBackgroundType.NORMAL;
        this.dropShadowBorder = new DropShadowBorder(Colors.Swing.MENU_BG);
        getPopupMenu().setBorder(dropShadowBorder);

        getPopupMenu().addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuCanceled(final PopupMenuEvent e) {                
            }
            public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {                
            }
            public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
                final Point p = getPopupMenuOrigin();
                final Dimension d = getPopupMenu().getPreferredSize();
                dropShadowBorder.paintUnderneathBorder(BrowserPopupSubMenu.this, p.x, p.y, d.width, d.height);
            }            
        });
    }

    /**
     * @see javax.swing.JMenu#add(javax.swing.JMenuItem)
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
