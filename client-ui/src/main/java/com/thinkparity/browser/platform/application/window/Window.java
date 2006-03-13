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
	 * A lookup for window sizes for avatars.
	 * 
	 */
	private final WindowSize windowSize;

	/**
	 * Create a Window.
	 * 
	 * @param l18Context
	 *            The localization context
	 */
	public Window(final AbstractJFrame owner, final Boolean modal,
			final String l18nContext) {
		super(owner, modal, l18nContext);
		this.windowSize = new WindowSize();
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
		setSize(windowSize.get(avatar.getId()));
		invalidate();
		setVisible(true);
	}

	protected void initComponents(final Avatar avatar) {
		avatar.reload();

		windowPanel = new WindowPanel();
		windowPanel.addAvatar(avatar);

		add(windowPanel);
	}
}
