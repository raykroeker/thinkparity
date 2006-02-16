/*
 * Jan 6, 2006
 */
package com.thinkparity.browser.application.browser;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.thinkparity.browser.platform.util.persistence.PersistenceFactory;
import com.thinkparity.browser.platform.util.persistence.Persistence;

/**
 * This class is responsible for maintaining the browser's state between
 * sessions.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserWindowState {

	/**
	 * Type-safe keys to use when persisting the browser's state.
	 * 
	 */
	private enum PersistenceKey { LOCATION }

	/**
	 * The default location of the browser.
	 * 
	 */
	private static final Point DEFAULT_LOCATION = new Point(100, 100);

	/**
	 * Handle to the jFrame.
	 * 
	 */
	private final BrowserWindow jFrame;

	/**
	 * Persistence utility.
	 */
	private final Persistence jFramePersistence;

	/**
	 * Create a BrowserWindowState.
	 * 
	 * @param jFrame
	 *            The browser jFrame.
	 */
	BrowserWindowState(final BrowserWindow jFrame) {
		super();
		this.jFrame = jFrame;
		this.jFramePersistence =
			PersistenceFactory.getPersistence(jFrame.getClass());
		addListeners();
		setInitialState();
	}

	/**
	 * Add the appropriate listeners to capture state changes.
	 *
	 */
	private void addListeners() {
		jFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				jFramePersistence.set(
						PersistenceKey.LOCATION.toString(), jFrame.getLocation());
			}
		});
	}

	/**
	 * Set the initial state of the browser.
	 *
	 */
	private void setInitialState() {
		jFrame.setLocation(jFramePersistence.get(
				PersistenceKey.LOCATION.toString(), DEFAULT_LOCATION));
	}
}
