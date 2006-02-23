/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.thinkparity.browser.application.browser.display.avatar.history.CellRenderer;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

import com.thinkparity.model.parity.model.document.history.HistoryItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class DocumentHistoryAvatar extends Avatar {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Helper class for the info display avatars.
	 * 
	 */
	private final InfoAvatarHelper helper;

	/**
	 * The history list model.
	 * 
	 */
	private DefaultListModel jListModel;

	/**
	 * Create a DocumentHistoryAvatar.
	 * 
	 */
	protected DocumentHistoryAvatar() {
		super("DocumentHistory");
		this.helper = new InfoAvatarHelper(this);
		setLayout(new GridBagLayout());
		initComponents();
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
		reloadHistoryItems();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

	/**
	 * Use the data provider to obtain a list of history items.
	 * 
	 * @return A list of history items.
	 */
	private HistoryItem[] getHistoryItems() {
		return (HistoryItem[]) ((FlatContentProvider) contentProvider).getElements(input);
	}

	private void initComponents() {
		helper.addHeading(getString("History"));
		final GridBagConstraints c = new GridBagConstraints();

		jListModel = new DefaultListModel();

		// the list that resides on the history avatar
		//	* is a single selection list
		// 	* spans the width of the entire avatar
		//	* uses a custom cell renderer
		final JList jList = new JList(jListModel);
		// COLOR 235,240,246,255
		jList.setBackground(new Color(235, 240, 246, 255));
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setCellRenderer(new CellRenderer());

		final JScrollPane jListScrollPane = new JScrollPane(jList);
		jListScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		c.fill = GridBagConstraints.BOTH;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		add(jListScrollPane, c.clone());
	}

	/**
	 * Iterate the list of history items and add them all to the list model.
	 * 
	 * @param listModel
	 *            The list model.
	 * @param historyItems
	 *            The history items.
	 */
	private void loadHistoryItems(final DefaultListModel listModel,
			final HistoryItem[] historyItems) {
		for(final HistoryItem historyItem : historyItems) {
			logger.debug("Adding history item.");
			listModel.addElement(historyItem);
		}
	}

	/**
	 * Reload the history items. Clear the model; query the data provider for
	 * the history; and load the items into the model.
	 * 
	 */
	private void reloadHistoryItems() {
		jListModel.clear();
		if(null != input)
			loadHistoryItems(jListModel, getHistoryItems());
	}
}
