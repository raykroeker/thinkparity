/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

import com.thinkparity.browser.model.util.ParityObjectUtil;
import com.thinkparity.browser.ui.UIConstants;
import com.thinkparity.browser.ui.component.LabelFactory;

import com.thinkparity.codebase.ResourceUtil;

import com.thinkparity.model.parity.api.ParityObjectType;
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
		super(list, ICON, document.getName());
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

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.BrowserMainListItem#createListItemJMenuItems(javax.swing.JPopupMenu)
	 * 
	 */
	public void createListItemJMenuItems(final JPopupMenu jPopupMenu) {
		final JMenuItem sendJMenuItem =
			createListItemJMenuItem("Send", new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					getController().displaySessionSendFormAvatar();
				}});
		jPopupMenu.add(sendJMenuItem);
		if(isKeyHolder()) {
			final JMenuItem sendKeyJMenuItem =
				createListItemJMenuItem("SendKey", new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						getController().displaySessionSendKeyFormAvatar();
					}});
			jPopupMenu.add(sendKeyJMenuItem);
		}
		else {
			final JMenuItem requestKeyJMenuItem =
				createListItemJMenuItem("RequestKey", new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						getController().runRequestArtifactKey(documentId);
					}});
			jPopupMenu.add(requestKeyJMenuItem);
		}
		if(canClose()) {
			final JMenuItem closeJMenuItem =
				createListItemJMenuItem("Close", new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						getController().runCloseDocument(documentId);
					}});
			jPopupMenu.addSeparator();
			jPopupMenu.add(closeJMenuItem);
		}
		if(canDelete()) {
			final JMenuItem deleteJMenuItem =
				createListItemJMenuItem("Delete", new ActionListener() {
						public void actionPerformed(final ActionEvent e) {
							getController().runDeleteDocument(documentId);
						}});
			jPopupMenu.addSeparator();
			jPopupMenu.add(deleteJMenuItem);
		}
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
	private Boolean canClose() {
		return ParityObjectUtil.canClose(documentId, ParityObjectType.DOCUMENT);
	}

	/**
	 * Determine whether or not the user can send the document.
	 * 
	 * @return True if the user can send the document; false otherwise.
	 */
	private Boolean canDelete() {
		return ParityObjectUtil.canDelete(documentId, ParityObjectType.DOCUMENT);
	}

	/**
	 * Determine whether or not this document has been seen.
	 * 
	 * @return True if it has been seen; false otherwise.
	 */
	private Boolean hasBeenSeen() {
		return ParityObjectUtil.hasBeenSeen(documentId, ParityObjectType.DOCUMENT);
	}

	/**
	 * Determine whether or not the current user is the key holder of the
	 * document.
	 * 
	 * @return True if the user is the key holder; false otherwise.
	 */
	private Boolean isKeyHolder() {
		return ParityObjectUtil.isKeyHolder(documentId, ParityObjectType.DOCUMENT);
	}

	/**
	 * Run the open document action.
	 *
	 */
	private void runOpenDocument() {
		getController().runOpenDocument(documentId);
	}
}
