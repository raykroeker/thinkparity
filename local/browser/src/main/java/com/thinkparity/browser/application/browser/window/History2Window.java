/*
 * Mar 11, 2006
 */
package com.thinkparity.browser.application.browser.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.border.EtchedBorder;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.javax.swing.AbstractJDialog;
import com.thinkparity.browser.javax.swing.border.CustomEtechedBorder;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class History2Window extends AbstractJDialog {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	private static Point calculateLocation(final BrowserWindow browserWindow, final Dimension hws) {
		final Point bwl = browserWindow.getLocation();
		final Dimension bws = browserWindow.getSize();
		return new Point(bwl.x + bws.width, bwl.y + (bws.height - hws.height));
	}

	/**
	 * Create a HistoryWindow.
	 * 
	 * @param browserWindow
	 *            The main browser window.
	 */
	public History2Window(final BrowserWindow browserWindow, final Avatar avatar) {
		super(browserWindow, Boolean.FALSE, "");
		final Color c1 = new Color(196, 213, 255, 255);
		final Color c2 = new Color(117, 130, 162, 255);
		getRootPane().setBorder(new CustomEtechedBorder(EtchedBorder.RAISED, c1, c2));
		setUndecorated(true);
		setLayout(new GridBagLayout());
		setSize(new Dimension(306, 462));
		setLocation(calculateLocation(browserWindow, getSize()));
		initComponents(avatar);
	}

	/**
	 * @see com.thinkparity.browser.platform.application.window.Window#initComponents(com.thinkparity.browser.platform.application.display.avatar.Avatar)
	 * 
	 */
	protected void initComponents(final Avatar avatar) {
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		add(avatar, c.clone());
	}
}
