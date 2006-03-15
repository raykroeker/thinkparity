/*
 * Feb 23, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.history;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.TextFactory;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.platform.util.ImageIOUtil;

import com.thinkparity.model.parity.model.document.history.HistoryItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CellRenderer2 extends AbstractJPanel implements ListCellRenderer {

	private static final BufferedImage BACKGROUND;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static { BACKGROUND = ImageIOUtil.read("DocumentHistoryCell.png"); }

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
	public CellRenderer2() {
		super("DocumentHistory");
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
		setText((HistoryItem) value);
		return this;
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g.create();
		try { g2.drawImage(BACKGROUND, getInsets().left, getInsets().top, this); }
		finally { g2.dispose(); }
	}

	/**
	 * Initialize the swing components.
	 *
	 */
	private void initComponents() {
		dateJLabel = LabelFactory.create();
		dateJLabel.setFont(BrowserConstants.SmallFont);

		eventJTextArea = TextFactory.createArea();
		eventJTextArea.setFont(BrowserConstants.SmallFont);
		eventJTextArea.setLineWrap(true);
		eventJTextArea.setWrapStyleWord(true);
		eventJTextArea.setOpaque(false);

		versionJLabel = LabelFactory.create();
		versionJLabel.setFont(BrowserConstants.SmallFont);

		final GridBagConstraints c = new GridBagConstraints();

		c.insets.left = 7;
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
		c.weighty = 1;
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
