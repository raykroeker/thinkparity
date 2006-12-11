/**
 * Created On: 4-Dec-06 2:24:56 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.codebase.swing.border.DropShadowBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityBasicMenuItem;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserPopupMenu extends JPopupMenu {
    
    /** A drop shadow border. */
    private final DropShadowBorder dropShadowBorder;
    
    /** List of menu items in this JMenu. */
    private final List<ThinkParityBasicMenuItem> thinkParityMenuItems;
    
    /**
     * Create ShadowPopupMenu.
     *
     * @throws AWTException
     */
    public BrowserPopupMenu() throws AWTException {
        super();
        this.thinkParityMenuItems = new ArrayList<ThinkParityBasicMenuItem>();
        this.dropShadowBorder = new DropShadowBorder(Colors.Swing.MENU_BG);
        setBorder(dropShadowBorder);
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
     * @see javax.swing.JPopupMenu#add(javax.swing.JMenuItem)
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
}
