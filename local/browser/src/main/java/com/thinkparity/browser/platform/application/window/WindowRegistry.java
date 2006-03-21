/*
 * Mar 9, 2006
 */
package com.thinkparity.browser.platform.application.window;

import java.util.HashMap;
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
	private static final Map<WindowId, Object> REGISTRY;

	static {
		final int windowCount = WindowId.values().length;
		REGISTRY = new HashMap<WindowId, Object>(windowCount, 1.0F);
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
		synchronized(REGISTRY) { return REGISTRY.containsKey(windowId); }
	}

	/**
	 * Dispose of the window.
	 * 
	 * @param windowId
	 *            The window id.
	 */
	public void dispose(final WindowId windowId) {
		final Window window;
		synchronized(REGISTRY) { window = (Window) REGISTRY.remove(windowId); }
		window.dispose();
	}

	/**
	 * Obtain a window for the window id.
	 * 
	 * @param windowId
	 *            The window id.
	 * @return The window.
	 */
	public Window get(final WindowId windowId) {
		synchronized(REGISTRY) { return (Window) REGISTRY.get(windowId); }
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
		synchronized(REGISTRY) { return (Window) REGISTRY.put(windowId, window); }
	}
}
