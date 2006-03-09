/*
 * Mar 9, 2006
 */
package com.thinkparity.browser.platform.application.window;

import java.util.Hashtable;
import java.util.Map;

import com.thinkparity.browser.application.browser.display.DisplayId;
import com.thinkparity.browser.application.browser.window.WindowId;
import com.thinkparity.browser.javax.swing.AbstractJDialog;
import com.thinkparity.browser.platform.application.display.Display;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class Window extends AbstractJDialog {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * A registry of all displays in this window.
	 * 
	 */
	protected final Map<DisplayId, Object> displayRegistry;

	/**
	 * The panel onto which all displays are dropped.
	 * 
	 */
	protected WindowPanel windowPanel;

	/**
	 * Create a Window.
	 * 
	 * @param l18Context
	 *            The localization context
	 */
	public Window(final String l18Context) {
		super(l18Context);
		this.displayRegistry = new Hashtable<DisplayId, Object>();
		setTitle(getString("Title"));
		setResizable(false);
		initComponents();
	}

	/**
	 * Add a display to the window.
	 * 
	 * @param display
	 *            The display to add.
	 */
	public void addDisplay(final Display display) {
		displayRegistry.put(display.getId(), display);
	}

	/**
	 * Determine whether or not the window currently contains the display.
	 * 
	 * @param displayId
	 *            The display id.
	 * @return True if the window contains the display; false otherwise.
	 */
	public Boolean containsDisplay(final DisplayId displayId) {
		return displayRegistry.containsKey(displayId);
	}

	/**
	 * Obtain the display for the given id.
	 * 
	 * @param displayId
	 *            The display id.
	 * @return The display.
	 */
	public Display getDisplay(final DisplayId displayId) {
		return (Display) displayRegistry.get(displayId);
	}

	/**
	 * Obtain the window id.
	 * 
	 * @return The window id.
	 */
	public abstract WindowId getId();

	/**
	 * Initialize the window's components.
	 *
	 */
	private void initComponents() {
		windowPanel = new WindowPanel();
		add(windowPanel);
	}
}
