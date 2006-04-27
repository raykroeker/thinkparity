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

    /** The status display background image. */
    private static final BufferedImage BG_IMAGE;

    /** An alternate background image. */
    private static final BufferedImage BG_IMAGE_ALT;

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    static {
        BG_IMAGE = ImageIOUtil.read("StatusDisplay.png");
        BG_IMAGE_ALT = ImageIOUtil.read("StatusDisplay_Alternate.png");
    }

    /** The current background image. */
    private BufferedImage backgroundImage;

    /** Create a StatusDisplay. */
    public StatusDisplay() {
        super("StatusDisplay", Color.WHITE);
        this.backgroundImage = BG_IMAGE;
    }

    public void toggleImage() {
        if(backgroundImage.equals(BG_IMAGE)) {
            backgroundImage = BG_IMAGE_ALT;
        }
        else { backgroundImage = BG_IMAGE; }

        repaint();
    }

    /** @see com.thinkparity.browser.platform.application.display.Display#getId() */
    public DisplayId getId() { return DisplayId.STATUS; }

    /** @see javax.swing.JComponent#paintComponent(java.awt.Graphics) */
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        try { g2.drawImage(backgroundImage, getInsets().left, getInsets().top, this); }
        finally { g2.dispose(); }
    }
}
