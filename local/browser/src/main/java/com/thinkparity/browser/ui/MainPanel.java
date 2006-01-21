/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Hashtable;
import java.util.Map;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.ui.display.Display;
import com.thinkparity.browser.ui.display.DisplayFactory;
import com.thinkparity.browser.ui.display.DisplayId;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainPanel extends AbstractJPanel {

	/**
	 * @see java.io.Serializable
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
		setLayout(new GridBagLayout());
		addLogoDisplay();
		addContentDisplay();
		addInfoDisplay();
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
	 * Add the display to the main panel.
	 *
	 */
	private void add(final Display display, final Object displayConstraints) {
		displayMap.put(display.getId(), display);
		super.add(display, displayConstraints);
	}

	/**
	 * Add the content display to the main panel.
	 *
	 */
	private void addContentDisplay() {
		final GridBagConstraints contentDisplayConstraints = new GridBagConstraints();
		contentDisplayConstraints.gridy = 1;
		contentDisplayConstraints.weightx = 1.0;
		contentDisplayConstraints.weighty = 0.53;
		contentDisplayConstraints.fill = GridBagConstraints.BOTH;
		add(DisplayFactory.create(DisplayId.CONTENT), contentDisplayConstraints);
	}

	/**
	 * Add the info display to the main panel.
	 *
	 */
	private void addInfoDisplay() {
		final GridBagConstraints infoDisplayConstraints = new GridBagConstraints();
		infoDisplayConstraints.gridy = 2;
		infoDisplayConstraints.weightx = 1.0;
		infoDisplayConstraints.weighty = 0.31;
		infoDisplayConstraints.fill = GridBagConstraints.BOTH;
		add(DisplayFactory.create(DisplayId.INFO), infoDisplayConstraints);
	}

	/**
	 * Add the logo display to the main panel.
	 *
	 */
	private void addLogoDisplay() {
		final GridBagConstraints logoDisplayConstraints = new GridBagConstraints();
		logoDisplayConstraints.gridy = 0;
		logoDisplayConstraints.weightx = 1.0;
		logoDisplayConstraints.weighty = 0.16;
		logoDisplayConstraints.fill = GridBagConstraints.BOTH;
		add(DisplayFactory.create(DisplayId.LOGO), logoDisplayConstraints);
	}
}
