/*
 * Dec 30, 2005
 */
package com.thinkparity.browser.javax.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JRootPane;

import org.apache.log4j.Logger;

import com.thinkparity.browser.Browser;
import com.thinkparity.browser.java.awt.StackLayout;
import com.thinkparity.browser.java.awt.StackLayout.Orientation;
import com.thinkparity.browser.javax.swing.browser.Controller;
import com.thinkparity.browser.javax.swing.browser.MainJPanel;
import com.thinkparity.browser.javax.swing.misc.ColorPanel;
import com.thinkparity.browser.log4j.LoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserJFrame extends JFrame {

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

	/**
	 * Singleton instance of the JFrame.
	 * 
	 */
	private static BrowserJFrame singleton;

	static { defaultSize = new Dimension(550, 400); }

	/**
	 * Obtain the default size of the browser.
	 *
	 */
	public static Dimension getDefaultSize() { return defaultSize; }

	/**
	 * Obtain the singleton instance of the JFrame.
	 * 
	 * @return The singleton instance of the JFrame.
	 */
	public static BrowserJFrame getSingleton() { return singleton; }

	/**
	 * Open the main browser jFrame.
	 * 
	 * @param browser
	 *            A handle to the browser main class.
	 */
	public static void open(final Browser browser) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() { doOpen(browser); }
        });
    }

	/**
	 * Set the parity look and feel; create an instance of the browser jFrame
	 * then set the frame to be visible.
	 * 
	 * @param browser
	 *            A handle to the browser main class.
	 */
	private static void doOpen(final Browser browser) {
		BrowserUI.setParityLookAndFeel();
		singleton = new BrowserJFrame(browser);
		singleton .getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		singleton .setTitle("Parity Browser");

		singleton .setVisible(true);
		((Graphics2D) singleton .getGraphics())
			.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		Controller.getInstance().showDocumentList();
	}

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final Color backgroundColor =
		BrowserColorUtil.getRGBColor(249, 249, 249, 255);

	public static Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * The display com.thinkparity.browser.javax.swing.component for documents.
	 * 
	 */
//	private final DocumentShuffler documentShuffler;

	/**
	 * Create a BrowserJFrame.
	 * 
	 * @throws HeadlessException
	 */
	public BrowserJFrame(final Browser browser) throws HeadlessException {
		super();
		// initialize the state
		new BrowserJFrameState(this);
		// initialize the components
		setLayout(new StackLayout());
		setResizable(false);
		add(new ColorPanel(backgroundColor), Orientation.BOTTOM);
		add(new MainJPanel(this), Orientation.TOP);
		addListeners(browser);
	}

	/**
	 * Add the required listeners to the jFrame.
	 * 
	 * @param browser
	 *            The main browser.
	 */
	private void addListeners(final Browser browser) {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { browser.exit(0); }
		});
	}
}
