/*
 * Created On: Jan 21, 2006
 * $Id$
 */
package com.thinkparity.codebase.swing;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

/**
 * An abstraction of a swing JFrame.  Used by all thinkParity main windows.
 * 
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
