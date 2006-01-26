/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

import com.thinkparity.browser.Controller;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.model.util.ParityObjectUtil;
import com.thinkparity.browser.ui.UIConstants;
import com.thinkparity.browser.ui.component.LabelFactory;
import com.thinkparity.browser.ui.display.provider.ContentProvider;
import com.thinkparity.browser.ui.display.provider.FlatContentProvider;
import com.thinkparity.browser.util.State;

import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.ParityObjectType;
import com.thinkparity.model.parity.model.document.Document;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * NOTE Updates\new documents are BOLD - Includes receive of history items as 
 * well as ownership request; send ownership.
 * 
 * NOTE  The system messages will be displayed in the document list.
 * 
 * NOTE Icon colours:  Orange; system message: Green; Key Holder: Blue; Active
 * Gray; Closed, Bold Update documents.
 * 
 * NOTE The RFO should display the document's history info.
 *   * The details of the System messages should be displayed in the info panel.
 */
class DocumentListAvatar extends Avatar {

	/**
	 * Represents an individual list item in the document list avatar.
	 * 
	 */
	private class ListItem extends AbstractJPanel implements MouseListener {

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
			super("DocumentListAvatar$ListItem", listItemBackground);
			this.document = document;
			setLayout(new GridBagLayout());
			addMouseListener(this);

			final GridBagConstraints c = new GridBagConstraints();
			final String iconPath = isClosed() ?
					"images/documentIconGray.png" : "images/documentIconBlue.png";
			final JLabel documentIcon = LabelFactory.create();
			documentIcon.setIcon(new ImageIcon(ResourceUtil.getURL(iconPath)));
			documentIcon.addMouseListener(new MouseAdapter() {
				public void mouseEntered(final MouseEvent e) {
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
				public void mouseExited(final MouseEvent e) {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			});
			c.insets = new Insets(0, 16, 0, 0);
			add(documentIcon, c.clone());

			// h:  20 px
			// x indent:  40 px
			c.anchor = GridBagConstraints.WEST;
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.insets = new Insets(3, 16, 3, 0);
			final Font labelFont = hasBeenSeen() ?
					UIConstants.DefaultFont : UIConstants.DefaultFontBold;
			add(LabelFactory.create(labelFont, document.getName()), c.clone());
		}

		/**
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(final MouseEvent e) {
			if(2 == e.getClickCount()) { runOpenDocument(document.getId()); }
		}

		/**
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(final MouseEvent e) {
			selectDocument(document.getId());
		}
	
		/**
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		public void mouseExited(final MouseEvent e) {
			unselectDocument(document.getId());
		}
	
		/**
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(final MouseEvent e) {}

		/**
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(final MouseEvent e) {}

		/**
		 * Obtain the unique id of the document this list item represents.
		 * 
		 * @return The document unique id.
		 */
		private UUID getDocumentId() { return document.getId(); }

		/**
		 * Determine whether or not the document has been seen.
		 * 
		 * @return True if the document has been seen; false otherwise.
		 */
		private Boolean hasBeenSeen() {
			try {
				return ParityObjectUtil.hasBeenSeen(
							document.getId(), ParityObjectType.DOCUMENT);
			}
			catch(ParityException px) {
				// NOTE Error Handler Code
				return Boolean.FALSE;
			}
		}

		/**
		 * Determine whether or not the document has been closed.
		 * 
		 * @return True if the document has been closed; false otherwise.
		 */
		private Boolean isClosed() {
			try {
				return ParityObjectUtil.isClosed(
						document.getId(), ParityObjectType.DOCUMENT);
			}
			catch(ParityException px) {
				// NOTE Error Handler Code
				return Boolean.FALSE;
			}
		}

		/**
		 * Select this list item.
		 *
		 */
		private void select() {
			setBackground(listItemBackgroundSelect);
			repaint();
		}

		/**
		 * Unselect this list item.
		 *
		 */
		private void unselect() {
			setBackground(listItemBackground);
			repaint();
		}
	}

	/**
	 * Background color of the list item.
	 * 
	 */
	private static final Color listItemBackground;

	/**
	 * Background color of the selected list item.
	 * 
	 */
	private static final Color listItemBackgroundSelect;

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	static {
		listItemBackground = new Color(237, 241, 244, 255);
		listItemBackgroundSelect = new Color(215, 231, 244, 255);
	}

	/**
	 * Main controller.
	 * 
	 */
	private final Controller controller;

	/**
	 * Current document selection.
	 * 
	 * @see #selectDocument(UUID)
	 */
	private UUID currentSelection;

	/**
	 * Map of document ids to the list items.
	 * 
	 */
	private final Map<UUID, Component> documentItemMap;

	/**
	 * Timer used to control the selection of the document within the main
	 * controller.
	 * 
	 */
	private final Timer selectionTimer;

	/**
	 * Create a DocumentListAvatar.
	 * 
	 */
	DocumentListAvatar(final Controller controller) {
		super("DocumentListAvatar", new Color(255, 255, 255, 255));
		this.controller = controller;
		this.documentItemMap = new Hashtable<UUID, Component>(20, 0.75F);
		this.selectionTimer = new Timer(500, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.selectDocument(currentSelection);
			}
		});
		this.selectionTimer.setRepeats(false);
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

	/**
	 * Add a list item to the document list.
	 * 
	 * @param listItem
	 *            The list item.
	 * @param constraints
	 *            The list item constraints.
	 */
	private void add(final ListItem listItem, final Object constraints) {
		documentItemMap.put(listItem.getDocumentId(), listItem);
		super.add(listItem, constraints);
	}

	/**
	 * Run the open document action.
	 * 
	 * @param documentId
	 *            The document unique id.
	 */
	private void runOpenDocument(final UUID documentId) {
		controller.runOpenDocument(documentId);
	}

	/**
	 * Select the document in the list.
	 * 
	 * @param documentId
	 *            The document unique id.
	 */
	private void selectDocument(final UUID documentId) {
		Assert.assertNotNull("Cannot select null document.", documentId);
		// if it's the same do nothing
		if(this.currentSelection == documentId
				|| documentId.equals(currentSelection)) { return; }

		final Collection<Component> listItems = documentItemMap.values();
		for(Component c : listItems) { ((ListItem) c).unselect(); }

		final ListItem listItem = (ListItem) documentItemMap.get(documentId);
		listItem.select();

		currentSelection = documentId;

		selectionTimer.start();
	}

	private void unselectDocument(final UUID documentId) {
		Assert.assertNotNull("Cannot unselect null document.", documentId);

		selectionTimer.stop();

		currentSelection = null;

		final ListItem listItem = (ListItem) documentItemMap.get(documentId);
		listItem.unselect();
		controller.unselectDocument(documentId);
	}
}