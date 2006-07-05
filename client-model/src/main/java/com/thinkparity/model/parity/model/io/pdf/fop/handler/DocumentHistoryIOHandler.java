/*
 * Mar 3, 2006
 */
package com.thinkparity.model.parity.model.io.pdf.fop.handler;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.FileUtil;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.DocumentIOHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentHistoryIOHandler extends AbstractIOHandler implements
		com.thinkparity.model.parity.model.io.handler.DocumentHistoryIOHandler {

	/**
	 * Document db i\o.
	 * 
	 */
	private final DocumentIOHandler documentIO;

	/**
	 * Create a DocumentHistoryIOHandler.
	 * 
	 * @param outputDirectory
	 *            The pdf output directory.
	 */
	public DocumentHistoryIOHandler(final File outputDirectory) {
		super(outputDirectory);
		this.documentIO = IOFactory.getDefault().createDocumentHandler();
	}

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.DocumentHistoryIOHandler#archive(java.util.List)
	 * 
	 */
	public File archive(final Long documentId, final List<HistoryItem> history) {
		final File tempDirectory = createTempDirectory();
		final Document document = documentIO.get(documentId);
		// Copy document
		{
			final DocumentVersionContent versionContent = documentIO.readLatestVersionContent(documentId);
			copyToTemp(tempDirectory, document.getName(), versionContent.getContent());
		}
		// Copy versions
		final List<DocumentVersion> versions = documentIO.listVersions(documentId);
		DocumentVersionContent versionContent;
		for(final DocumentVersion version : versions) {
			{
				versionContent = documentIO.readVersionContent(
						documentId, version.getVersionId());
				copyToTemp(tempDirectory, createVersionName(version),
						versionContent.getContent());
			}
		}
		// Write History
		for(final HistoryItem historyItem : history) {}
		// Zip
		final String archiveName = FileUtil.getName(document.getName());
		final Calendar archiveDateTime = history.get(0).getDate();
		final File zipFile = zip(tempDirectory, archiveName, archiveDateTime);
		deleteTempDirectory(tempDirectory);
		return zipFile;
	}
}
