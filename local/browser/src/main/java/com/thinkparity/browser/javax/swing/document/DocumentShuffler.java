/*
 * Jan 5, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import com.thinkparity.browser.RandomData;

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
	 * The document that has "focus".
	 * 
	 */
	private DocumentAvatar currentDocument;

	/**
	 * The provider is used to retreive documents from an external model.
	 * 
	 */
	private DocumentProvider documentProvider;

	/**
	 * The list documents currently being displayed.
	 * 
	 */
	private final List<DocumentAvatar> documents;

	/**
	 * Create a DocumentShuffler.
	 * 
	 */
	public DocumentShuffler() {
		super();
		setOpaque(false);
		this.documents = new LinkedList<DocumentAvatar>();
		this.currentDocument = null;
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

	/**
	 * Refresh the document shuffler.
	 *
	 */
	private void refresh() {
		documents.clear();
		final List<Document> documents = documentProvider.getDocuments();
		int i = 0;
		for(final Document document : documents) {
			this.documents.add(translate(i++, document));
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
	private DocumentAvatar translate(final int index, final Document document) {
		final DocumentAvatar avatar = new DocumentAvatar();
		avatar.setIndex(index);
		avatar.setName(document.getName());

		// NOTE Random data.
		final RandomData randomData = new RandomData();
		avatar.setKeyHolder(randomData.getArtifactKeyHolder());
		avatar.setState(randomData.getArtifactState());

		return avatar;
	}

	/**
	 * Here we draw the document avatars. Each avatar will know how to draw
	 * itself; however the positioning is previously controlled in the shuffer.
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g.create();
		documents.iterator().next().paint(g2);
		for(DocumentAvatar da : documents) { da.paint(g2); }
		g2.dispose();
	}
}
