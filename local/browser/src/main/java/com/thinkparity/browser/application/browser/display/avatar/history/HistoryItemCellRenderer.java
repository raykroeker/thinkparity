/*
 * Feb 23, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.history;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.ListCellRenderer;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.javax.swing.AbstractJPanel;

import com.thinkparity.model.parity.model.document.history.HistoryItem;

/**
 * An abstraction of the history item cell. Writes the date/event/version info
 * and paints a background image for the cell.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * @see ActiveCellRenderer
 * @see ClosedCellRenderer
 */
public abstract class HistoryItemCellRenderer extends AbstractJPanel implements ListCellRenderer {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The background image to paint.
	 * 
	 * @see #paintComponent(Graphics)
	 */
	protected BufferedImage background;

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
	public HistoryItemCellRenderer() {
		super("HistoryItem");
		setLayout(new GridBagLayout());
		initComponents();
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g.create();
		try { g2.drawImage(background, getInsets().left, getInsets().top, this); }
		finally { g2.dispose(); }
	}

	/**
     * Set the current background image.
     * 
     * @param background
     *            The background image.
     */
	protected void setBackground(final BufferedImage background) {
		this.background = background;
	}

	/**
     * Set the text for the history item.
     * 
     * @param historyItem
     *            The history item.
     */
	protected void setText(final HistoryItem historyItem) {
		setDateText(historyItem);
		setEventText(historyItem);
		setVersionText(historyItem);
	}

	/**
	 * Initialize the swing components.
	 *
	 */
	private void initComponents() {
		dateJLabel = LabelFactory.create(BrowserConstants.SmallFont);
		eventJLabel = LabelFactory.create(BrowserConstants.SmallFont);
		versionJLabel = LabelFactory.create(BrowserConstants.SmallFont);

		final GridBagConstraints c = new GridBagConstraints();

		c.insets.left = 7;
		c.anchor = GridBagConstraints.WEST;
		add(dateJLabel, c.clone());

		c.insets.left = 0;
		add(versionJLabel, c.clone());

		c.weightx = 1;
		add(eventJLabel, c.clone());
	}

	/**
     * Set the history item's date.
     * 
     * @param historyItem
     *            The history item.
     */
	private void setDateText(final HistoryItem historyItem) {
		dateJLabel.setText(getString("Date", new Object[] {historyItem.getDate().getTime()}));
	}

	/**
     * Set the history item event text.
     * 
     * @param historyItem
     *            The history item.
     */
	private void setEventText(final HistoryItem historyItem) {
		eventJLabel.setText(historyItem.getEvent());
	}

	/**
     * Set the history item version text.
     * 
     * @param historyItem
     *            The history item.
     */
	private void setVersionText(final HistoryItem historyItem) {
		if(historyItem.isSetVersionId()) {
			versionJLabel.setText(getString("Version", new Object[] {historyItem.getVersionId()}));
		}
		else { versionJLabel.setText(getString("Version.Empty")); }
	}
}
