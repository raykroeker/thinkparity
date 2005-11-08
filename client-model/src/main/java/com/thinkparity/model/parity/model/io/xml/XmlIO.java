/*
 * 25-Oct-2005
 */
package com.thinkparity.model.parity.model.io.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentContent;
import com.thinkparity.model.parity.model.document.DocumentVersion;
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
	 * Handle to an apache logger.
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(getClass());

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
	 * Obtain the xml file for the given document.
	 * 
	 * @param document
	 *            The document to obtain the xml file for.
	 * @return The xml file.
	 */
	protected File getXmlFile(final Document document) {
		logger.info("getXmlFile(Document)");
		logger.debug(document);
		return xmlPathBuilder.getXmlFile(document);
	}

	/**
	 * Obtain the xml file for the given document content.
	 * 
	 * @param content
	 *            The document content to obtain the xml file for.
	 * @return The xml file.
	 */
	protected File getXmlFile(final DocumentContent content) {
		logger.info("getXmlFile(DocumentContent)");
		logger.debug(content);
		return xmlPathBuilder.getXmlFile(content);
	}

	/**
	 * Obtain the xml file for the given document version.
	 * 
	 * @param version
	 *            The document version to obtain the xml file for.
	 * @return The xml file.
	 */
	protected File getXmlFile(final DocumentVersion version) {
		logger.info("getXmlFile(DocumentVersion)");
		logger.debug(version);
		return xmlPathBuilder.getXmlFile(version);
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
	protected File[] getXmlFiles(final Document document) {
		logger.info("getXmlFiles(Document)");
		logger.debug(document);
		return xmlPathBuilder.getXmlFiles(document);
	}

	/**
	 * Obtain the xml file for the given project.
	 * 
	 * @param project
	 *            The project to obtain the xml file for.
	 * @return The xml file.
	 */
	protected File getXmlFile(final Project project) {
		logger.info("getXmlFile(Project)");
		logger.debug(project);
		return xmlPathBuilder.getXmlFile(project);
	}

	/**
	 * Obtain the directory of the xml file for the document.
	 * 
	 * @param document
	 *            The document to obtain the directory for.
	 * @return The directory of the document's xml file.
	 */
	protected File getXmlFileDirectory(final Document document) {
		logger.info("getXmlFileDirectory(Document)");
		logger.debug(document);
		return xmlPathBuilder.getXmlFileDirectory(document);
	}

	/**
	 * Obtain the directory of the xml file for the project.
	 * 
	 * @param project
	 *            The project to obtain the xml file directory for.
	 * @return The directory of the project xml file.
	 */
	protected File getXmlFileDirectory(final Project project) {
		logger.info("getXmlFileDirectory(Project)");
		logger.debug(project);
		return xmlPathBuilder.getXmlFileDirectory(project);
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
		logger.info("readDocument(File)");
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
		logger.info("readDocumentContent(File)");
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
		logger.info("readDocumentVersion(File)");
		return (DocumentVersion) fromXml(readXmlFile(xmlFile));
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
		logger.info("readProject(File)");
		return (Project) fromXml(readXmlFile(xmlFile));
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
	protected String readXmlFile(final File xmlFile)
			throws FileNotFoundException, IOException {
		logger.info("readXmlFile(File)");
		logger.debug(xmlFile);
		final byte[] xmlBytes = FileUtil.readFile(xmlFile);
		logger.debug(xmlBytes);
		final String xml =
			new String(xmlBytes, IXmlIOConstants.DEFAULT_CHARSET.getCharsetName());
		logger.debug(xml);
		return xml;
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
		logger.info("write(Document,File)");
		writeXmlFile(toXml(document), xmlFile);
	}

	/**
	 * Write the document content to the xml file.
	 * @param content The content to write.
	 * @param xmlFile The xml file to write to.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void write(final DocumentContent content, final File xmlFile)
			throws FileNotFoundException, IOException {
		logger.info("write(DocumentContent,File)");
		writeXmlFile(toXml(content), xmlFile);
	}

	/**
	 * Write the document version to the xml file.
	 * 
	 * @param documentVersion
	 *            The document version to write.
	 * @param xmlFile
	 *            The xml file to write to.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void write(final DocumentVersion documentVersion,
			final File xmlFile) throws FileNotFoundException, IOException {
		logger.info("write(DocumentVersion,File)");
		logger.debug(documentVersion);
		writeXmlFile(toXml(documentVersion), xmlFile);
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
		logger.info("write(Project,File)");
		writeXmlFile(toXml(project), xmlFile);
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
		FileUtil.writeFile(xmlFile, xml.getBytes());
	}
}
