/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.thinkparity.browser.application.browser.display.avatar.history.DefaultRenderer;
import com.thinkparity.browser.application.browser.display.avatar.history.LinkRenderer;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
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
class DocumentHistoryAvatarv3 extends Avatar {

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
	private DefaultTableModel jTableModel;

	/**
	 * Create a DocumentHistoryAvatar.
	 * 
	 */
	protected DocumentHistoryAvatarv3() {
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
		logger.debug("jTableModel.getRowCount():  " + jTableModel.getRowCount());
		reloadHistoryItems();
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
	 * 
	 */
	public void setState(final State state) {}

	/**
	 * Clear the history items.
	 * 
	 * @param tableModel
	 *            The history item model.
	 */
	private void clearHistoryItems(final DefaultTableModel tableModel) {
		for(int i = tableModel.getRowCount() - 1; i >= 0; i--) {
			logger.debug("Removing history item:  " + i);
			tableModel.removeRow(i);
		}
	}

	/**
	 * Obtain the action row data for the history item.
	 * 
	 * @param historyItem
	 *            The history item.
	 * @return The action row data.
	 */
	private String getActionRowData(final HistoryItem historyItem) {
		return getString("Action." + historyItem.getEvent().toString());
	}

	/**
	 * Obtain the date row data from the history item.
	 * 
	 * @param historyItem
	 *            The history item.
	 * @return The date row data.
	 */
	private String getDateRowData(final HistoryItem historyItem) {
		return getString("Date", new Object[] {historyItem.getDate().getTime()});
	}

	/**
	 * Use the data provider to obtain a list of history items.
	 * 
	 * @return A list of history items.
	 */
	private HistoryItem[] getHistoryItems() {
		return (HistoryItem[]) ((FlatContentProvider) contentProvider).getElements(input);
	}

	/**
	 * Obtain the info row data.
	 * 
	 * @param historyItem
	 *            The history item.
	 * @return The info row data.
	 */
	private String getInfoRowData(final HistoryItem historyItem) {
		final Object[] arguments;
		switch(historyItem.getEvent()) {
		case CLOSE:
		case CREATE:
			return getString("Info.Empty");
		case RECEIVE:
		case RECEIVE_KEY:
			final ReceiveHistoryItem rhi = (ReceiveHistoryItem) historyItem;
			arguments = new Object[] {ModelUtil.getName(rhi.getReceivedFrom())};
			return getString("Info.RECEIVED_FROM", arguments);
		case SEND:
			final SendHistoryItem shi = (SendHistoryItem) historyItem;
			String localKey;
			int i = 0;
			final StringBuffer buffer = new StringBuffer();
			for(final User sentTo : shi.getSentTo()) {
				if(0 == i++) { localKey = "Info.SENT_TO_0"; }
				else { localKey = "Info.SENT_TO_N"; }
				buffer.append(getString(localKey, new Object[] {ModelUtil.getName(sentTo)}));
			}
			return buffer.toString();
		case SEND_KEY:
			final SendKeyHistoryItem skhi = (SendKeyHistoryItem) historyItem;
			return getString("Info.SENT_TO_0", new Object[] {ModelUtil.getName(skhi.getSentTo())});
		default:
			throw Assert.createUnreachable("Unknown history item event:  " +
					historyItem.getEvent());
		}
	}

	/**
	 * Obtain the version row data.
	 * 
	 * @param historyItem
	 *            The history item.
	 * @return The version row data.
	 */
	private String getVersionRowData(final HistoryItem historyItem) {
		if(historyItem.isSetVersionId()) {
			return getString("Version", new Object[] {historyItem.getVersionId()});
		}
		else { return getString("Version.Empty"); }
	}

	private void initComponents() {
		helper.addHeading(getString("History"));
		final GridBagConstraints c = new GridBagConstraints();

		jTableModel = new DefaultTableModel();
		jTableModel.addColumn("Date");
		jTableModel.addColumn("Action");
		jTableModel.addColumn("Version");
		jTableModel.addColumn("Info");

		// the history avatar consists of a table in order to allow both 
		// scrolling 
		final JTable jTable = new JTable(jTableModel);
		// COLOR 235,240,246,255
		jTable.setBackground(new Color(235, 240, 246, 255));
		jTable.setColumnSelectionAllowed(false);
		jTable.getColumn("Version").setCellRenderer(new LinkRenderer(getController()));
		jTable.getColumn("Version").setCellEditor(null);
		jTable.setDefaultRenderer(java.lang.String.class, new DefaultRenderer());
		jTable.setGridColor(jTable.getBackground());
		jTable.setRowSelectionAllowed(false);
		jTable.setShowGrid(false);
		jTable.setTableHeader(null);

		final JScrollPane jListScrollPane = new JScrollPane(jTable);
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
	private void loadHistoryItems(final DefaultTableModel tableModel,
			final HistoryItem[] historyItems) {
		Vector<String> rowData;
		for(final HistoryItem historyItem : historyItems) {
			logger.debug("Adding history item.");
			rowData = new Vector<String>(4);
			rowData.add(0, getDateRowData(historyItem));
			rowData.add(1, getActionRowData(historyItem));
			rowData.add(2, getVersionRowData(historyItem));
			rowData.add(3, getInfoRowData(historyItem));
			tableModel.addRow(rowData);
		}
	}

	/**
	 * Reload the history items. Clear the model; query the data provider for
	 * the history; and load the items into the model.
	 * 
	 */
	private void reloadHistoryItems() {
		clearHistoryItems(jTableModel);
		if(null != input)
			loadHistoryItems(jTableModel, getHistoryItems());
	}
}
