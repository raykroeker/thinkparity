/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.javax.swing.util;

import java.awt.Component;
import java.awt.Point;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SwingUtil {

	/**
	 * Key used to store the mouse tracker for the custom tool tip.
	 * 
	 */
	private static final String CUSTOM_MOUSE_TRACKER = "CUSTOM_MOUSE_TRACKER";

	/**
	 * Singleton instance.
	 * 
	 */
	private static final SwingUtil singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new SwingUtil();
		singletonLock = new Object();
	}

	/**
	 * Hide a custom tool tip. This tool tip must previously have been
	 * displayed.
	 * 
	 * @param customToolTip
	 *            The custom tool tip.
	 * @see SwingUtil#showCustomToolTip(Component, JPanel, Point)
	 */
	public static void hideCustomToolTip(final JPanel customToolTip) {
		synchronized(singletonLock) {
			singleton.doHideCustomToolTip(customToolTip);
		}
	}

	/**
	 * Show a custom tool tip. The tool tip to display is a JPanel, and can be
	 * rendered on top of any component.
	 * 
	 * @param parent
	 *            The parent component.
	 * @param customToolTip
	 *            The custom tool tip.
	 * @param relLocation
	 *            The location of the tool tip relative to the location of the
	 *            parent.
	 * @see SwingUtil#hideCustomToolTip(JPanel)
	 */
	public static void showCustomToolTip(final Component parent,
			final JPanel customToolTip, final Point relLocation) {
		synchronized(singletonLock) {
			singleton.doShowCustomToolTip(parent, customToolTip, relLocation);
		}
	}

	/**
	 * Create a SwingUtil [Singleton]
	 * 
	 */
	private SwingUtil() { super(); }

	/**
	 * Hide a custom tool tip. This tool tip must previously have been
	 * displayed.
	 * 
	 * @param customToolTip
	 *            The custom tool tip.
	 */
	private void doHideCustomToolTip(final JPanel customToolTip) {
		final CustomToolTipMouseTracker mouseTracker =
			(CustomToolTipMouseTracker) customToolTip.getClientProperty(CUSTOM_MOUSE_TRACKER);
		mouseTracker.uninstall();
		final JLayeredPane jLayeredPane =
			SwingUtilities.getRootPane(customToolTip).getLayeredPane();
		jLayeredPane.remove(customToolTip);
		jLayeredPane.repaint();
	}

	/**
	 * Show a custom tool tip. The tool tip to display is a JPanel, and can be
	 * rendered on top of any component.
	 * 
	 * @param parent
	 *            The parent component.
	 * @param customToolTip
	 *            The custom tool tip.
	 * @param relLocation
	 *            The location of the tool tip relative to the location of the
	 *            parent.
	 */
	private void doShowCustomToolTip(final Component parent,
			final JPanel customToolTip, final Point relLocation) {
		final JLayeredPane jLayeredPane =
			SwingUtilities.getRootPane(parent).getLayeredPane();
		customToolTip.setLocation(SwingUtilities.convertPoint(parent, relLocation, jLayeredPane));
		jLayeredPane.add(customToolTip, JLayeredPane.POPUP_LAYER);
		// NOTE We have to install the mouse tracker afer we add the tool tip to
		// the layered pane
		final CustomToolTipMouseTracker mouseTracker = new CustomToolTipMouseTracker(customToolTip);
		mouseTracker.install();
		customToolTip.putClientProperty(CUSTOM_MOUSE_TRACKER, mouseTracker);
	}
}
