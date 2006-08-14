/*
 * Created On: Jan 21, 2006
 * $Id$
 */
package com.thinkparity.codebase.swing;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.log4j.Logger;

import com.thinkparity.browser.platform.util.l10n.JFrameLocalization;

/**
 * An abstraction of the swing JDialog.
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public abstract class AbstractJDialog extends JDialog {

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/** Resource bundle based localziation. */
	protected final JFrameLocalization localization;

	/** An apache logger. */
	protected final Logger logger;

	/**
	 * Create a AbstractJFrame.
	 * 
	 * @param l18Context
	 *            The localization context.
	 */
	protected AbstractJDialog(final AbstractJFrame owner, final Boolean modal,
			final String l18Context) {
		this((JFrame) owner, modal, l18Context);
	}

	protected AbstractJDialog(final JFrame owner, final Boolean modal,
			final String l18nContext) {
		super(owner, modal);
		this.localization = new JFrameLocalization(l18nContext);
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
	 * @see JFrameLocalization#getString(String)
	 * 
	 */
	protected String getString(final String localKey) {
		return localization.getString(localKey);
	}

	/**
	 * @see JFrameLocalization#getString(String, Object[])
	 * 
	 */
	protected String getString(final String localKey, final Object[] arguments) {
		return localization.getString(localKey, arguments);
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
