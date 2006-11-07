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
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.border.DropShadowBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserMenu extends JMenu {
    
    /** A drop shadow border. */
    final DropShadowBorder dropShadowBorder;
    
    /**
     * @param text
     *          Menu text.
     */
    public BrowserMenu(final String text) throws AWTException {
        super(text);
        
        // Make it transparent. The override on paintComponent will paint a gradient.
        setBackground(new Color(255, 255, 255, 0));
        
        // Set up the shadow border on the popup menu
        final Color[] colors = {Colors.Browser.Menu.MENU_BORDER, Colors.Swing.MENU_BG, Colors.Swing.MENU_BG};
        dropShadowBorder = new DropShadowBorder(colors, 3);
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
}
