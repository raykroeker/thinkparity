/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.platform.application.window;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class WindowPanel extends AbstractJPanel {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Grid bag constraints for the avatars.
	 * 
	 */
	private final GridBagConstraints ac;

	/**
	 * List of all avatars currently on the panel.
	 * 
	 */
	private final List<Avatar> avatars;

	/**
	 * Create a MainPanel.
	 * 
	 */
	public WindowPanel() {
            super("MainPanel");
            this.ac = new GridBagConstraints();
            this.ac.fill = GridBagConstraints.BOTH;
            this.ac.weightx = 1;
            this.ac.weighty = 1;
            this.ac.gridy = 1;
            this.avatars = new LinkedList<Avatar>();
            setLayout(new GridBagLayout());
            setOpaque(false);
            initComponents();
	}

	/**
	 * @see com.thinkparity.browser.javax.swing.AbstractJPanel#debugGeometry()
	 * 
	 */
	public void debugGeometry() { super.debugGeometry(); }

	/**
	 * Add an avatar to the main panel.
	 * 
	 * @param avatar
	 *            The avatar to add.
	 */
	void addAvatar(final Avatar avatar) {
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		add(new WindowTitleDecoration(), c.clone());

		avatars.add(avatar);
		ac.gridy++;
		add((Component) avatar, ac.clone());
	}

	/**
	 * Clear all avatars from the window panel.
	 *
	 */
	void clearAvatars() {
		ac.gridy = 1;
		for(final Avatar avatar : avatars) {
			remove(avatar);
		}
		avatars.clear();
	}

	/**
	 * Remove an avatar from the window panel.
	 * 
	 * @param avatar
	 *            The avatar to remove.
	 */
	void removeAvatar(final Avatar avatar) {
		avatars.remove(avatar);
		ac.gridy--;
		remove(avatar);
	}

	/**
	 * Initialize the window components.
	 * 
	 * TODO Insert a title panel for all windows.
	 *
	 */
	private void initComponents() {}
}
