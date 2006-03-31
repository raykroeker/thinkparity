/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.document;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractIndexor;
import com.thinkparity.model.parity.model.Context;

/**
 * Adds an ability to index documents.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class DocumentIndexor extends AbstractIndexor {

	/**
	 * Create a DocumentIndexor.
	 * 
	 * @param context
	 *            The parity context.
	 */
	DocumentIndexor(final Context context) { super(context); }

	void create(final Long documentId, final String documentName)
			throws ParityException {
		getInternalIndexModel().createDocument(documentId, documentName);
	}

	void delete(final Long artifactId) throws ParityException {
		getInternalIndexModel().deleteArtifact(artifactId);
	}
}
