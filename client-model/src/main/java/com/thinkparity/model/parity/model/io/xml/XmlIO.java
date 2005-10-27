/*
 * 25-Oct-2005
 */
package com.thinkparity.model.parity.model.io.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.FileUtil;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.workspace.Workspace;

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
	 * Obtain the root directory within which all of the xml persistance is
	 * stored.
	 * 
	 * @return The root of the xml storage.
	 */
	protected File getRootXmlDirectory() {
		return xmlPathBuilder.getRootXmlDirectory();
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
	 * Write the xml to the xml file.
	 * 
	 * @param xml
	 *            The xml to write.
	 * @param xmlFile
	 *            The xml file to write to.
	 */
	protected void writeXmlFile(final String xml, final File xmlFile)
			throws FileNotFoundException, IOException {
		logger.info("writeXmlFile(String,File)");
		logger.debug(xml);
		logger.debug(xmlFile);
		FileUtil.writeFile(xmlFile, xml.getBytes());
	}
}
