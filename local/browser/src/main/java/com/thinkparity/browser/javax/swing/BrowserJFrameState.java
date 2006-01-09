/*
 * Jan 6, 2006
 */
package com.thinkparity.browser.javax.swing;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.thinkparity.browser.javax.swing.persistence.BrowserPersistenceFactory;
import com.thinkparity.browser.javax.swing.persistence.Persistence;

/**
 * This class is responsible for maintaining the browser's state between
 * sessions.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserJFrameState {

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
	private final BrowserJFrame jFrame;

	/**
	 * Persistence utility.
	 */
	private final Persistence jFramePersistence;

	/**
	 * Create a BrowserJFrameState.
	 * 
	 * @param jFrame
	 *            The browser jFrame.
	 */
	BrowserJFrameState(final BrowserJFrame jFrame) {
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
				PersistenceKey.SIZE.toString(), BrowserJFrame.getDefaultSize()));
		jFrame.setLocation(jFramePersistence.get(
				PersistenceKey.LOCATION.toString(), DEFAULT_LOCATION));
	}
}
