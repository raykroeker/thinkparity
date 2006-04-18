/*
 * Dec 30, 2005
 */
package com.thinkparity.browser.application.browser;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.DisplayId;
import com.thinkparity.browser.javax.swing.AbstractJFrame;
import com.thinkparity.browser.javax.swing.border.ImageBorder;
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
		BROWSER_ICON = ImageIOUtil.read("ThinkParity32x32.png");
	}

	/**
	 * Obtain the size of the main window.
	 * 
	 * @return The size of the main window.
	 */
	public static Dimension getMainWindowSize() {
		if(null == mainWindowSize) {
			// DIMENSION BrowserWindow 525x587
			mainWindowSize = new Dimension(525, 587);
		}
		return mainWindowSize;
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
	 * The browser application.
	 * 
	 */
	private final Browser browser;

	/**
	 * Create a BrowserWindow.
	 * 
	 * @throws HeadlessException
	 */
	BrowserWindow(final Browser browser) throws HeadlessException {
		super("BrowserWindow");
		this.browser = browser;
		// initialize the state
		new BrowserWindowState(this);
		getRootPane().setBorder(new ImageBorder("MainWindowBorderTop.png",
                "MainWindowBorderLeft.png", "MainWindowBorderBottom.png",
        "MainWindowBorderRight.png"));
		setIconImage(BROWSER_ICON);
		setTitle(getString("Title"));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
	 * Open the main window.
	 * 
	 * @return The main window.
	 */
	void open() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setVisible(true);
				applyRenderingHints();
				debugGeometry();
				debugLookAndFeel();

		    	browser.displayTitleAvatar();
		    	browser.displayInfoAvatar();
		    	browser.displayDocumentListAvatar();
                browser.displayStatusAvatar();
			}
		});
	}

	/**
	 * Apply native skin attributes to the browser window.
	 * 
	 */
	private void applyNativeSkin() { NativeSkinUtil.applyNativeSkin(this); }

	/**
	 * Add the main panel to the window.
	 * 
	 */
	private void initComponents() {
		mainPanel = new MainPanel();
		add(mainPanel);
	}
}
