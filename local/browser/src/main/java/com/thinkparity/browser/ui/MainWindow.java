/*
 * Dec 30, 2005
 */
package com.thinkparity.browser.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JRootPane;

import org.apache.log4j.Logger;

import com.thinkparity.browser.Controller;
import com.thinkparity.browser.javax.swing.AbstractJFrame;
import com.thinkparity.browser.javax.swing.BrowserColorUtil;
import com.thinkparity.browser.javax.swing.BrowserUI;
import com.thinkparity.browser.ui.display.Display;
import com.thinkparity.browser.ui.display.DisplayId;
import com.thinkparity.browser.util.log4j.LoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainWindow extends AbstractJFrame {

	private static final Color backgroundColor =
		BrowserColorUtil.getRGBColor(249, 249, 249, 255);

	/**
	 * The default size of the browser.
	 * 
	 */
	private static final Dimension defaultSize;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static { defaultSize = new Dimension(352, 552); }

	public static Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Obtain the default size of the browser.
	 *
	 */
	public static Dimension getDefaultSize() { return defaultSize; }

	/**
	 * Set the parity look and feel; create an instance of the main window and
	 * display it.
	 * 
	 * @param controller
	 *            A handle to the browser main class.
	 * @return The main window.
	 */
	public static MainWindow open(final Controller controller) {
		BrowserUI.setParityLookAndFeel();
		final MainWindow mainWindow = new MainWindow(controller);

		mainWindow.setVisible(true);
		((Graphics2D) mainWindow.getGraphics())
			.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		mainWindow.debugGeometry();
		return mainWindow;
	}

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Main panel.
	 * 
	 */
	private MainPanel mainPanel;

	/**
	 * Create a MainWindow.
	 * 
	 * @param controller
	 *            The main controller.
	 * @throws HeadlessException
	 */
	private MainWindow(final Controller controller) throws HeadlessException {
		super("MainWindow");
		// initialize the state
		new MainWindowState(this);

		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle(getString("Title"));

		initMainWindowComponents();
	}

	/**
	 * Obtain a display.
	 * 
	 * @param displayId
	 *            The display id.
	 * @return The display.
	 */
	public Display getDisplay(final DisplayId displayId) {
		return mainPanel.getDisplay(displayId);
	}

	/**
	 * Obtain the displays in the main window.
	 * 
	 * @return The displays in the main window.
	 */
	public Display[] getDisplays() { return mainPanel.getDisplays(); }

	/**
	 * Debug the geometry of the main window; and all of the children throughout
	 * the hierarchy.
	 * 
	 */
	private void debugGeometry() {
		logger.info("MainWindow");
		logger.debug("l:" + getLocation());
		logger.debug("b:" + getBounds());
		logger.debug("i:" + getInsets());
		mainPanel.debugGeometry();
	}

	/**
	 * Add the main panel to the window.
	 * 
	 */
	private void initMainWindowComponents() {
		mainPanel = new MainPanel();
		add(mainPanel);
	}
}
