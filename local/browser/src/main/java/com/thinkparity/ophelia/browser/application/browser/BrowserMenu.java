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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.border.DropShadowBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityBasicMenuItem;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserMenu extends JMenu {
    
    /** A drop shadow border. */
    private final DropShadowBorder dropShadowBorder;
    
    /** List of menu items in this JMenu. */
    private final List<ThinkParityBasicMenuItem> thinkParityMenuItems;
    
    /**
     * @param text
     *          Menu text.
     */
    public BrowserMenu(final String text) throws AWTException {
        super(text);       
        this.thinkParityMenuItems = new ArrayList<ThinkParityBasicMenuItem>();
        
        // Make it transparent. The override on paintComponent will paint a gradient.
        setBackground(new Color(255, 255, 255, 0));
        
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
                dropShadowBorder.paintUnderneathBorder(BrowserMenu.this, p.x, p.y, d.width, d.height);
            }            
        });
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
