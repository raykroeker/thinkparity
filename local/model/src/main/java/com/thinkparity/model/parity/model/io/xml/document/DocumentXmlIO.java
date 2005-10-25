/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.io.xml.document;

import java.io.File;
import java.io.IOException;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.io.xml.AbstractXmlIO;
import com.thinkparity.model.xstream.XStreamUtil;

/**
 * DocumentXmlIO
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentXmlIO extends AbstractXmlIO {

	public static void deleteXml(final Document document) {
		XStreamUtil.delete(document);
	}

	public static Document readXml(final File documentMetaDataFile)
			throws IOException {
		final Document document = (Document) XStreamUtil.read(documentMetaDataFile);
		return document;
	}

	public static void serializeXml(final DocumentVersion version)
			throws IOException {
		final File parentDirectory =
			version.getDocument().getMetaDataDirectory();
		final File documentVersionXmlFile =
			version.getMetaDataFile(parentDirectory);
		if(documentVersionXmlFile.exists())
			XStreamUtil.delete(parentDirectory, version);
		XStreamUtil.write(parentDirectory, version);
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
	 * Create a DocumentXmlIO [Singleton]
	 */
	private DocumentXmlIO() { super(); }
}
