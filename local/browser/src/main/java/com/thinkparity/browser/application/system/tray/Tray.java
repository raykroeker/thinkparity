/*
 * Mar 18, 2006
 */
package com.thinkparity.browser.application.system.tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

import org.apache.log4j.Logger;

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import com.thinkparity.browser.application.system.SystemApplication;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.util.ImageIOUtil;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.codebase.assertion.Assert;

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

    /** An apache logger. */
    protected final Logger logger;

    /**
	 * Create a Tray.
	 * 
	 * @param systemApplication
	 *            The system application.
	 */
	public Tray(final SystemApplication systemApplication) {
		super();
        this.logger = LoggerFactory.getLogger(getClass());
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

        reloadConnectionStatus(systemApplication.getConnection());
	}

    /**
     * Reload the connection information.
     *
     * @param cx
     *      The platform connection.
     */
    public void reloadConnectionStatus(final Platform.Connection cx) {
        logger.info("[LBROWSER] [APPLICATION] [SYSTEM] [TRAY] [RELOAD CONNECTION]");
        logger.debug(cx);
        if(Platform.Connection.OFFLINE == cx) { updateMenuOffline(); }
        else if(Platform.Connection.ONLINE == cx) { updateMenuOnline(); }
        else { Assert.assertUnreachable("[LBROWSER] [APPLICATION] [SYSTEM] [TRAY] [RELOAD CONNECTION]"); }
    }

    /** Update the offline menu. */
    private void updateMenuOffline() {
        logger.info("[LBROWSER] [APPLICATION] [SYSTEM] [TRAY] [UPDATE MENU OFFLINE]");
        menuBuilder.editProfile.setEnabled(false);
        menuBuilder.logout.setEnabled(false);

        menuBuilder.login.setEnabled(true);
    }

    /** Update the online menu. */
    private void updateMenuOnline() {
        logger.info("[LBROWSER] [APPLICATION] [SYSTEM] [TRAY] [UPDATE MENU ONLINE]");
        menuBuilder.editProfile.setEnabled(true);
        menuBuilder.logout.setEnabled(true);

        menuBuilder.login.setEnabled(false);
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
