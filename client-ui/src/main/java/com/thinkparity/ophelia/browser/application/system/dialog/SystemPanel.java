/**
 * Created On: Mar 15, 2007 1:27:33 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public abstract class SystemPanel extends AbstractJPanel {

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
