/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.thinkparity.browser.Controller;
import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.ui.component.LabelFactory;
import com.thinkparity.browser.ui.component.MenuItemFactory;

import com.thinkparity.codebase.JVMUniqueId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class BrowserMainListItem extends AbstractJPanel implements MouseInputListener {

	/**
	 * Maximum character count for each list item.
	 * 
	 * @see BrowserMainListItem#getListItemText(String)
	 */
	private static final int LIST_ITEM_TEXT_MAX_LENGTH = 55;

	/**
	 * Background color of the list item.
	 * 
	 */
	private static final Color listItemBackground;

	/**
	 * Background color of the selected list item.
	 * 
	 */
	private static final Color listItemBackgroundSelect;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static {
		// COLOR 237, 241, 244, 255
		listItemBackground = new Color(237, 241, 244, 255);
		// COLOR 215, 231, 244, 255
		listItemBackgroundSelect = new Color(215, 231, 244, 255);
	}

	/**
	 * Limit the size of the list item text to a pre-determined number of
	 * characters.
	 * 
	 * @param text
	 *            The list item text.
	 * @return The truncated text (if applicable).
	 */
	private static String getListItemText(final String text) {
		if(text.length() < LIST_ITEM_TEXT_MAX_LENGTH) { return text; }
		else { return text.substring(0, LIST_ITEM_TEXT_MAX_LENGTH - 4) + "..."; }
	}

	/**
	 * The main controller.
	 * 
	 * @see #getController()
	 */
	private Controller controller;

	/**
	 * The main list.
	 * 
	 */
	private BrowserMainAvatar list;

	/**
	 * The list item id.
	 * 
	 */
	private final JVMUniqueId listItemId;

	/**
	 * The list item label.
	 * 
	 */
	private JLabel listItemJLabel;

	/**
	 * The list item menu.
	 * 
	 * @see #getListItemMenu()
	 */
	private JPopupMenu listItemMenu;

	protected BrowserMainListItem(final BrowserMainAvatar list,
			final ImageIcon listItemIcon, final String listItemText) {
		this(list, listItemIcon, listItemText, null);
	}

	/**
	 * Create a BrowserMainListItem.
	 * 
	 * @param listItemIcon
	 *            The list item icon.
	 * @param listItemText
	 *            The list item text.
	 * @param listItemFont
	 *            The list item font.
	 */
	BrowserMainListItem(final BrowserMainAvatar list,
			final ImageIcon listItemIcon, final String listItemText,
			final Font listItemFont) {
		super("BrowserMainListItem", listItemBackground);
		this.list = list;
		this.listItemId = JVMUniqueId.nextId();
		setLayout(new GridBagLayout());
		addMouseMotionListener(this);
		addMouseListener(this);

		final GridBagConstraints c = new GridBagConstraints();
		final JLabel listItemIconJLabel = LabelFactory.create();
		listItemIconJLabel.setIcon(listItemIcon);
		listItemIconJLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				pinSelection();
				final JPopupMenu jPopupMenu = getListItemMenu();
				jPopupMenu.addPopupMenuListener(new PopupMenuListener() {
					public void popupMenuCanceled(final PopupMenuEvent e) {
						unpinSelection();
					}
					public void popupMenuWillBecomeInvisible(
							final PopupMenuEvent e) {}
					public void popupMenuWillBecomeVisible(
							final PopupMenuEvent e) {}
				});
				createListItemJMenuItems(jPopupMenu);
				jPopupMenu.show(listItemIconJLabel, listItemIconJLabel.getWidth(), 0);
			}
			public void mouseEntered(final MouseEvent e) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(final MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		c.insets = new Insets(0, 16, 0, 0);
		add(listItemIconJLabel, c.clone());

		// h:  20 px
		// x indent:  40 px
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(4, 16, 4, 0);
		listItemJLabel = LabelFactory.create(
				getListItemText(listItemText), listItemFont);
		add(listItemJLabel, c.clone());
	}

	/**
	 * Build the menu for the list item.
	 * 
	 */
	public abstract void createListItemJMenuItems(final JPopupMenu jPopupMenu);

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseClicked(final MouseEvent e) {
		if(2 == e.getClickCount()) { fireDoubleClick(e); }
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseDragged(final MouseEvent e) {}

	/**
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseEntered(final MouseEvent e) {
		if(!isSelectionPinned()) { fireSelect(); }
	}

	/**
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseExited(final MouseEvent e) {
		if(!isWithinListItem(e)) {
			if(!isSelectionPinned()) { fireUnselect(); }
		}
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseMoved(final MouseEvent e) {}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 * 
	 */
	public void mousePressed(final MouseEvent e) {}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 * 
	 */
	public void mouseReleased(final MouseEvent e) {}

	/**
	 * Create a list item menu item. This will retreive the localized text; the
	 * localized mnemonic and attach the action listener.
	 * 
	 * @param localKey
	 *            The localization local key.
	 * @param actionListener
	 *            The action to perform when the menu item is clicked.
	 * @return The JMenuItem.
	 */
	protected JMenuItem createListItemJMenuItem(final String localKey,
			final ActionListener actionListener) {
		final JMenuItem jMenuItem = MenuItemFactory.create(
				getString(localKey), getJMenuItemMnemonic(localKey));
		jMenuItem.addActionListener(actionListener);
		jMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				unpinSelection();
			}
		});
		return jMenuItem;
	}

	/**
	 * The user has double clicked on the main list item.
	 * 
	 * @param e
	 *            The mouse event.
	 */
	protected abstract void fireDoubleClick(final MouseEvent e);

	/**
	 * The main list item has been selected.
	 *
	 */
	protected void fireSelect() {
		highlightListItem(Boolean.TRUE);
	}

	/**
	 * The main list item has been unselected.
	 * 
	 */
	protected void fireUnselect() {
		highlightListItem(Boolean.FALSE);
	}

	/**
	 * Obtain the main controller.
	 * 
	 * @return The main controller.
	 */
	protected Controller getController() {
		if(null == controller) { controller = Controller.getInstance(); }
		return controller;
	}

	/**
	 * Obtain a menu item mnemonic from the l18n resources. This will simply
	 * look for the item with Mnemonic tacked on the end of the key, and convert
	 * the first character in that string to an acsii integer.
	 * 
	 * @param localKey
	 *            The local key.
	 * @return The menu item mnemonic.
	 * 
	 * @see AbstractJPanel#getString(String)
	 */
	protected Integer getJMenuItemMnemonic(final String localKey) {
		final String mnemonicString = getString(localKey + "Mnemonic");
		return new Integer(mnemonicString.charAt(0));
	}

	/**
	 * Select this list item.
	 *
	 */
	protected void highlightListItem(final Boolean doHighlight) {
		if(doHighlight) { setBackground(listItemBackgroundSelect); }
		else { setBackground(listItemBackground); }
		repaint();
	}

	/**
	 * Set the font of the list item
	 * 
	 */
	protected void setListItemFont(final Font font) {
		listItemJLabel.setFont(font);
	}

	/**
	 * Obtain the list item id.
	 * 
	 * @return The list item id.
	 */
	JVMUniqueId getId() { return listItemId; }

	/**
	 * Obtain the list item menu.
	 * 
	 * @return The list item menu.
	 */
	private JPopupMenu getListItemMenu() {
		if(null == listItemMenu) {
			listItemMenu = new JPopupMenu();
			listItemMenu.setForeground(Color.RED);
		}
		listItemMenu.removeAll();
		return listItemMenu;
	}

	/**
	 * Determine whether or not a selection is currently pinned.
	 * 
	 * @return True if a selection is pinned false otherwise.
	 */
	private Boolean isSelectionPinned() { return list.isSelectionPinned(); }

	/**
	 * Determine whether the mouse event lies within the main list item.
	 * 
	 * @param e
	 *            The mouse event.
	 * @return True if the mouse event lies within the main list item; false
	 *         otherwise.
	 */
	private Boolean isWithinListItem(final MouseEvent e) {
		if(e.getPoint().x >= 0 && e.getPoint().x <= getBounds().width - 1)
			if(e.getPoint().y >= 0 && e.getPoint().y <= getBounds().height - 1)
				return Boolean.TRUE;
		return Boolean.FALSE;
	}

	/**
	 * Pin this list item's selection.
	 *
	 */
	private void pinSelection() { list.pinSelection(getId()); }

	/**
	 * Unpin any list selection.
	 *
	 */
	private void unpinSelection() { list.unpinSelection(); }
}
