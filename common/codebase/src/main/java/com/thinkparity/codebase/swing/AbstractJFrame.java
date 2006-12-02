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

	/** Container tools for swing. */
	private final ContainerTools containerTools;

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

    /** A helper class dedicated to encapsulation of a move visitor. */
    private JComponentMoveHelper moveHelper;

	/**
	 * Determine whether the user input for the frame is valid.
	 * 
	 * @return True if the input is valid; false otherwise.
	 */
	public Boolean isInputValid() { return Boolean.TRUE; }

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
