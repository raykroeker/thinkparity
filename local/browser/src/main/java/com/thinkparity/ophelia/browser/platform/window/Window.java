/*
 * Mar 9, 2006
 */
package com.thinkparity.ophelia.browser.platform.window;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;

import com.thinkparity.codebase.swing.AbstractJDialog;
import com.thinkparity.codebase.swing.AbstractJFrame;
import com.thinkparity.codebase.swing.JFrameAlphaPanel;
import com.thinkparity.codebase.swing.border.MovableDropShadowBorder;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.Constants;
import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.Constants.Sundry;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.application.window.WindowPanel;
import com.thinkparity.ophelia.browser.util.window.WindowUtil;
import com.thinkparity.ophelia.browser.util.window.WindowUtilProvider;

/**
 * <b>Title:</b>thinkParity OpheliaUI Platform Window Abstraction<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Window extends AbstractJDialog {

    /** A panel used to create an alpha "sheen" on top of the owner window. */
    private static final JFrameAlphaPanel ALPHA_PANEL;

    /** An instance of <code>WindowUtil</code>. */
    private static final WindowUtil WINDOW_UTIL;

	static {
        ALPHA_PANEL = new JFrameAlphaPanel(Sundry.ALPHA_PANEL_ALPHA,
                Colors.Browser.Window.ALPHA_PANEL_COLOR);
        WINDOW_UTIL = WindowUtilProvider.getInstance().getWindowUtil();
    }

    /** The panel onto which all displays are dropped. */
	protected WindowPanel windowPanel;

    /** The border. */
    private MovableDropShadowBorder border = null;

    /** The owner <code>AbstractJFrame</code>. */
    private final AbstractJFrame owner;

	/**
     * Create Window.
     * 
     * @param owner
     *            The window owner.
     * @param modal
     *            Whether or not to display a modal window.
     * @param titleText
     *            Whether or not to display title text.         
     * @param l18nContext
     *            The localisation context.
     */
	Window(final AbstractJFrame owner, final Boolean modal) {
        super(owner, modal);
        setUndecorated(true);
        this.owner = owner;
    }

    /**
     * Open an avatar in the window.
     * 
     * @param avatar
     *            An <code>Avatar</code>.
     */
	public void open(final Avatar avatar) {
        if (null == owner) {
            logger.logInfo("No window owner for avatar {0}.", avatar.getId());
        } else {
            owner.applyAlphaPanel(ALPHA_PANEL);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(final WindowEvent e) {
                    removeWindowListener(this);
                    owner.removeAlphaPanel(ALPHA_PANEL);
                }
            });
        }
        initComponents(avatar);
        debugGeometry();
        debugLookAndFeel();
        getRootPane().setBorder(getBorder());
        pack();
        setLocation(calculateLocation());
        roundCorners();
        invalidate();
        initBorder();
        setVisible(true);
	}

    /**
     * @see java.awt.Component#setBounds(java.awt.Rectangle)
     * 
     */
    @Override
    public void setBounds(final Rectangle r) {
        getBorder().settingBounds(r);
        super.setBounds(r);
    }

    /**
     * @see java.awt.Component#setLocation(int, int)
     * 
     */
    @Override
    public void setLocation(final int x, final int y) {
        getBorder().settingLocation(x, y);    
        super.setLocation(x, y);
    }

    /**
     * @see java.awt.Component#setSize(int, int)
     * 
     */
    @Override
    public void setSize(int width, int height) {
        getBorder().settingSize(width, height);
        super.setSize(width, height);
    }

    /**
     * Bind the escape key to dispose.
     *
     */
    protected final void bindEscapeToDispose() {
        WindowHelper.bindEscape(this, new AbstractAction() {
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
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

    /**
     * Initialize the swing components on the window.
     * 
     * @param avatar
     *            The avatar.
     */
	protected void initComponents(final Avatar avatar) {
		avatar.reload();
        avatar.bindF1Key();

        windowPanel = new WindowPanel();
        windowPanel.addPanel(avatar);

        add(windowPanel);     
    }

	/**
     * Obtain the border for the window. If the border does not yet exist it
     * will be created.
     * 
     * @return A <code>MovableDropShadowBorder</code>.
     */
    private MovableDropShadowBorder getBorder() {
        if (null == border) {
            try {
                border = new MovableDropShadowBorder(this,
                        Colors.Browser.Window.BORDER_TOP, Colors.Browser.Window.BORDER_BOTTOM,
                        Colors.Browser.Window.BORDER_TOP_LEFT, Colors.Browser.Window.BORDER_BOTTOM_LEFT,
                        Colors.Browser.Window.BORDER_TOP_RIGHT, Colors.Browser.Window.BORDER_BOTTOM_RIGHT);
            } catch (final AWTException awtx) {
                throw new BrowserException("Cannot instantiate window border.", awtx);
            }
        }
        return border;
    }

    /**
     * Initialize the border. This should be done just
     * before the dialog is made visible.
     * 
     */
    private void initBorder() {
        getBorder().initialize();
    }

    /**
     * Apply a rounded corner to the window.
     * 
     * @see WindowUtil#applyRoundedEdges(java.awt.Window, Integer)
     */
    private void roundCorners() {
        WINDOW_UTIL.applyRoundedEdges(this, Constants.WindowUtil.DEFAULT_SIZE);
    }
}
