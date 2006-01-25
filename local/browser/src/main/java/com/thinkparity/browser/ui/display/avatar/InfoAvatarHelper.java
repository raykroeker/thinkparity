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
	 * Component used as a filler.
	 * 
	 */
	private static final Component FILLER;

	static { FILLER = LabelFactory.create(); }

	/**
	 * The avatar.
	 * 
	 */
	private Avatar avatar;

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
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		avatar.add(FILLER, c.clone());
	}

	/**
	 * Add the heading bannder.
	 * 
	 * @param headingText
	 *            The heading text.
	 */
	void addHeading(final String headingText) {
		// h:  24 px
		// x indent:  29 px
		final GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		final JLabel jLabel =
			LabelFactory.create(UIConstants.DefaultFontBold, headingText);
		jLabel.setBackground(new Color(117, 138, 155));
		jLabel.setForeground(Color.WHITE);
		jLabel.setIcon(getHeadingIcon());
        jLabel.setOpaque(true);

		avatar.add(jLabel, c.clone());
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
