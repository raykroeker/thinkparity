/**
 * Created On: 21-Dec-07 4:52:48 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.system.SystemApplication;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.util.State;
import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public abstract class SystemAvatar extends Avatar {

    /** A background image. */
    private static final BufferedImage BACKGROUND;

    static {
        BACKGROUND = ImageIOUtil.read("Dialog_Background.png");
    }

    /** The scaled background image. */
    private Image scaledBackground;

    /** The scaled backgroung image size. */
    private Dimension scaledSize;

    /**
     * Create SystemAvatar.
     * 
     * @param l18nContext
     *            A localization context <code>String</code>.
     */
    public SystemAvatar(final String l18nContext) {
        super(l18nContext);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getState()
     */
    @Override
    public State getState() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.ophelia.browser.platform.util.State)
     */
    @Override
    public void setState(final State state) {
    }

    /**
     * Obtain the avatar's application.
     * 
     * @return A <code>SystemApplication</code>.
     */
    protected final SystemApplication getApplication() {
        /* TODO raymond@thinkparity.com - 2007-12-22
         * move as to the avatar abstraction and make generic */
        return (SystemApplication) applicationRegistry.get(ApplicationId.SYSTEM);
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        // The preferred size is used as the minimum image size. This improves
        // the look and performance of the dialog during animation.
        if (null == scaledBackground || null == scaledSize ||
                getSize().width > scaledSize.width ||
                getSize().height > scaledSize.height) {
            scaledSize = new Dimension(
                    Math.max(getPreferredSize().width, getSize().width),
                    Math.max(getPreferredSize().height, getSize().height));
            scaledBackground = BACKGROUND.getScaledInstance(scaledSize.width,
                    scaledSize.height, Image.SCALE_SMOOTH);
        }
        g.drawImage(scaledBackground, 0, 0, null);

        // These images help to make the rounded corners look good.
        // Draw them only when the panel is full size. This helps the look of animation.
        if (getSize().height >= getPreferredSize().height) {
            g.drawImage(Images.BrowserTitle.SYSTEM_DIALOG_NO_TITLE_TOP_LEFT_INNER,
                    0,
                    0,
                    Images.BrowserTitle.SYSTEM_DIALOG_NO_TITLE_TOP_LEFT_INNER.getWidth(),
                    Images.BrowserTitle.SYSTEM_DIALOG_NO_TITLE_TOP_LEFT_INNER.getHeight(), this);
            g.drawImage(Images.BrowserTitle.SYSTEM_DIALOG_NO_TITLE_TOP_RIGHT_INNER,
                    getSize().width - Images.BrowserTitle.SYSTEM_DIALOG_NO_TITLE_TOP_RIGHT_INNER.getWidth(),
                    0,
                    Images.BrowserTitle.SYSTEM_DIALOG_NO_TITLE_TOP_RIGHT_INNER.getWidth(),
                    Images.BrowserTitle.SYSTEM_DIALOG_NO_TITLE_TOP_RIGHT_INNER.getHeight(), this);
            g.drawImage(Images.BrowserTitle.SYSTEM_DIALOG_BOTTOM_LEFT_INNER,
                    0,
                    getSize().height - Images.BrowserTitle.SYSTEM_DIALOG_BOTTOM_LEFT_INNER.getHeight(),
                    Images.BrowserTitle.SYSTEM_DIALOG_BOTTOM_LEFT_INNER.getWidth(),
                    Images.BrowserTitle.SYSTEM_DIALOG_BOTTOM_LEFT_INNER.getHeight(), this);
            g.drawImage(Images.BrowserTitle.SYSTEM_DIALOG_BOTTOM_RIGHT_INNER,
                    getSize().width - Images.BrowserTitle.SYSTEM_DIALOG_BOTTOM_RIGHT_INNER.getWidth(),
                    getSize().height - Images.BrowserTitle.SYSTEM_DIALOG_BOTTOM_RIGHT_INNER.getHeight(),
                    Images.BrowserTitle.SYSTEM_DIALOG_BOTTOM_RIGHT_INNER.getWidth(),
                    Images.BrowserTitle.SYSTEM_DIALOG_BOTTOM_RIGHT_INNER.getHeight(), this);
        }
    }
}
