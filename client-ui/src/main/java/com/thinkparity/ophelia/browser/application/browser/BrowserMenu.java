/**
 * Created On: 23-Aug-06 10:55:44 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.border.DropShadowBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.Constants.Dimensions;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityPopupMenu;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserMenu extends JMenu implements ThinkParityPopupMenu {

    /** A <code>DropShadowBorder</code>. */
    private final DropShadowBorder dropShadowBorder;

    /** A <code>MenuBackgroundType</code>. */
    private MenuBackgroundType menuBackgroundType;

    /**
     * Create BrowserMenu.
     * 
     * @param text
     *          Menu text.
     */
    public BrowserMenu(final String text) throws AWTException {
        super(text);
        this.menuBackgroundType = MenuBackgroundType.NORMAL;
        this.dropShadowBorder = new DropShadowBorder(Colors.Swing.MENU_BG);
        getPopupMenu().setBorder(dropShadowBorder);

        // Make it transparent. The override on paintComponent will paint a gradient.
        setBackground(new Color(255, 255, 255, 0));

        getPopupMenu().addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuCanceled(final PopupMenuEvent e) {                
            }
            public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {                
            }
            public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
                final Point p = getPopupMenuOrigin();
                final Dimension d = getPopupMenu().getPreferredSize();
                dropShadowBorder.paintUnderneathBorder(BrowserMenu.this, p.x, p.y, d.width, d.height);
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
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     * 
     */
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        try {
            GradientPainter.paintVertical(g2, getSize(),
                    Colors.Browser.MainTitleTop.BG_GRAD_START,
                    Colors.Browser.MainTitleTop.BG_GRAD_FINISH);
        }
        finally { g2.dispose(); }
        super.paintComponent(g);        
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
