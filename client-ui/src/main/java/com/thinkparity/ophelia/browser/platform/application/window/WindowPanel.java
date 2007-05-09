/*
 * Jan 20, 2006
 */
package com.thinkparity.ophelia.browser.platform.application.window;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.window.Window;

/**
 * An {@link AbstractJPanel} placed on the {@link Window} used to contain the
 * window's title and any {@link Avatar}. This panel is not opaque; so all
 * display properties are controlled by both the {@link WindowTitle} and the
 * {@link Avatar}.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.7
 */
public class WindowPanel extends AbstractJPanel {

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
     * The window title.
     */
    private final WindowTitle windowTitle;

	/**
	 * Create a MainPanel.
	 * 
	 */
	public WindowPanel() {
	    super();
        this.ac = new GridBagConstraints();
        this.ac.fill = GridBagConstraints.BOTH;
        this.ac.weightx = 1;
        this.ac.weighty = 1;
        this.ac.gridy = 1;
        this.jPanels = new LinkedList<AbstractJPanel>();
        this.windowTitle = new WindowTitle();
        setLayout(new GridBagLayout());
        setOpaque(false);
    }

	/**
     * @see com.thinkparity.codebase.swing.AbstractJPanel#debugGeometry()
     * 
     */
	public void debug() {
        super.debug();
	}

	/**
	 * Add an avatar to the main panel.
	 * 
	 * @param avatar
	 *            The avatar to add.
     * @param titleText
     *            Whether or not to display title text.              
	 */
	public void addPanel(final Avatar avatar) {
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
        if (avatar.isAvatarTitle()) {
		    add(windowTitle, c.clone());
        }

		jPanels.add(avatar);
		ac.gridy++;
		add((Component) avatar, ac.clone());
	}

	/**
	 * Clear all jPanels from the window panel.
	 *
	 */
    public void clearAvatars() {
		ac.gridy = 1;
		for(final AbstractJPanel jPanel : jPanels) {
			remove(jPanel);
		}
		jPanels.clear();
	}

    /**
     * Get the window title.
     * 
     * @return The <code>WindowTitle</code>.
     */
    public WindowTitle getWindowTitle() {
        return windowTitle;
    }

	/**
	 * Remove an avatar from the window panel.
	 * 
	 * @param avatar
	 *            The avatar to remove.
	 */
    public void removeAvatar(final AbstractJPanel avatar) {
		jPanels.remove(avatar);
		ac.gridy--;
		remove(avatar);
	}
}
