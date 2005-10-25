/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.io.xml.document;

import java.io.File;
import java.io.IOException;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.io.xml.AbstractXmlIO;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xstream.XStreamUtil;

/**
 * DocumentXmlIO
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class DocumentXmlIO extends AbstractXmlIO {

	/**
	 * Create a DocumentXmlIO.
	 * 
	 * @param workspace
	 *            The parity workspace to work within.
	 */
	public DocumentXmlIO(final Workspace workspace) { super(workspace); }

	/**
	 * Delete the xml file for the given document.
	 * 
	 * @param document
	 *            The document to delete the xml for.
	 */
	public void deleteXml(final Document document) {
		logger.info("deleteXml(Document)");
		logger.debug(document);
		XStreamUtil.delete(document);
	}

	/**
	 * Obtain the xml file to use with the given document.
	 * 
	 * @param document
	 *            The document for which the xml file will be used.
	 * @return The xml file.
	 * @see AbstractXmlIO#getXmlFile(ParityObject)
	 */
	public File getXmlFile(final Document document) {
		return super.getXmlFile(document);
	}

	/**
	 * Read a document from an xml file.
	 * 
	 * @param documentMetaDataFile
	 *            The document xml file.
	 * @return A document.
	 * @throws IOException
	 */
	public Document readXml(final File documentMetaDataFile)
			throws IOException {
		logger.info("readXml(File)");
		logger.debug(documentMetaDataFile);
		final Document document = (Document) XStreamUtil.read(documentMetaDataFile);
		return document;
	}

	/**
	 * Serialize a document version to xml.
	 * 
	 * @param version
	 *            The document version to serialize.
	 * @throws IOException
	 */
	public void serializeXml(final DocumentVersion version)
			throws IOException {
		logger.info("serializeXml(DocumentVersion)");
		logger.debug(version);
		final File parentDirectory =
			version.getDocument().getMetaDataDirectory();
		final File documentVersionXmlFile =
			version.getMetaDataFile(parentDirectory);
		if(documentVersionXmlFile.exists())
			XStreamUtil.delete(parentDirectory, version);
		XStreamUtil.write(parentDirectory, version);
	}

	/**
	 * Write creation xml for the given document.
	 * 
	 * @param document
	 *            The document to write the xml for.
	 * @throws IOException
	 */
	public void writeCreationXml(final Document document)
			throws IOException {
		logger.info("writeCreationXml(Document)");
		logger.debug(document);
		XStreamUtil.write(document);
	}

	/**
	 * Write update xml for the given document.
	 * 
	 * @param document
	 *            The document to write the xml for.
	 * @throws IOException
	 */
	public void writeUpdateXml(final Document document)
			throws IOException {
		logger.info("writeUpdateXml(Document)");
		logger.debug(document);
		final boolean didDelete = XStreamUtil.delete(document);
		if(false == didDelete)
			throw new IOException("Could not delete document xml.");
		XStreamUtil.write(document);
	}
}
