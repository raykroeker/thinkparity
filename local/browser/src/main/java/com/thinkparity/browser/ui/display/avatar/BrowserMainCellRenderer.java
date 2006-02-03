/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.javax.swing.border.TopBorder;
import com.thinkparity.browser.ui.component.LabelFactory;

/**
 * The browser main cell renderer is responsible for rendering each of the list
 * items in the browser's main avatar. At this time it includes system messages
 * and documents. These items are abstracted into the BrowserMainListItem bean.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class BrowserMainCellRenderer extends AbstractJPanel implements
		ListCellRenderer {

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
	 * Create a BrowserMainCellRenderer.
	 * 
	 */
	BrowserMainCellRenderer() {
		super("BrowserMainCellRenderer", listItemBackground);
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

		final BrowserMainListItem listItem = (BrowserMainListItem) value;
		cellIconJLabel.setIcon(listItem.getIcon());
		cellIconJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				final JPopupMenu jPopupMenu = getJPopupMenu();
				jPopupMenu.addPopupMenuListener(new PopupMenuListener() {
					public void popupMenuCanceled(final PopupMenuEvent e) {}
					public void popupMenuWillBecomeInvisible(
							final PopupMenuEvent e) {}
					public void popupMenuWillBecomeVisible(
							final PopupMenuEvent e) {}
				});
				listItem.populateMenu(jPopupMenu);
				jPopupMenu.show(cellIconJLabel, cellIconJLabel.getWidth(), 0);
			}
		});
		cellNameJLabel.setText(listItem.getName());

		if(true == isSelected) { logger.debug(listItem.getName() + "IS SELECTED."); }
		if(true == cellHasFocus) { logger.debug(listItem.getName() + "HAS FOCUS."); }

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
	}
}
