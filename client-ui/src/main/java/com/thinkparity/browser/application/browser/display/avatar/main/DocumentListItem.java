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
import com.thinkparity.browser.platform.util.ImageIOUtil;

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
	 * Background for documents requiring immediate action.
	 * 
	 */
	private static final BufferedImage BG_URGENT;

	/**
	 * Background for selected documents requiring immediate action.
	 * 
	 */
	private static final BufferedImage BG_URGENT_SEL;

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

	/**
	 * Text color of documents requiring immediate action.
	 * 
	 */
	private static final Color NAME_FOREGROUND_URGENT;

	static {
		BG = ImageIOUtil.read("DocumentCell.png");
		BG_SEL = ImageIOUtil.read("DocumentCellSelected.png");
		BG_CLOSED = ImageIOUtil.read("DocumentCellClosed.png");
		BG_CLOSED_SEL = ImageIOUtil.read("DocumentCellClosedSelected.png");
		BG_URGENT = ImageIOUtil.read("DocumentCellUrgent.png");
		BG_URGENT_SEL = ImageIOUtil.read("DocumentCellUrgentSelected.png");
		KEY_ICON = ImageIOUtil.readIcon("Key.png");
		// COLOR DocumentListItem Text BLACK
		NAME_FOREGROUND = Color.BLACK;
		// COLOR DocumentListItem Closed Text 127,131,134,255
		NAME_FOREGROUND_CLOSED = new Color(127, 131, 134, 255);
		NAME_FOREGROUND_URGENT = NAME_FOREGROUND;
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

	private final DisplayDocument displayDocument;

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
	DocumentListItem(final DisplayDocument displayDocument) {
		super("DocumentListItem");
		this.displayDocument = displayDocument;
		setDocumentId(displayDocument.getDocumentId());

		if(displayDocument.isUrgent()) {
			setBackgroundImage(BG_URGENT);
			setBackgroundImageSelected(BG_URGENT_SEL);
			setNameForeground(NAME_FOREGROUND_URGENT);

			if(displayDocument.hasBeenSeen()) { setNameFont(BrowserConstants.DefaultFont); }
			else { setNameFont(BrowserConstants.DefaultFontBold); }
		}
		else {
			if(displayDocument.isClosed()) {
				setBackgroundImage(BG_CLOSED);
				setBackgroundImageSelected(BG_CLOSED_SEL);
				setNameForeground(NAME_FOREGROUND_CLOSED);

				if(displayDocument.hasBeenSeen()) { setNameFont(BrowserConstants.DefaultFont); }
				else { setNameFont(BrowserConstants.DefaultFontBold); }
			}
			else {
				setBackgroundImage(BG);
				setBackgroundImageSelected(BG_SEL);
				setNameForeground(NAME_FOREGROUND);
	
				if(displayDocument.hasBeenSeen()) { setNameFont(BrowserConstants.DefaultFont); }
				else { setNameFont(BrowserConstants.DefaultFontBold); }
			}
		}

		setName(displayDocument.getDisplay());
        setNameInfo(displayDocument.getDisplayToolTip());
		if(displayDocument.isKeyHolder()) { setInfoIcon(KEY_ICON); }
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(final Object obj) {
		if(null != obj && obj instanceof DocumentListItem) {
			return ((DocumentListItem) obj).getDocumentId().equals(getDocumentId());
		}
		return false;
	}

	/**
	 * @see com.thinkparity.browser.application.browser.display.avatar.main.ListItem#fireSelection()
	 * 
	 */
	public void fireSelection() {
		getController().selectDocument(getDocumentId());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	public int hashCode() { return getDocumentId().hashCode(); }

	/**
	 * @see com.thinkparity.browser.application.browser.display.avatar.main.ListItem#populateMenu(java.awt.event.MouseEvent,
	 *      javax.swing.JPopupMenu)
	 * 
	 */
	public void populateMenu(final MouseEvent e, final JPopupMenu jPopupMenu) {
		if(displayDocument.isUrgent()) {
			displayDocument.populateUrgentMenu(this,e, jPopupMenu);
			jPopupMenu.addSeparator();
		}
		jPopupMenu.add(getOpenMenuItem());
		if(displayDocument.isClosed()) {
			jPopupMenu.addSeparator();
			jPopupMenu.add(getDeleteMenuItem());
		}
		else {
			jPopupMenu.add(getSendMenuItem());

			if(displayDocument.isKeyHolder()) {
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
			final JMenuItem idJMenuItem = new JMenuItem("Id:" + displayDocument.getDocumentId());
			idJMenuItem.putClientProperty("COPY_ME", displayDocument.getDocumentId());
			idJMenuItem.addActionListener(debugActionListener);

			final JMenuItem uidJMenuItem = new JMenuItem("U Id:" + displayDocument.getDocumentUniqueId());
			uidJMenuItem.putClientProperty("COPY_ME", displayDocument.getDocumentUniqueId());
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
	public Long getDocumentId() { return (Long) getProperty("documentId"); }

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
					getController().runRequestKey(getDocumentId());
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
