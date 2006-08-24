/**
 * Created On: 23-Aug-06 10:55:44 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JMenu;

import com.thinkparity.browser.Constants.Colors.Browser;

import com.thinkparity.codebase.swing.GradientPainter;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserMenu extends JMenu {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     * 
     */
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        try {
            GradientPainter.paintVertical(g2, getSize(),
                    Browser.MainTitleTop.BG_GRAD_START,
                    Browser.MainTitleTop.BG_GRAD_FINISH);
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
    }   
}
