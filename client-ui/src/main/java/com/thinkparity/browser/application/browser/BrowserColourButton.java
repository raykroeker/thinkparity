/**
 * Created On: 24-Aug-06 4:42:33 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Action;
import javax.swing.JButton;

import com.thinkparity.browser.Constants.Colors.Browser;

import com.thinkparity.codebase.swing.GradientPainter;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserColourButton extends JButton {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    // Paint colours
    private static final Color COLOR_NORMAL;
    private static final Color COLOR_ROLLOVER;
    
    static {
        COLOR_NORMAL = new Color(247, 234, 202);
        COLOR_ROLLOVER = new Color(255, 234, 175);
    }
    private Color currentColor = COLOR_NORMAL;
    
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
            g2.setColor(currentColor);
            g2.fill3DRect(5, 2, getWidth()-10, getHeight()-5,true);
        }
        finally { g2.dispose(); }
        super.paintComponent(g);        
    }

    /**
     * @param action
     *          The button action
     */
    public BrowserColourButton(Action action) {
        super(action);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = COLOR_NORMAL;
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                currentColor = COLOR_ROLLOVER;
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                currentColor = COLOR_NORMAL;
            }
        });
    }    
}
