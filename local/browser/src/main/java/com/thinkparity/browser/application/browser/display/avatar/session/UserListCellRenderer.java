/*
 * Feb 3, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.session;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.model.util.ModelUtil;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UserListCellRenderer implements ListCellRenderer {

	private static final Color listItemBackground;

	private static final Color listItemBackgroundSelect;

	static {
		// COLOR 237, 241, 244, 255
		listItemBackground = new Color(237, 241, 244, 255);
		// COLOR 215, 231, 244, 255
		listItemBackgroundSelect = new Color(215, 231, 244, 255);
	}

	/**
	 * The label.
	 * 
	 */
	private final JLabel jLabel;

	/**
	 * Create a UserListCellRenderer.
	 * 
	 */
	public UserListCellRenderer() {
		super();
		this.jLabel = LabelFactory.create();
		this.jLabel.setOpaque(true);
	}

	/**
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
	 *      java.lang.Object, int, boolean, boolean)
	 * 
	 */
	public Component getListCellRendererComponent(final JList list,
			final Object value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {
		if(isSelected) { jLabel.setBackground(listItemBackgroundSelect); }
		else { jLabel.setBackground(listItemBackground); }

		jLabel.setText(ModelUtil.getName((User) value));
		return jLabel;
	}
}
