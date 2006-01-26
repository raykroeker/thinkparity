/*
 * Jan 21, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.thinkparity.browser.ui.UIConstants;
import com.thinkparity.browser.ui.component.LabelFactory;
import com.thinkparity.browser.util.State;

import com.thinkparity.codebase.ResourceUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class BrowserLogoAvatar extends Avatar {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The logo icon.
	 * 
	 */
	private Image logoImage;

	/**
	 * Create a BrowserLogoAvatar.
	 * 
	 */
	public BrowserLogoAvatar() {
		super("BrowserLogoAvatar");
		setLayout(new GridBagLayout());

		initAvatarComponents();
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.BROWSER_LOGO; }

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setState(com.thinkparity.browser.util.State)
	 * 
	 */
	public void setState(State state) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g.create();
		// TODO Center the image vertically based upon its dimensions; and the
		// dimensions of the avatar.
		try { g2.drawImage(getLogoImage(), 0, 10, this); }
		finally { g2.dispose(); }
	}

	/**
	 * Obtain the logo image. After the first read; it will remain in memory.
	 * 
	 * @return The logo image.
	 */
	private Image getLogoImage() {
		if(null == logoImage) {
			try {
				logoImage =
					ImageIO.read(ResourceUtil.getURL("images/parityLogo.png"));
			}
			catch(IOException iox) {
				// NOTE Error Handler Code
				logoImage = null;
			}
		}
		return logoImage;
	}

	/**
	 * Obtain the icon used to separate the add and settings labels.
	 * 
	 * @return The icon.
	 */
	private Icon getSeparatorIcon() {
		return new ImageIcon(ResourceUtil.getURL("images/logoSeparator.png"));
	}

	/**
	 * Initialize the avatar components.
	 *
	 */
	private void initAvatarComponents() {
		// : - 3px
		// Add------:------Settings------:------Login------:------Help------|
		//      11px    8px            9x    9px       10px   10px       7px
		//		
		//		
		//		
		//		
		//		
		//		
		//		
		//		
		//		
		//
		final GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridheight = 2;
		c.insets = new Insets(33, 28, 0, 0);
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(LabelFactory.create(), c.clone());

		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 0.0;
		c.insets = new Insets(11, 0, 0, 0);

		c.gridx = 1;
		add(LabelFactory.create(UIConstants.SmallFont, getString("Add")), c.clone());

		c.gridx = 2;
		c.insets.left = 7;
		c.insets.right = 7;
		add(new JLabel(getSeparatorIcon()), c.clone());

		c.gridx = 3;
		c.insets.left = 0;
		c.insets.right = 0;
		add(LabelFactory.create(UIConstants.SmallFont, getString("Settings")), c.clone());

		c.gridx = 4;
		c.insets.left = 7;
		c.insets.right = 7;
		add(new JLabel(getSeparatorIcon()), c.clone());

		c.gridx = 5;
		c.insets.left = 0;
		c.insets.right = 0;
		add(LabelFactory.create(UIConstants.SmallFont, getString("Login")), c.clone());

		c.gridx = 6;
		c.insets.left = 7;
		c.insets.right = 7;
		add(new JLabel(getSeparatorIcon()), c.clone());

		c.gridx = 7;
		c.insets.left = 0;
		c.insets.right = 6;
		add(LabelFactory.create(UIConstants.SmallFont, getString("Help")), c.clone());
	}
}
