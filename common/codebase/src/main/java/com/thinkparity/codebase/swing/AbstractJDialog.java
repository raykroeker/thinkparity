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

import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b>thinkParity CommonCodebase Abstract JDialog<br>
 * <b>Description:</b>An abstraction of a swing dialog.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.4
 */
public abstract class AbstractJDialog extends JDialog {

	/** An apache logger. */
	protected final Log4JWrapper logger;

	/**
     * Create a AbstractJDialog.
     * 
     * @param owner
     *            The owner <code>AbstractJFrame</code>.
     * @param modal
     *            Whether or not to display the dialog in a modal fashion.
     */
	protected AbstractJDialog(final AbstractJFrame owner, final Boolean modal) {
		super(owner, modal);
        this.logger = new Log4JWrapper(getClass());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
	 * Debug the geometry of the main window; and all of the children throughout
	 * the hierarchy.
	 * 
	 */
	protected void debugGeometry() {
		logger.logDebug(getClass().getSimpleName());
		logger.logDebug("l:" + getLocation());
		logger.logDebug("b:" + getBounds());
		logger.logDebug("i:" + getInsets());
	}

	/**
	 * Debug the look and feel.
	 *
	 */
	protected void debugLookAndFeel() {
		logger.logDebug(getClass().getSimpleName());
		logger.logDebug("lnf:{0}", UIManager.getLookAndFeel().getClass().getName());
		final StringBuilder builder = new StringBuilder("installed lnf:[");
		boolean isFirst = true;
		for (final LookAndFeelInfo lnfi : UIManager.getInstalledLookAndFeels()) {
			if (isFirst) {
                isFirst = false;
			} else {
                builder.append(",");
			}
			builder.append(lnfi.getClassName());
		}
		builder.append("]");
		logger.logDebug(builder.toString());
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
