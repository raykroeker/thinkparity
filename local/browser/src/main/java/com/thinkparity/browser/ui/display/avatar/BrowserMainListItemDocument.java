/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import com.thinkparity.browser.ui.UIConstants;
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

	/**
	 * The selection timer.
	 * 
	 */
	private Timer selectionTimer;

	/**
	 * Create a BrowserMainListItemDocument.
	 * 
	 */
	BrowserMainListItemDocument(final Document document) {
		super(ICON, document.getName());
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
	 * @see com.thinkparity.browser.ui.display.avatar.BrowserMainListItem#fireDoubleClick(java.awt.event.MouseEvent)
	 * 
	 */
	protected void fireDoubleClick(final MouseEvent e) { runOpenDocument(); }

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

	/**
	 * Run the open document action.
	 *
	 */
	private void runOpenDocument() {
		getController().runOpenDocument(documentId);
	}
}
