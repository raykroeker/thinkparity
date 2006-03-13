/*
 * Feb 2, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.model.util.ArtifactUtil;
import com.thinkparity.browser.platform.util.ImageIOUtil;

import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.document.Document;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentListItem extends ListItem {

	private static final BufferedImage BG;

	private static final BufferedImage BG_CLOSED;

	private static final BufferedImage BG_CLOSED_SEL;

	private static final BufferedImage BG_SEL;

	/**
	 * Info icon used when the user is the key holder.
	 * 
	 */
	private static final ImageIcon KEY_ICON;

	/**
	 * Text colour.
	 * 
	 */
	private static final Color NAME_FOREGROUND;

	/**
	 * Text colour when document is closed.
	 * 
	 */
	private static final Color NAME_FOREGROUND_CLOSED;

	static {
		BG = ImageIOUtil.read("DocumentCell.png");
		BG_SEL = ImageIOUtil.read("DocumentCellSelected.png");
		BG_CLOSED = ImageIOUtil.read("DocumentCellClosed.png");
		BG_CLOSED_SEL = ImageIOUtil.read("DocumentCellClosedSelected.png");
		KEY_ICON = ImageIOUtil.readIcon("Key.png");
		// COLOR DocumentListItem Text BLACK
		NAME_FOREGROUND = Color.BLACK;
		// COLOR DocumentListItem Closed Text 127,131,134,255
		NAME_FOREGROUND_CLOSED = new Color(127, 131, 134, 255);
	}

	private static Boolean hasBeenSeen(final Long documentId) {
		return ArtifactUtil.hasBeenSeen(documentId, ArtifactType.DOCUMENT);
	}

	private static Boolean isClosed(final Long documentId) {
		return ArtifactUtil.isClosed(documentId, ArtifactType.DOCUMENT);
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
	 * Flag indicating whether or not the user is this document list item's key
	 * holder.
	 * 
	 * @see #DocumentListItem(Document, Boolean)
	 */
	private final Boolean isKeyHolder;

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
		this.document = document;
		this.isKeyHolder = isKeyHolder;
		setDocumentId(document.getId());

		if(isClosed(document.getId())) {
			setBackgroundImage(BG_CLOSED);
			setBackgroundImageSelected(BG_CLOSED_SEL);
			setNameForeground(NAME_FOREGROUND_CLOSED);
		}
		else {
			setBackgroundImage(BG);
			setBackgroundImageSelected(BG_SEL);
			setNameForeground(NAME_FOREGROUND);

			if(hasBeenSeen(document.getId())) { setNameFont(BrowserConstants.DefaultFont); }
			else { setNameFont(BrowserConstants.DefaultFontBold); }
		}

		setName(document.getName());
		if(isKeyHolder) { setInfoIcon(KEY_ICON); }
	}

	private final Document document;

	/**
	 * @see com.thinkparity.browser.application.browser.display.avatar.main.ListItem#fireSelection()
	 * 
	 */
	public void fireSelection() {
		getController().selectDocument(getDocumentId());
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.avatar.main.ListItem#populateMenu(java.awt.event.MouseEvent,
	 *      javax.swing.JPopupMenu)
	 * 
	 */
	public void populateMenu(final MouseEvent e, final JPopupMenu jPopupMenu) {
		jPopupMenu.add(getOpenMenuItem());
		if(isClosed(getDocumentId())) {
			jPopupMenu.addSeparator();
			jPopupMenu.add(getDeleteMenuItem());
		}
		else {
			jPopupMenu.add(getSendMenuItem());

			if(isKeyHolder) {
				jPopupMenu.addSeparator();
				jPopupMenu.add(getCloseMenuItem());
			}
			else {
				jPopupMenu.add(getRequestKeyMenuItem());
				jPopupMenu.addSeparator();
				jPopupMenu.add(getDeleteMenuItem());
			}
		}
		// DEBUG Document Menu Options
		if(e.isShiftDown()) {
			final Clipboard systemClipboard =
				Toolkit.getDefaultToolkit().getSystemClipboard();
			final ActionListener debugActionListener = new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					final StringSelection stringSelection =
						new StringSelection(((JComponent) e.getSource()).getClientProperty("COPY_ME").toString());
					systemClipboard.setContents(stringSelection, null);
				}
			};
			final JMenuItem idJMenuItem = new JMenuItem("Id:" + document.getId());
			idJMenuItem.putClientProperty("COPY_ME", document.getId());
			idJMenuItem.addActionListener(debugActionListener);

			final JMenuItem uidJMenuItem = new JMenuItem("U Id:" + document.getUniqueId());
			uidJMenuItem.putClientProperty("COPY_ME", document.getUniqueId());
			uidJMenuItem.addActionListener(debugActionListener);

			jPopupMenu.addSeparator();
			jPopupMenu.add(idJMenuItem);
			jPopupMenu.add(uidJMenuItem);
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
