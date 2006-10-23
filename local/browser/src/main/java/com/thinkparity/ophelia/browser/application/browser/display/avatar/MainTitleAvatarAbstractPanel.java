/*
 * Created On: Aug 12, 2006 3:05:19 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.event.MouseEvent;

import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;


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

    /**
     * Create MainTitleAvatarAbstractPanel.
     * 
     * @param l18nContext
     *            A localization context.
     */
    protected MainTitleAvatarAbstractPanel() {
        super();
        
        // Double click to maximize the browser window
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(final java.awt.event.MouseEvent e) {
                if (e.getButton()==MouseEvent.BUTTON1) {
                    if (e.getClickCount() % 2 == 0) {
                        getBrowser().maximize();
                    }
                }
            }
        });
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
    protected void setMainTitleAvatar(final MainTitleAvatar mainTitleAvatar) {
        this.mainTitleAvatar = mainTitleAvatar;
    }
    
    /**
     * Obtain the thinkParity browser application.
     * 
     * @return The thinkParity browser application.
     */
    protected Browser getBrowser() {
        final ApplicationRegistry applicationRegistry = new ApplicationRegistry();
        return (Browser) applicationRegistry.get(ApplicationId.BROWSER);
    }
}
