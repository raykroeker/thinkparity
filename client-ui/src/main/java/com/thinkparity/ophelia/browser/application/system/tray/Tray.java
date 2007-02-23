/*
 * Mar 18, 2006
 */
package com.thinkparity.ophelia.browser.application.system.tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.Version;
import com.thinkparity.ophelia.browser.Constants.Icons;
import com.thinkparity.ophelia.browser.application.system.SystemApplication;
import com.thinkparity.ophelia.browser.platform.Platform;

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public final class Tray {

	/** Indicates whether or not the system tray is installed. */
	private Boolean isInstalled;

    /** An apache logger wrapper. */
    private final Log4JWrapper logger;

    /** A menu builder for the system tray application. */
    private final TrayMenuBuilder menuBuilder;

    /** The local user's profile. */
    private final Profile profile;

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
	public Tray(final SystemApplication systemApplication, final Profile profile) {
		super();
        this.logger = new Log4JWrapper(getClass());
        this.menuBuilder = new TrayMenuBuilder(systemApplication);
        this.profile = profile;
		this.isInstalled = Boolean.FALSE;
		this.systemApplication = systemApplication;
	}

    /**
     * Install the system tray.
     * 
     */
	public void install() {
		systemTrayIcon = new TrayIcon(Icons.Tray.TRAY_ICON_OFFLINE);
        systemTrayIcon.addBalloonActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
                if(systemApplication.isBrowserRunning()) {
                    systemApplication.runIconify(Boolean.FALSE);
                    systemApplication.runMoveBrowserToFront();
                } else {
                    systemApplication.runRestoreBrowser();
                }
			}
		});
		systemTrayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
                if(systemApplication.isBrowserRunning()) {
                    systemApplication.runIconify(Boolean.FALSE);
                    systemApplication.runMoveBrowserToFront();
                } else {
                    systemApplication.runRestoreBrowser();
                }
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
        logger.logVariable("cx", cx);
        switch(cx) {
        case OFFLINE:
            updateMenuOffline();
            setCaption();
            setIcon();
            break;
        case ONLINE:
            updateMenuOnline();
            setCaption();
            setIcon();
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
     * Set the system tray icon.
     * 
     */
    private void setIcon() {
        switch (systemApplication.getConnection()) {
        case OFFLINE:
            systemTrayIcon.setIcon(Icons.Tray.TRAY_ICON_OFFLINE);
            break;
        case ONLINE:
            systemTrayIcon.setIcon(Icons.Tray.TRAY_ICON_ONLINE);
            break;
        default:
            throw Assert.createUnreachable("Unknown connection.");
        }
    }

    /** Update the offline menu. */
    private void updateMenuOffline() {}

    /** Update the online menu. */
    private void updateMenuOnline() {}
}
