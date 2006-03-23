/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;

import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.javax.swing.AbstractJPanel;

/**
 * The browser main cell renderer is responsible for rendering each of the list
 * items in the browser's main avatar. At this time it includes system messages
 * and documents. These items are abstracted into the ListItem bean.
 * 
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
	 * The background image of the cell to paint.
	 * 
	 */
	private BufferedImage backgroundImage;

	/**
	 * Display's additional info about the cell.
	 * 
	 */
	private JLabel cellInfoIconJLabel;

	/**
	 * Displays the cell name.
	 * 
	 */
	private JLabel cellNameJLabel;

	/**
	 * The JPopupMenu that is attached to the cellIconJLabel.
	 * 
	 * @see #getJPopupMenu()
	 */
	private JPopupMenu jPopupMenu;

	/**
	 * Create a CellRenderer.
	 * 
	 */
	public CellRenderer() {
		super("CellRenderer");
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
		final ListItem listItem = (ListItem) value;
		backgroundImage = listItem.getBackgroundImage(isSelected);

		cellNameJLabel.setText(listItem.getName());
		cellNameJLabel.setForeground(listItem.getNameForeground());
		cellNameJLabel.setFont(listItem.getNameFont());

		setToolTipText(listItem.getNameInfo());

		cellInfoIconJLabel.setIcon(listItem.getInfoIcon());

		logger.debug(listItem.getName());
		logger.debug("isSelected:  " + isSelected);
		return this;
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g.create();
		try { g2.drawImage(backgroundImage, getInsets().left, getInsets().top, this); }
		finally { g2.dispose(); }
	}

	/**
	 * Grab the JPopupMenu for display; and remove all menu items.
	 * 
	 * @return The JPopupMenu.
	 */
	private JPopupMenu getJPopupMenu() {
		if(null == jPopupMenu) { jPopupMenu = MenuFactory.createPopup(); }
		jPopupMenu.removeAll();
		return jPopupMenu;
	}

	/**
	 * Initialize all of the list item's swing components.
	 *
	 */
	private void initComponents() {
		cellNameJLabel = LabelFactory.create();
		cellInfoIconJLabel = LabelFactory.create();

		final GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.insets.left = 49;
		c.weightx = 1;
		add(cellNameJLabel, c.clone());

		c.anchor = GridBagConstraints.CENTER;
		c.insets.left = 0;
		c.insets.right = 32;
		c.weightx = 0;
		add(cellInfoIconJLabel, c.clone());
	}
}
