/*
 * Feb 2, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.model.tmp.system.message.Message;

import com.thinkparity.codebase.ResourceUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class BrowserMainListItem {

	/**
	 * The message icon.
	 * 
	 */
	private static final ImageIcon MESSAGE_ICON;

	static {
		MESSAGE_ICON =
			new ImageIcon(ResourceUtil.getURL("images/systemMessageIconOrange.png"));
	}

	/**
	 * Create an instance of a main list item for a system message.
	 * 
	 * @param message
	 *            The system document.
	 * @return The main list item.
	 */
	static BrowserMainListItem create(final Message message) {
		return new BrowserMainListItem(Type.SYSTEM_MESSAGE,
				MESSAGE_ICON, message.getHeader(), message.getId());
	}

	/**
	 * The optional list item data.
	 * 
	 */
	private final Object data;

	/**
	 * The list item icon.
	 * 
	 */
	private final ImageIcon icon;

	/**
	 * The list item name.
	 * 
	 */
	private final String name;

	/**
	 * The list item type.
	 * 
	 */
	private final Type type;

	/**
	 * Create a BrowserMainListItem.
	 * 
	 * @param type
	 *            The list item type.
	 * @param icon
	 *            The list item icon.
	 * @param name
	 *            The list item name.
	 * @param data
	 *            The list item data (optional).
	 */
	protected BrowserMainListItem(final Type type,
			final ImageIcon icon, final String name, final Object data) {
		super();
		this.data = data;
		this.type = type;
		this.name = name;
		this.icon = icon;
	}

	/**
	 * Obtain the list item data.
	 * 
	 * @return The list item data.
	 */
	public Object getData() { return data; }

	/**
	 * Obtain the list item's icon.
	 * 
	 * @return The list item's icon.
	 */
	public ImageIcon getIcon() { return icon; }

	/**
	 * Obtain the list item name.
	 * 
	 * @return The list item name.
	 */
	public String getName() { return name; }

	/**
	 * Obtain the list item type.
	 * @return The list item type.
	 * 
	 * @see Type
	 */
	public Type getType() { return type; }

	/**
	 * Populate the JPopupMenu for the list item.
	 * 
	 * @param jPopupMenu
	 *            The JPopupMenu to populate.
	 */
	protected void populateMenu(final JPopupMenu jPopupMenu) {}

	/**
	 * The type of list item. [DOCUMENT,SYSTEM_MESSAGE]
	 * 
	 */
	public enum Type { DOCUMENT, SYSTEM_MESSAGE }
}
