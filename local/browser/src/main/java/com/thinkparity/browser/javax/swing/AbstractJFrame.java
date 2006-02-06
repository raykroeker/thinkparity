/*
 * Jan 21, 2006
 */
package com.thinkparity.browser.javax.swing;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.log4j.Logger;

import com.thinkparity.browser.platform.util.l10n.JFrameLocalization;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractJFrame extends JFrame {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Resource bundle based localziation.
	 * 
	 */
	protected final JFrameLocalization localization;

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Create a AbstractJFrame.
	 * 
	 * @param l18Context
	 *            The localization context.
	 */
	protected AbstractJFrame(final String l18Context) {
		super();
		this.localization = new JFrameLocalization(l18Context);
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
	 * Determine whether the user input for the frame is valid.
	 * 
	 * @return True if the input is valid; false otherwise.
	 */
	public Boolean isInputValid() { return Boolean.TRUE; }
}
