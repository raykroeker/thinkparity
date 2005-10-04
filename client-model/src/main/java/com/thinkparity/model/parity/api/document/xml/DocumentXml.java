/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.api.document.xml;

import java.io.File;
import java.io.IOException;


import com.thinkparity.model.parity.api.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.xml.XmlUtil;

/**
 * DocumentXml
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentXml {

	/**
	 * Register a document converter.  The document converter is responsible for
	 * translating a document to xml and vice-versa.
	 */
	static {
		XmlUtil.registerTranslator(new DocumentXmlTranslator());
		XmlUtil.registerTranslator(new DocumentVersionXmlTranslator());
	}

	public static void deleteXml(final Document document) {
		XmlUtil.delete(document);
	}

	public static Document readXml(final File documentMetaDataFile)
			throws IOException {
		final Document document = (Document) XmlUtil.read(documentMetaDataFile);
		return document;
	}

	public static void serializeXml(final DocumentVersion documentVersion)
			throws IOException {
		final File parentDirectory =
			documentVersion.getDocument().getMetaDataDirectory();
		final File documentVersionXmlFile =
			documentVersion.getMetaDataFile(parentDirectory);
		if(documentVersionXmlFile.exists())
			XmlUtil.delete(parentDirectory, documentVersion);
		XmlUtil.write(parentDirectory, documentVersion);
	}

	public static StringBuffer toXml(final Document document) {
		return XmlUtil.toXml(document);
	}

	public static StringBuffer toXml(final DocumentVersion documentVersion) {
		return XmlUtil.toXml(documentVersion);
	}

	public static void writeCreationXml(final Document document)
			throws IOException {
		XmlUtil.write(document);
	}

	public static void writeUpdateXml(final Document document)
			throws IOException {
		final boolean didDelete = XmlUtil.delete(document);
		if(false == didDelete)
			throw new IOException("Could not delete document xml.");
		XmlUtil.write(document);
	}

	/**
	 * Create a DocumentXml [Singleton]
	 */
	private DocumentXml() { super(); }
}
