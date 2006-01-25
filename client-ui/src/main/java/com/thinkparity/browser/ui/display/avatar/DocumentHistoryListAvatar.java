/*
 * Jan 20, 2006
 * 
 * NOTE The history should highlight updates\new versions as well as the document
 * list.
 * 
 * NOTE Document list sorting:  New\Updates, By Name
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import javax.swing.JLabel;

import com.thinkparity.browser.Controller;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.ui.UIConstants;
import com.thinkparity.browser.ui.component.LabelFactory;
import com.thinkparity.browser.ui.display.provider.ContentProvider;
import com.thinkparity.browser.ui.display.provider.FlatContentProvider;
import com.thinkparity.browser.util.RandomData;
import com.thinkparity.browser.util.State;

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
	private class ListItem extends AbstractJPanel implements MouseListener {

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
			addMouseListener(this);
			initListItemComponents();
		}

		/**
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(final MouseEvent e) {}

		/**
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(final MouseEvent e) {}

		/**
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		public void mouseExited(final MouseEvent e) {}

		/**
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(final MouseEvent e) {}

		/**
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(final MouseEvent e) {}

		/**
		 * Initialize the list item components.
		 *
		 */
		private void initListItemComponents() {
			final GridBagConstraints c = new GridBagConstraints();

			// NOTE Random Data
			final RandomData randomData = new RandomData();
			final String mainText = new StringBuffer(randomData.getAction())
				.append(randomData.getUser())
				.append(" ")
				.append(randomData.getDate())
				.toString();
			c.anchor = GridBagConstraints.WEST;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.9;
			c.insets = new Insets(2, 8, 2, 0);
			add(LabelFactory.create(
					UIConstants.DefaultFont, mainText), c.clone());

			final String versionText = new StringBuffer(version.getVersionId())
				.toString();
			final JLabel versionJLabel = LabelFactory.create(
					UIConstants.DefaultFont, versionText);
			versionJLabel.setForeground(Color.BLUE);
			versionJLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(final MouseEvent e) {
					runOpenVersion(version.getDocumentId(), version.getVersionId());
				}
				public void mouseEntered(final MouseEvent e) {
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
				public void mouseExited(final MouseEvent e) {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			});
			c.anchor = GridBagConstraints.EAST;
			c.fill = GridBagConstraints.NONE;
			c.insets = new Insets(2, 0, 2, 8);
			c.weightx = 0.1;
			add(versionJLabel, c.clone());
		}
	}

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The main controller.
	 * 
	 */
	private final Controller controller;

	/**
	 * Helper for info avatars.
	 * 
	 */
	private final InfoAvatarHelper helper;

	/**
	 * All list items currently on the avatar.
	 * 
	 */
	private Collection<Component> listItems;

	/**
	 * Create a DocumentHistoryListAvatar.
	 * 
	 */
	DocumentHistoryListAvatar(final Controller controller) {
		super("DocumentHistoryListAvatar");
		this.controller = controller;
		this.helper = new InfoAvatarHelper(this);
		setLayout(new GridBagLayout());

		helper.addHeading(getString("History"));
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
			final GridBagConstraints c = new GridBagConstraints();
			for(final Object element : elements) {
				listItem = new ListItem((DocumentVersion) element);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridwidth = GridBagConstraints.REMAINDER;
				c.gridx = 0;
				c.weightx = 1.0;
				c.insets = new Insets(0, 0, 1, 0);

				addListItem(listItem, c.clone());
			}
			helper.addFiller();
		}
		helper.addFiller();
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

	/**
	 * Open the document version.
	 * 
	 * @param documentId
	 *            The document id.
	 * @param versionId
	 *            The version id.
	 */
	private void runOpenVersion(final UUID documentId, final String versionId) {
		controller.runOpenDocumentVersion(documentId, versionId);
	}
}
