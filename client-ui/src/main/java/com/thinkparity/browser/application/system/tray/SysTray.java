/*
 * Mar 18, 2006
 */
package com.thinkparity.browser.application.system.tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import com.thinkparity.browser.application.system.SysApp;
import com.thinkparity.browser.platform.util.ImageIOUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SysTray {

	/**
	 * Read the system tray icon.
	 * 
	 * @return The image icon.
	 */
	private static Icon readTrayIcon() { return ImageIOUtil.readIcon("SystemTray.png"); }

	/** Indicates whether or not the system tray is installed. */
	private Boolean isInstalled;

    /** A menu builder for the system tray application. */
    private final MenuBuilder menuBuilder;

    /** The system application. */
	private final SysApp sysApp;

    /** The system tray. */
	private SystemTray systemTray;

    /** The system tray icon. */
	private TrayIcon systemTrayIcon;

    /**
	 * Create a SysTray.
	 * 
	 * @param sysApp
	 *            The system application.
	 */
	public SysTray(final SysApp sysApp) {
		super();
        this.menuBuilder = new MenuBuilder(sysApp);
		this.isInstalled = Boolean.FALSE;
		this.sysApp = sysApp;
	}

    public void display(final SysTrayNotification notification) {
        displayInfo(notification);
    }

    /** Install the system tray.*/
	public void install() {
		systemTrayIcon = new TrayIcon(readTrayIcon());
		systemTrayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
                sysApp.runRestoreBrowser();
			}
		});
        systemTrayIcon.setPopupMenu(menuBuilder.createPopup());
		systemTrayIcon.setIconAutoSize(true);

		systemTray  = SystemTray.getDefaultSystemTray();
		systemTray.addTrayIcon(systemTrayIcon);
		isInstalled = Boolean.TRUE;
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
     * Set the current queue count.
     * 
     * @param queueCount
     *            The queue count.
     */
    void setQueueCount(final Integer queueCount) {
        if(0 == queueCount) { systemTrayIcon.setCaption(getCaption0()); }
        else if(1 == queueCount) { systemTrayIcon.setCaption(getCaption1()); }
        else { systemTrayIcon.setCaption(getCaptionN(queueCount)); }
    }

    private void displayInfo(final SysTrayNotification notification) {
        systemTrayIcon.displayMessage(
                sysApp.getString("Notification.InfoCaption"),
                notification.getMessage(), TrayIcon.INFO_MESSAGE_TYPE);
    }

    private String getCaption0() {
        return sysApp.getString("TRAY_CAPTION.0");
    }

    private String getCaption1() {
        return sysApp.getString("TRAY_CAPTION.1");
    }

    private String getCaptionN(final Integer queueCount) {
        return sysApp.getString("TRAY_CAPTION.N", new Object[] {queueCount});
    }
}
