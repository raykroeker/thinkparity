/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.application.browser;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Hashtable;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.browser.Constants.Dimensions.BrowserWindow;
import com.thinkparity.browser.application.browser.display.DisplayFactory;
import com.thinkparity.browser.application.browser.display.DisplayId;
import com.thinkparity.browser.platform.application.display.Display;

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
		this.displayMap = new Hashtable<DisplayId, Display>(
                DisplayId.values().length, 1.0F);

		initComponents();
	}

	/**
	 * @see com.thinkparity.codebase.swing.AbstractJPanel#debugGeometry()
	 * 
	 */
	public void debug() {
		super.debug();
		for (final Display d : displayMap.values()) {
            d.debug();
		}
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
     *            The display to be added.
     * @param constraints
     *            An object expressing layout contraints for this component
     */
	private void add(final Display display, final Object constraints) {
		displayMap.put(display.getId(), display);
		add((Component) display, constraints);
	}

    /**
     * Initialize the main panel components. This consists of adding all of the
     * displays to the main panel.
     * 
     */
    private void initComponents() {
        final GridBagConstraints gbc = new GridBagConstraints();
        setLayout(new GridBagLayout());

        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.ipady = 28;
        gbc.weightx = 1.0;
        add(createDisplay(DisplayId.TITLE, BrowserWindow.Display.TITLE_HEIGHT), gbc.clone());

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy++;
        gbc.ipady = 0;
        gbc.weighty = 1.0;
        add(DisplayFactory.create(DisplayId.CONTENT), gbc.clone());

        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy++;
        gbc.ipady = 0;
        gbc.weighty = 0.0;
        add(createDisplay(DisplayId.STATUS, BrowserWindow.Display.STATUS_HEIGHT), gbc.clone());
    }

    /**
     * Create a display.
     * 
     * @param id
     *            A <code>DisplayId</code>.
     * @param height
     *            The height of the display <code>int</code>.
     * @return A <code>Display</code>.
     */
    private Display createDisplay(final DisplayId id, final int height) {
        final Display display = DisplayFactory.create(id);
        return display;
    }
}
