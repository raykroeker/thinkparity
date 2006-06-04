/*
 * Created On: Fri Jun 02 2006 15:59 PDT
 * $Id$
 */
package com.thinkparity.browser.profile;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import com.thinkparity.browser.javax.swing.AbstractJDialog;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;

/**
 *
 * @author raymond@thinkparity.com
 * @revision $Revision$
 */
class ProfileManagerDialog extends AbstractJDialog {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** A border registry. */
    private final BorderRegistry borderRegistry;

    /** Create ProfileManagerDialog. */
    ProfileManagerDialog(final ProfileManagerWindow window) {
        super(window, Boolean.TRUE, "ProfileManagerDialog");
        this.borderRegistry = new BorderRegistry();
        setUndecorated(true);
    }

    /**
	 * Calculate the location for the window based upon its owner and its size.
	 * 
	 * @return The location of the window centered on the owner.
	 */
	protected Point calculateLocation() {
        final Dimension os = getOwner().getSize();
        final Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension ws = getSize();
        if(0 == os.width || 0 == os.height) {
            final Point l = getLocation();
            l.x = (ss.width - ws.width) / 2;
            l.y = (ss.height - ws.height) / 2;
            return l;
        }
        else {
            final Point l = getOwner().getLocation();
            l.x += (os.width - ws.width) / 2;
            l.y += (os.height - ws.height) / 2;
    
            if(l.x + ws.width > (ss.width)) { l.x = ss.width - ws.width; }
            if(l.y + ws.height > (ss.height)) { l.y = ss.height - ws.height; }
    
            if(l.x < 0) { l.x = 0; }
            if(l.y < 0) { l.y = 0; }
            return l;
        }
	}

    /**
     * Open the avatar in the profile window.
     * 
     * @param avatar
     *            The profile manager avatar.
     */
    void open(final String title, final Avatar avatar) {
        getRootPane().setBorder(borderRegistry.get(avatar.getClass()));
        add(avatar);
        pack();
        avatar.reload();
        setLocation(calculateLocation());
        setVisible(true);
    }
}
