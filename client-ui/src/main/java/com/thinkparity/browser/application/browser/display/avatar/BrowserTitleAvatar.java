/*
 * Jan 27, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import com.thinkparity.browser.application.browser.display.avatar.title.ButtonPanel;
import com.thinkparity.browser.application.browser.display.avatar.title.SearchPanel;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class BrowserTitleAvatar extends Avatar {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The mouse input adapter.  Used to drag the window by this avatar.
	 * 
	 */
	private final MouseInputAdapter mouseInputAdapter;

	/**
	 * Create a BrowserTitleAvatar.
	 * 
	 */
	BrowserTitleAvatar() {
		super("BrowserTitleAvatar");
		this.mouseInputAdapter = new MouseInputAdapter() {
			int offsetX;
			int offsetY;
			public void mouseDragged(final MouseEvent e) {
				getController().moveMainWindow(
						new Point(e.getPoint().x - offsetX,
								e.getPoint().y - offsetY));
			}
			public void mousePressed(MouseEvent e) {
				offsetX = e.getPoint().x;
				offsetY = e.getPoint().y;
			}
		};
		addMouseListener(mouseInputAdapter);
		addMouseMotionListener(mouseInputAdapter);
		setLayout(new GridBagLayout());
		setOpaque(false);
		initComponents();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.BROWSER_TITLE; }

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() { return null; }

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

	/**
	 * Initialize the browser's title components.
	 *
	 */
	private void initComponents() {
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0.5;
		add(new ButtonPanel(this, mouseInputAdapter), c.clone());

		c.gridy = 1;
		add(new SearchPanel(this, mouseInputAdapter), c.clone());
	}
}
