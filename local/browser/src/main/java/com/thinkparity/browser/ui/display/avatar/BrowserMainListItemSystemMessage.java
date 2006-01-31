/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

import com.thinkparity.browser.model.tmp.system.message.Message;
import com.thinkparity.browser.ui.UIConstants;

import com.thinkparity.codebase.ResourceUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserMainListItemSystemMessage extends BrowserMainListItem {

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

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

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
	public BrowserMainListItemSystemMessage(final BrowserMainAvatar list,
			final Message systemMessage) {
		super(list, ICON, systemMessage.getHeader(), FONT);
		this.selectionTimer = new Timer(500, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getController().selectSystemMessage(systemMessage.getId());
				getController().displaySystemMessageAvatar();
			}
		});
		this.selectionTimer.setRepeats(false);
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.BrowserMainListItem#createListItemJMenuItems(javax.swing.JPopupMenu)
	 * 
	 */
	public void createListItemJMenuItems(final JPopupMenu jPopupMenu) {
		final JMenuItem acceptJMenuItem =
			createListItemJMenuItem("Accept", new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
				}
			});
		jPopupMenu.add(acceptJMenuItem);
		final JMenuItem declineJMenuItem =
			createListItemJMenuItem("Decline", new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
				}
			});
		jPopupMenu.add(declineJMenuItem);
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.BrowserMainListItem#displayIconMenu(java.awt.Point)
	 * 
	 */
	public void displayIconMenu(final Point iconLocation) {}

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
