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
	 * Contains the history item.
	 * 
	 */
	private JLabel itemJLabel;

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
	protected void setText(final DisplayHistoryItem displayHistoryItem) {
            itemJLabel.setText(displayHistoryItem.getDisplay());
	}

        /**
         * Initialize the swing components.
         *
         */
        private void initComponents() {
            itemJLabel = LabelFactory.create(BrowserConstants.SmallFont);

            final GridBagConstraints c = new GridBagConstraints();
 
            c.insets.left = 7;
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1;
            add(itemJLabel, c.clone());
        }
}
