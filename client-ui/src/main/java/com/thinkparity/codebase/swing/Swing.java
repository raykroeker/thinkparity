/*
 * Created On: Jun 3, 2006 2:16:48 PM
 * $Id$
 */
package com.thinkparity.codebase.swing;

import java.awt.image.BufferedImage;

import javax.swing.UIManager;

import com.thinkparity.browser.platform.util.ImageIOUtil;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

/**
 * thinkParity Swing
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Swing {

    /**
     * Initialize thinkParity Swing.
     *
     */
    public static void init() {
        System.setProperty("sun.awt.noerasebackground", "true");

        try { UIManager.setLookAndFeel(new WindowsLookAndFeel()); }
        catch(final Throwable t) { throw new RuntimeException(t); }
    }

    /** Create Swing. */
    private Swing() { super(); }

    public static final class Constants {
        public static final class Images {
            public static final BufferedImage WINDOW_ICON_IMAGE = ImageIOUtil.read("ThinkParity32x32.png");
        }
    }
}
