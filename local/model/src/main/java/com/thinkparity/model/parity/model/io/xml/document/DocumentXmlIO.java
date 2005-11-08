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

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentContent;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.io.xml.IXmlIOConstants;
import com.thinkparity.model.parity.model.io.xml.XmlIO;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.workspace.Workspace;

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
	 * Copy a document from its source to a new destination project.
	 * 
	 * @param document
	 *            The document to copy.
	 * @param destination
	 *            The destination project.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void copy(final Document document, final Project destination)
			throws FileNotFoundException, IOException {
		logger.info("copy(Document,Project)");
		logger.debug(document);
		logger.debug(destination);
		final File destinationXmlFileDirectory = getXmlFileDirectory(destination);
		File target;
		for(File documentFile : getXmlFiles(document)) {
			target = new File(destinationXmlFileDirectory, documentFile.getName());
			FileUtil.copy(documentFile, target);
		}
	}

	/**
	 * Create the document xml. This
	 * 
	 * @param document
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void create(final Document document) throws FileNotFoundException,
			IOException {
		logger.info("create(Document)");
		logger.debug(document);
		write(document, getXmlFile(document));
	}

	/**
	 * Create the document's content xml.
	 * 
	 * @param content
	 *            The document's content.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void create(final DocumentContent content)
			throws FileNotFoundException, IOException {
		logger.info("create(DocumentContent)");
		logger.debug(content);
		write(content, getXmlFile(content));
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
		write(version, getXmlFile(version));
	}

	/**
	 * Delete the document's xml.
	 * 
	 * @param document
	 *            The document.
	 */
	public void delete(final Document document) {
		logger.info("delete(Document)");
		logger.debug(document);
		final File xmlFile = getXmlFile(document);
		Assert.assertTrue("delete(Document)", xmlFile.delete());
	}
	/**
	 * Delete the document content's xml.
	 * 
	 * @param content
	 *            The content.
	 */
	public void delete(final DocumentContent content) {
		logger.info("delete(DocumentContent)");
		logger.debug(content);
		final File xmlFile = getXmlFile(content);
		Assert.assertTrue("delete(DocumentContent)", xmlFile.delete());
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
	 * Obtain the document content for a given document.
	 * 
	 * @param document
	 *            The document.
	 * @return The document content.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public DocumentContent getContent(final Document document)
			throws FileNotFoundException, IOException {
		logger.info("getContent(Document)");
		logger.debug(document);
		final File xmlFileDirectory = getXmlFileDirectory(document);
		final DocumentContent content = readDocumentContent(
				getContentXmlFile(document.getName(), xmlFileDirectory));
		content.setDocument(document);
		return content;
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
		Document document;
		for(File xmlFile : xmlFiles) {
			document = readDocument(xmlFile);
			document.setParent(project);
			documents.add(document);
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
		DocumentVersion version;
		for(File xmlFile : xmlFiles) {
			version = readDocumentVersion(xmlFile);
			version.setDocument(document);
			versions.add(version);
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
		write(document, getXmlFile(document));
	}

	/**
	 * Update a document's content xml.
	 * 
	 * @param content
	 *            The document content.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void update(final DocumentContent content)
			throws FileNotFoundException, IOException {
		logger.info("update(DocumentContent)");
		logger.debug(content);
		write(content, getXmlFile(content));
	}

	/**
	 * Obtain the xml file for the document content; given the document name.
	 * 
	 * @param documentName
	 *            The document name.
	 * @param xmlFileDirectory
	 *            The xml file directory.
	 * @return The document content xml file.
	 */
	private File getContentXmlFile(final String documentName,
			final File xmlFileDirectory) {
		return new File(
				xmlFileDirectory,
				new StringBuffer(documentName)
					.append(IXmlIOConstants.FILE_EXTENSION_DOCUMENT_CONTENT)
					.toString());
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
}
