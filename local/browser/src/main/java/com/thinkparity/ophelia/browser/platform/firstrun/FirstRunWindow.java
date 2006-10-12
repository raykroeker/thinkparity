/*
 * Created on June 10, 2006, 11:17 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.thinkparity.codebase.swing.AbstractJFrame;

import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.application.window.WindowBorder;

/**
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
class FirstRunWindow extends AbstractJFrame {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * Create FirstRunWindow.
     *
     */
    public FirstRunWindow() {
        super("FirstRunWindow");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(com.thinkparity.ophelia.browser.Constants.Images.WINDOW_ICON_IMAGE);
        setResizable(false);
        setUndecorated(true);
    }

    /**
     * Calculate the location for the window based upon its owner and its size.
     * 
     * @return The location of the window centered on the owner.
     */
    protected Point calculateLocation() {
        final Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension ws = getSize();

        final Point l = getLocation();
        l.x = (ss.width - ws.width) / 2;
        l.y = (ss.height - ws.height) / 2;

        if(l.x + ws.width > (ss.width)) { l.x = ss.width - ws.width; }
        if(l.y + ws.height > (ss.height)) { l.y = ss.height - ws.height; }

        if(l.x < 0) { l.x = 0; }
        if(l.y < 0) { l.y = 0; }
        return l;
	}

    /**
     * Open the avatar in the profile window.
     * 
     * @param avatar
     *            The profile manager avatar.
     */
    void open(final String title, final Avatar avatar) {
        getRootPane().setBorder(new WindowBorder());
        setTitle(title);
        add(avatar);
        pack();
        avatar.reload();
        setLocation(calculateLocation());
        setVisible(true);
    }
}
