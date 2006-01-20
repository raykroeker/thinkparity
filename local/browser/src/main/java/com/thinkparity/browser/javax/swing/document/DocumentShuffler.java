/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

import org.apache.log4j.Logger;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.log4j.LoggerFactory;
import com.thinkparity.browser.provider.ContentProvider;
import com.thinkparity.browser.provider.FlatContentProvider;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.project.Project;

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
public class DocumentShuffler extends AbstractJPanel {

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
	private Project input;

	/**
	 * Create a DocumentShuffler.
	 * 
	 */
	public DocumentShuffler() {
		super("DocumentShuffler");
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
				"The DocumentShuffler content provider must not be null.", contentProvider);
		Assert.assertOfType(
				"The DocumentShuffler content provider must be of type FlatContentProvider.",
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
		Assert.assertOfType("", Project.class, input);
		// they're the same; do nothing
		if(this.input == input || input.equals(this.input)) { return; }

		this.input = (Project) input;
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
			DocumentAvatar avatar;
			int i = 0;
			removeAll();
			for(final Object element : elements) {
				avatar = translate((Document) element);
				avatar.transferToDisplay();
				add(avatar, createAvatarConstraints(), i);
				i++;
			}
			add(createFiller(), createFillerConstraints());
		}
		invalidate();
	}

	/**
	 * Translate a parity document into a display avatar.
	 * 
	 * @param document
	 *            The parity document.
	 * @return The display avatar.
	 */
	private DocumentAvatar translate(final Document document) {
		final DocumentAvatar avatar = new DocumentAvatar();
		avatar.setInput(document);
		return avatar;
	}
}
