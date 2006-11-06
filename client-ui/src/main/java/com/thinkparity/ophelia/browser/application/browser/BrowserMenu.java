/**
 * Created On: 23-Aug-06 10:55:44 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.border.MultiColourLineBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserMenu extends JMenu {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /** The thinkParity application registry. */
    private final ApplicationRegistry applicationRegistry;
    
    /** An application. */
    public final Browser browser;
    
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
     * @param text
     *          Menu text.
     */
    public BrowserMenu(final String text) {
        super(text);
        this.applicationRegistry = new ApplicationRegistry();
        this.browser = (Browser)applicationRegistry.get(ApplicationId.BROWSER);
        
        // Make it transparent. The override on paintComponent will paint a gradient.
        setBackground(new Color(255, 255, 255, 0));
        
        // Set up the shadow border on the popup menu
        final Color[] colors = {Colors.Browser.Menu.MENU_BORDER, Colors.Swing.MENU_BG, Colors.Swing.MENU_BG};
        getPopupMenu().setBorder(new MultiColourLineBorder(colors, 3, Boolean.TRUE));
        
        // Force an immediate redraw of the browser title area when switching menus
        // so the shadow menu doesn't pick up any artifacts from the previous menu.
        getPopupMenu().addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuCanceled(final PopupMenuEvent e) {   
            }
            public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {    
            }
            public void popupMenuWillBecomeVisible(final PopupMenuEvent e) { 
                browser.paintMainTitleImmediately();
                browser.paintMenuBarImmediately();
            }            
        });
    }   
}
