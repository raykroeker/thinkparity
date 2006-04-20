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
 * An {@link AbstractJPanel} placed on the {@link Window} used to contain the
 * window's title and any {@link Avatar}. This panel is not opaque; so all
 * display properties are controlled by both the {@link WindowTitle} and the
 * {@link Avatar}.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.7
 */
class WindowPanel extends AbstractJPanel {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Grid bag constraints for the jPanels.
	 * 
	 */
	private final GridBagConstraints ac;

	/**
	 * List of all jPanels currently on the panel.
	 * 
	 */
	private final List<AbstractJPanel> jPanels;

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
        this.jPanels = new LinkedList<AbstractJPanel>();
        setLayout(new GridBagLayout());
        setOpaque(false);
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
	void addPanel(final AbstractJPanel jPanel) {
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
        // TMP.0 Remove the title bar of the dialogue windows
		//add(new WindowTitle(), c.clone());

		jPanels.add(jPanel);
        // TMP.0 Remove the title bar of the dialogue windows
		//ac.gridy++;
		add((Component) jPanel, ac.clone());
	}

	/**
	 * Clear all jPanels from the window panel.
	 *
	 */
	void clearAvatars() {
		ac.gridy = 1;
		for(final AbstractJPanel jPanel : jPanels) {
			remove(jPanel);
		}
		jPanels.clear();
	}

	/**
	 * Remove an avatar from the window panel.
	 * 
	 * @param avatar
	 *            The avatar to remove.
	 */
	void removeAvatar(final Avatar avatar) {
		jPanels.remove(avatar);
		ac.gridy--;
		remove(avatar);
	}
}
