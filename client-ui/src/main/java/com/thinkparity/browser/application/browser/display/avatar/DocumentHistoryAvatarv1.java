/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Vector;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.UIConstants;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.model.util.ModelUtil;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.parity.model.document.history.ReceiveHistoryItem;
import com.thinkparity.model.parity.model.document.history.SendHistoryItem;
import com.thinkparity.model.parity.model.document.history.SendKeyHistoryItem;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class DocumentHistoryAvatarv1 extends Avatar {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The main controller.
	 * 
	 */
	private final Browser controller;

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
	 * Create a DocumentHistoryAvatar.
	 * 
	 */
	DocumentHistoryAvatarv1(final Browser controller) {
		// COLOR 235,240,246,255
		super("DocumentHistoryAvatar", ScrollPolicy.VERTICAL, new Color(235, 240, 246, 255));
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
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.DOCUMENT_HISTORY; }

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() { return null; }

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
	 * 
	 */
	public void reload() {
		helper.removeFiller();
		removeAllListItems();

		if(null != input) {
			final Object[] elements =
				((FlatContentProvider) contentProvider).getElements(input);
			logger.info("reload():  " + elements.length);
			ListItem listItem;
			final GridBagConstraints c = new GridBagConstraints();
			for(final Object element : elements) {
				listItem = new ListItem((HistoryItem) element);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridwidth = GridBagConstraints.REMAINDER;
				c.gridx = 0;
				c.weightx = 1.0;
				c.insets = new Insets(0, 0, 1, 0);
				addListItem(listItem, c.clone());
			}
		}

		helper.addFiller();
		revalidate();
		repaint();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setContentProvider(com.thinkparity.browser.application.browser.display.provider.ContentProvider)
	 * 
	 */
	public void setContentProvider(ContentProvider contentProvider) {
		Assert.assertOfType("Content provider must be a flat content provider.",
				FlatContentProvider.class, contentProvider);
		super.setContentProvider(contentProvider);
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setInput(java.lang.Object)
	 * 
	 */
	public void setInput(Object input) {
		Assert.assertOfType("Input must be a Long.", Long.class, input);
		super.setInput(input);
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

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
	private void runOpenVersion(final Long documentId, final Long versionId) {
		controller.runOpenDocumentVersion(documentId, versionId);
	}

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
		private final HistoryItem historyItem;

		/**
		 * Create a ListItem.
		 * 
		 */
		protected ListItem(final HistoryItem historyItem) {
			super("DocumentHistoryAvatar$ListItem");
			this.historyItem = historyItem;
			setLayout(new GridBagLayout());
			addMouseListener(this);
			initListItemComponents();
		}

		/**
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(final MouseEvent e) {
			if(2 == e.getClickCount()) {
				if(historyItem.isSetVersionId())
					runOpenVersion(historyItem.getDocumentId(), historyItem.getVersionId());
			}
		}

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

			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1;
			c.insets = new Insets(2, 8, 2, 0);
			add(LabelFactory.create(getHistoryItemText(),
					UIConstants.DefaultFont), c.clone());
		}

		private String getHistoryItemText() {
			final Object[] arguments = new Object[4];
			arguments[0] = historyItem.getDate().getTime();
			arguments[1] = getHistoryItemEventText();
			arguments[2] = getHistoryItemVersionText();
			arguments[3] = getHistoryItemEventInfoText();
			return getString("HistoryItem", arguments);
		}

		private String getHistoryItemEventText() {
			return getString("HistoryItem." + historyItem.getEvent().toString());
		}

		private String getHistoryItemEventInfoText() {
			final Object[] arguments;
			switch(historyItem.getEvent()) {
			case CLOSE:
			case CREATE:
				return "";
			case RECEIVE:
			case RECEIVE_KEY:
				final ReceiveHistoryItem rhi = (ReceiveHistoryItem) historyItem;
				arguments = new Object[] {ModelUtil.getName(rhi.getReceivedFrom())};
				return getString("HistoryItem.MetaData.RECEIVED_FROM", arguments);
			case SEND:
				final SendHistoryItem shi = (SendHistoryItem) historyItem;
				String localKey;
				int i = 0;
				final StringBuffer infoText = new StringBuffer();
				for(final User sentTo : shi.getSentTo()) {
					if(0 == i++) { localKey = "HistoryItem.MetaData.SENT_TO_0"; }
					else { localKey = "HistoryItem.MetaData.SENT_TO_N"; }
					infoText.append(getString(localKey, new Object[] {ModelUtil.getName(sentTo)}));
				}
				return infoText.toString();
			case SEND_KEY:
				final SendKeyHistoryItem skhi = (SendKeyHistoryItem) historyItem;
				return getString("HistoryItem.SENT_TO_0", new Object[] {ModelUtil.getName(skhi.getSentTo())});
			default:
				throw Assert.createUnreachable("Unknown history item event:  " + historyItem.getEvent());
			}
		}

		private String getHistoryItemVersionText() {
			if(historyItem.isSetVersionId()) {
				final Object[] data = new Object[] {historyItem.getVersionId()};
				return getString("HistoryItem.Version", data);
			}
			else { return ""; }
		}
	}
}
