/*
 * Dec 30, 2005
 */
package com.thinkparity.browser.application.browser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.DisplayId;
import com.thinkparity.browser.javax.swing.AbstractJFrame;
import com.thinkparity.browser.platform.application.display.Display;
import com.thinkparity.browser.platform.util.ImageIOUtil;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;
import com.thinkparity.browser.util.NativeSkinUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserWindow extends AbstractJFrame {

	/**
	 * The browser window border.
	 * 
	 * @see #getBorder()
	 */
	private static Border border;

	/**
	 * The window icon.
	 * 
	 */
	private static final BufferedImage BROWSER_ICON;

	/**
	 * The size of the main window.
	 * 
	 * @see #getMainWindowSize()
	 */
	private static Dimension mainWindowSize;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static {
		BROWSER_ICON = ImageIOUtil.read("BrowserIcon.png");
	}

	/**
	 * Obtain the border for the browser window.
	 * 
	 * @return The browser window border.
	 */
	public static Border getBorder() {
		if(null == border) {
			final Color c1 = new Color(196, 213, 255, 255);
			final Color c2 = new Color(117, 130, 162, 255);
			border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED, c1, c2);
		}
		return border;
	}

	/**
	 * Obtain the size of the main window.
	 * 
	 * @return The size of the main window.
	 */
	public static Dimension getMainWindowSize() {
		if(null == mainWindowSize) {
			// DIMENSION BrowserWindow 350x550
			mainWindowSize = new Dimension(350, 550);
		}
		return mainWindowSize;
	}

	/**
	 * Open the main window.
	 * 
	 * @return The main window.
	 */
	static BrowserWindow open() {
		final BrowserWindow mainWindow = new BrowserWindow();
		mainWindow.setVisible(true);
		mainWindow.applyRenderingHints();
		mainWindow.debugGeometry();
		mainWindow.debugLookAndFeel();
		return mainWindow;
	}

	/**
	 * Handle to an apache logger.
	 * 
	 */
	public final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Main panel.
	 * 
	 */
	public MainPanel mainPanel;

	/**
	 * The main controller.
	 * 
	 * @see #getController()
	 */
	private Browser controller;

	/**
	 * Create a BrowserWindow.
	 * 
	 * @throws HeadlessException
	 */
	private BrowserWindow() throws HeadlessException {
		super("BrowserWindow");
		// initialize the state
		new BrowserWindowState(this);
		getRootPane().setBorder(getBorder());
		setIconImage(BROWSER_ICON);
		setTitle(getString("Title"));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(BrowserWindow.getMainWindowSize());
		applyNativeSkin();
		initComponents();
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
	 * Apply native skin attributes to the browser window.
	 * 
	 */
	private void applyNativeSkin() { NativeSkinUtil.applyNativeSkin(this); }

	/**
	 * Obtain the main controller.
	 * 
	 * @return The main controller.
	 */
	private Browser getController() {
		if(null == controller) { controller = Browser.getInstance(); }
		return controller;
	}

	/**
	 * Add the main panel to the window.
	 * 
	 */
	private void initComponents() {
		mainPanel = new MainPanel();
		add(mainPanel);
	}
}
