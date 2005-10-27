/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.io.xml.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.io.xml.XmlIO;
import com.thinkparity.model.parity.model.io.xml.IXmlIOConstants;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xstream.XStreamUtil;

/**
 * DocumentXmlIO
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class DocumentXmlIO extends XmlIO {

	/**
	 * Create a DocumentXmlIO.
	 * 
	 * @param workspace
	 *            The parity workspace to work within.
	 */
	public DocumentXmlIO(final Workspace workspace) { super(workspace); }

	/**
	 * Create the document xml.  This 
	 * @param document
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void create(final Document document) throws FileNotFoundException,
			IOException {
		logger.info("create(Document)");
		final File xmlFile = getXmlFile(document);
		logger.debug(xmlFile);
		final String xml = XStreamUtil.toXML(document);
		logger.debug(xml);
		writeXmlFile(xml, xmlFile);
	}

	/**
	 * Serialize a document version to xml.
	 * 
	 * @param version
	 *            The document version to serialize.
	 * @throws IOException
	 */
	public void create(final DocumentVersion version)
			throws FileNotFoundException, IOException {
		logger.info("create(DocumentVersion)");
		logger.debug(version);
		final File xmlFile = getXmlFile(version);
		logger.debug(xmlFile);
		final String xml = XStreamUtil.toXML(version);
		logger.debug(xml);
		writeXmlFile(xml, xmlFile);
	}

	/**
	 * Delete the document's xml.
	 * @param document The document.
	 */
	public void delete(final Document document) {
		logger.info("delete(Document)");
		logger.debug(document);
		final File xmlFile = getXmlFile(document);
		Assert.assertTrue("delete(Document)", xmlFile.delete());
	}

	/**
	 * Delete the document version's xml.
	 * 
	 * @param version
	 *            The document version.
	 */
	public void delete(final DocumentVersion version) {
		logger.info("delete(DocumentVersion)");
		logger.debug(version);
		final File xmlFile = getXmlFile(version);
		Assert.assertTrue("delete(DocumentVersion)", xmlFile.delete());
	}

	/**
	 * Obtain a document for a given name within a parent project.
	 * 
	 * @param name
	 *            The name of the document.
	 * @param parent
	 *            The parent.
	 * @return The document; or null if no document can be found.
	 */
	public Document get(final String name, final Project parent)
			throws FileNotFoundException, IOException {
		logger.info("get(String,Project)");
		logger.debug(name);
		logger.debug(parent);
		final File parentXmlFileDirectory = getXmlFileDirectory(parent);
		logger.debug(parentXmlFileDirectory);
		final File xmlFile = new File(parentXmlFileDirectory, getXmlFileName(name));
		logger.debug(xmlFile);
		if(xmlFile.exists()) {
			final String xml = readXmlFile(xmlFile);
			logger.debug(xml);
			final Document document = (Document) XStreamUtil.fromXML(xml);
			return document;
		}
		else { return null; }
	}

	/**
	 * Obtain a named document version for a document.
	 * 
	 * @param versionId
	 *            The unique version id for a document.
	 * @param document
	 *            The document.
	 * @return The document version.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public DocumentVersion getVersion(final String versionId, final Document document)
			throws FileNotFoundException, IOException {
		logger.info("getVersion(String,Document)");
		logger.debug(versionId);
		logger.debug(document);
		final File xmlFileDirectory = getXmlFileDirectory(document);
		logger.debug(xmlFileDirectory);
		final String xmlFileName = getXmlFileName(document.getName(), versionId);
		logger.debug(xmlFileName);
		final File xmlFile = new File(xmlFileDirectory, xmlFileName);
		logger.debug(xmlFile);
		if(xmlFile.exists()) {
			final String xml = readXmlFile(xmlFile);
			logger.debug(xml);
			final DocumentVersion dVersion = (DocumentVersion) XStreamUtil.fromXML(xml);
			logger.debug(versionId);
			return dVersion;
		}
		else { return null; }
	}

	/**
	 * Update a document's xml.
	 * 
	 * @param document
	 *            The document to update.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void update(final Document document) throws FileNotFoundException,
			IOException {
		logger.info("update(Document)");
		logger.debug(document);
		final File xmlFile = getXmlFile(document);
		logger.debug(xmlFile);
		Assert.assertTrue("update(document)", xmlFile.delete());
		final String xml = XStreamUtil.toXML(document);
		logger.debug(xml);
		writeXmlFile(xml, xmlFile);
	}

	/**
	 * Obtain the xml file to use with the given document.
	 * 
	 * @param document
	 *            The document for which the xml file will be used.
	 * @return The xml file.
	 * @see XmlIO#getXmlFile(ParityObject)
	 */
	protected File getXmlFile(final Document document) {
		return super.getXmlFile(document);
	}

	/**
	 * Obtain the xml file name for a given document name.
	 * 
	 * @param name
	 *            The document name.
	 * @return The xml file name.
	 */
	private String getXmlFileName(final String name) {
		return new StringBuffer(name)
			.append(IXmlIOConstants.FILE_EXTENSION_DOCUMENT)
			.toString();
	}

	private String getXmlFileName(final String name, final String versionId) {
		return new StringBuffer(name)
			.append(".")
			.append(versionId)
			.append(".")
			.append(IXmlIOConstants.FILE_EXTENSION_DOCUMENT_VERSION)
			.toString();
	}
}
