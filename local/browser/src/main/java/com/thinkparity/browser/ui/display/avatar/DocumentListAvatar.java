/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.UUID;

import javax.swing.JLabel;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.ui.display.provider.ContentProvider;
import com.thinkparity.browser.ui.display.provider.FlatContentProvider;
import com.thinkparity.browser.util.State;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.Document;

/**
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
			super("DocumentListAvatar$ListItem", new Color(237, 241, 244, 255));
			this.document = document;
			setLayout(new GridBagLayout());

			// h:  20 px
			// x indent:  40 px
			final GridBagConstraints nameConstraints = new GridBagConstraints();
			nameConstraints.anchor = GridBagConstraints.WEST;
			nameConstraints.fill = GridBagConstraints.BOTH;
			nameConstraints.weightx = 1.0;
			nameConstraints.weighty = 1.0;
			nameConstraints.insets = new Insets(3, 40, 3, 0);
			add(new JLabel(document.getName()), nameConstraints);
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
		setLayout(new GridBagLayout());
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
			final Object[] elements =
				((FlatContentProvider) contentProvider).getElements(input);
			ListItem listItem;
			GridBagConstraints listItemConstraints;
			for(final Object element : elements) {
				listItem = new ListItem((Document) element);
				listItemConstraints = new GridBagConstraints();
				listItemConstraints.fill = GridBagConstraints.HORIZONTAL;
				listItemConstraints.gridx = 0;
				listItemConstraints.weightx = 1.0;
				listItemConstraints.insets = new Insets(0, 0, 1, 0);

				add(listItem, listItemConstraints);
			}
		}
		final GridBagConstraints fillerConstraints = new GridBagConstraints();
		fillerConstraints.fill = GridBagConstraints.BOTH;
		fillerConstraints.gridx = 0;
		fillerConstraints.weightx = 1.0;
		fillerConstraints.weighty = 1.0;
		add(new JLabel(), fillerConstraints);
		revalidate();
		repaint();
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setContentProvider(com.thinkparity.browser.ui.display.provider.ContentProvider)
	 * 
	 */
	public void setContentProvider(ContentProvider contentProvider) {
		Assert.assertOfType("Content provider must be a flat content provider.",
				FlatContentProvider.class, contentProvider);
		super.setContentProvider(contentProvider);
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setInput(java.lang.Object)
	 * 
	 */
	public void setInput(Object input) {
		Assert.assertOfType("Input must be a UUID.", UUID.class, input);
		super.setInput(input);
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setState(com.thinkparity.browser.util.State)
	 * 
	 */
	public void setState(State state) {
		// TODO Auto-generated method stub
	}
}