/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.io.xml.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.io.xml.IXmlIOConstants;
import com.thinkparity.model.parity.model.io.xml.XmlIO;
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
	 * Obtain a list of documents for a project.
	 * 
	 * @param project
	 *            The project to get the documents for.
	 * @return A list of documents for a project.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Collection<Document> list(final Project project)
			throws FileNotFoundException, IOException {
		logger.info("get(Project)");
		logger.debug(project);
		final File xmlFileDirectory = getXmlFileDirectory(project);
		final File[] xmlFiles = getXmlFiles(xmlFileDirectory);
		final Collection<Document> documents = new Vector<Document>(xmlFiles.length);
		for(File xmlFile : xmlFiles) {
			documents.add(fromXml(xmlFile));
		}
		return documents;
	}

	/**
	 * Obtain a list of document versions for a document.
	 * 
	 * @param document
	 *            The document which contains the versions.
	 * @return The list of document versions.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Collection<DocumentVersion> listVersions(final Document document)
			throws FileNotFoundException, IOException {
		logger.info("listVersions(Document)");
		logger.debug(document);
		final File xmlFileDirectory = getXmlFileDirectory(document);
		final File[] xmlFiles = getVersionXmlFiles(document.getName(), xmlFileDirectory);
		final Collection<DocumentVersion> versions = new Vector<DocumentVersion>(xmlFiles.length);
		for(File xmlFile : xmlFiles) {
			versions.add(fromVersionXml(xmlFile));
		}
		return versions;
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

	private DocumentVersion fromVersionXml(final File xmlFile)
			throws FileNotFoundException, IOException {
		final String xml = readXmlFile(xmlFile);
		return (DocumentVersion) XStreamUtil.fromXML(xml);
	}

	private Document fromXml(final File xmlFile) throws FileNotFoundException,
			IOException {
		final String xml = readXmlFile(xmlFile);
		return (Document) XStreamUtil.fromXML(xml);
	}

	/**
	 * Obtain a list of document xml files for a given xml file directory.
	 * 
	 * @param xmlFileDirectory
	 *            The xml file directory.
	 * @return A list of files representing document xml files.
	 */
	private File[] getXmlFiles(final File xmlFileDirectory) {
		return xmlFileDirectory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if(name.endsWith(IXmlIOConstants.FILE_EXTENSION_DOCUMENT)) {
					return true;
				}
				else { return false; }
			}
		});
	}

	/**
	 * Obtain a list of document version xml files for a given xml file
	 * directory.
	 * 
	 * @param documentName
	 *            The name of the document.
	 * @param xmlFileDirectory
	 *            The xml file directory.
	 * @return A list of files representing document version xml files.
	 */
	private File[] getVersionXmlFiles(final String documentName,
			final File xmlFileDirectory) {
		return xmlFileDirectory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if(name.startsWith(documentName)) {
					if(name.endsWith(IXmlIOConstants.FILE_EXTENSION_DOCUMENT_VERSION)) {
						return true;
					}
				}
				return false;
			}
		});
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
