/*
 * Feb 11, 2006
 */
package com.thinkparity.browser.application.gadget;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.thinkparity.browser.platform.util.persistence.Persistence;
import com.thinkparity.browser.platform.util.persistence.PersistenceFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class GadgetWindowState {

	/**
	 * The gadget window persistence.
	 * 
	 */
	private final Persistence persistence;

	/**
	 * Create a GadgetWindowState.
	 * 
	 * @param gadgetWindow
	 *            The gadget window.
	 */
	GadgetWindowState(final Window gadgetWindow) {
		super();
		this.persistence = PersistenceFactory.getPersistence(getClass());

		gadgetWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(final WindowEvent e) {
				persistence.set(
						PersistenceKey.LOCATION.toString(), gadgetWindow.getLocation());
			}
		});
		gadgetWindow.setLocation(persistence.get(
				PersistenceKey.LOCATION.toString(), getDefaultLocation()));
	}

	/**
	 * Obtain the default location of the gadget window.
	 * 
	 * @return The deafult location.
	 */
	private Point getDefaultLocation() {
		// POINT 100x100
		return new Point(100, 100);
	}

	/**
	 * The state persistence keys.
	 * 
	 */
	private enum PersistenceKey { LOCATION }

	
}
