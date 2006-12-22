/**
 * Created On: 6-Dec-06 2:33:36 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.profile;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.Browser;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
abstract class EditProfileAvatarAbstractTabPanel extends AbstractJPanel {
    
    /** The browser. */
    final Browser browser;
    
    /** The edit profile avatar. */
    final EditProfileAvatar avatar;
    
    /** The tab name. */
    final String tabName;
    
    /** The clipped background image. */
    private BufferedImage clippedBackgroundImage = null;
    
    /** The scaled background image. */
    private BufferedImage scaledBackgroundImage = null;
    
    /**
     * Create EditProfileAvatarAbstractTabPanel.
     */
    protected EditProfileAvatarAbstractTabPanel(final Browser browser, final EditProfileAvatar avatar, final String tabName) {
        super();
        this.browser = browser;
        this.avatar = avatar;
        this.tabName = tabName;
    }
        
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics g2 = g.create();
        try {
            final int extra = 5;
            
            // Scale the background image to the correct size or slightly larger.
            // For performance reasons, don't use scaling to resize with every pixel change.
            if ((null == scaledBackgroundImage)
                    || (scaledBackgroundImage.getWidth() < getWidth())
                    || (scaledBackgroundImage.getWidth() > (getWidth() + extra))
                    || (scaledBackgroundImage.getHeight() < getHeight())
                    || (scaledBackgroundImage.getHeight() > (getHeight() + extra))) {
                final Image image = Images.BrowserTitle.DIALOG_BACKGROUND.getScaledInstance(
                        getWidth() + extra, getHeight() + extra, Image.SCALE_FAST);
                scaledBackgroundImage = new BufferedImage(getWidth() + extra, getHeight() + extra, BufferedImage.TYPE_INT_RGB);
                final Graphics gImage = scaledBackgroundImage.createGraphics();
                try {
                    gImage.drawImage(image, 0, 0, null);
                }
                finally { gImage.dispose(); }
            }
            
            // Clip the image to the exact size.
            if (null != scaledBackgroundImage) {
                if ((null == clippedBackgroundImage)
                        || (clippedBackgroundImage.getWidth() != getWidth())
                        || (clippedBackgroundImage.getHeight() != getHeight())) {
                    clippedBackgroundImage = scaledBackgroundImage.getSubimage(
                            0, 0, getWidth(), getHeight());
                }
            }
            
            // Draw the background image.
            if (null != clippedBackgroundImage) {                
                g2.drawImage(clippedBackgroundImage, 0, 0, getWidth(), getHeight(), null);
            }
        }
        finally { g2.dispose(); }
    }

    /**
     * Get the Browser.
     * 
     * @return Browser.
     */
    protected Browser getController() {
        return browser;
    }
    
    /**
     * Get the avatar.
     * 
     * @return EditProfileAvatar.
     */
    protected EditProfileAvatar getAvatar() {
        return avatar;
    }
       
    /**
     * @return the tabName
     */
    protected String getTabName() {
        return tabName;
    }

    /**
     * Reload the panel.
     * 
     * @param profile
     *            The local user profile.
     * @param emails
     *            The local user emails.         
     */
    protected void reload(final Profile profile, final List<ProfileEMail> emails) {}
    
    /**
     * Save the panel.
     * 
     */
    protected void save() {}
}
