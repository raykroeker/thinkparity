/*
 * Mar 11, 2006
 */
package com.thinkparity.browser.application.browser.window;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.application.window.Window;
import com.thinkparity.browser.platform.util.ImageIOUtil;
import com.thinkparity.browser.util.NativeSkinUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HistoryWindowv1 extends Window {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	private static final BufferedImage windowImage;

	static { windowImage = ImageIOUtil.read("HistoryWindow.png"); }

	public static BufferedImage getImage() { return windowImage; }

	private static Point calculateLocation(final BrowserWindow browserWindow) {
		final Point bwl = browserWindow.getLocation();
		final Dimension bws = browserWindow.getSize();
		return new Point(bwl.x + bws.width, bwl.y + 35);
	}

	/**
	 * Create a HistoryWindow.
	 * 
	 * @param browserWindow
	 *            The main browser window.
	 */
	public HistoryWindowv1(final BrowserWindow browserWindow) {
		super(browserWindow, Boolean.FALSE, "");
		setUndecorated(true);
		setLayout(new GridBagLayout());
		setLocation(calculateLocation(browserWindow));
		setResizable(false);
		applyNativeSkin();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.window.Window#getId()
	 * 
	 */
	public WindowId getId() { return WindowId.HISTORY; }

	/**
	 * Apply a rounded border to this window.
	 *
	 */
	private void applyNativeSkin() {
		NativeSkinUtil.applyNativeSkin(this);
	}

	/**
	 * @see com.thinkparity.browser.platform.application.window.Window#initComponents(com.thinkparity.browser.platform.application.display.avatar.Avatar)
	 * 
	 */
	protected void initComponents(final Avatar avatar) {
		avatar.reload();

		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		add(avatar, c.clone());
	}
}
