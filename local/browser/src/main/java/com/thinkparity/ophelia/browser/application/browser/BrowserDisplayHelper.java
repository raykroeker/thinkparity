/*
 * Created On: Sep 24, 2006 10:52:33 AM
 */
package com.thinkparity.ophelia.browser.application.browser;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.application.browser.display.DisplayId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.display.Display;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;

/**
 * The browser display helper is used by the browser application to display
 * avatars on displays (in the main window) and on windows.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class BrowserDisplayHelper extends BrowserHelper {

    /** The <code>BrowserWindow</code>. */
    private BrowserWindow browserWindow;

    /**
     * Create BrowserDisplayHelper.
     */
    BrowserDisplayHelper(final Browser browserApplication) {
        super(browserApplication);
    }

    /**
     * Display an avatar on the status display.
     * 
     * @param id
     *            An avatar id.
     */
    void displayStatus(final AvatarId id) {
        logger.logApiId();
        displayAvatar(id, DisplayId.STATUS);
    }

    /**
     * Display a tab avatar on the content display.
     * 
     * @param id
     *            An avatar id.
     */
    void displayTab(final AvatarId id) {
        logger.logApiId();
        displayAvatar(id, DisplayId.CONTENT);
    }

    /**
     * Display an avatar on the tile display.
     * 
     * @param id
     *            An avatar id.
     */
    void displayTitle(final AvatarId id) {
        logger.logApiId();
        displayAvatar(id, DisplayId.TITLE);
    }

    /**
     * Obtain the browserWindow
     *
     * @return The BrowserWindow.
     */
    BrowserWindow getBrowserWindow() {
        return browserWindow;
    }

    /**
     * Set browserWindow.
     *
     * @param browserWindow The BrowserWindow.
     */
    void setBrowserWindow(final BrowserWindow browserWindow) {
        this.browserWindow = browserWindow;
    }

    /**
     * Display an avatar on a display.
     * 
     * @param avatar
     *            An <code>Avatar</code>.
     * @param displayId
     *            A <code>Display</code>.
     */
    private void displayAvatar(final Avatar avatar, final Data input,
            final DisplayId displayId) {
        Assert.assertNotNull(avatar, "Avatar is null.");
        Assert.assertNotNull(displayId, "Display id is null.");
        final Display display = browserWindow.getDisplay(displayId);

        if (null == input) {
            logger.logWarning("Input for avatar {0} on display {1} is null.",
                    avatar.getId(), displayId);
        } else {
            avatar.setInput(input);
        }

        display.setAvatar(avatar);
        display.displayAvatar();
        avatar.reload();
        display.revalidate();
        display.repaint();
        avatar.requestFocusInWindow();
    }

    /**
     * Display an avatar on a display.
     * 
     * @param id
     *            An <code>AvatarId</code>.
     * @param input
     *            An avatar's input <code>Data</code>.
     * @param displayId
     *            A <code>DisplayId</code>.
     */
    private void displayAvatar(final AvatarId id, final DisplayId displayId) {
        final Data input = (Data) browserApplication.getAvatarInput(id);
        displayAvatar(browserApplication.getAvatar(id), input, displayId);
    }
}
