/*
 * 25-Oct-2005
 */
package com.thinkparity.model.parity.model.io.xml;

import java.io.File;

import org.apache.log4j.Logger;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * This is an abstraction of the xml input\output routines used by the parity
 * xml input\output classes.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public abstract class AbstractXmlIO {

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(getClass());

	/**
	 * Parity workspace to work within.
	 */
	private final Workspace workspace;

	/**
	 * Used to build xml file paths for parity objects.
	 */
	private final XmlPathBuilder xmlPathBuilder;

	/**
	 * Create a AbstractXmlIO.
	 */
	protected AbstractXmlIO(final Workspace workspace) {
		super();
		this.workspace = workspace;
		this.xmlPathBuilder = new XmlPathBuilder(workspace);
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
	File getXmlFileDirectory(final Document document) {
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
	File getXmlFileDirectory(final Project project) {
		logger.info("getXmlFileDirectory(Project)");
		logger.debug(project);
		return xmlPathBuilder.getXmlFileDirectory(project);
	}

	
}
