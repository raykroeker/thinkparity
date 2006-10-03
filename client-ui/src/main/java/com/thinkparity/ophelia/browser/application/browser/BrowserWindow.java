 /*
 * Dec 30, 2005
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.swing.AbstractJFrame;

import com.l2fprod.gui.nativeskin.NativeSkin;
import com.l2fprod.gui.region.Region;
import com.thinkparity.ophelia.browser.Constants.Dimensions;
import com.thinkparity.ophelia.browser.application.browser.display.DisplayId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.platform.application.display.Display;
import com.thinkparity.ophelia.browser.platform.application.window.WindowBorder2;
import com.thinkparity.ophelia.browser.platform.util.persistence.Persistence;
import com.thinkparity.ophelia.browser.platform.util.persistence.PersistenceFactory;
import com.thinkparity.ophelia.browser.util.Swing.Constants.Images;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserWindow extends AbstractJFrame {
    
    /**
	 * The size of the main window.
     * The variable is here so it persists.
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
	private static Dimension getMainWindowSize() {
		if(null == mainWindowSize) {
			// DIMENSION BrowserWindow 450x587
			mainWindowSize = new Dimension(Dimensions.BrowserWindow.DEFAULT_SIZE);
		}
		return mainWindowSize;
	}
    
    /** The browser application. */
	private final Browser browser;
    
    /** An apache logger. */
	protected final Logger logger;

	/** The main panel. */
	public MainPanel mainPanel;

    /** A parity persistence. */
    protected final Persistence persistence;
    
    /** The Resizer */
    @SuppressWarnings("unused")
    private final Resizer resizer;

	/**
	 * Create a BrowserWindow.
	 * 
	 * @throws HeadlessException
	 */
	BrowserWindow(final Browser browser) throws HeadlessException {
		super("BrowserWindow");
		this.browser = browser;
		this.logger = Logger.getLogger(getClass());
        this.persistence = PersistenceFactory.getPersistence(getClass());       
		getRootPane().setBorder(new WindowBorder2());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                persist();
                browser.hibernate();
            }});
        initMenu();       
		setIconImage(Images.WINDOW_ICON_IMAGE);
		setTitle(java.util.ResourceBundle.getBundle("localization/JFrame_Messages").getString("BrowserWindow.Title"));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        final Point location = persistence.get("location", new Point(100, 100));      
        location.x = (location.x < 0 ? 0 : location.x );
        location.y = (location.y < 0 ? 0 : location.y );
        setLocation(location.x, location.y);
        final Dimension size = persistence.get("size", getMainWindowSize());
        mainWindowSize.setSize(size);        
        setResizable(true);
        setMinimumSize(getMainWindowSize());
        setSize(getMainWindowSize());
		initComponents();
        resizer = new Resizer(browser, this, Boolean.FALSE, Resizer.ResizeEdges.ALL_EDGES);
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
	 * Add the main panel to the window.
	 * 
	 */
	private void initComponents() {
		mainPanel = new MainPanel();
		add(mainPanel);
        roundCorners();
	}
    
    /**
     * Make the corners round.
     */
    private void roundCorners() {
        final NativeSkin nativeSkin = NativeSkin.getInstance();
        Region region = nativeSkin.createRoundRectangleRegion(0,0,this.getWidth()+1,this.getHeight()+1,9,9);
        nativeSkin.setWindowRegion(this, region, true);
    }

	/**
     * Add the menu to the window.
     */
    private void initMenu() {
        final JMenuBar menuBar = new BrowserMenuBar(browser);
        setJMenuBar(menuBar);
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
        }
        catch(final InterruptedException ix) { throw new RuntimeException(ix); }
        catch(final InvocationTargetException itx) { throw new RuntimeException(itx); }
    }
    
    /** Persist any window state. */
    private void persist() {
        persistence.set("location", getLocation());
        persistence.set("size", getMainWindowSize());
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
        browser.displayTabContainerAvatar();
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
            setMinimumSize(dFinal);
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
            setMinimumSize(dFinal);
            mainWindowSize.setSize(dFinal);
            roundCorners();
            validate();
        }
        else if (move) {
            // Move only
            setLocation(pFinal);
        }
    }
}
