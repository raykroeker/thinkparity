/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.application.browser;

import java.awt.Color;
import java.awt.Component;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.BoxLayout;

import com.thinkparity.browser.application.browser.display.DisplayFactory;
import com.thinkparity.browser.application.browser.display.DisplayId;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.platform.application.display.Display;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainPanel extends AbstractJPanel {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Map of display ids to the displays.
	 * 
	 */
	private final Map<DisplayId, Display> displayMap;

	/**
	 * Create a MainPanel.
	 * 
	 */
	public MainPanel() {
		super("MainPanel", Color.RED);
		this.displayMap = new Hashtable<DisplayId, Display>(3, 1.0F);

		initMainPanelComponents();
	}

	/**
	 * @see com.thinkparity.browser.javax.swing.AbstractJPanel#debugGeometry()
	 * 
	 */
	public void debugGeometry() {
		super.debugGeometry();
		for(Display d : displayMap.values()) { d.debugGeometry(); }
	}

	/**
	 * Obtain the display for the given id.
	 * 
	 * @param displayId
	 *            The display id.
	 * @return The display.
	 */
	Display getDisplay(final DisplayId displayId) {
		Assert.assertNotNull("Cannot retreive display for null id.", displayId);
		Assert.assertTrue(
				"Display [" + displayId + "] does not exist.", displayMap.containsKey(displayId));
		return displayMap.get(displayId);
	}

	/**
	 * Obtain all of the displays on the main panel.
	 * 
	 * @return The displays on the main panel.
	 */
	Display[] getDisplays() {
		return displayMap.values().toArray(new Display[] {});
	}

	/**
	 * Add a display to the main panel.
	 * 
	 * @param display
	 *            The display to add.
	 */
	private void add(final Display display) {
		displayMap.put(display.getId(), display);
		add((Component) display);
	}

	/**
	 * Initialize the main panel components. This consists of adding all of the
	 * displays to the main panel.
	 * 
	 */
	private void initMainPanelComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(DisplayFactory.create(DisplayId.TITLE));
		add(DisplayFactory.create(DisplayId.LOGO));
		add(DisplayFactory.create(DisplayId.CONTENT));
		add(DisplayFactory.create(DisplayId.INFO));
		add(DisplayFactory.create(DisplayId.FORM));
	}
}
