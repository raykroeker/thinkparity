/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

import com.thinkparity.browser.ui.UIConstants;
import com.thinkparity.browser.ui.component.LabelFactory;
import com.thinkparity.browser.util.RandomData;

import com.thinkparity.codebase.ResourceUtil;

import com.thinkparity.model.parity.model.document.Document;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserMainListItemDocument extends BrowserMainListItem {

	/**
	 * List item icon.
	 * 
	 */
	private static final ImageIcon ICON;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static {
		ICON = new ImageIcon(ResourceUtil.getURL("images/documentIconBlue.png"));
	}

	/**
	 * The document unique id.
	 * 
	 */
	private final UUID documentId;

	private List<JMenuItem> listItemMenuItems;

	/**
	 * The selection timer.
	 * 
	 */
	private Timer selectionTimer;

	/**
	 * Create a BrowserMainListItemDocument.
	 * 
	 */
	BrowserMainListItemDocument(final BrowserMainAvatar list,
			final Document document) {
		super(list, ICON, getListItemText(document));
		this.documentId = document.getId();
		this.selectionTimer = new Timer(500, new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				getController().selectDocument(documentId);
				getController().displayDocumentHistoryListAvatar();
			}
		});
		this.selectionTimer.setRepeats(false);
		if(hasBeenSeen()) { setListItemFont(UIConstants.DefaultFont); }
		else { setListItemFont(UIConstants.DefaultFontBold); }

		if(isKeyHolder()) {
			final GridBagConstraints c = new GridBagConstraints();
			final JLabel jLabel =
				LabelFactory.create(new ImageIcon(
						ResourceUtil.getURL("images/keyHolder.png")));

			c.anchor = GridBagConstraints.EAST;
			c.insets.right = 18;
			add(jLabel, c.clone());
		}
	}

	private static final int LIST_ITEM_TEXT_MAX_LENGTH = 37;

	private static String getListItemText(final Document document) {
		final String name = document.getName();
		if(name.length() < LIST_ITEM_TEXT_MAX_LENGTH) { return name; }
		else { return name.substring(0, LIST_ITEM_TEXT_MAX_LENGTH - 4) + "..."; }
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.BrowserMainListItem#createListItemJMenuItems(javax.swing.JPopupMenu)
	 * 
	 */
	public void createListItemJMenuItems(final JPopupMenu jPopupMenu) {
		final List<JMenuItem> jMenuItems = getListItemMenuItems();
		for(JMenuItem jMenuItem : jMenuItems) { jPopupMenu.add(jMenuItem); }
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.BrowserMainListItem#fireDoubleClick(java.awt.event.MouseEvent)
	 * 
	 */
	protected void fireDoubleClick(final MouseEvent e) { runOpenDocument(); }

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.BrowserMainListItem#fireSelect()
	 * 
	 */
	protected void fireSelect() {
		super.fireSelect();
		selectionTimer.start();
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.BrowserMainListItem#fireUnselect()
	 * 
	 */
	protected void fireUnselect() {
		super.fireUnselect();
		selectionTimer.stop();
	}

	/**
	 * Determine whether or not the user can close the document.
	 * 
	 * @return True if the user can close the document; false otherwise.
	 */
	private Boolean canClose() { return Boolean.TRUE; }

	/**
	 * Determine whether or not the user can send the document.
	 * 
	 * @return True if the user can send the document; false otherwise.
	 */
	private Boolean canDelete() { return Boolean.TRUE; }

	/**
	 * Obtain the list of menu items for the list item. The menu items are built
	 * upon request; and will not be updated unless the list item is rebuilt.
	 * 
	 * @return The list of menu items for this list item.
	 */
	private List<JMenuItem> getListItemMenuItems() {
		if(null == listItemMenuItems) {
			listItemMenuItems = new LinkedList<JMenuItem>();
			listItemMenuItems.add(
					createListItemJMenuItem("Send", new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
						}}));
			if(canClose()) {
				listItemMenuItems.add(
						createListItemJMenuItem("Close", new ActionListener() {
							public void actionPerformed(final ActionEvent e) {
							}}));
			}
			if(canDelete()) {
				listItemMenuItems.add(
						createListItemJMenuItem("Delete", new ActionListener() {
							public void actionPerformed(final ActionEvent e) {
							}}));
			}
			if(isKeyHolder()) {
				listItemMenuItems.add(
						createListItemJMenuItem("SendKey", new ActionListener() {
							public void actionPerformed(final ActionEvent e) {
							}}));
			}
			else {
				listItemMenuItems.add(
						createListItemJMenuItem("RequestKey", new ActionListener() {
							public void actionPerformed(final ActionEvent e) {
							}}));
			}
		}
		return listItemMenuItems;
	}

	/**
	 * Determine whether or not this document has been seen.
	 * 
	 * @return True if it has been seen; false otherwise.
	 */
	private Boolean hasBeenSeen() {
		// NOTE Random Data
		final RandomData randomData = new RandomData();
		return randomData.hasBeenSeen();
//		try {
//			return ParityObjectUtil.hasBeenSeen(
//					documentId, ParityObjectType.DOCUMENT);
//		}
//		catch(ParityException px) {
//			// NOTE Error Handler Code
//			logger.error("", px);
//			return Boolean.FALSE;
//		}
	}

	/**
	 * Determine whether or not the current user is the key holder of the
	 * document.
	 * 
	 * @return True if the user is the key holder; false otherwise.
	 */
	private Boolean isKeyHolder() {
		return Boolean.TRUE;
	}

	/**
	 * Run the open document action.
	 *
	 */
	private void runOpenDocument() {
		getController().runOpenDocument(documentId);
	}
}
