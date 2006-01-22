/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.ui.display.provider.ContentProvider;
import com.thinkparity.browser.ui.display.provider.FlatContentProvider;
import com.thinkparity.browser.util.State;

import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.DocumentVersion;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class DocumentHistoryListAvatar extends Avatar {

	/**
	 * History list item that is displayed in the document history list.
	 * 
	 */
	private class ListItem extends AbstractJPanel {

		/**
		 * @see java.io.Serializable
		 * 
		 */
		private static final long serialVersionUID = 1;

		/**
		 * Document.
		 * 
		 */
		private final DocumentVersion version;

		/**
		 * Create a ListItem.
		 * 
		 */
		protected ListItem(final DocumentVersion version) {
			super("DocumentHistoryListAvatar$ListItem");
			this.version = version;
			setLayout(new GridBagLayout());
		}
	}

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * All list items currently on the avatar.
	 * 
	 */
	private Collection<Component> listItems;

	/**
	 * Create a DocumentHistoryListAvatar.
	 * 
	 */
	DocumentHistoryListAvatar() {
		super("DocumentHistoryListAvatar");
		setLayout(new GridBagLayout());

		addHeading();
	}

	/**
	 * Add the list item to the avatar.  Also keep track of it locally for
	 * easy removal.
	 * 
	 */
	public void addListItem(final ListItem listItem, Object constraints) {
		listItems.add((Component) listItem);
		super.add(listItem, constraints);
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.DOCUMENT_HISTORY_LIST; }

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
		removeAllListItems();
		if(null != input) {
			final Object[] elements =
				((FlatContentProvider) contentProvider).getElements(input);
			ListItem listItem;
			GridBagConstraints listItemConstraints;
			for(final Object element : elements) {
				listItem = new ListItem((DocumentVersion) element);
				listItemConstraints = new GridBagConstraints();
				listItemConstraints.fill = GridBagConstraints.HORIZONTAL;
				listItemConstraints.gridx = 0;
				listItemConstraints.weightx = 1.0;
				listItemConstraints.insets = new Insets(0, 0, 1, 0);

				addListItem(listItem, listItemConstraints);
			}
			final GridBagConstraints fillerConstraints = new GridBagConstraints();
			fillerConstraints.fill = GridBagConstraints.BOTH;
			fillerConstraints.gridx = 0;
			fillerConstraints.weightx = 1.0;
			fillerConstraints.weighty = 1.0;
			add(new JLabel(), fillerConstraints);
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

	/**
	 * Add the heading to the avatar.
	 *
	 */
	private void addHeading() {
		// h:  24 px
		// x indent:  29 px
		final GridBagConstraints headingConstraints = new GridBagConstraints();
		headingConstraints.gridx = 0;
		headingConstraints.anchor = GridBagConstraints.WEST;
		headingConstraints.fill = GridBagConstraints.HORIZONTAL;
		headingConstraints.weightx = 1.0;

		add(new JLabel(getHeadingIcon()), headingConstraints);
	}

	/**
	 * Obtain the history heading icon.
	 * 
	 * @return The history heading icon.
	 */
	private Icon getHeadingIcon() {
		return new ImageIcon(ResourceUtil.getURL("images/historyHeading.png"));
	}

	/**
	 * Obtain a list of all of the list items on the avatar.
	 * 
	 * @return A list of all of the list items.
	 */
	private Component[] getListItems() {
		if(null == listItems) { listItems = new Vector<Component>(7); }
		return listItems.toArray(new Component[] {});
	}

	/**
	 * Remove all of the list item components. The document history list has a
	 * set of header components; which do not need to be removed.
	 * 
	 */
	private void removeAllListItems() {
		final Component[] listItems = getListItems();
		for(Component listItem : listItems) { remove(listItem); }
	}
}
