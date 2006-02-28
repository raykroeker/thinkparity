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

import com.thinkparity.codebase.StringUtil.Separator;

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

		jLabel.setText(getDisplayText((User) value));
		return jLabel;
	}

	private String getDisplayText(final User user) {
		if(isSetFirstAndLastName(user)) {
			return new StringBuffer(user.getLastName())
				.append(Separator.CommaSpace)
				.append(user.getFirstName())
				.toString();
		}
		else if(isSetFirstName(user)) { return user.getFirstName(); }
		else { return user.getUsername(); }
	}

	private Boolean isSetFirstAndLastName(final User user) {
		return isSetFirstName(user) && isSetLastName(user);
	}

	private Boolean isSetFirstName(final User user) {
		final String firstName = user.getFirstName();
		return null != firstName && 0 < firstName.length();
	}
	
	private Boolean isSetLastName(final User user) {
		final String lastName = user.getLastName();
		return null != lastName && 0 < lastName.length();
	}
}
