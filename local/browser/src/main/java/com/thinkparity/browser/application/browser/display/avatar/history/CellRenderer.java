/*
 * Feb 23, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.history;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.thinkparity.browser.application.browser.UIConstants;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.model.util.ModelUtil;

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
public class CellRenderer extends AbstractJPanel implements ListCellRenderer {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Contains the action label.
	 * 
	 */
	private JLabel actionJLabel;

	/**
	 * Contains the date label.
	 * 
	 */
	private JLabel dateJLabel;

	/**
	 * Contains any action info.
	 * 
	 */
	private JLabel infoJLabel;

	/**
	 * Contains the version link.
	 * 
	 */
	private JLabel versionJLabel;

	/**
	 * Create a CellRenderer.
	 * 
	 */
	public CellRenderer() {
		super("DocumentHistory");
		setLayout(new GridBagLayout());
		setOpaque(false);
		initComponents();
	}

	/**
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
	 *      java.lang.Object, int, boolean, boolean)
	 * 
	 */
	public Component getListCellRendererComponent(final JList list,
			final Object value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {
		setText((HistoryItem) value);
		logger.debug("actionJLabel.getSize():" + actionJLabel.getSize());
		logger.debug("versionJLabel.getSize():" + versionJLabel.getSize());
		return this;
	}

	/**
	 * Initialize the swing components.
	 *
	 */
	private void initComponents() {
		final GridBagConstraints c = new GridBagConstraints();

		dateJLabel = LabelFactory.create();
		c.anchor = GridBagConstraints.WEST;
		c.insets.left = 3;
		add(dateJLabel, c.clone());

		actionJLabel = LabelFactory.create();
		// DIMENSION 52,15
		actionJLabel.setPreferredSize(new Dimension(52, 15));
		add(actionJLabel, c.clone());

		versionJLabel = LabelFactory.createLink("", UIConstants.DefaultFont);
		// DIMENSION 68,15
		versionJLabel.setPreferredSize(new Dimension(68,15));
		c.weightx = 0;
		add(versionJLabel, c.clone());

		infoJLabel = LabelFactory.create();
		c.weightx = 1;
		add(infoJLabel, c.clone());
	}

	private void setActionText(final HistoryItem historyItem) {
		actionJLabel.setText(getString("Action." + historyItem.getEvent().toString()));
	}

	private void setDateText(final HistoryItem historyItem) {
		dateJLabel.setText(getString("Date", new Object[] {historyItem.getDate().getTime()}));
	}

	private void setInfoText(final HistoryItem historyItem) {
		final Object[] arguments;
		final String infoText;
		switch(historyItem.getEvent()) {
		case CLOSE:
		case CREATE:
			infoText = "";
			break;
		case RECEIVE:
		case RECEIVE_KEY:
			final ReceiveHistoryItem rhi = (ReceiveHistoryItem) historyItem;
			arguments = new Object[] {ModelUtil.getName(rhi.getReceivedFrom())};
			infoText = getString("Info.RECEIVED_FROM", arguments);
			break;
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
			infoText = buffer.toString();
			break;
		case SEND_KEY:
			final SendKeyHistoryItem skhi = (SendKeyHistoryItem) historyItem;
			infoText = getString("Info.SENT_TO_0", new Object[] {ModelUtil.getName(skhi.getSentTo())});
			break;
		default:
			infoText = null;
			Assert.assertUnreachable("Unknown history item event:  " + historyItem.getEvent());
		}
		infoJLabel.setText(infoText);
	}

	private void setText(final HistoryItem historyItem) {
		setDateText(historyItem);
		setActionText(historyItem);
		setVersionText(historyItem);
		setInfoText(historyItem);
	}

	private void setVersionText(final HistoryItem historyItem) {
		if(historyItem.isSetVersionId()) {
			versionJLabel.setText(getString("Version", new Object[] {historyItem.getVersionId()}));
		}
		else { versionJLabel.setText(""); }
	}
}
