/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.javax.swing.session;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.UUID;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.javax.swing.browser.MainJPanel;
import com.thinkparity.browser.javax.swing.user.UserList;
import com.thinkparity.browser.ui.display.provider.ProviderFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendJPanel extends AbstractJPanel {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Form input.
	 * 
	 */
	private UUID input;

	/**
	 * Main browser jPanel.
	 * 
	 */
	private final MainJPanel mainJPanel;

	private Component userList;

	/**
	 * Create a SendJPanel.
	 * 
	 */
	public SendJPanel(final MainJPanel mainJPanel) {
		super("SendJPanel");
		this.mainJPanel = mainJPanel;

		setBackground(Color.RED);
		setLayout(new GridBagLayout());
		addUserList();
	}

	public void setInput(final Object input) {
		((UserList) userList).setInput(input);
	}

	private void addUserList() {
		if(null == userList) { userList = createUserList(); }
		add(userList, createUserListConstraints());
	}

	private Component createUserList() {
		final UserList userList = new UserList();
		userList.setContentProvider(ProviderFactory.getUserProvider());
		return userList;
	}

	private Object createUserListConstraints() {
		return new GridBagConstraints(0, 0,
				1, 1,
				1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0);
	}
}
