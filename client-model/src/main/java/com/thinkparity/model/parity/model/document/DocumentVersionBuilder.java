/*
 * Jun 18, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.io.FileFilter;

import com.thinkparity.model.parity.api.document.DocumentVersion;

/**
 * DocumentVersionBuilder
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentVersionBuilder {

	/**
	 * File extension of the document version files.
	 */
	private static final String documentVersionFileExtension = new StringBuffer(
			".").append(DocumentVersion.class.getSimpleName().toLowerCase())
			.toString();

	/**
	 * Obtain the next version id in the sequence.
	 * @param document <code>Document</code>
	 * @return <code>java.lang.String</code>
	 */
	private static String getNewVersion(final Document document) {
		// use a file filter to get a list of "name.v?.documentversion" documents
		final FileFilter documentVersionFileFilter = new FileFilter() {
			public boolean accept(File pathname) {
				final String name = pathname.getName();
				if(pathname.isFile())
					if(name.startsWith(document.getName() + ".v"))
						if(name.endsWith(documentVersionFileExtension))
							return Boolean.TRUE;
				return Boolean.FALSE;
			}
		};
		final File[] versionFiles = document.getMetaDataDirectory().listFiles(
				documentVersionFileFilter);
		// use the size of the files returned to imply the next version
		Integer numberOfVersions = 0;
		if(null != versionFiles) { numberOfVersions = versionFiles.length; }
		return new StringBuffer("v").append(++numberOfVersions).toString();
	}

	static DocumentVersion newDocumentVersion(final Document document) {
		final String newVersion = getNewVersion(document);
		return new DocumentVersion(document, newVersion);
	}

	public static DocumentVersion newDocumentVersion(final Document document,
			final String version) {
		return new DocumentVersion(document, version);
	}

	/**
	 * Create a DocumentVersionBuilder [Builder, Singleton]
	 */
	private DocumentVersionBuilder() { super(); }
}
