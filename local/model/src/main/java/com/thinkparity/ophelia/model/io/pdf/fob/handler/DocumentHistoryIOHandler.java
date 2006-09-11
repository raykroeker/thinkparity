/*
 * Mar 3, 2006
 */
package com.thinkparity.ophelia.model.io.pdf.fob.handler;

import java.io.File;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.audit.HistoryItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentHistoryIOHandler extends AbstractIOHandler implements
		com.thinkparity.ophelia.model.io.handler.DocumentHistoryIOHandler {

	/**
	 * Create a DocumentHistoryIOHandler.
	 * 
	 * @param outputDirectory
	 *            The pdf output directory.
	 */
	public DocumentHistoryIOHandler(final File outputDirectory) {
		super(outputDirectory);
	}

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.DocumentHistoryIOHandler#archive(java.util.List)
	 * 
	 */
	public File archive(final Long documentId, final List<HistoryItem> history) {
	    throw Assert
                .createNotYetImplemented("DocumentHistoryIOHandler#archive");
	}
}
