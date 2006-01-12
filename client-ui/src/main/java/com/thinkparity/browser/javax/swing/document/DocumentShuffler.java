/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.thinkparity.browser.RandomData;
import com.thinkparity.browser.log4j.BrowserLoggerFactory;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.Document;

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
public class DocumentShuffler extends JPanel {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());

	/**
	 * The provider is used to retreive documents from an external model.
	 * 
	 */
	private DocumentProvider documentProvider;

	/**
	 * Create a DocumentShuffler.
	 * 
	 */
	public DocumentShuffler() {
		super();
		setOpaque(false);
		setLayout(new GridBagLayout());
	}

	/**
	 * Obtain the current content provider.
	 * 
	 * @return The content provider.
	 */
	public DocumentProvider getContentProvider() { return documentProvider; }

	/**
	 * Set the current content provider.
	 * 
	 * @param documentProvider
	 *            The new content provider.
	 */
	public void setDocumentProvider(final DocumentProvider documentProvider) {
		Assert.assertNotNull("", documentProvider);
		// they're the same; do nothing
		if(this.documentProvider == documentProvider) { return; }

		this.documentProvider = documentProvider;
		refresh();
	}

	private Object createDocumentConstraints() {
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
	private void refresh() {
		final List<Document> documents = documentProvider.getDocuments();
		DocumentAvatar avatar;
		int i = 0;
		for(final Document document : documents) {
			avatar = translate(document);
			avatar.putClientProperty("addConstraints", createDocumentConstraints());
			avatar.putClientProperty("addIndex", i);
			avatar.transferToDisplay();
			add(avatar, createDocumentConstraints(), i);

			i++;
		}
		add(createFiller(), createFillerConstraints());
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
		final DocumentAvatar avatar = new DocumentAvatar(this);
		avatar.setId(document.getId());
		avatar.setName(document.getName());

		// NOTE Random data.
		final RandomData randomData = new RandomData();
		avatar.setKeyHolder(randomData.getArtifactKeyHolder());

		return avatar;
	}
}
