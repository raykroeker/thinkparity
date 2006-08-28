/*
 * Mar 3, 2006
 */
package com.thinkparity.model.parity.model.io.pdf.fop.handler;

import java.io.File;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.audit.HistoryItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentHistoryIOHandler extends AbstractIOHandler implements
		com.thinkparity.model.parity.model.io.handler.DocumentHistoryIOHandler {

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
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentHistoryIOHandler#archive(java.util.List)
	 * 
	 */
	public File archive(final Long documentId, final List<HistoryItem> history) {
	    throw Assert
                .createNotYetImplemented("DocumentHistoryIOHandler#archive");
	}
}
