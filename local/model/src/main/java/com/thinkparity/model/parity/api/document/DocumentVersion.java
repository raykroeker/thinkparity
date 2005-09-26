/*
 * Apr 16, 2005
 */
package com.thinkparity.model.parity.api.document;

import java.io.File;

import com.thinkparity.model.parity.api.ParityObjectVersion;
import com.thinkparity.model.parity.api.document.xml.DocumentXml;

/**
 * DocumentVersion
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentVersion extends ParityObjectVersion {

	private Document document;

	/**
	 * Create a DocumentVersion
	 * 
	 * @param document
	 *            The document upon which to base the new version.
	 * @param versionId
	 *            The version id of the new version.
	 */
	public DocumentVersion(final Document document, final String versionId) {
		super(versionId);
		this.document = document;
	}

	public byte[] getContent() { return document.getContent(); }

	/**
	 * Obtain the author of the originating document.
	 * @return <code>java.lang.String</code>
	 */
	public String getCreatedBy() { return document.getCreatedBy(); }

	public String getDescription() { return document.getDescription(); }

	/**
	 * Obtain document.
	 * @return <code>Document</code>
	 */
	public Document getDocument() { return document; }

	/**
	 * @see com.thinkparity.model.parity.api.ParityXmlSerializable#getMetaDataFile(java.io.File)
	 */
	public File getMetaDataFile(File parentDirectory) {
		final String filename = new StringBuffer(document.getName())
			.append(".").append(getVersion())
			.append(".").append(getClass().getSimpleName().toLowerCase()).toString();
		return new File(parentDirectory, filename);
	}

	public String getName() { return document.getName(); }

	/**
	 * @see com.thinkparity.codebase.log4j.Loggable#logMe()
	 */
	public StringBuffer logMe() { return DocumentXml.toXml(this); }
}
