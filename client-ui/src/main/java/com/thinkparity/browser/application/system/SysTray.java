/*
 * Mar 18, 2006
 */
package com.thinkparity.browser.application.system;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import com.thinkparity.browser.platform.util.ImageIOUtil;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class SysTray {

	/**
	 * Read the system tray icon.
	 * 
	 * @return The image icon.
	 */
	private static Icon readTrayIcon() { return ImageIOUtil.readIcon("SystemTray.png"); }

	/**
	 * Flag indicating whether or not the system tray is currently installed.
	 * 
	 */
	private Boolean isInstalled;

	/**
	 * The system application.
	 * 
	 */
	private final SysApp sysApp;

	private SystemTray systemTray;

	private TrayIcon systemTrayIcon;

	/**
	 * Create a SysTray.
	 * 
	 * @param sysApp
	 *            The system application.
	 */
	SysTray(final SysApp sysApp) {
		super();
		this.sysApp = sysApp;
		this.isInstalled = Boolean.FALSE;
	}

	void install() {
		systemTrayIcon = new TrayIcon(
				readTrayIcon(), sysApp.getString("TrayIconCaption"));
		systemTrayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				sysApp.systemTrayActionPerformed(e);
			}
		});
		systemTrayIcon.setIconAutoSize(true);

		systemTray  = SystemTray.getDefaultSystemTray();
		systemTray.addTrayIcon(systemTrayIcon);
		isInstalled = Boolean.TRUE;
	}

	Boolean isInstalled() { return isInstalled; }

	void unInstall() {
		systemTray.removeTrayIcon(systemTrayIcon);
		systemTrayIcon = null;
		systemTray = null;

		isInstalled = Boolean.FALSE;
	}
}
