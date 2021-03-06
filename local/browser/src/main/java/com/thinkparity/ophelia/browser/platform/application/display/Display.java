/*
 * Jan 20, 2006
 */
package com.thinkparity.ophelia.browser.platform.application.display;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.AbstractJPanel;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.DisplayId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar.ScrollPolicy;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class Display extends AbstractJPanel {

	/** The avatar currently being displayed. */
	protected Avatar avatar;

    /**
	 * Create Display.
	 * 
	 * @param l18nContext
	 *            The localization context for the display.
	 */
	protected Display(final String l18nContext, final Color background) {
		super(background);
		setLayout(new GridBagLayout());
	}

	/**
	 * Display the avatar.
	 * 
	 */
	public void displayAvatar() {
		removeAll();
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		if(avatar.getScrollPolicy() == ScrollPolicy.NONE) {
			add(avatar, c.clone());
		}
		else {
			final JScrollPane jScrollPane;
			switch(avatar.getScrollPolicy()) {
			case HORIZONTAL:
				jScrollPane = new JScrollPane(avatar,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				break;
			case VERTICAL:
				jScrollPane = new JScrollPane(avatar,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				jScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent e) {
						avatar.revalidate();
						avatar.repaint();
					}
				});
				break;
			case BOTH:
				jScrollPane = new JScrollPane(avatar,
						ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				break;
			default:
				throw Assert.createUnreachable(
						"Cannot determine scroll policy for avatar.");
			}
			jScrollPane.setBackground(avatar.getBackground());
			jScrollPane.setBorder(null);
			add(jScrollPane, c.clone());
		}
	}

	/**
	 * Obtain the avatar currently being displayed.
	 * 
	 * @return The avatar currently being displayed.
	 * 
	 * @see #setAvatar()
	 * @see #displayAvatar()
	 */
	public AbstractJPanel getAvatar() { return avatar; }

	/**
	 * Obtain the display id.
	 * 
	 * @return The display id.
	 */
	public abstract DisplayId getId();

	/**
	 * Set the avatar to display.
	 * 
	 * @param avatar
	 *            The avatar to display.
	 * @see #getAvatar()
	 * @see #displayAvatar()
	 */
	public void setAvatar(final Avatar avatar) { this.avatar = avatar; }

	/**
     * Obtain the browser application.
     * 
     * @return The browser application.
     */
    protected Browser getApplication() {
        return (Browser) new ApplicationRegistry().get(ApplicationId.BROWSER);
    }
}
