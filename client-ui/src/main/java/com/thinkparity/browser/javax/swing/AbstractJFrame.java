/*
 * Jan 21, 2006
 */
package com.thinkparity.browser.javax.swing;

import javax.swing.JFrame;

import com.thinkparity.browser.util.l10n.JFrameLocalization;

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
}
