/*
 * Mar 9, 2006
 */
package com.thinkparity.browser.platform.application.window;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JDialog;

import com.thinkparity.browser.application.browser.window.WindowId;
import com.thinkparity.browser.javax.swing.AbstractJDialog;
import com.thinkparity.browser.javax.swing.AbstractJFrame;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class Window extends AbstractJDialog {

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/** The panel onto which all displays are dropped. */
	protected WindowPanel windowPanel;

	/** A lookup for window sizes. */
	private final WindowSize windowSize;

    /**
     * Create a parity Window.
     * 
     * @param owner
     *            The window owner.
     * @param modal
     *            Whether or not to display a modal window.
     * @param l18nContext
     *            The localisation context.
     */
	public Window(final AbstractJFrame owner, final Boolean modal,
            final String l18nContext) {
        super(owner, modal, l18nContext);
        this.windowSize = new WindowSize();
        setTitle(getString("Title"));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setUndecorated(true);
    }

    /**
	 * Obtain the window id.
	 * 
	 * @return The window id.
	 */
	public abstract WindowId getId();
   
	/**
     * Open an avatar in the window.
     * 
     * @param avatar
     *            The avatar.
     */
	public void open(final Avatar avatar) {
        initComponents(avatar);
        debugGeometry();
        debugLookAndFeel();
        getRootPane().setBorder(new WindowBorder());
        setSize(windowSize.get(avatar.getId()));
        setLocation(calculateLocation());
        invalidate();
        setVisible(true);
	}

    /** Apply an escape listener that will dispose of the window. */
    protected void applyEscapeListener() {
        new WindowCustodian().applyEscapeListener(this);
    }

    /**
	 * Calculate the location for the window based upon its owner and its size.
	 * 
	 * @return The location of the window centered on the owner.
	 */
	protected Point calculateLocation() {
		final Dimension os = getOwner().getSize();
		final Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension ws = getSize();
		if(0 == os.width || 0 == os.height) {
			final Point l = getLocation();
			l.x = (ss.width - ws.width) / 2;
			l.y = (ss.height - ws.height) / 2;
			return l;
		}
		else {
			final Point l = getOwner().getLocation();
			l.x += (os.width - ws.width) / 2;
			l.y += (os.height - ws.height) / 2;
	
			if(l.x + ws.width > (ss.width)) { l.x = ss.width - ws.width; }
			if(l.y + ws.height > (ss.height)) { l.y = ss.height - ws.height; }
	
			if(l.x < 0) { l.x = 0; }
			if(l.y < 0) { l.y = 0; }
			return l;
		}
	}

    /** Dispose of the window. */
    protected void disposeWindow() { dispose(); }

    /**
     * Initialize the swing components on the window.
     * 
     * @param avatar
     *            The avatar.
     */
	protected void initComponents(final Avatar avatar) {
		avatar.reload();

        windowPanel = new WindowPanel();
        windowPanel.addPanel(avatar);

        add(windowPanel);
    }
}
