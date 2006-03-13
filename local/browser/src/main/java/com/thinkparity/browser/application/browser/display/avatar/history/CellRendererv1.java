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

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.javax.swing.AbstractJPanel;

import com.thinkparity.model.parity.model.document.history.HistoryItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CellRendererv1 extends AbstractJPanel implements ListCellRenderer {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Contains the date label.
	 * 
	 */
	private JLabel dateJLabel;

	/**
	 * Contains any action info.
	 * 
	 */
	private JLabel eventJLabel;

	/**
	 * Contains the version link.
	 * 
	 */
	private JLabel versionJLabel;

	/**
	 * Create a CellRenderer.
	 * 
	 */
	public CellRendererv1() {
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
		logger.debug("versionJLabel.getSize():" + versionJLabel.getSize());
		return this;
	}

	/**
	 * Initialize the swing components.
	 *
	 */
	private void initComponents() {
		dateJLabel = LabelFactory.create();
		eventJLabel = LabelFactory.create();
		versionJLabel = LabelFactory.createLink("", BrowserConstants.DefaultFont);
		versionJLabel.setPreferredSize(new Dimension(68,15));

		final GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.insets.left = 3;
		add(dateJLabel, c.clone());

		c.weightx = 1;
		add(eventJLabel, c.clone());

		c.weightx = 0;
		add(versionJLabel, c.clone());
	}

	private void setDateText(final HistoryItem historyItem) {
		dateJLabel.setText(getString("Date", new Object[] {historyItem.getDate().getTime()}));
	}

	private void setEventText(final HistoryItem historyItem) {
		eventJLabel.setText(historyItem.getEvent());
	}

	private void setText(final HistoryItem historyItem) {
		setDateText(historyItem);
		setEventText(historyItem);
		setVersionText(historyItem);
	}

	private void setVersionText(final HistoryItem historyItem) {
		if(historyItem.isSetVersionId()) {
			versionJLabel.setText(getString("Version", new Object[] {historyItem.getVersionId()}));
		}
		else { versionJLabel.setText(""); }
	}
}
