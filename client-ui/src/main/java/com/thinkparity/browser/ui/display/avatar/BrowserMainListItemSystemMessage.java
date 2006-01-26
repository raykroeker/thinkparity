/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import com.thinkparity.browser.ui.UIConstants;
import com.thinkparity.browser.ui.display.DisplayId;

import com.thinkparity.codebase.ResourceUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserMainListItemSystemMessage extends BrowserMainListItem {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The list item font.
	 * 
	 */
	private static final Font FONT;

	/**
	 * The list item icon.
	 * 
	 */
	private static final ImageIcon ICON;

	static {
		ICON = new ImageIcon(ResourceUtil.getURL("images/systemMessageIconOrange.png"));
		FONT = UIConstants.DefaultFont;
	}

	/**
	 * The selection timer.
	 * 
	 */
	private final Timer selectionTimer;

	/**
	 * Create a BrowserMainListItemSystemMessage.
	 * 
	 * @param systemMessageHeader
	 *            The system message header.
	 */
	public BrowserMainListItemSystemMessage(final String systemMessageHeader) {
		super(ICON, systemMessageHeader, FONT);
		this.selectionTimer = new Timer(500, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// NOTE Random Data
				getController().selectSystemMessage(null);
				getController().displayAvatar(DisplayId.INFO, AvatarId.SYSTEM_MESSAGE);
			}
		});
		this.selectionTimer.setRepeats(false);
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.BrowserMainListItem#fireDoubleClick(java.awt.event.MouseEvent)
	 * 
	 */
	protected void fireDoubleClick(final MouseEvent e) {}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.BrowserMainListItem#fireSelect()
	 * 
	 */
	protected void fireSelect() {
		highlightListItem(Boolean.TRUE);
		selectionTimer.start();
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.BrowserMainListItem#fireUnselect()
	 * 
	 */
	protected void fireUnselect() {
		highlightListItem(Boolean.FALSE);
		selectionTimer.stop();
	}
}
