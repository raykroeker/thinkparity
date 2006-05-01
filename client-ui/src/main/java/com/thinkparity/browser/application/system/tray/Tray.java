/*
 * Mar 18, 2006
 */
package com.thinkparity.browser.application.system.tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import com.thinkparity.browser.application.system.SystemApplication;
import com.thinkparity.browser.platform.util.ImageIOUtil;

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

    /**
	 * Create a Tray.
	 * 
	 * @param systemApplication
	 *            The system application.
	 */
	public Tray(final SystemApplication systemApplication) {
		super();
        this.menuBuilder = new TrayMenuBuilder(systemApplication);
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

        updateMenu();
	}                                                                   

    /** Update the menu for the system tray. */
    public void updateMenu() { updateMenu(systemApplication.isOnline()); }

    /**
     * Update the menu for the system tray.
     *
     * @param isOnline
     *      A flag indicating whether or not the application is
     *      online.
     */
    public void updateMenu(final Boolean isOnline) {
        if(isOnline) {
            menuBuilder.login.setEnabled(false);
            menuBuilder.logout.setEnabled(true);
        }
        else {
            menuBuilder.login.setEnabled(true);
            menuBuilder.logout.setEnabled(false);
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
     * Display an informational notification.
     * 
     * @param notification
     *            The notification.
     */
    private void displayInfo(final TrayNotification notification) {
        systemTrayIcon.displayMessage(
                systemApplication.getString("Notification.InfoCaption"),
                notification.getMessage(), TrayIcon.INFO_MESSAGE_TYPE);
    }

    /**
     * Set the system tray icon caption.
     * 
     * @param notification
     *            The notification.
     */
    private void setCaption(final TrayNotification notification) {
        systemTrayIcon.setCaption(notification.getMessage());
    }
}
