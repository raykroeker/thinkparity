/*
 * 25-Oct-2005
 */
package com.thinkparity.model.parity.model.io.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Stack;
import java.util.UUID;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentContent;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
import com.thinkparity.model.parity.model.io.xml.index.Index;
import com.thinkparity.model.parity.model.io.xml.index.IndexXmlIO;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xstream.XStreamUtil;

/**
 * This is an abstraction of the xml input\output routines used by the parity
 * xml input\output classes.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public abstract class XmlIO {

	/**
	 * Since the xml index is a singleton; all read\write access to the xml file
	 * should be synchronized.
	 */
	private static final Object indexLock;

	static { indexLock = new Object(); }

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(getClass());

	/**
	 * Handle to the parity workspace.
	 */
	private final Workspace workspace;

	/**
	 * Used to build xml file paths for parity objects.
	 */
	private final XmlIOPathBuilder xmlPathBuilder;

	/**
	 * Create a XmlIO.
	 */
	protected XmlIO(final Workspace workspace) {
		super();
		this.xmlPathBuilder = new XmlIOPathBuilder(workspace);
		this.workspace = workspace;
	}

	/**
	 * Obtain the index xml file.
	 * 
	 * @return The index xml file.
	 */
	protected File getIndexXmlFile() {
		return xmlPathBuilder.getIndexXmlFile();
	}

	/**
	 * Obtain the root of the xml io filesystem.
	 * 
	 * @return The root of the xml io filesystem.
	 */
	protected File getRoot() {
		final File root = xmlPathBuilder.getRoot();
		if(!root.exists()) {
			Assert.assertTrue("getRoot()", root.mkdir());
		}
		return root;
	}

	/**
	 * Obtain the xml file for the document.
	 * 
	 * @param document
	 *            The document.
	 * @return The document's xml file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected File getXmlFile(final Document document)
			throws FileNotFoundException, IOException {
		final File xmlFile = lookupXmlFile(document.getId());
		if(null != xmlFile) { return xmlFile; }
		else {
			return xmlPathBuilder.getXmlFile(document, fillInStack(document));
		}
	}

	/**
	 * Obtain the xml file for the document content.
	 * 
	 * @param document
	 *            The document.
	 * @param content
	 *            The document's content.
	 * @return The document content's xml file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected File getXmlFile(final Document document,
			final DocumentContent content) throws FileNotFoundException,
			IOException {
		return new File(getXmlFileDirectory(document),
				new StringBuffer(document.getName())
					.append(IXmlIOConstants.FILE_EXTENSION_DOCUMENT_CONTENT)
					.toString());
	}

	/**
	 * Obtain the xml file for the given document version.
	 * 
	 * @param document
	 *            The document.
	 * @param versionId
	 *            The version id.
	 * @return The xml file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected File getXmlFile(final Document document,
			final String versionId) throws FileNotFoundException,
			IOException {
		return new File(
				getXmlFileDirectory(document),
				new StringBuffer(document.getName())
					.append(".")
					.append(versionId)
					.append(IXmlIOConstants.FILE_EXTENSION_DOCUMENT_VERSION)
					.toString());
	}

	/**
	 * Obtain the xml file for the given document version content.
	 * 
	 * @param document
	 *            The document.
	 * @param versionContent
	 *            The document version content.
	 * @return the xml file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected File getXmlFile(final Document document,
			final DocumentVersionContent versionContent)
			throws FileNotFoundException, IOException {
		return new File(getXmlFileDirectory(document), new StringBuffer(document.getName())
				.append(".")
				.append(versionContent.getVersionId())
				.append(IXmlIOConstants.FILE_EXTENSION_DOCUMENT_VERSION_CONTENT)
				.toString());
	}

	/**
	 * Obtain the project's xml file.
	 * 
	 * @param project
	 *            The project.
	 * @return The project's xml file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected File getXmlFile(final Project project)
			throws FileNotFoundException, IOException {
		final File xmlFile = lookupXmlFile(project.getId());
		if(null != xmlFile) { return xmlFile; }
		else { return xmlPathBuilder.getXmlFile(project, fillInStack(project)); }
	}

	/**
	 * Obtain the xml file's directory of the document.
	 * 
	 * @param document
	 *            The document.
	 * @return The document's xml file's directory.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected File getXmlFileDirectory(final Document document)
			throws FileNotFoundException, IOException {
		return getXmlFile(document).getParentFile();
	}

	/**
	 * Obtain the project's xml file's directory.
	 * 
	 * @param project
	 *            The project.
	 * @return The project's xml file's directory.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected File getXmlFileDirectory(final Project project)
			throws FileNotFoundException, IOException {
		return getXmlFile(project).getParentFile();
	}

	/**
	 * Obtain all of the xml files for the given document. This includes the
	 * document, the document content as well as all of the document version
	 * files.
	 * 
	 * @param document
	 *            The document to obtain the xml files for.
	 * @return The list of xml files for the document.
	 */
	protected File[] getXmlFiles(final Document document)
			throws FileNotFoundException, IOException {
		return xmlPathBuilder.getXmlFiles(document, fillInStack(document));
	}

	/**
	 * Lookup the xml file within the internal xml index for an object of a
	 * given id.
	 * 
	 * @param id
	 *            The object id.
	 * @return The xml file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected File lookupXmlFile(final UUID id) throws FileNotFoundException,
			IOException {
		final IndexXmlIO indexXmlIO = new IndexXmlIO(workspace);
		return indexXmlIO.get().lookupXmlFile(id);
	}

	/**
	 * Move a document, it's content and its versions to a new xml directory
	 * location.
	 * 
	 * @param document
	 *            The document.
	 * @param content
	 *            The document content.
	 * @param versions
	 *            The document versions.
	 * @param versionsContent
	 *            The document versions' content.
	 * @param targetXmlFileDirectory
	 *            The target directory to move the document to.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void move(final Document document, final DocumentContent content,
			final Collection<DocumentVersion> versions,
			final Collection<DocumentVersionContent> versionsContent,
			final File targetXmlFileDirectory) throws FileNotFoundException,
			IOException {
		final File documentXmlFile = getXmlFile(document);
		final File contentXmlFile = getXmlFile(document, content);
		final File[] versionXmlFiles = getVersionXmlFiles(document, versions);
		final File[] versionContentXmlFiles = getVersionContentXmlFiles(document, versionsContent);

		final File targetDocumentXmlFile =
			new File(targetXmlFileDirectory, documentXmlFile.getName());
		FileUtil.copy(documentXmlFile, targetDocumentXmlFile);
		Assert.assertTrue(
				"move(Document,DocumentContent,DocumentVersion,DocumentVersionContent,File)",
				documentXmlFile.delete());
		FileUtil.copy(contentXmlFile, new File(targetXmlFileDirectory, contentXmlFile.getName()));
		Assert.assertTrue(
				"move(Document,DocumentContent,DocumentVersion,DocumentVersionContent,File)",
				contentXmlFile.delete());
		for(File versionXmlFile : versionXmlFiles) {
			FileUtil.copy(versionXmlFile, new File(targetXmlFileDirectory, versionXmlFile.getName()));
			Assert.assertTrue(
					"move(Document,DocumentContent,DocumentVersion,DocumentVersionContent,File)",
					versionXmlFile.delete());
		}
		for(File versionContentXmlFile : versionContentXmlFiles) {
			FileUtil.copy(versionContentXmlFile, new File(targetXmlFileDirectory, versionContentXmlFile.getName()));
			Assert.assertTrue(
					"move(Document,DocumentContent,DocumentVersion,DocumentVersionContent,File)",
					versionContentXmlFile.delete());
		}
		final Index index = readIndex(getIndexXmlFile());
		index.addXmlFileLookup(document.getId(), targetDocumentXmlFile);
		write(index, getIndexXmlFile());
	}

	/**
	 * Read a document from an xml file.
	 * 
	 * @param xmlFile
	 *            The xml file for the document.
	 * @return A document
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected Document readDocument(final File xmlFile)
			throws FileNotFoundException, IOException {
		return (Document) fromXml(readXmlFile(xmlFile));
	}

	/**
	 * Read the document content from an xml file.
	 * 
	 * @param xmlFile
	 *            The xml file for the document content.
	 * @return The document content.
	 */
	protected DocumentContent readDocumentContent(final File xmlFile)
			throws FileNotFoundException, IOException {
		return (DocumentContent) fromXml(readXmlFile(xmlFile));
	}

	/**
	 * Read a document version from an xml file.
	 * 
	 * @param xmlFile
	 *            The xml file for the document version.
	 * @return The document version.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected DocumentVersion readDocumentVersion(final File xmlFile)
			throws FileNotFoundException, IOException {
		return (DocumentVersion) fromXml(readXmlFile(xmlFile));
	}

	/**
	 * Read the version content from an xml file.
	 * 
	 * @param xmlFile
	 *            The xml file for the version content.
	 * @return The version content.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected DocumentVersionContent readDocumentVersionContent(
			final File xmlFile) throws FileNotFoundException, IOException {
		return (DocumentVersionContent) fromXml(readXmlFile(xmlFile));
	}

	/**
	 * Read the index from an xml file.
	 * 
	 * @param xmlFile
	 *            The xml file for the index.
	 * @return The index.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected Index readIndex(final File xmlFile) throws FileNotFoundException,
			IOException {
		synchronized(indexLock) {
			return (Index) fromXml(readXmlFile(xmlFile));
		}
	}

	/**
	 * Read a project from an xml file.
	 * 
	 * @param xmlFile
	 *            The xml file for the project.
	 * @return A project.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected Project readProject(final File xmlFile)
			throws FileNotFoundException, IOException {
		return (Project) fromXml(readXmlFile(xmlFile));
	}

	/**
	 * Remove an entry from the xml file index.
	 * 
	 * @param parityObject
	 *            The parity object to remove.
	 * @return The previous xml file value.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected File removeXmlFileLookup(final ParityObject parityObject)
			throws FileNotFoundException, IOException {
		final Index index = readIndex(getIndexXmlFile());
		final File xmlFile = index.removeXmlFileLookup(parityObject.getId());
		write(index, getIndexXmlFile());
		return xmlFile;
	}

	/**
	 * Write the document to the xml file.
	 * 
	 * @param document
	 *            The document to write.
	 * @param xmlFile
	 *            The xml file to write to.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void write(final Document document, final File xmlFile)
			throws FileNotFoundException, IOException {
		writeXmlFile(toXml(document), xmlFile);
		writeIndexXml(document);
	}

	/**
	 * Write the document content to the xml file.
	 * 
	 * @param content
	 *            The content to write.
	 * @param xmlFile
	 *            The xml file to write to.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void write(final DocumentContent content, final File xmlFile)
			throws FileNotFoundException, IOException {
		writeXmlFile(toXml(content), xmlFile);
	}

	/**
	 * Write the document version to the xml file.
	 * 
	 * @param version
	 *            The version to write.
	 * @param xmlFile
	 *            The xml file to write to.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void write(final DocumentVersion version, final File xmlFile)
			throws FileNotFoundException, IOException {
		writeXmlFile(toXml(version), xmlFile);
	}

	/**
	 * Write the document version content to the xml file.
	 * 
	 * @param versionContent
	 *            The version content.
	 * @param xmlFile
	 *            The xml file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void write(final DocumentVersionContent versionContent,
			final File xmlFile) throws FileNotFoundException, IOException {
		writeXmlFile(toXml(versionContent), xmlFile);
	}

	/**
	 * Write the index to the xml file.
	 * 
	 * @param index
	 *            The index to write.
	 * @param xmlFile
	 *            The xml file to write to.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void write(final Index index, final File xmlFile)
			throws FileNotFoundException, IOException {
		writeXmlFile(toXml(index), xmlFile);
	}

	/**
	 * Write the project to the xml file.
	 * 
	 * @param project
	 *            The project to write.
	 * @param xmlFile
	 *            The xml file to write to.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void write(final Project project, final File xmlFile)
			throws FileNotFoundException, IOException {
		writeXmlFile(toXml(project), xmlFile);
		writeIndexXml(project);
	}

	/**
	 * Fill in the project stack for a given parity object. This will build a
	 * stack of parent projects.
	 * 
	 * @param parityObject
	 *            The parity object.
	 * @return The parity object's parent stack.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Stack<Project> fillInStack(final ParityObject parityObject)
			throws FileNotFoundException, IOException {
		final Stack<Project> stack = new Stack<Project>();
		UUID parentId = parityObject.getParentId();
		while(null != parentId) {
			stack.push(readProject(lookupXmlFile(parentId)));
			parentId = stack.peek().getParentId();
		}
		return stack;
	}

	/**
	 * Use the XStream framework to read an object from xml.
	 * 
	 * @param xml
	 *            The xml to read.
	 * @return A constructed object.
	 */
	private Object fromXml(final String xml) {
		return XStreamUtil.fromXML(xml);
	}

	/**
	 * Obtain a list of all of the version xml files for a given document.
	 * 
	 * @param document
	 *            The document.
	 * @param versions
	 *            The document versions.
	 * @return A list of all of the xml files for the document's versions.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private File[] getVersionXmlFiles(final Document document,
			final Collection<DocumentVersion> versions)
			throws FileNotFoundException, IOException {
		final Collection<File> xmlFiles = new Vector<File>(7);
		for(DocumentVersion version : versions) {
			xmlFiles.add(getXmlFile(document, version.getVersionId()));
		}
		return xmlFiles.toArray(new File[] {});
	}

	private File[] getVersionContentXmlFiles(final Document document,
			final Collection<DocumentVersionContent> versionsContent)
			throws IOException {
		final Collection<File> xmlFiles = new Vector<File>(versionsContent.size());
		for(DocumentVersionContent versionContent : versionsContent) {
			xmlFiles.add(getXmlFile(document, versionContent));
		}
		return xmlFiles.toArray(new File[] {});
	}

	/**
	 * Read an xml file's content into a string.
	 * 
	 * @param xmlFile
	 *            The xml file to read.
	 * @return The xml content of the file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String readXmlFile(final File xmlFile)
			throws FileNotFoundException, IOException {
		logger.info("readXmlFile(File)");
		logger.debug(xmlFile);
		final String xml = FileUtil.readString(xmlFile);
		logger.debug(xml);
		return xml;
	}

	/**
	 * Use the XStream framework to serialize an object to xml.
	 * 
	 * @param object
	 *            The object to serialize.
	 * @return The xml representation of the object.
	 */
	private String toXml(final Object object) {
		return XStreamUtil.toXML(object);
	}

	/**
	 * Write a document to the xml index.
	 * 
	 * @param document
	 *            The document to add to the xml index.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeIndexXml(final Document document)
			throws FileNotFoundException, IOException {
		synchronized(indexLock) {
			final IndexXmlIO indexXmlIO = new IndexXmlIO(workspace);
			final Index index = indexXmlIO.get();
			index.addXmlFileLookup(document.getId(), getXmlFile(document));
			write(index, getIndexXmlFile());
		}
	}

	/**
	 * Write a project to the xml index.
	 * 
	 * @param project
	 *            The project to add to the xml index.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeIndexXml(final Project project)
			throws FileNotFoundException, IOException {
		synchronized(indexLock) {
			final IndexXmlIO indexXmlIO = new IndexXmlIO(workspace);
			final Index index = indexXmlIO.get();
			index.addXmlFileLookup(project.getId(), getXmlFile(project));
			write(index, getIndexXmlFile());
		}
	}

	/**
	 * Write the xml to the xml file. If the xml file already exists, it will be
	 * deleted first.
	 * 
	 * @param xml
	 *            The xml to write.
	 * @param xmlFile
	 *            The xml file to write to.
	 */
	private void writeXmlFile(final String xml, final File xmlFile)
			throws FileNotFoundException, IOException {
		if(xmlFile.exists()) {
			Assert.assertTrue("writeXmlFile(String,File)", xmlFile.delete());
		}
		FileUtil.writeBytes(xmlFile, xml.getBytes());
	}
}
