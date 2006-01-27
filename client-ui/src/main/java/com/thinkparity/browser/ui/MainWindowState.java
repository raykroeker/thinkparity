/*
 * Jan 6, 2006
 */
package com.thinkparity.browser.ui;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.thinkparity.browser.util.persistence.BrowserPersistenceFactory;
import com.thinkparity.browser.util.persistence.Persistence;

/**
 * This class is responsible for maintaining the browser's state between
 * sessions.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainWindowState {

	/**
	 * Type-safe keys to use when persisting the browser's state.
	 * 
	 */
	private enum PersistenceKey { LOCATION, SIZE }

	/**
	 * The default location of the browser.
	 * 
	 */
	private static final Point DEFAULT_LOCATION = new Point(100, 100);

	/**
	 * Handle to the jFrame.
	 * 
	 */
	private final MainWindow jFrame;

	/**
	 * Persistence utility.
	 */
	private final Persistence jFramePersistence;

	/**
	 * Create a MainWindowState.
	 * 
	 * @param jFrame
	 *            The browser jFrame.
	 */
	MainWindowState(final MainWindow jFrame) {
		super();
		this.jFrame = jFrame;
		this.jFramePersistence =
			BrowserPersistenceFactory.getPersistence(jFrame.getClass());
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
						PersistenceKey.SIZE.toString(), jFrame.getSize());
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
		jFrame.setSize(jFramePersistence.get(
				PersistenceKey.SIZE.toString(), MainWindow.getDefaultSize()));
		jFrame.setLocation(jFramePersistence.get(
				PersistenceKey.LOCATION.toString(), DEFAULT_LOCATION));
	}
}
