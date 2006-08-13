/*
 * Mar 11, 2006
 */
package com.thinkparity.browser.application.browser.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.BorderFactory;

import com.thinkparity.codebase.swing.AbstractJDialog;

import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class History2Window extends AbstractJDialog {

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

    /**
     * Calculate the location of the history window based upon the browser
     * window.
     * 
     * @param The
     *            parity owner.
     * @param hws
     *            The history window size.
     * @return The history window location.
     */
	private static Point calculateLocation(final BrowserWindow owner,
            final Dimension hws) {
		final Point bwl = owner.getLocation();
		final Dimension bws = owner.getSize();
		return new Point(bwl.x + bws.width, bwl.y + (bws.height - hws.height) - 45);
	}

	/**
     * Create a History2Window.
     * 
     * @param browserWindow
     *            The parity owner.
     * @param avatar
     *            The history avatar.
     */
	public History2Window(final BrowserWindow owner, final Avatar avatar) {
		super(owner, Boolean.FALSE, "History2Window");
		getRootPane().setBorder(BorderFactory.createLineBorder(new Color(117, 130, 162, 255)));
        setUndecorated(true);
		setLayout(new GridBagLayout());
        setSize(new Dimension(306, 430));
		setLocation(calculateLocation(owner, getSize()));
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

