 /*
 * Dec 30, 2005
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.swing.AbstractJFrame;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.Constants.Dimensions;
import com.thinkparity.ophelia.browser.application.browser.display.DisplayId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.platform.application.display.Display;
import com.thinkparity.ophelia.browser.platform.application.window.WindowBorder2;
import com.thinkparity.ophelia.browser.platform.util.persistence.Persistence;
import com.thinkparity.ophelia.browser.platform.util.persistence.PersistenceFactory;
import com.thinkparity.ophelia.browser.util.l2fprod.NativeSkin;

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
    
    /**
     * The location of the main window.
     * The variable is here so it persists.
     * 
     * @see #getMainWindowLocation()
     */
    private static Point mainWindowLocation;

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

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
    
    /** The browser application. */
	private final Browser browser;
    
    /** An apache logger. */
	protected final Logger logger;

	/** The main panel. */
	public MainPanel mainPanel;

    /** A parity persistence. */
    protected final Persistence persistence;
        
    /** The semi-transparent JPanel */
    private final SemiTransparentJPanel semiTransparentJPanel;
    
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
        final Boolean maximized = persistence.get("maximized", Boolean.FALSE);
		getRootPane().setBorder(new WindowBorder2());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                persist();
                browser.hibernate();
            }});
        initMenu(maximized);       
		setIconImage(com.thinkparity.ophelia.browser.Constants.Images.WINDOW_ICON_IMAGE);
		setTitle(java.util.ResourceBundle.getBundle("localization/JFrame_Messages").getString("BrowserWindow.Title"));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        final Point location = persistence.get("location", getMainWindowLocation());      
        location.x = (location.x < 0 ? 0 : location.x );
        location.y = (location.y < 0 ? 0 : location.y );
        setLocation(location.x, location.y);
        mainWindowLocation.setLocation(location); 
        final Dimension size = persistence.get("size", getMainWindowSize());
        mainWindowSize.setSize(size);
        if (maximized) {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            setMaximizedBounds(env.getMaximumWindowBounds());
            setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        }        
        setResizable(true);
        setMinimumSize(getMainWindowSize());
        setSize(getMainWindowSize());
		initComponents();
        if (!maximized) {
            roundCorners();
        }
        
        // Set up the semi-transparent JPanel
        semiTransparentJPanel = new SemiTransparentJPanel(Boolean.FALSE);
        getLayeredPane().add(semiTransparentJPanel, JLayeredPane.PALETTE_LAYER);
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
	 * Add the main panel to the window.
	 * 
	 */
	private void initComponents() {
		mainPanel = new MainPanel();
		add(mainPanel);
	}
    
    /**
     * Make the corners round.
     */
    private void roundCorners() {
    	new NativeSkin().roundCorners(this, Dimensions.BrowserWindow.CORNER_SIZE);
    }

	/**
     * Add the menu to the window.
     */
    private void initMenu(final Boolean maximized) {
        final JMenuBar menuBar = new BrowserMenuBar(browser, maximized);
        addMoveListener(menuBar);
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
    
    /**
     * Maximize (or un-maximize) the browser application.
     */
    public void maximizeMainWindow(final Boolean maximize) {
        if (maximize) {
            // Take care to save the un-maximized size and location in mainWindowSize
            // and mainWindowLocation, since these are the values we want to persist.
            mainWindowSize.setSize(getSize());
            mainWindowLocation.setLocation(getLocation());
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            setMaximizedBounds(env.getMaximumWindowBounds());
            setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        } else {
            setExtendedState(JFrame.NORMAL);
        }
        
        roundCorners();
    }
   
    /**
     * Enable or disable the semi-transparent layer.
     * 
     * @param enable
     *            Flag to enable or disable the semi-transparent layer.
     */
    public void enableSemiTransparentLayer(final Boolean enable) {
        if (null!=semiTransparentJPanel) {
            semiTransparentJPanel.setEnabled(enable);
            semiTransparentJPanel.paintImmediately(new Rectangle(0, 0, semiTransparentJPanel.getWidth(), semiTransparentJPanel.getHeight()));
        }
    }
    
    /**
     * A semi-transparent JPanel that covers the entire Browser window.
     */
    private class SemiTransparentJPanel extends javax.swing.JPanel {
        
        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;
        
        /** Flag indicating if the semi-transparent panel is enabled or not */
        private Boolean enabled;
        
        public SemiTransparentJPanel(final Boolean enabled) {
            super();
            setBorder(null);
            setOpaque(false);
            setSize(BrowserWindow.this.getSize());
            setLocation(0,0);
            this.enabled = enabled;
        }
        
        public void setEnabled(final Boolean enabled) {
            this.enabled = enabled;            
        }
        
        private AlphaComposite makeComposite(float alpha) {
            int type = AlphaComposite.SRC_OVER;
            return(AlphaComposite.getInstance(type, alpha));
        }
        
        public void paintComponent(final Graphics g) {
            super.paintComponent(g);
            if (enabled) {
                setSize(BrowserWindow.this.getSize());
                final Graphics2D g2 = (Graphics2D) g.create();
                try {
                    g2.setComposite(makeComposite(Colors.Browser.SemiTransparentLayer.LAYER_ALPHA));
                    g2.setPaint(Colors.Browser.SemiTransparentLayer.LAYER_COLOR);
                    g2.fill(new Rectangle(BrowserWindow.this.getSize()));
                }
                finally { g2.dispose(); }
            }
        }
    }   
}
