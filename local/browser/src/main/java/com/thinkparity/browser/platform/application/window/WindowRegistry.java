/*
 * Mar 9, 2006
 */
package com.thinkparity.browser.platform.application.window;

import java.util.Hashtable;
import java.util.Map;

import com.thinkparity.browser.application.browser.window.WindowId;

/**
 * A simple wrapper around a map from window id to a window.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class WindowRegistry {

	/**
	 * The underlying registry.
	 * 
	 */
	private static final Map<WindowId, Object> registry;

	static {
		final int windowCount = WindowId.values().length;
		registry = new Hashtable<WindowId, Object>(windowCount, 1.0F);
	}

	/**
	 * Create a WindowRegistry.
	 * 
	 */
	public WindowRegistry() { super(); }

	/**
	 * Determine whether or not the registry contains a window.
	 * 
	 * @param windowId
	 *            The window id.
	 * @return True if the registry contains the window; false otherwise.
	 */
	public Boolean contains(final WindowId windowId) {
		return registry.containsKey(windowId);
	}

	/**
	 * Obtain a window for the window id.
	 * 
	 * @param windowId
	 *            The window id.
	 * @return The window.
	 */
	public Window get(final WindowId windowId) {
		return (Window) registry.get(windowId);
	}

	/**
	 * Set a window in the registry.
	 * 
	 * @param windowId
	 *            The window id.
	 * @param window
	 *            The window.
	 * @return The previous value in the registry.
	 */
	public Window put(final WindowId windowId, final Window window) {
		return (Window) registry.put(windowId, window);
	}
}
