/*
 * Created On: Jan 21, 2006
 * $Id$
 */
package com.thinkparity.codebase.swing;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import com.thinkparity.codebase.assertion.Assert;

import org.apache.log4j.Logger;

/**
 * An abstraction of a swing JFrame.  Used by all thinkParity main windows.
 * 
 * TODO Add an abstract jframe within the ophelia UI component that will apply
 * the native skin.
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public abstract class AbstractJFrame extends JFrame {

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/** An apache logger. */
	protected final Logger logger;

    /** The current alpha panel. */
    private JFrameAlphaPanel alphaPanel;

    /** Container tools for swing. */
	private final ContainerTools containerTools;

    /** An intercept pane used to "disable" all events in a the frame. */
    private JFrameInterceptPane interceptPane;

    /** A helper class dedicated to encapsulation of a move visitor. */
    private JComponentMoveHelper moveHelper;

    /** A helper class for requesting focus. */
    private JComponentRequestFocusHelper requestFocusHelper;

    /**
	 * Create a AbstractJFrame.
	 * 
	 * @param l18Context
	 *            The localization context.
	 */
	protected AbstractJFrame(final String l18Context) {
		super();
		this.containerTools = new ContainerTools(this);
        this.logger = Logger.getLogger(getClass());
	}

    /**
     * Apply an alpha panel. The most recent call to this api will be the
     * applied alpha panel.
     * 
     */
    public final void applyAlphaPanel(final JFrameAlphaPanel alphaPanel) {
        this.alphaPanel = alphaPanel;
        this.alphaPanel.setJFrame(this);
        getLayeredPane().add(this.alphaPanel, JLayeredPane.PALETTE_LAYER);
        repaint();
    }

    /**
     * Apply the intercept pane.
     *
     */
    public final void applyInterceptPane() {
        Assert.assertNotTrue(isInterceptPaneApplied(),
                "Intercept pane has already been applied.");
        if (null == interceptPane) {
            interceptPane = new JFrameInterceptPane();
        }
        setGlassPane(interceptPane);
        interceptPane.setVisible(true);
        repaint();
    }

    /**
     * Determine if an alpha panel is set.
     * 
     * @return True if an alpha panel is set.
     */
    public final Boolean isAppliedAlphaPanel() {
        return null != alphaPanel;
    }

    /**
     * Determine if an specific alpha panel is set.
     * 
     * @return True if an alpha panel is set.
     */
    public final Boolean isAppliedAlphaPanel(final JFrameAlphaPanel alphaPanel) {
        if (isAppliedAlphaPanel()) {
            return this.alphaPanel.equals(alphaPanel);
        } else {
            return Boolean.FALSE;
        }
    }

    /**
	 * Determine whether the user input for the frame is valid.
	 * 
	 * @return True if the input is valid; false otherwise.
	 */
	public Boolean isInputValid() { return Boolean.TRUE; }

    /**
     * Determine if the intercept pane is applied.
     * 
     * @return True if the intercept pane is applied.
     */
    public final Boolean isInterceptPaneApplied() {
        if (null == interceptPane) {
            return Boolean.FALSE;
        } else {
            return getGlassPane().equals(interceptPane)
                    && Boolean.valueOf(interceptPane.isVisible());
        }
    }

    /**
     * Remove the alpha panel.
     *
     */
    public final void removeAlphaPanel(final JFrameAlphaPanel alphaPanel) {
        Assert.assertTrue(isAppliedAlphaPanel(alphaPanel), "Alpha panel has not been applied.");
        getLayeredPane().remove(this.alphaPanel);
        this.alphaPanel = null;
        repaint();
    }

    /**
     * Remove the intercept pane.
     *
     */
    public final void removeInterceptPane() {
        Assert.assertTrue(isInterceptPaneApplied(),
                "Intercept pane has not been applied.");
        getGlassPane().setVisible(false);
        repaint();
    }

    /**
     * Add a move listener to the component for the panel. A move listener will
     * allow the user will be able to click on and drag the component in order
     * to move the underlying window ancestor.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     */
    protected final void addMoveListener(final JComponent jComponent) {
        if (null == moveHelper) {
            moveHelper = new JComponentMoveHelper(this);
        }
        moveHelper.addListener(jComponent);
    }

	/**
	 * <p>Apply specific renderings for the JFrame.</p>
	 * <p>The hints applied are:<ul>
	 * <li>rendering:  render quality
	 * <li>antialiasing:  on</p>
	 * 
	 */
	protected void applyRenderingHints() {
		final Graphics2D g2 = (Graphics2D) getGraphics();
		applyRenderingHint(g2, RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		applyRenderingHint(g2, RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

    /**
     * Calculate the location for the window based upon its owner and its size.
     * 
     * @return The location <code>Point</code>.
     */
    protected Point calculateLocation() {
    	final Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
    	final Dimension ws = getSize();

        final Point l = getLocation();
        l.x = (ss.width - ws.width) / 2;
        l.y = (ss.height - ws.height) / 2;

        if (l.x + ws.width > (ss.width)) {
            l.x = ss.width - ws.width;
        }
        if (l.y + ws.height > (ss.height)) {
            l.y = ss.height - ws.height;
        }

        if (l.x < 0) {
            l.x = 0;
        }
        if (l.y < 0) {
            l.y = 0;
        }

        return l;
    }

    /**
	 * Debug the container.
	 *
	 */
	protected void debug() {
        containerTools.debug();
	}

    /**
     * Remove a request focus listener from a component.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     * @see AbstractJFrame#addFocusListener(JComponent)
     */
    protected final void removeRequestFocusListener(final JComponent jComponent) {
        if (null == requestFocusHelper)
            return;
        requestFocusHelper.removeListener(jComponent);
    }

	/**
     * Remove a move listener from a component for the panel.
     * 
     * @param jComponent
     *            A <code>JPanel</code>.
     * @see AbstractJPanel#addMoveListener(JComponent)
     */
    protected final void removeMoveListener(final JComponent jComponent) {
        if (null == moveHelper)
            return;
        moveHelper.removeListener(jComponent);
    }

    /**
	 * Apply a specific rendering hint to the graphics.
	 * 
	 * @param g2
	 *            The 2D graphics.
	 * @param key
	 *            The rendering hint key.
	 * @param value
	 *            The rendering hint value.
	 */
	private void applyRenderingHint(final Graphics2D g2,
			final RenderingHints.Key key, final Object value) {
		g2.setRenderingHint(key, value);
	}
}
