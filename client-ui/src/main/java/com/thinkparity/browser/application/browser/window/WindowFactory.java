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
		case HISTORY: return doCreateHistory(browserWindow);
		case POPUP: return doCreatePopup(browserWindow);
		default:
			throw Assert.createUnreachable("Unknown window:  " + windowId);
		}
	}

	private Window doCreateHistory(final BrowserWindow browserWindow) {
//		if(null == history) {
			history = new HistoryWindow(browserWindow);
			register(history);
//		}
		return history;
	}

	private void register(final Window window) {
		registry.put(window.getId(), window);
	}

	/**
	 * Create the modal popup window.
	 * 
	 * @return The modal popup window.
	 */
	private Window doCreatePopup(final BrowserWindow browserWindow) {
//		if(null == popup) {
			popup = new PopupWindow(browserWindow);
			register(popup);
//		}
		return popup;
	}
}
