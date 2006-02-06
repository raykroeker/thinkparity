/*
 * Feb 2, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.model.util.ParityObjectUtil;

import com.thinkparity.codebase.ResourceUtil;

import com.thinkparity.model.parity.api.ParityObjectType;
import com.thinkparity.model.parity.model.document.Document;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentListItem extends ListItem {

	/**
	 * Info icon used when the user is the key holder.
	 * 
	 */
	private static final ImageIcon KEY_ICON;

	/**
	 * Menu icon for the document list item.
	 * 
	 */
	private static final ImageIcon MENU_ICON;

	static {
		MENU_ICON = new ImageIcon(ResourceUtil.getURL("images/documentIconBlue.png"));
		KEY_ICON = new ImageIcon(ResourceUtil.getURL("images/keyHolder.png"));
	}

	/**
	 * @see ParityObjectUtil#canClose(UUID, ParityObjectType)
	 * 
	 */
	private static Boolean canClose(final UUID documentId) {
		return ParityObjectUtil.canClose(
				documentId, ParityObjectType.DOCUMENT);
	}

	/**
	 * @see ParityObjectUtil#canDelete(UUID, ParityObjectType)
	 * 
	 */
	private static Boolean canDelete(final UUID documentId) {
		return ParityObjectUtil.canDelete(
				documentId, ParityObjectType.DOCUMENT);
	}

	/**
	 * @see ParityObjectUtil#isKeyHolder(UUID, ParityObjectType)
	 * 
	 */
	private static Boolean isKeyHolder(final UUID documentId) {
		return ParityObjectUtil.isKeyHolder(
				documentId, ParityObjectType.DOCUMENT);
	}

	/**
	 * Close menu item.
	 * 
	 */
	private JMenuItem closeMenuItem;

	/**
	 * The action listener for the close menu item.
	 * 
	 */
	private ActionListener closeMenuItemActionListener;

	/**
	 * Delete menu item.
	 * 
	 */
	private JMenuItem deleteMenuItem;

	/**
	 * The action listener for the delete menu item.
	 * 
	 */
	private ActionListener deleteMenuItemActionListener;

	/**
	 * Request key menu item.
	 * 
	 */
	private JMenuItem requestKeyMenuItem;

	/**
	 * The action listener for the request key menu item.
	 * 
	 */
	private ActionListener requestKeyMenuItemActionListener;
	/**
	 * Send keymenu item.
	 * 
	 */
	private JMenuItem sendKeyMenuItem;

	/**
	 * The action listener for the send menu item.
	 * 
	 */
	private ActionListener sendKeyMenuItemActionListener;

	/**
	 * Send menu item.
	 * 
	 */
	private JMenuItem sendMenuItem;

	/**
	 * The action listener for the send menu item.
	 * 
	 */
	private ActionListener sendMenuItemActionListener;

	/**
	 * Create a DocumentListItem.
	 * 
	 * @param document
	 *            The document.
	 */
	DocumentListItem(final Document document) {
		super("DocumentListItem");
		setDocumentId(document.getId());
		setMenuIcon(MENU_ICON);
		setName(document.getName());
		if(isKeyHolder(document.getId())) { setInfoIcon(KEY_ICON); }
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.avatar.main.ListItem#fireSelection()
	 * 
	 */
	public void fireSelection() {
		getController().selectDocument(getDocumentId());
		getController().displayDocumentHistoryListAvatar();
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.avatar.main.ListItem#populateMenu(javax.swing.JPopupMenu)
	 * 
	 */
	public void populateMenu(final JPopupMenu jPopupMenu) {
		jPopupMenu.add(getSendMenuItem());
		if(isKeyHolder(getDocumentId())) {
			jPopupMenu.add(getSendKeyMenuItem());
		}
		else {
			jPopupMenu.add(getRequestKeyMenuItem());
		}
		if(canClose(getDocumentId())) {
			jPopupMenu.addSeparator();
			jPopupMenu.add(getCloseMenuItem());
		}
		if(canDelete(getDocumentId())) {
			jPopupMenu.addSeparator();
			jPopupMenu.add(getDeleteMenuItem());
		}
	}

	/**
	 * Obtain the close menu item.
	 * 
	 * @return The close menu item.
	 */
	private JMenuItem getCloseMenuItem() {
		if(null == closeMenuItem) {
			closeMenuItem = createJMenuItem(getString("Close"),
					getMnemonic("CloseMnemonic"),
					getCloseMenuItemActionListener());
		}
		return closeMenuItem;
	}

	/**
	 * Obtain the action listener for the close menu item.
	 * 
	 * @return The action listener for the close menu item.
	 */
	private ActionListener getCloseMenuItemActionListener() {
		if(null == closeMenuItemActionListener) {
			closeMenuItemActionListener = new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					getController().runCloseDocument(getDocumentId());
				}
			};
		}
		return closeMenuItemActionListener;
	}

	/**
	 * Obtain the delete menu item.
	 * 
	 * @return The delete menu item.
	 */
	private JMenuItem getDeleteMenuItem() {
		if(null == deleteMenuItem) {
			deleteMenuItem = createJMenuItem(getString("Delete"),
					getMnemonic("DeleteMnemonic"),
					getDeleteMenuItemActionListener());
		}
		return deleteMenuItem;
	}

	/**
	 * Obtain the action listener for the send menu item.
	 * 
	 * @return The action listener for the send menu item.
	 */
	private ActionListener getDeleteMenuItemActionListener() {
		if(null == deleteMenuItemActionListener) {
			deleteMenuItemActionListener = new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					getController().runDeleteDocument(getDocumentId());
				}
			};
		}
		return deleteMenuItemActionListener;
	}

	/**
	 * Get the document id.
	 * 
	 * @return The document unique id.
	 */
	private UUID getDocumentId() { return (UUID) getProperty("documentId"); }

	/**
	 * Obtain the request key menu item.
	 * 
	 * @return the request key menu item.
	 */
	private JMenuItem getRequestKeyMenuItem() {
		if(null == requestKeyMenuItem) {
			requestKeyMenuItem = createJMenuItem(getString("RequestKey"),
					getMnemonic("RequestKeyMnemonic"),
					getRequestKeyMenuItemActionListener());
		}
		return requestKeyMenuItem;
	}

	/**
	 * Obtain the action listener for the request key menu item.
	 * 
	 * @return The action listener for the reqeust key menu item.
	 */
	private ActionListener getRequestKeyMenuItemActionListener() {
		if(null == requestKeyMenuItemActionListener) {
			requestKeyMenuItemActionListener = new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					getController().runRequestArtifactKey(getDocumentId());
				}
			};
		}
		return requestKeyMenuItemActionListener;
	}

	/**
	 * Get the send key menu item.
	 * 
	 * @return The send key menu item.
	 */
	private JMenuItem getSendKeyMenuItem() {
		if(null == sendKeyMenuItem) {
			sendKeyMenuItem = createJMenuItem(getString("SendKey"),
					getMnemonic("SendKeyMnemonic"),
					getSendKeyMenuItemActionListener());
		}
		return sendKeyMenuItem;
	}

	/**
	 * Obtain the action listener for the send key menu item.
	 * 
	 * @return The action listener for the send key menu item.
	 */
	private ActionListener getSendKeyMenuItemActionListener() {
		if(null == sendKeyMenuItemActionListener) {
			sendKeyMenuItemActionListener = new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					getController().displaySessionSendKeyFormAvatar();
				}
			};
		}
		return sendKeyMenuItemActionListener;
	}

	/**
	 * Get the send menu item.
	 * 
	 * @return The send menu item.
	 */
	private JMenuItem getSendMenuItem() {
		if(null == sendMenuItem) {
			sendMenuItem = createJMenuItem(getString("Send"),
					getMnemonic("SendMnemonic"), getSendMenuItemActionListener());
		}
		return sendMenuItem;
	}

	/**
	 * Obtain the action listener for the send menu item.
	 * 
	 * @return The action listener for the send menu item.
	 */
	private ActionListener getSendMenuItemActionListener() {
		if(null == sendMenuItemActionListener) {
			sendMenuItemActionListener = new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					getController().selectDocument(getDocumentId());
					getController().displaySessionSendFormAvatar();
				}
			};
		}
		return sendMenuItemActionListener;
	}

	/**
	 * Set the document id.
	 * 
	 * @param documentId
	 *            The document unique id.
	 */
	private void setDocumentId(final UUID documentId) {
		setProperty("documentId", documentId);
	}
}
