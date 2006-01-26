/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import com.thinkparity.browser.ui.UIConstants;
import com.thinkparity.browser.ui.display.DisplayId;

import com.thinkparity.codebase.ResourceUtil;

import com.thinkparity.model.parity.model.document.Document;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserMainListItemDocument extends BrowserMainListItem {

	/**
	 * List item font.
	 */
	private static final Font FONT;

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
		FONT = UIConstants.DefaultFont;
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
		super(ICON, document.getName(), FONT);
		this.documentId = document.getId();
		this.selectionTimer = new Timer(500, new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				getController().selectDocument(documentId);
				getController().displayAvatar(DisplayId.INFO, AvatarId.DOCUMENT_HISTORY_LIST);
			}
		});
		this.selectionTimer.setRepeats(false);
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
