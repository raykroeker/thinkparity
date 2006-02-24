/*
 * Feb 2, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.UIConstants;
import com.thinkparity.browser.model.util.ArtifactUtil;

import com.thinkparity.codebase.ResourceUtil;

import com.thinkparity.model.parity.model.artifact.ArtifactType;
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

	/**
	 * Menu icon for closed documents.
	 * 
	 */
	private static final ImageIcon MENU_ICON_CLOSED;

	private static final Color NAME_FOREGROUND;

	private static final Color NAME_FOREGROUND_CLOSED;

	static {
		MENU_ICON = new ImageIcon(ResourceUtil.getURL("images/documentIconGreen.png"));
		MENU_ICON_CLOSED = new ImageIcon(ResourceUtil.getURL("images/documentIconGray.png"));
		// COLOR BLACK
		NAME_FOREGROUND = Color.BLACK;
		// COLOR 127,131,134,255
		NAME_FOREGROUND_CLOSED = new Color(127,131,134,255);
		KEY_ICON = new ImageIcon(ResourceUtil.getURL("images/key.png"));
	}

	/**
	 * @see ArtifactUtil#canClose(Long, ArtifactType)
	 * 
	 */
	private static Boolean canClose(final Long documentId) {
		return ArtifactUtil.canClose(documentId);
	}

	/**
	 * Flag indicating whether or not the user is this document list item's key
	 * holder.
	 * 
	 * @see #DocumentListItem(Document, Boolean)
	 */
	private final Boolean isKeyHolder;

	/**
	 * Close menu item.
	 * 
	 */
	private JMenuItem closeMenuItem;

	/**
	 * Open menu item.
	 * 
	 */
	private JMenuItem openMenuItem;

	/**
	 * The action listener for the open menu item.
	 * 
	 */
	private ActionListener openMenuItemActionListener;

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
	 * @param isKeyHolder
	 *            A flag indicating whether or not the user is the document's
	 *            key holder.
	 */
	DocumentListItem(final Document document, final Boolean isKeyHolder) {
		super("DocumentListItem");
		this.isKeyHolder = isKeyHolder;
		setDocumentId(document.getId());

		if(isClosed(document.getId())) {
			setMenuIcon(MENU_ICON_CLOSED);
			setNameForeground(NAME_FOREGROUND_CLOSED);
		}
		else {
			setMenuIcon(MENU_ICON);
			setNameForeground(NAME_FOREGROUND);

			if(hasBeenSeen(document.getId())) { setNameFont(UIConstants.DefaultFont); }
			else { setNameFont(UIConstants.DefaultFontBold); }
		}

		setName(document.getName());
		if(isKeyHolder) { setInfoIcon(KEY_ICON); }
	}
	private static Boolean hasBeenSeen(final Long documentId) {
		return ArtifactUtil.hasBeenSeen(documentId, ArtifactType.DOCUMENT);
	}
	private static Boolean isClosed(final Long documentId) {
		return ArtifactUtil.isClosed(documentId, ArtifactType.DOCUMENT);
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.avatar.main.ListItem#fireSelection()
	 * 
	 */
	public void fireSelection() {
		getController().selectDocument(getDocumentId());
		getController().displayDocumentHistoryAvatar();
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.avatar.main.ListItem#populateMenu(javax.swing.JPopupMenu)
	 * 
	 */
	public void populateMenu(final JPopupMenu jPopupMenu) {
		jPopupMenu.add(getOpenMenuItem());
		if(isClosed(getDocumentId())) {
			jPopupMenu.addSeparator();
			jPopupMenu.add(getDeleteMenuItem());
		}
		else {
			jPopupMenu.add(getSendMenuItem());

			if(isKeyHolder) { jPopupMenu.add(getSendKeyMenuItem()); }
			else { jPopupMenu.add(getRequestKeyMenuItem()); }

			if(canClose(getDocumentId())) {
				jPopupMenu.addSeparator();
				jPopupMenu.add(getCloseMenuItem());
			}
			else { jPopupMenu.addSeparator(); }
			jPopupMenu.add(getDeleteMenuItem());
		}
	}

	/**
	 * Obtain the open menu item.
	 * 
	 * @return The open menu item.
	 */
	private JMenuItem getOpenMenuItem() {
		if(null == openMenuItem) {
			openMenuItem = createJMenuItem(getString("Open"),
					getMnemonic("Open"),
					getOpenMenuItemActionListener());
		}
		return openMenuItem;
	}

	/**
	 * Obtain the open menu item action listener.
	 * 
	 * @return The open menu item action listener.
	 */
	private ActionListener getOpenMenuItemActionListener() {
		if(null == openMenuItemActionListener) {
			openMenuItemActionListener = new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					getController().runOpenDocument(getDocumentId());
				}
			};
		}
		return openMenuItemActionListener;
	}

	/**
	 * Obtain the close menu item.
	 * 
	 * @return The close menu item.
	 */
	private JMenuItem getCloseMenuItem() {
		if(null == closeMenuItem) {
			closeMenuItem = createJMenuItem(getString("Close"),
					getMnemonic("Close"),
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
					getMnemonic("Delete"),
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
	private Long getDocumentId() { return (Long) getProperty("documentId"); }

	/**
	 * Obtain the request key menu item.
	 * 
	 * @return the request key menu item.
	 */
	private JMenuItem getRequestKeyMenuItem() {
		if(null == requestKeyMenuItem) {
			requestKeyMenuItem = createJMenuItem(getString("RequestKey"),
					getMnemonic("RequestKey"),
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
					getMnemonic("SendKey"),
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
					getMnemonic("Send"), getSendMenuItemActionListener());
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
	private void setDocumentId(final Long documentId) {
		setProperty("documentId", documentId);
	}
}
