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
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.log4j.Logger;

import com.thinkparity.browser.Controller;
import com.thinkparity.browser.javax.swing.AbstractJFrame;
import com.thinkparity.browser.ui.display.Display;
import com.thinkparity.browser.ui.display.DisplayId;
import com.thinkparity.browser.util.log4j.LoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainWindow extends AbstractJFrame {

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

	/**
	 * Obtain the size of the main window.
	 * 
	 * @return The size of the main window.
	 */
	public static Dimension getMainWindowSize() {
		if(null == mainWindowSize) {
			mainWindowSize = new Dimension(402, 552);
		}
		return mainWindowSize;
	}

	/**
	 * Set the parity look and feel; create an instance of the main window and
	 * display it.
	 * 
	 * @return The main window.
	 */
	public static MainWindow open() {
		final MainWindow mainWindow = new MainWindow();
		mainWindow.setVisible(true);
		((Graphics2D) mainWindow.getGraphics())
			.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		mainWindow.debugGeometry();
		mainWindow.debugLookAndFeel();
		return mainWindow;
	}

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * The main controller.
	 * 
	 * @see #getController()
	 */
	private Controller controller;

	/**
	 * Main panel.
	 * 
	 */
	private MainPanel mainPanel;

	/**
	 * Create a MainWindow.
	 * 
	 * @throws HeadlessException
	 */
	private MainWindow() throws HeadlessException {
		super("MainWindow");
		// initialize the state
		new MainWindowState(this);

		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setRegion();
		setResizable(false);
		setSize(MainWindow.getMainWindowSize());

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
		logger.debug("MainWindow");
		logger.debug("l:" + getLocation());
		logger.debug("b:" + getBounds());
		logger.debug("i:" + getInsets());
		mainPanel.debugGeometry();
	}

	/**
	 * Debug the look and feel.
	 *
	 */
	private void debugLookAndFeel() {
		logger.debug("MainWindow");
		logger.debug("lnf:" + UIManager.getLookAndFeel().getClass().getName());
		final StringBuffer buffer = new StringBuffer("installed lnf:[");
		boolean isFirst = true;
		for(LookAndFeelInfo lnfi : UIManager.getInstalledLookAndFeels()) {
			if(isFirst) { isFirst = false; }
			else { buffer.append(","); }
			buffer.append(lnfi.getClassName());
		}
		buffer.append("]");
		logger.debug(buffer);
	}

	/**
	 * Obtain the main controller.
	 * 
	 * @return The main controller.
	 */
	private Controller getController() {
		if(null == controller) { controller = Controller.getInstance(); }
		return controller;
	}

	/**
	 * Add the main panel to the window.
	 * 
	 */
	private void initMainWindowComponents() {
		mainPanel = new MainPanel();
		add(mainPanel);
	}

	/**
	 * Set the region of the main window. We set the region to be a default
	 * window shape with rounded corners at the top.
	 * 
	 */
	private void setRegion() {
//		final RegionBuilder regionBuilder = new RegionBuilder();
//		final Region region =
//			regionBuilder.createRoundRectangleRegion(
//					0, 0, getWidth() + 1, UIConstants.TitlePaneHeight * 2,
//					UIConstants.TitlePaneCurvature,
//					UIConstants.TitlePaneCurvature);
//		final Region region2 = regionBuilder.createRectangleRegion(0,
//				UIConstants.TitlePaneHeight, getWidth(), getHeight());
//		final Region region3 = regionBuilder.combineRegions(region, region2,
//				NativeConstants.REGION_OR);
//		NativeSkin.getInstance().setWindowRegion(this, region3, true);
	}
}
