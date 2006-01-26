/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import javax.swing.JLabel;
import javax.swing.Timer;

import com.thinkparity.browser.Controller;
import com.thinkparity.browser.ui.display.provider.CompositeFlatContentProvider;
import com.thinkparity.browser.ui.display.provider.ContentProvider;
import com.thinkparity.browser.util.State;

import com.thinkparity.codebase.JVMUniqueId;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.Document;

/**
 * The main list avatar displays a list of crucial system messages; as well as
 * the document list.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * NOTE Updates\new documents are BOLD - Includes receive of history items as 
 * well as ownership request; send ownership.
 * 
 * NOTE  The system messages will be displayed in the main avatar.
 * 
 * NOTE Icon colours:  Orange; system message: Green; Key Holder: Blue; Active
 * Gray; Closed, Bold Update documents.
 * 
 * NOTE The RFO should display the document's history info.
 *   * The details of the System messages should be displayed in the info panel.
 */
class BrowserMainAvatar extends Avatar {
	
	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

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
	private final Map<JVMUniqueId, Component> listItemMap;

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
	BrowserMainAvatar(final Controller controller) {
		super("DocumentListAvatar", new Color(255, 255, 255, 255));
		this.listItemMap = new Hashtable<JVMUniqueId, Component>(20, 0.75F);
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
	public AvatarId getId() { return AvatarId.BROWSER_MAIN; }

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
		if(null != input && 2 == ((Object[]) input).length) {
			reloadSystemMessages();
			reloadDocuments();
		}
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(new JLabel(), c.clone());
		revalidate();
		repaint();
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setContentProvider(com.thinkparity.browser.ui.display.provider.ContentProvider)
	 * 
	 */
	public void setContentProvider(ContentProvider contentProvider) {
		Assert.assertOfType(
				"Content provider must be a composite flat content provider.",
				CompositeFlatContentProvider.class, contentProvider);
		super.setContentProvider(contentProvider);
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setInput(java.lang.Object)
	 * 
	 */
	public void setInput(Object input) {
		Assert.assertOfType("Input must be an object array.", Object[].class, input);
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
	private void add(final BrowserMainListItem listItem, final Object constraints) {
		listItemMap.put(listItem.getId(), listItem);
		super.add(listItem, constraints);
	}

	/**
	 * Reload the document list.
	 *
	 */
	private void reloadDocuments() {
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.weightx = 1.0;
		c.insets = new Insets(0, 0, 1, 0);

		final Object[] elements = ((CompositeFlatContentProvider) contentProvider).getElements(1, ((Object[]) input)[1]);
		BrowserMainListItem mainListItem;
		for(final Object element : elements) {
			mainListItem = new BrowserMainListItemDocument((Document) element);
			add(mainListItem, c.clone());
		}
	}

	/**
	 * Reload the system message list.
	 *
	 */
	private void reloadSystemMessages() {
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.weightx = 1.0;
		c.insets = new Insets(0, 0, 1, 0);

		final Object[] elements = ((CompositeFlatContentProvider) contentProvider).getElements(0, ((Object[]) input)[0]);
		BrowserMainListItem mainListItem;
		for(Object element : elements) {
			mainListItem = new BrowserMainListItemSystemMessage((String) element);
			add(mainListItem, c.clone());
		}
	}
}