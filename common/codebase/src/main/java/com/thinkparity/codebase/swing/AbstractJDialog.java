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

import org.apache.log4j.Logger;

/**
 * An abstraction of the swing JDialog.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public abstract class AbstractJDialog extends JDialog {

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/** An apache logger. */
	protected final Logger logger;

	/**
     * Create a AbstractJDialog.
     * 
     * @param owner
     *            The owner <code>AbstractJFrame</code>.
     * @param modal
     *            Whether or not to display the dialog in a modal fashion.
     * @param l18nContext
     *            A localization context <code>String</code>.
     */
	protected AbstractJDialog(final AbstractJFrame owner, final Boolean modal,
			final String l18Context) {
		super(owner, modal);
        this.logger = Logger.getLogger(getClass());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
	 * Debug the geometry of the main window; and all of the children throughout
	 * the hierarchy.
	 * 
	 */
	protected void debugGeometry() {
		logger.debug(getClass().getSimpleName());
		logger.debug("l:" + getLocation());
		logger.debug("b:" + getBounds());
		logger.debug("i:" + getInsets());
	}

	/**
	 * Debug the look and feel.
	 *
	 */
	protected void debugLookAndFeel() {
		logger.debug(getClass().getSimpleName());
		logger.debug("lnf:" + UIManager.getLookAndFeel().getClass().getName());
		final StringBuffer buffer = new StringBuffer("installed lnf:[");
		boolean isFirst = true;
		for(LookAndFeelInfo lnfi : UIManager.getInstalledLookAndFeels()) {
			if(isFirst) { isFirst = false; }
			else { buffer.append(","); }
			buffer.append(lnfi.getClassName());
		}
		buffer.append("]");
		logger.debug(buffer);
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
