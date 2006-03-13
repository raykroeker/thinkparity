/*
 * Feb 23, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.history;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.TextFactory;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.javax.swing.border.TopBorder;

import com.thinkparity.model.parity.model.document.history.HistoryItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CellRenderer extends AbstractJPanel implements ListCellRenderer {

	/**
	 * Text foreground color.
	 * 
	 */
	private static final Color FOREGROUND;

	/**
	 * History list background color.
	 * 
	 */
	private static final Color HISTORY_LIST_BACKGROUND;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static {
		// COLOR History List Text Foreground
		FOREGROUND = Color.WHITE;
		// COLOR History List Background
		HISTORY_LIST_BACKGROUND = new Color(103, 111, 128, 255);
	}
	/**
	 * Contains the date label.
	 * 
	 */
	private JLabel dateJLabel;

	/**
	 * Contains any action info.
	 * 
	 */
	private JTextArea eventJTextArea;

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
		setBackground(HISTORY_LIST_BACKGROUND);
		setLayout(new GridBagLayout());
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
		if(0 < index) { setBorder(SEP_BORDER); }
		else { setBorder(null); }
		setText((HistoryItem) value);
		revalidate();

		return this;
	}

	private static final Border SEP_BORDER = new TopBorder(Color.WHITE);

	/**
	 * Initialize the swing components.
	 *
	 */
	private void initComponents() {
		dateJLabel = LabelFactory.create();
		dateJLabel.setFont(BrowserConstants.SmallFont);
		dateJLabel.setForeground(FOREGROUND);

		eventJTextArea = TextFactory.createArea();
		eventJTextArea.setFont(BrowserConstants.SmallFont);
		eventJTextArea.setForeground(FOREGROUND);
		eventJTextArea.setLineWrap(true);
		eventJTextArea.setWrapStyleWord(true);
		eventJTextArea.setOpaque(false);

		versionJLabel = LabelFactory.create();
		versionJLabel.setFont(BrowserConstants.SmallFont);
		versionJLabel.setForeground(FOREGROUND);

		final GridBagConstraints c = new GridBagConstraints();

		c.insets.top = 3;
		c.anchor = GridBagConstraints.WEST;
		add(dateJLabel, c.clone());

		c.weightx = 1;
		add(versionJLabel, c.clone());

		c.insets.top = 0;
		c.insets.bottom = 3;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.gridy = 1;
		add(eventJTextArea, c.clone());
	}

	private void setDateText(final HistoryItem historyItem) {
		dateJLabel.setText(getString("Date", new Object[] {historyItem.getDate().getTime()}));
	}

	private void setEventText(final HistoryItem historyItem) {
		eventJTextArea.setText(historyItem.getEvent());
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
