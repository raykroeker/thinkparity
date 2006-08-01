 /*
 * Dec 30, 2005
 */
package com.thinkparity.browser.application.browser;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.browser.Constants.Dimensions;
import com.thinkparity.browser.application.browser.display.DisplayId;
import com.thinkparity.browser.javax.swing.AbstractJFrame;
import com.thinkparity.browser.javax.swing.Swing.Constants.Images;
import com.thinkparity.browser.platform.application.display.Display;
import com.thinkparity.browser.platform.application.window.WindowBorder2;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;
import com.thinkparity.browser.platform.util.persistence.Persistence;
import com.thinkparity.browser.platform.util.persistence.PersistenceFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserWindow extends AbstractJFrame {

	/**
	 * The size of the main window.
	 * 
	 * @see #getMainWindowSize()
	 */
	private static Dimension mainWindowSize;

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/**
	 * Obtain the size of the main window.
	 * 
	 * @return The size of the main window.
	 */
	public static Dimension getMainWindowSize() {
		if(null == mainWindowSize) {
			// DIMENSION BrowserWindow 450x587
			mainWindowSize = new Dimension(450, 587);
		}
		return mainWindowSize;
	}

	/** The main panel. */
	public MainPanel mainPanel;

    /** An apache logger. */
	protected final Logger logger;

	/** A parity persistence. */
    protected final Persistence persistence;

	/** The browser application. */
	private final Browser browser;

	/**
	 * Create a BrowserWindow.
	 * 
	 * @throws HeadlessException
	 */
	BrowserWindow(final Browser browser) throws HeadlessException {
		super("BrowserWindow");
		this.browser = browser;
		this.logger = LoggerFactory.getLogger(getClass());
        this.persistence = PersistenceFactory.getPersistence(getClass());
		getRootPane().setBorder(new WindowBorder2());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                persist();
                browser.hibernate();
            }});
		setIconImage(Images.WINDOW_ICON_IMAGE);
		setTitle(getString("Title"));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        final Point location = persistence.get("location", new Point(100, 100));
        setLocation(location.x, location.y);
        setMinimumSize(Dimensions.BrowserWindow.MIN_SIZE);
        setSize(Dimensions.BrowserWindow.MIN_SIZE);
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
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() { reOpen(); }
            });
        }
        catch(final InterruptedException ix) { throw new RuntimeException(ix); }
        catch(final InvocationTargetException itx) { throw new RuntimeException(itx); }
    }

    /**
     * Re open the main window.
     * 
     * @return The main window.
     */
    void reOpen() {
        setVisible(true);
        applyRenderingHints();
        debugGeometry();
        debugLookAndFeel();

        browser.displayTitleAvatar();
        browser.displayContentAvatar();
        browser.displayStatusAvatar();
        browser.displayContainersTab(Boolean.TRUE);
    }

	/**
	 * Add the main panel to the window.
	 * 
	 */
	private void initComponents() {
		mainPanel = new MainPanel();
		add(mainPanel);
	}

	/** Persist any window state. */
    private void persist() {
        persistence.set("location", getLocation());
    }
}
