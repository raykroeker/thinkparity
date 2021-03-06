/*
 * Feb 3, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.user;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import com.thinkparity.codebase.model.user.User;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UserRenderer extends DefaultListCellRenderer {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a UserListCellRenderer.
	 * 
	 */
	public UserRenderer() { super(); }

	/**
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
	 *      java.lang.Object, int, boolean, boolean)
	 * 
	 */
	public Component getListCellRendererComponent(final JList list,
			final Object value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {
		final JLabel jLabel = (JLabel) super.getListCellRendererComponent(list, value,
				index, isSelected, cellHasFocus);
		jLabel.setText(((User) value).getName());
		return jLabel;
	}
}
