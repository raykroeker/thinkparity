/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.io.xml.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentContent;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.io.xml.IXmlIOConstants;
import com.thinkparity.model.parity.model.io.xml.XmlIO;
import com.thinkparity.model.parity.model.io.xml.index.Index;
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
	 * Create the document xml. This
	 * 
	 * @param document
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void create(final Document document, final DocumentContent content)
			throws FileNotFoundException, IOException {
		logger.info("create(Document)");
		logger.debug(document);
		logger.debug(content);
		write(document, getXmlFile(document));
		write(content, getXmlFile(document, content));
	}

	/**
	 * Serialize a document version to xml.
	 * 
	 * @param version
	 *            The document version to serialize.
	 * @throws IOException
	 */
	public void create(final Document document, final DocumentVersion version)
			throws FileNotFoundException, IOException {
		logger.info("create(DocumentVersion)");
		logger.debug(version);
		write(version, getXmlFile(document, version));
	}

	/**
	 * Delete the document's xml.
	 * 
	 * @param document
	 *            The document.
	 */
	public void delete(final Document document) throws FileNotFoundException,
			IOException {
		logger.info("delete(Document)");
		logger.debug(document);
		final DocumentContent content = getContent(document);
		final Collection<DocumentVersion> versions = listVersions(document);
		Assert.assertTrue("delete(Document)", getXmlFile(document, content).delete());
		for(DocumentVersion version : versions) {
			Assert.assertTrue("delete(Document)", getXmlFile(document, version).delete());
		}
		Assert.assertTrue("delete(Document)", getXmlFile(document).delete());
	}

	/**
	 * Obtain a document with a specified id.
	 * 
	 * @param id
	 *            The id of the document.
	 * @return The document
	 */
	public Document get(final UUID id) throws FileNotFoundException,
			IOException {
		logger.info("get(UUID)");
		logger.debug(id);
		final Index index = readIndex(getIndexXmlFile());
		return readDocument(index.lookupXmlFile(id));
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
			versions.add(version);
		}
		return versions;
	}

	/**
	 * Move a document from its source to a new destination project.
	 * 
	 * @param document
	 *            The document to move.
	 * @param destination
	 *            The destination project.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void move(final Document document, final Project destination)
			throws FileNotFoundException, IOException {
		logger.info("move(Document,Project)");
		logger.debug(document);
		logger.debug(destination);
		move(document, getContent(document), listVersions(document), getXmlFileDirectory(destination));
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
	public void update(final Document document, final DocumentContent content)
			throws FileNotFoundException, IOException {
		logger.info("update(DocumentContent)");
		logger.debug(content);
		write(content, getXmlFile(document, content));
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
