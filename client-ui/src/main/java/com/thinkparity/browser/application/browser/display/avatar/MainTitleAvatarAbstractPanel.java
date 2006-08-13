/*
 * Created On: Aug 12, 2006 3:05:19 PM
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import com.thinkparity.codebase.swing.AbstractJPanel;


/**
 * <b>Title:</b>thinkparity Main Title Panel Abstraction<br>
 * <b>Description:</b>An abstraction of the title panel that include the get\
 * set of the main title avatar and the installation of the move mouse listener.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class MainTitleAvatarAbstractPanel extends AbstractJPanel {

    /** The main title avatar. */
    protected MainTitleAvatar mainTitleAvatar;

    /** Used to drag the window by this avatar. */
    private final MouseInputAdapter mouseInputAdapter;

    /**
     * Create MainTitleAvatarAbstractPanel.
     * 
     * @param l18nContext
     *            A localization context.
     */
    protected MainTitleAvatarAbstractPanel(final String l18nContext) {
        super(l18nContext);
        this.mouseInputAdapter = new MouseInputAdapter() {
            int offsetX;
            int offsetY;
            public void mouseDragged(final MouseEvent e) {
                mainTitleAvatar.getController().moveBrowserWindow(
                        new Point(
                                e.getPoint().x - offsetX,
                                e.getPoint().y - offsetY));
            }
            public void mousePressed(final MouseEvent e) {
                offsetX = e.getPoint().x;
                offsetY = e.getPoint().y;
            }
        };
        addMouseListener(mouseInputAdapter);
        addMouseMotionListener(mouseInputAdapter);
    }

    /**
     * Obtain the mainTitleAvatar
     *
     * @return The MainTitleAvatar.
     */
    protected MainTitleAvatar getMainTitleAvatar() {
        return mainTitleAvatar;
    }

    /**
     * Set mainTitleAvatar.
     *
     * @param mainTitleAvatar The MainTitleAvatar.
     */
    public void setMainTitleAvatar(final MainTitleAvatar mainTitleAvatar) {
        this.mainTitleAvatar = mainTitleAvatar;
    }
}
