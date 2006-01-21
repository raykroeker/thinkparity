/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.util.UUID;

import javax.swing.JLabel;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.ui.display.provider.FlatContentProvider;
import com.thinkparity.browser.util.State;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.Document;


/**
 * Note that due to the custom display of the documents; we are manually
 * painting them in the overriden paintComponent api. The key to which documents
 * are displayed is the list of document avatars. Also of note is the document
 * provider (reads from the model to get a list of documents) and the current
 * document.
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class DocumentListAvatar extends Avatar {

	/**
	 * Represents an individual list item in the document list avatar.
	 * 
	 */
	private class ListItem extends AbstractJPanel {

		/**
		 * @see java.io.Serializable
		 */
		private static final long serialVersionUID = 1;

		/**
		 * The document.
		 * 
		 */
		private final Document document;

		/**
		 * Create a ListItem.
		 * 
		 * @param document
		 *            The document.
		 */
		private ListItem(final Document document) {
			super("DocumentListAvatar$ListItem");
			this.document = document;
			add(new JLabel(document.getName()));
		}
	}

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a DocumentListAvatar.
	 * 
	 */
	DocumentListAvatar() {
		super("DocumentListAvatar", new Color(255, 255, 255, 255));
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.DOCUMENT_LIST; }

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#reload()
	 * 
	 */
	public void reload() {
		removeAll();
		if(null != input) {
			Assert.assertOfType(
					"Input:  " + input.getClass() + " unsupported:  " + getId(),
					UUID.class, input);
			final Object[] elements =
				((FlatContentProvider) contentProvider).getElements(input);
			ListItem listItem;
			GridBagConstraints listItemConstraints;
			for(final Object element : elements) {
				listItem = new ListItem((Document) element);
				listItemConstraints = new GridBagConstraints();

				add(listItem, listItemConstraints);
			}
			final GridBagConstraints fillerConstraints = new GridBagConstraints();
			add(new JLabel(), fillerConstraints);
		}
		invalidate();
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setState(com.thinkparity.browser.util.State)
	 * 
	 */
	public void setState(State state) {
		// TODO Auto-generated method stub
	}
}