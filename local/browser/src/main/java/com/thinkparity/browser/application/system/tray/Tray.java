/*
 * Mar 18, 2006
 */
package com.thinkparity.browser.application.system.tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.Version;
import com.thinkparity.browser.application.system.SystemApplication;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.util.ImageIOUtil;

import com.thinkparity.model.parity.model.profile.Profile;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Tray {

	/**
	 * Read the system tray icon.
	 * 
	 * @return The image icon.
	 */
	private static Icon readTrayIcon() { return ImageIOUtil.readIcon("SystemTray.png"); }

    /** Indicates whether or not the system tray is installed. */
	private Boolean isInstalled;

    /** A menu builder for the system tray application. */
    private final TrayMenuBuilder menuBuilder;

    /** The system application. */
	private final SystemApplication systemApplication;

    /** The system tray. */
	private SystemTray systemTray;

    /** The system tray icon. */
	private TrayIcon systemTrayIcon;

    /** The local user's profile. */
    private final Profile profile;

    /**
	 * Create a Tray.
	 * 
	 * @param systemApplication
	 *            The system application.
	 */
	public Tray(final SystemApplication systemApplication, final Profile profile) {
		super();
        this.menuBuilder = new TrayMenuBuilder(systemApplication);
        this.profile = profile;
		this.isInstalled = Boolean.FALSE;
		this.systemApplication = systemApplication;
	}

    /**
     * Display a notification.
     * 
     * @param notification
     *            The tray notification.
     */
    public void display(final TrayNotification notification) {
        setCaption(notification);
        displayInfo(notification);
    }

    /** Install the system tray.*/
	public void install() {
		systemTrayIcon = new TrayIcon(readTrayIcon());
        systemTrayIcon.addBalloonActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
                if(systemApplication.isBrowserRunning())
                    systemApplication.runMoveBrowserToFront();
                else
                    systemApplication.runRestoreBrowser();
			}
		});
		systemTrayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
                if(systemApplication.isBrowserRunning())
                    systemApplication.runMoveBrowserToFront();
                else
                    systemApplication.runRestoreBrowser();
			}
		});
        systemTrayIcon.setPopupMenu(menuBuilder.createPopup());
		systemTrayIcon.setIconAutoSize(true);

		systemTray  = SystemTray.getDefaultSystemTray();
		systemTray.addTrayIcon(systemTrayIcon);
		isInstalled = Boolean.TRUE;

        reloadConnection(systemApplication.getConnection());
	}

    /**
     * Reload the connection information.
     *
     * @param cx
     *      The platform connection.
     */
    public void reloadConnection(final Platform.Connection cx) {
        systemApplication.logApiId();
        systemApplication.debugVariable("cx", cx);
        switch(cx) {
        case OFFLINE:
            updateMenuOffline();
            setCaption();
            break;
        case ONLINE:
            updateMenuOnline();
            setCaption();
            break;
        default:
            Assert.assertUnreachable("[UNKNOWN CONNECTION]");
        }
    }

    /** Uninstall the system tray. */
	public void unInstall() {
		systemTray.removeTrayIcon(systemTrayIcon);
		systemTrayIcon = null;
		systemTray = null;

		isInstalled = Boolean.FALSE;
	}

    /**
     * Determine whether or not the system tray is installed.
     * 
     * @return True if the system tray is installed; false otherwise.
     */
	Boolean isInstalled() { return isInstalled; }

    /**
     * Create the caption for the system tray. The caption will include the
     * platform's connection status.
     * 
     * @return A buffer containing the caption.
     */
    private StringBuffer createCaption() {
        switch(systemApplication.getConnection()) {
        case OFFLINE:
            return new StringBuffer(getString(Version.getMode() + ".OFFLINE",
                    new Object[] { profile.getName() }));
        case ONLINE:
            return new StringBuffer(getString(Version.getMode() + ".ONLINE",
                    new Object[] { profile.getName() }));
        default:
            throw Assert.createUnreachable("[UNKNOWN CONNECTION]");
        }
    }

    /**
     * Display an informational notification.
     * 
     * @param notification
     *            The notification.
     */
    private void displayInfo(final TrayNotification notification) {
        if(notification.getMessage().length() > 0) {
            systemTrayIcon.displayMessage(
                    systemApplication.getString("Notification.InfoCaption"),
                    notification.getMessage(), TrayIcon.INFO_MESSAGE_TYPE);
        }
    }

    /**
     * Obtain a localized string from the system application.
     * 
     * @param key
     *            A local key.
     * @param argument
     *            A formatting argument.
     * @return A localized string.
     */
    private String getString(final String key, final Object[] arguments) {
        return systemApplication.getString(key, arguments);
    }

    /**
     * Set the system tray icon caption.  The platform connection status will
     * always be displayed in the icon.
     * 
     */
    private void setCaption() {
        systemTrayIcon.setCaption(createCaption().toString());
    }

    /**
     * Set the system tray icon caption for a notification. The platform
     * connection status will always be displayed; in addition to the
     * notification.
     * 
     * @param notification
     *            The notification.
     */
    private void setCaption(final TrayNotification notification) {
        systemTrayIcon.setCaption(createCaption()
                .append(getString("LastUpdate", new Object[] {notification.getMessage()}))
                .toString());
    }

    /** Update the offline menu. */
    private void updateMenuOffline() {}

    /** Update the online menu. */
    private void updateMenuOnline() {}
}
