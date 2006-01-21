/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.javax.swing.user;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.UUID;

import javax.swing.JLabel;

import org.apache.log4j.Logger;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.ui.display.provider.ContentProvider;
import com.thinkparity.browser.ui.display.provider.FlatContentProvider;
import com.thinkparity.browser.util.log4j.LoggerFactory;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.xmpp.user.User;

/**
 * Note that due to the custom display of the documents; we are manually
 * painting them in the overriden paintComponent api. The key to which documents
 * are displayed is the list of document avatars. Also of note is the document
 * provider (reads from the model to get a list of documents) and the current
 * document.
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UserList extends AbstractJPanel {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * The provider is used to retreive documents from an external model.
	 * 
	 */
	private FlatContentProvider contentProvider;

	/**
	 * The input for the content provider.
	 * 
	 */
	private UUID input;

	/**
	 * Create a UserList.
	 * 
	 */
	public UserList() {
		super("UserList");
		setLayout(new GridBagLayout());
	}

	/**
	 * Obtain the current content provider.
	 * 
	 * @return The content provider.
	 */
	public ContentProvider getContentProvider() { return contentProvider; }

	/**
	 * Obtain the content provider's input.
	 * 
	 * @return The input.
	 */
	public Object getInput() { return input; }

	/**
	 * Set the current content provider.
	 * 
	 * @param documentProvider
	 *            The new content provider.
	 */
	public void setContentProvider(final ContentProvider contentProvider) {
		Assert.assertNotNull(
				"The UserList content provider must not be null.", contentProvider);
		Assert.assertOfType(
				"The UserList content provider must be of type FlatContentProvider.",
				FlatContentProvider.class, contentProvider);
		// they're the same; do nothing
		if(this.contentProvider == contentProvider) { return; }

		this.contentProvider = (FlatContentProvider) contentProvider;
		refresh();
	}

	/**
	 * Set the content provider's input.
	 * 
	 * @param input
	 *            The content provider input.
	 */
	public void setInput(final Object input) {
		Assert.assertNotNull("", input);
		Assert.assertOfType("", UUID.class, input);
		// they're the same; do nothing
		if(this.input == input || input.equals(this.input)) { return; }

		this.input = (UUID) input;
		refresh();
	}

	private Object createAvatarConstraints() {
		return new GridBagConstraints(0, GridBagConstraints.RELATIVE,
				1, 1,
				1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	private Component createFiller() { return new JLabel(""); }

	private Object createFillerConstraints() {
		return new GridBagConstraints(0, GridBagConstraints.RELATIVE,
				1, 1,
				1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	/**
	 * Refresh the document shuffler.
	 *
	 */
	public void refresh() {
		if(null != input) {
			final Object[] elements = contentProvider.getElements(input);
			UserAvatar avatar;
			int i = 0;
			removeAll();
			for(final Object element : elements) {
				avatar = translate((User) element);
				avatar.transferToDisplay();
				add(avatar, createAvatarConstraints(), i);
				i++;
			}
			add(createFiller(), createFillerConstraints());
		}
		invalidate();
	}

	/**
	 * Translate a user into a display avatar.
	 * 
	 * @param user
	 *            The parity user.
	 * @return The display avatar.
	 */
	private UserAvatar translate(final User user) {
		final UserAvatar avatar = new UserAvatar();
		avatar.setInput(user);
		return avatar;
	}
}
