/*
 * Mar 9, 2006
 */
package com.thinkparity.browser.application.browser.window;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.platform.application.window.Window;
import com.thinkparity.browser.platform.application.window.WindowRegistry;

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

	/**
	 * A popup window.
	 * 
	 */
	private Window popup;

	/**
	 * The window registry.
	 * 
	 */
	private final WindowRegistry registry;

	/**
	 * The session send popup window.
	 * 
	 */
	private Window sessionSendPopup;

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
		case POPUP: return doCreatePopup(browserWindow);
		case SESSION_SEND_POPUP: return doCreateSessionSendPopup();
		default:
			throw Assert.createUnreachable("Unknown window:  " + windowId);
		}
	}

	/**
	 * Create the modal popup window.
	 * 
	 * @return The modal popup window.
	 */
	private Window doCreatePopup(final BrowserWindow browserWindow) {
		if(null == popup) {
			popup = new PopupWindow(browserWindow);
			registry.put(WindowId.POPUP, popup);
		}
		return popup;
	}

	/**
	 * Create the modal popup window.
	 * 
	 * @return The modal popup window.
	 */
	private Window doCreateSessionSendPopup() {
		if(null == sessionSendPopup) {
			sessionSendPopup = null;;
			registry.put(WindowId.SESSION_SEND_POPUP, sessionSendPopup);
		}
		return sessionSendPopup;
	}
}
