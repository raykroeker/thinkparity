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
import com.thinkparity.browser.application.browser.display.avatar.title.ButtonPanel2;
import com.thinkparity.browser.application.browser.display.avatar.title.SearchPanel;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.assertion.Assert;

/**
 * The title avatar contains the {@link ButtonPanel} (contacts, minimize, close)
 * and the {@link SearchPanel} for the browser.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserTitleAvatar extends BrowserAvatar {

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
   
    /** The title button panel [contacts, help, min, close]. */
    private final ButtonPanel2 buttonPanel;

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
				moveBrowserWindow(
						new Point(e.getPoint().x - offsetX,
								e.getPoint().y - offsetY));
			}
			public void mousePressed(MouseEvent e) {
				offsetX = e.getPoint().x;
				offsetY = e.getPoint().y;
			}
		};
		this.buttonPanel = new ButtonPanel2(this, mouseInputAdapter);
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
     * Reload the connection status.
     * 
     * @param connection
     *            The connection status.
     */
    public void reloadConnectionStatus(final Connection connection) {
        if(connection == Connection.OFFLINE) {
            buttonPanel.reloadConnectionStatus(connection);
        }
        else if(connection == Connection.ONLINE) {}
        else { Assert.assertUnreachable(""); }
    }

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
		add(buttonPanel, c.clone());

		c.gridy = 1;
		add(new SearchPanel(this, mouseInputAdapter), c.clone());
	}
}
