/*
 * Mar 18, 2006
 */
package com.thinkparity.ophelia.browser.application.system.tray;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.system.SystemApplication;

/**
 * <b>Title:</b>thinkParity Ophelia UI Application System Tray<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Tray {

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
	public Tray(final SystemApplication systemApplication, final Profile profile) {
		super();
        this.menuBuilder = new TrayMenuBuilder(systemApplication);
		this.isInstalled = Boolean.FALSE;
		this.systemApplication = systemApplication;
	}

    /**
     * Fire a connection offline event.
     *
     */
    public void fireConnectionOffline() {
        setCaption();
        setIcon();
    }

    /**
     * Fire a connection online event.
     *
     */
    public void fireConnectionOnline() {
        setCaption();
        setIcon();
    }

    /**
     * Install the system tray.
     * 
     */
	public void install() throws AWTException {
		systemTrayIcon = new TrayIcon(Images.Tray.TRAY_ICON_OFFLINE);
		systemTrayIcon.addMouseListener(new MouseAdapter() {
            /**
             * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
             *
             */
            @Override
            public void mousePressed(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 ) {
                    if(systemApplication.isBrowserRunning()) {
                        systemApplication.runIconify(Boolean.FALSE);
                        systemApplication.runMoveBrowserToFront();
                    } else {
                        systemApplication.runRestoreBrowser();
                    }
                }
            }
        });
        systemTrayIcon.setPopupMenu(menuBuilder.createPopup());

		systemTray  = SystemTray.getSystemTray();
		systemTray.add(systemTrayIcon);
		isInstalled = Boolean.TRUE;

        setCaption();
        setIcon();
	}

    /**
     * Set the state of the quit menu.
     * 
     * @param enabled
     *            A <code>Boolean</code>.
     */
    public void setQuitEnabled(final Boolean enabled) {
        menuBuilder.setQuitEnabled(enabled);
    }

    /** Uninstall the system tray. */
	public void unInstall() {
		systemTray.remove(systemTrayIcon);
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
            return new StringBuffer(getString("OFFLINE"));
        case ONLINE:
            return new StringBuffer(getString("ONLINE"));
        default:
            throw Assert.createUnreachable("[UNKNOWN CONNECTION]");
        }
    }

    /**
     * Obtain a localized string from the system application.
     * 
     * @param key
     *            A local key.
     * @return A localized string.
     */
    private String getString(final String key) {
        return systemApplication.getString(key);
    }

    /**
     * Set the system tray icon caption.  The platform connection status will
     * always be displayed in the icon.
     * 
     */
    private void setCaption() {
        systemTrayIcon.setToolTip(createCaption().toString());
    }
    
    /**
     * Set the system tray icon.
     * 
     */
    private void setIcon() {
        switch (systemApplication.getConnection()) {
        case OFFLINE:
            systemTrayIcon.setImage(Images.Tray.TRAY_ICON_OFFLINE);
            break;
        case ONLINE:
            systemTrayIcon.setImage(Images.Tray.TRAY_ICON_ONLINE);
            break;
        default:
            throw Assert.createUnreachable("Unknown connection.");
        }
    }
}
