/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.api.document.xml;

import java.io.File;
import java.io.IOException;

import com.thinkparity.model.parity.api.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.xstream.XStreamUtil;

/**
 * DocumentXml
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentXml {

	public static void deleteXml(final Document document) {
		XStreamUtil.delete(document);
	}

	public static Document readXml(final File documentMetaDataFile)
			throws IOException {
		final Document document = (Document) XStreamUtil.read(documentMetaDataFile);
		return document;
	}

	public static void serializeXml(final DocumentVersion documentVersion)
			throws IOException {
		final File parentDirectory =
			documentVersion.getDocument().getMetaDataDirectory();
		final File documentVersionXmlFile =
			documentVersion.getMetaDataFile(parentDirectory);
		if(documentVersionXmlFile.exists())
			XStreamUtil.delete(parentDirectory, documentVersion);
		XStreamUtil.write(parentDirectory, documentVersion);
	}

	public static void writeCreationXml(final Document document)
			throws IOException {
		XStreamUtil.write(document);
	}

	public static void writeUpdateXml(final Document document)
			throws IOException {
		final boolean didDelete = XStreamUtil.delete(document);
		if(false == didDelete)
			throw new IOException("Could not delete document xml.");
		XStreamUtil.write(document);
	}

	/**
	 * Create a DocumentXml [Singleton]
	 */
	private DocumentXml() { super(); }
}
