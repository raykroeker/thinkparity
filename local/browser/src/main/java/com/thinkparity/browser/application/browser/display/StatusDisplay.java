/*
 * Apr 11, 2006
 */
package com.thinkparity.browser.application.browser.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.thinkparity.browser.javax.swing.border.MultiLineBorder;
import com.thinkparity.browser.platform.application.display.Display;
import com.thinkparity.browser.platform.util.ImageIOUtil;

/**
 * The status display contains a single status avatar.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class StatusDisplay extends Display {

    /** The source colour of the gradient. */
    private static final BufferedImage BG_IMAGE;

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    static { BG_IMAGE = ImageIOUtil.read("StatusDisplay.png"); }

    /** Create a StatusDisplay. */
    public StatusDisplay() {
        super("StatusDisplay", Color.WHITE);
        // BORDER Status Bar Multiline Top 137,139,142,255 1; 238,238,238,255 1; 
        setBorder(new MultiLineBorder(new Color[] {new Color(137, 139, 142, 255), new Color(238, 238, 238, 255)}));
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.Display#getId()
     * 
     */
    public DisplayId getId() { return DisplayId.STATUS; }

    /** @see javax.swing.JComponent#paintComponent(java.awt.Graphics) */
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        try { g2.drawImage(BG_IMAGE, getInsets().left, getInsets().top, this); }
        finally { g2.dispose(); }
    }
}
