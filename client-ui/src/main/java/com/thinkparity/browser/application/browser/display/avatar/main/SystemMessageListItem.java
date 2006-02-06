/*
 * Feb 2, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.model.tmp.system.message.Message;
import com.thinkparity.browser.model.tmp.system.message.MessageId;

import com.thinkparity.codebase.ResourceUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessageListItem extends ListItem {

	/**
	 * The message menu icon.
	 * 
	 */
	private static final ImageIcon MENU_ICON;

	static {
		MENU_ICON =
			new ImageIcon(ResourceUtil.getURL("images/systemMessageIconOrange.png"));
	}

	/**
	 * Get the accept menu item.
	 * 
	 */
	private JMenuItem acceptMenuItem;

	/**
	 * The accept menu item action listener.
	 * 
	 */
	private ActionListener acceptMenuItemActionListener;

	/**
	 * Get the decline menu item.
	 * 
	 */
	private JMenuItem declineMenuItem;

	/**
	 * The decline menu item action listener.
	 * 
	 */
	private ActionListener declineMenuItemActionListener;

	/**
	 * Create a SystemMessageListItem.
	 * 
	 * @param message
	 *            The message.
	 */
	SystemMessageListItem(final Message message) {
		super("SystemMessageListItem");
		setMenuIcon(MENU_ICON);
		setName(message.getHeader());
		setMessageId(message.getId());
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.avatar.main.ListItem#fireSelection()
	 * 
	 */
	public void fireSelection() {
		// TODO Set the message id in the controller.
		getController().displaySystemMessageAvatar();
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.avatar.main.ListItem#populateMenu(javax.swing.JPopupMenu)
	 * 
	 */
	public void populateMenu(JPopupMenu jPopupMenu) {
		jPopupMenu.add(getAcceptMenuItem());
		jPopupMenu.add(getDeclineMenuItem());
	}

	/**
	 * Obtain the accept menu item.
	 * 
	 * @return The accept menu item.
	 */
	private JMenuItem getAcceptMenuItem() {
		if(null == acceptMenuItem) {
			acceptMenuItem = createJMenuItem(getString("Accept"),
					getMnemonic("AcceptMnemonic"), getAcceptMenuItemActionListener());
		}
		return acceptMenuItem;
	}

	/**
	 * Get the accept menu item action listener.
	 * 
	 * @return The accept menu item action listener.
	 */
	private ActionListener getAcceptMenuItemActionListener() {
		if(null == acceptMenuItemActionListener) {
			acceptMenuItemActionListener = new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
				}
			};
		}
		return acceptMenuItemActionListener;
	}

	/**
	 * Obtain the decline menu item.
	 * 
	 * @return The decline menu item.
	 */
	private JMenuItem getDeclineMenuItem() {
		if(null == declineMenuItem) {
			declineMenuItem = createJMenuItem(getString("Decline"),
					getMnemonic("DeclineMnemonic"), getDeclineMenuItemActionListener());
		}
		return declineMenuItem;
	}

	/**
	 * Get the decline menu item action listener.
	 * 
	 * @return The decline menu item action listener.
	 */
	private ActionListener getDeclineMenuItemActionListener() {
		if(null == declineMenuItemActionListener) {
			declineMenuItemActionListener = new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
				}
			};
		}
		return declineMenuItemActionListener;
	}

	/**
	 * Set the message id.
	 * 
	 * @param messageId
	 *            The message id.
	 */
	private void setMessageId(final MessageId messageId) {
		setProperty("messageId", messageId);
	}
}
