/*
 * Jan 25, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.thinkparity.browser.ui.UIConstants;
import com.thinkparity.browser.ui.component.LabelFactory;

import com.thinkparity.codebase.ResourceUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class InfoAvatarHelper {

	/**
	 * The avatar.
	 * 
	 */
	private Avatar avatar;

	/**
	 * Component used as a filler.
	 * 
	 */
	private Component fillerJLabel;

	/**
	 * Heading label.
	 * 
	 */
	private JLabel headingJLabel;

	/**
	 * Create a InfoAvatarHelper.
	 * 
	 */
	InfoAvatarHelper(final Avatar avatar) {
		super();
		this.avatar = avatar;
	}

	/**
	 * Add a filler component.
	 *
	 */
	void addFiller() {
		if(null == fillerJLabel) {
			fillerJLabel = LabelFactory.create();
			fillerJLabel.setName("InfoAvatarHelper - Filler Label");
		}
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;

		avatar.add(fillerJLabel, c.clone());
	}

	/**
	 * Add the heading bannder.
	 * 
	 * @param headingText
	 *            The heading text.
	 */
	void addHeading(final String headingText) {
		if(null == headingJLabel) {
			headingJLabel =
				LabelFactory.create(headingText, UIConstants.DefaultFontBold);
			headingJLabel.setBackground(new Color(117, 138, 155));
			headingJLabel.setForeground(Color.WHITE);
			headingJLabel.setIcon(getHeadingIcon());
			headingJLabel.setOpaque(true);
		}
		// h:  24 px
		// x indent:  29 px
		final GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;

		avatar.add(headingJLabel, c.clone());
	}

	/**
	 * Remove the filler component.
	 *
	 */
	void removeFiller() {
		if(null != fillerJLabel) { avatar.remove(fillerJLabel); }
	}

	/**
	 * Obtain the history heading ICON.
	 * 
	 * @return The history heading ICON.
	 */
	private Icon getHeadingIcon() {
		return new ImageIcon(ResourceUtil.getURL("images/infoHeading.png"));
	}
}
