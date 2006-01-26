/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

import com.thinkparity.browser.ui.display.provider.ContentProvider;
import com.thinkparity.browser.ui.display.provider.SingleContentProvider;
import com.thinkparity.browser.util.State;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class SystemMessageAvatar extends Avatar {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Helper for info avatars.
	 * 
	 */
	private final InfoAvatarHelper helper;

	/**
	 * The system message component.
	 * 
	 */
	private Component systemMessage;

	/**
	 * Create a SystemMessageAvatar.
	 * 
	 */
	SystemMessageAvatar() {
		super("SystemMessageAvatar");
		this.helper = new InfoAvatarHelper(this);
		setLayout(new GridBagLayout());
		helper.addHeading(getString("SystemMessage"));
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getId()
	 * 
	 */
	public AvatarId getId() { return AvatarId.SYSTEM_MESSAGE; }

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#getState()
	 * 
	 */
	public State getState() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#reload()
	 * 
	 */
	public void reload() {
		helper.removeFiller();
		removeSystemMessage();

		final GridBagConstraints c = new GridBagConstraints();

		final Object element = ((SingleContentProvider) contentProvider).getElement(null);
		final JLabel systemMessage  = new JLabel((String) element);
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.insets = new Insets(2, 8, 2, 0);
		c.weightx = 1.0;
		addSystemMessage(systemMessage, c.clone());

		helper.addFiller();

		revalidate();
		repaint();
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setContentProvider(com.thinkparity.browser.ui.display.provider.ContentProvider)
	 * 
	 */
	public void setContentProvider(ContentProvider contentProvider) {
		final Class type = SingleContentProvider.class;
		Assert.assertOfType(
				"The content provider must be of type:  " + type,
				type, contentProvider);
		super.setContentProvider(contentProvider);
	}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setInput(java.lang.Object)
	 * 
	 */
	public void setInput(Object input) {}

	/**
	 * @see com.thinkparity.browser.ui.display.avatar.Avatar#setState(com.thinkparity.browser.util.State)
	 * 
	 */
	public void setState(State state) {
		// TODO Auto-generated method stub
	}

	/**
	 * Add the system message to the avatar.
	 * 
	 * @param systemMessage
	 *            The system message display component.
	 * @param constraints
	 *            The display constraints.
	 */
	private void addSystemMessage(final Component systemMessage,
			final Object constraints) {
		this.systemMessage = systemMessage;
		add(systemMessage, constraints);
	}

	/**
	 * Remove the system message from the avatar.
	 *
	 */
	private void removeSystemMessage() {
		if(null != systemMessage) { remove(systemMessage); }
	}
}
