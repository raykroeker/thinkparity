/*
 * Mar 9, 2006
 */
package com.thinkparity.browser.platform.application.window;

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

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The panel onto which all displays are dropped.
	 * 
	 */
	protected WindowPanel windowPanel;

	/**
	 * Create a Window.
	 * 
	 * @param l18Context
	 *            The localization context
	 */
	public Window(final AbstractJFrame owner, final Boolean modal,
			final String l18nContext) {
		super(owner, modal, l18nContext);
		setTitle(getString("Title"));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	/**
	 * Obtain the window id.
	 * 
	 * @return The window id.
	 */
	public abstract WindowId getId();

	/**
	 * Open the window.
	 *
	 */
	public void open(final Avatar avatar) {
		initComponents(avatar);
		debugGeometry();
		debugLookAndFeel();
		setVisible(true);
	}

	private void initComponents(final Avatar avatar) {
		windowPanel = new WindowPanel();
		windowPanel.addAvatar(avatar);

		add(windowPanel);
	}
}
