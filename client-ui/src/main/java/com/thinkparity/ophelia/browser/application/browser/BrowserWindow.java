/*
 * Dec 30, 2005
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.swing.AbstractJFrame;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants;
import com.thinkparity.ophelia.browser.Constants.Dimensions;
import com.thinkparity.ophelia.browser.application.browser.display.DisplayId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.platform.application.display.Display;
import com.thinkparity.ophelia.browser.platform.application.window.WindowBorder2;
import com.thinkparity.ophelia.browser.platform.util.persistence.Persistence;
import com.thinkparity.ophelia.browser.platform.util.persistence.PersistenceFactory;
import com.thinkparity.ophelia.browser.util.window.WindowUtil;
import com.thinkparity.ophelia.browser.util.window.WindowUtilProvider;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserWindow extends AbstractJFrame {

    /**
     * The location of the main window.
     * The variable is here so it persists.
     * 
     * @see #getMainWindowLocation()
     */
    private static Point mainWindowLocation;

    /**
	 * The size of the main window.
     * The variable is here so it persists.
	 * 
	 * @see #getMainWindowSize()
	 */
	private static Dimension mainWindowSize;

    /** An instance of <code>WindowUtil</code>. */
    private static final WindowUtil WINDOW_UTIL;
    
    static {
        WINDOW_UTIL = WindowUtilProvider.getInstance().getWindowUtil();
    }

    /**
     * Obtain the location of the main window.
     * (This is the location before maximizing.)
     */
    private static Point getMainWindowLocation() {
        if(null == mainWindowLocation) {
            // POINT BrowserWindow 100,100
            mainWindowLocation = new Point(Dimensions.BrowserWindow.DEFAULT_LOCATION);
        }
        return mainWindowLocation;
    }

    /**
	 * Obtain the size of the main window.
     * (This is the size before maximizing.)
	 * 
	 * @return The size of the main window.
	 */
	private static Dimension getMainWindowSize() {
		if(null == mainWindowSize) {
			// DIMENSION BrowserWindow 450x587
			mainWindowSize = new Dimension(Dimensions.BrowserWindow.DEFAULT_SIZE);
		}
		return mainWindowSize;
	}

	/** The main panel. */
	public MainPanel mainPanel;

    /** A <code>Log4JWrapper</code>. */
	protected final Log4JWrapper logger;

    /** A parity persistence. */
    protected final Persistence persistence;

    /** The browser application. */
	private final Browser browser;

    /** An boolean indicating whether the "busy" indicator is applied. */
    private boolean busyIndicator;
 
    /**
	 * Create BrowserWindow.
	 * 
	 * @throws HeadlessException
	 */
	BrowserWindow(final Browser browser) throws HeadlessException {
		super("BrowserWindow");
		this.browser = browser;
		this.logger = new Log4JWrapper(getClass());
        this.persistence = PersistenceFactory.getPersistence(getClass());
        this.busyIndicator = false;
        final Boolean maximized = persistence.get("maximized", Boolean.FALSE);
		getRootPane().setBorder(new WindowBorder2());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                persist();
                browser.hibernate();
            }});
        initMenu(maximized);       
		setIconImage(com.thinkparity.ophelia.browser.Constants.Images.WINDOW_ICON_IMAGE);
        setTitle(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("BrowserWindow.Title"));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        final Point location = persistence.get("location", getMainWindowLocation());      
        location.x = (location.x < 0 ? 0 : location.x );
        location.y = (location.y < 0 ? 0 : location.y );
        setLocation(location.x, location.y);
        mainWindowLocation.setLocation(location); 
        final Dimension size = persistence.get("size", getMainWindowSize());
        mainWindowSize.setSize(size);
        setMaximizedBounds(SwingUtil.getPrimaryDesktopBounds());        
        if (maximized) {
            setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        }        
        setResizable(true);
        setMinimumSize(Dimensions.BrowserWindow.MIN_SIZE);
        setSize(getMainWindowSize());
		initComponents();
        bindF1Key();
        installWindowStateListener();

        new Resizer(browser, this, Boolean.FALSE, Resizer.ResizeEdges.ALL_EDGES);
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
     * @see java.awt.Window#setCursor(java.awt.Cursor)
     *
     */
    @Override
    public void setCursor(final Cursor cursor) {
        if (busyIndicator) {
            logger.logInfo("Cursor has been disabled.");
        } else {
            super.setCursor(cursor);
        }
    }

    /**
     * Set the window size.
     * 
     * @param d
     *            The new dimensions.
     */
	public void setMainWindowSize(final Dimension d) {
        final Dimension dFinal = new Dimension(d);
        
        // Honour the minimum window size.
        if (dFinal.getWidth() < Dimensions.BrowserWindow.MIN_SIZE.getWidth()) {
            dFinal.width = (int)Dimensions.BrowserWindow.MIN_SIZE.getWidth();
        }
        if (dFinal.getHeight() < Dimensions.BrowserWindow.MIN_SIZE.getHeight()) {
            dFinal.height = (int)Dimensions.BrowserWindow.MIN_SIZE.getHeight();
        }
        
        if (!getSize().equals(dFinal)) {
            setSize(dFinal);
            mainWindowSize.setSize(dFinal);
            roundCorners();
            validate();
        }
    }

    /**
     * Set the window size and location.
     * 
     * @param p
     *            The new location.
     * @param d
     *            The new dimensions.
     */
	public void setMainWindowSizeAndLocation(final Point p, final Dimension d) {
        final Point pFinal = new Point(p);
        final Dimension dFinal = new Dimension(d);
       
        // Honour the minimum window size. When going below the size limit in
        // either x or y, limit both the resize and the move in that direction.
        if (dFinal.getWidth() < Dimensions.BrowserWindow.MIN_SIZE.getWidth()) {
            if (pFinal.x != getLocation().x) {
                pFinal.x += (dFinal.getWidth() - Dimensions.BrowserWindow.MIN_SIZE.getWidth());
            }
            dFinal.width = (int)Dimensions.BrowserWindow.MIN_SIZE.getWidth();
        }
        if (dFinal.getHeight() < Dimensions.BrowserWindow.MIN_SIZE.getHeight()) {
            if (pFinal.y != getLocation().y) {
                pFinal.y += (dFinal.getHeight() - Dimensions.BrowserWindow.MIN_SIZE.getHeight()); 
            }
            dFinal.height = (int)Dimensions.BrowserWindow.MIN_SIZE.getHeight();
        }
        
        final Boolean move = !getLocation().equals(pFinal);
        final Boolean resize = !getSize().equals(dFinal);
        
        if (resize) {
            if (move) { // Move and resize
                setBounds(pFinal.x, pFinal.y, (int)dFinal.getWidth(), (int)dFinal.getHeight());
            }
            else {      // Resize only                
                setSize(dFinal);
            }
            mainWindowSize.setSize(dFinal);
            mainWindowLocation.setLocation(pFinal);
            roundCorners();
            validate();
        }
        else if (move) {
            // Move only
            setLocation(pFinal);
            mainWindowLocation.setLocation(pFinal);
        }
    }

    public void setVisible(final boolean b) {
        super.setVisible(b);
        if (b) {
            if (!persistence.get("maximized", Boolean.FALSE)) {
                roundCorners();
            }
        }
    }

    /**
     * Apply a busy indicator for the browser window.
     *
     */
    void applyBusyIndicator() {
        // set the window cursor to "wait"
        SwingUtil.setCursor(this, Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        // the the busy flag
        busyIndicator = true;
        // apply an intercept pane
        applyInterceptPane();
    }

    /**
     * Determine if the application is "busy". This will check the session for a
     * set cursor and if it is set (by applyBusyIndicator) it will return true.
     * 
     * @return True if the application is "busy".
     */
    Boolean isBusyIndicatorApplied() {
        return busyIndicator;
    }

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
        } catch (final InterruptedException ix) {
            throw new RuntimeException(ix);
        } catch (final InvocationTargetException itx) {
            throw new RuntimeException(itx);
        }
    }

	/**
     * Remove the busy indicator for the browser window.
     *
     */
    void removeBusyIndicator() {
        // reset the "busy" indicator
        busyIndicator = false;
        // set the "default" cursor
        SwingUtil.setCursor(this, Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        // remove the intercept pane
        removeInterceptPane();
    }

    /**
     * Re open the main window.
     * 
     * @return The main window.
     */
    void reOpen() {
        setVisible(true);
        applyRenderingHints();
        debug();

        browser.displayMainTitleAvatar();
        browser.displayMainStatusAvatar();
        browser.displayContainerTabAvatar();
    }

    /**
     * Bind the F1 key to the appropriate action.
     * 
     */
    private void bindF1Key() {
        mainPanel.bindF1Key(new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                browser.runF1Action();
            }
        });
    }

    /**
	 * Add the main panel to the window.
	 * 
	 */
	private void initComponents() {
		mainPanel = new MainPanel();
		add(mainPanel);
	}

    /**
     * Add the menu to the window.
     */
    private void initMenu(final Boolean maximized) {
        final JMenuBar menuBar = new BrowserMenuBar(browser, this, maximized);
        addMoveListener(menuBar);
        new BrowserPopupHelper().addPopupListener(menuBar);
        setJMenuBar(menuBar);
    }

    /**
     * Install a window state listener.
     * If the ancestor window is maximized then the resize control will be disabled.
     * 
     * @param window
     *            The <code>Window</code>.
     */
    private void installWindowStateListener() {
        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(final WindowEvent e) {
                if (e.getID() == WindowEvent.WINDOW_STATE_CHANGED) {
                    if (isMaximized(e)) {
                        squareCorners();
                    } else {
                        roundCorners();
                    }
                }
            }
        });
    }

    /**
     * Determine if the window event indicates a maximized JFrame window.
     * 
     * @param e
     *            A <code>WindowEvent</code>.
     */
    private Boolean isMaximized(final WindowEvent e) {
        return (e.getNewState() & JFrame.MAXIMIZED_BOTH) > 0;
    }

    /** Persist any window state. */
    private void persist() {
        // The value of getMainWindowLocation() is guaranteed up-to-date only
        // if the application is currently maximized.
        if (browser.isBrowserWindowMaximized()) {
            persistence.set("location", getMainWindowLocation());
            persistence.set("size", getMainWindowSize());
            persistence.set("maximized", Boolean.TRUE);
        } else {
            persistence.set("location", getLocation());
            persistence.set("size", getSize());
            persistence.set("maximized", Boolean.FALSE);
        }
    }

    /**
     * Make the corners of the browser window round.
     */
    private void roundCorners() {
        WINDOW_UTIL.applyRoundedEdges(this, Constants.WindowUtil.BROWSER_WINDOW_SIZE);
    }

    /**
     * Make the corners of the browser window square.
     */
    private void squareCorners() {
        WINDOW_UTIL.applyRectangleEdges(this);
    }
}
