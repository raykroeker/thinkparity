/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.javax.swing.border.TopBorder;

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
	 * Border used for the cells.
	 * 
	 */
	private static final Border CELL_BORDER;

	private static final Color listItemBackground;

	private static final Color listItemBackgroundSelect;

	private static final long serialVersionUID = 1;

	static {
		// COLOR 237, 241, 244, 255
		listItemBackground = new Color(237, 241, 244, 255);
		// COLOR 215, 231, 244, 255
		listItemBackgroundSelect = new Color(215, 231, 244, 255);
		// COLOR WHITE
		CELL_BORDER = new TopBorder(Color.WHITE);
	}

	/**
	 * Displays the cell icon.
	 * 
	 */
	private JLabel cellIconJLabel;

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
		super("CellRenderer", listItemBackground);
		setLayout(new GridBagLayout());
		initBrowserMainCellComponents();
	}

	/**
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
	 *      java.lang.Object, int, boolean, boolean)
	 * 
	 */
	public Component getListCellRendererComponent(final JList list,
			final Object value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {
		if(0 == index) { setBorder(BorderFactory.createEmptyBorder()); }
		else { setBorder(CELL_BORDER); }

		if(isSelected) { setBackground(listItemBackgroundSelect); }
		else { setBackground(listItemBackground); }

		final ListItem listItem = (ListItem) value;

		cellIconJLabel.setIcon(listItem.getMenuIcon());
		cellNameJLabel.setText(listItem.getName());
		cellNameJLabel.setForeground(listItem.getNameForeground());
		cellInfoIconJLabel.setIcon(listItem.getInfoIcon());

		return this;
	}

	/**
	 * @see javax.swing.JComponent#getPreferredSize()
	 * 
	 */
	public Dimension getPreferredSize() {
		final Insets insets = getInsets();
		// DIMENSION 400x20
		final Dimension ps = super.getPreferredSize();
		ps.height = 20 + insets.top + insets.bottom;
		return ps;
	}

	/**
	 * Grab the JPopupMenu for display; and remove all menu items.
	 * 
	 * @return The JPopupMenu.
	 */
	private JPopupMenu getJPopupMenu() {
		if(null == jPopupMenu) {
			jPopupMenu = new JPopupMenu();
		}
		jPopupMenu.removeAll();
		return jPopupMenu;
	}

	/**
	 * Initialize all of the list item's swing components.
	 *
	 */
	private void initBrowserMainCellComponents() {
		final GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;

		cellIconJLabel = LabelFactory.create();
		c.insets.bottom = 1;
		c.insets.left = 13;
		add(cellIconJLabel, c.clone());

		cellNameJLabel = LabelFactory.create();
		c.gridx = 1;
		c.weightx = 1;
		add(cellNameJLabel, c.clone());

		cellInfoIconJLabel = LabelFactory.create();
		c.gridx = 2;
		c.weightx = 0;
		add(cellInfoIconJLabel, c.clone());
	}
}
