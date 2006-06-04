/*
 * Created On: Mar 9, 2006
 * $Id$
 */
package com.thinkparity.browser.application.browser.window;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.platform.application.window.Window;
import com.thinkparity.browser.platform.application.window.WindowRegistry;
import com.thinkparity.browser.platform.login.ui.LoginWindow;

import com.thinkparity.codebase.assertion.Assert;

/**
 * A factory for creating thinkParity windows.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 * @see WindowId
 */
public class WindowFactory {

	/** The singleton instance. */
	private static final WindowFactory SINGLETON;

	static { SINGLETON = new WindowFactory(); }

    /**
     * Create a window.
     * 
     * @param windowId
     *            The window id.
     * @return The window.
     */
	public static Window create(final WindowId windowId) {
		return SINGLETON.doCreate(windowId);
	}

	/**
	 * Create a window.
	 * 
	 * @param windowId
	 *            The window id.
	 * @return The window for the given id.
	 */
	public static Window create(final WindowId windowId, final BrowserWindow browserWindow) {
		return SINGLETON.doCreate(windowId, browserWindow);
	}

	/** A popup window. */
	private Window popup;

	/** The window registry. */
	private final WindowRegistry registry;

	/** Create WindowFactory. [Singleton, Factory] */
	private WindowFactory() {
		super();
		this.registry = new WindowRegistry();
	}

	/**
	 * Create a window.
	 * 
	 * @param windowId
	 *            The window id.
	 * @return The window for the given id.
	 */
	private Window doCreate(final WindowId windowId) {
		final Window window;
		switch(windowId) {
		case PLATFORM_LOGIN:
			window = doCreatePlatformLogin();
			break;
		default:
			throw Assert.createUnreachable("Unknown window:  " + windowId);
		}
		register(window);
		return window;
	}

	/**
	 * Create a window.
	 * 
	 * @param windowId
	 *            The window id.
	 * @return The window for the given id.
	 */
	private Window doCreate(final WindowId windowId,
			final BrowserWindow browserWindow) {
		switch(windowId) {
		case CONFIRM: return doCreateConfirm(browserWindow);
		case POPUP: return doCreatePopup(browserWindow);
        case RENAME: return doCreateRename(browserWindow);
		default:
			throw Assert.createUnreachable("Unknown window:  " + windowId);
		}
	}

    /**
     * Create the confirmation dialog window.
     * 
     * @param browserWindow
     *            The browser window.
     * @return A window.
     */
    private Window doCreateConfirm(final BrowserWindow browserWindow) {
        final Window window = new ConfirmWindow(browserWindow);
        register(window);
        return window;
    }

    /**
     * Create the platform login window.
     * 
     * @return A window.
     */
	private Window doCreatePlatformLogin() {
		final Window window = new LoginWindow();
		window.setModal(true);
		window.setResizable(false);
		return window;
	}

    /**
     * Create a popup window.
     * 
     * @param browserWindow
     *            The browser window.
     * @return A window.
     */
	private Window doCreatePopup(final BrowserWindow browserWindow) {
        popup = new PopupWindow(browserWindow);
		register(popup);
		return popup;
	}

    /**
     * Create a rename window.
     * 
     * @param browserWindow
     *            The browser window.
     * @return A window.
     */
    private Window doCreateRename(final BrowserWindow browserWindow) {
        final Window window = new RenameWindow(browserWindow);
        register(window);
        return window;
    }

    /**
     * Register a window.
     * 
     * @param window
     *            A window.
     */
	private void register(final Window window) {
        registry.put(window.getId(), window);
	}
}
