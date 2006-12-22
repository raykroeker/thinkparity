/**
 * Created On: 11-Dec-06 12:47:13 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.thinkparity.codebase.swing.border.DropShadowBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.Constants.Dimensions;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityBasicMenuItem;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityPopupSubMenu;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserPopupSubMenu extends ThinkParityPopupSubMenu {
    
    /** A drop shadow border. */
    private final DropShadowBorder dropShadowBorder;
    
    /** List of menu items in this JMenu. */
    private final List<ThinkParityBasicMenuItem> thinkParityMenuItems;
    
    /**
     * @param text
     *          Menu text.
     */
    public BrowserPopupSubMenu(final String text) throws AWTException {
        super(text);       
        this.thinkParityMenuItems = new ArrayList<ThinkParityBasicMenuItem>();
        
        // Set up the shadow border on the popup menu
        dropShadowBorder = new DropShadowBorder(Colors.Swing.MENU_BG);
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
        for (final ThinkParityBasicMenuItem earlierMenuItem : thinkParityMenuItems) {
            earlierMenuItem.setLast(Boolean.FALSE);            
        }
        if (menuItem instanceof ThinkParityBasicMenuItem) {
            ((ThinkParityBasicMenuItem)menuItem).setLast(Boolean.TRUE);
            thinkParityMenuItems.add((ThinkParityBasicMenuItem)menuItem);
        }
        setPreferredWidth(menuItem);
        return super.add(menuItem);
    }
    
    /**
     * @see javax.swing.JPopupMenu#addSeparator()
     */
    @Override
    public void addSeparator() {
        if (thinkParityMenuItems.size() > 0) {
            thinkParityMenuItems.get(thinkParityMenuItems.size()-1).setSeparatorNext(Boolean.TRUE);
        }
        super.addSeparator();
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
