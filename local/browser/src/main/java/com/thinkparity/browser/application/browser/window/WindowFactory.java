/*
 * Mar 9, 2006
 */
package com.thinkparity.browser.application.browser.window;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.platform.application.window.Window;
import com.thinkparity.browser.platform.application.window.WindowRegistry;
import com.thinkparity.browser.platform.login.ui.LoginWindow;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class WindowFactory {

	/**
	 * The singleton instance.
	 * 
	 */
	private static final WindowFactory singleton;

	static { singleton = new WindowFactory(); }

	/**
	 * Create a window.
	 * 
	 * @param windowId
	 *            The window id.
	 * @return The window for the given id.
	 */
	public static Window create(final WindowId windowId, final BrowserWindow browserWindow) {
		return singleton.doCreate(windowId, browserWindow);
	}

	public static Window create(final WindowId windowId) {
		return singleton.doCreate(windowId);
	}

	/**
	 * A popup window.
	 * 
	 */
	private Window popup;

	/**
	 * The history window.
	 * 
	 */
	private Window history;

	/**
	 * The window registry.
	 * 
	 */
	private final WindowRegistry registry;

	/**
	 * Create a WindowFactory [Singleton, Factory].
	 * 
	 */
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

    private Window doCreateConfirm(final BrowserWindow browserWindow) {
        final Window window = new ConfirmWindow(browserWindow);
        register(window);
        return window;
    }

	private Window doCreatePlatformLogin() {
		final Window window = new LoginWindow();
		window.setModal(true);
		window.setResizable(false);
		return window;
	}

	private Window doCreatePopup(final BrowserWindow browserWindow) {
        popup = new PopupWindow(browserWindow);
		register(popup);
		return popup;
	}

    private Window doCreateRename(final BrowserWindow browserWindow) {
        final Window window = new RenameWindow(browserWindow);
        register(window);
        return window;
    }

	private void register(final Window window) {
		registry.put(window.getId(), window);
	}


}
