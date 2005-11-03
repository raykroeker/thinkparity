/*
 * 26-Oct-2005
 */
package com.thinkparity.model.parity.model.io.xml;

import java.io.File;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentContent;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XmlIOPathBuilder {

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(XmlIOPathBuilder.class);

	/**
	 * Parity workspace.
	 */
	private final Workspace workspace;

	/**
	 * Create a XmlIOPathBuilder.
	 */
	XmlIOPathBuilder(final Workspace workspace) {
		super();
		this.workspace = workspace;
	}

	/**
	 * Obtain the root directory for the xml io driver.
	 * 
	 * @return The root directory of the xml io driver.
	 */
	File getRoot() {
		return new File(
				workspace.getDataURL().getFile(),
				IXmlIOConstants.DIRECTORY_NAME_XML_DATA);
	}

	/**
	 * Obtain the xml file for the given document.
	 * 
	 * @param document
	 *            The document to obtain the xml file for.
	 * @return The xml file.
	 */
	File getXmlFile(final Document document) {
		logger.info("getXmlFile(Document)");
		logger.debug(document);
		final File xmlFileDirectory = getXmlFileDirectory(document);
		logger.debug(xmlFileDirectory);
		final String xmlFileName = getXmlFileName(document);
		logger.debug(xmlFileName);
		return new File(xmlFileDirectory, xmlFileName);
	}

	/**
	 * Obtain the xml file for the given document content.
	 * 
	 * @param content
	 *            The document content.
	 * @return The xml file.
	 */
	File getXmlFile(final DocumentContent content) {
		logger.info("getXmlFile(DocumentContent)");
		logger.debug(content);
		final File xmlFileDirectory = getXmlFileDirectory(content.getDocument());
		logger.debug(xmlFileDirectory);
		final String xmlFileName = getXmlFileName(content);
		logger.debug(xmlFileName);
		return new File(xmlFileDirectory, xmlFileName);
	}

	/**
	 * Obtain the xml file for the given document version.
	 * 
	 * @param version
	 *            The document version to obtain the xml file for.
	 * @return The xml file.
	 */
	File getXmlFile(final DocumentVersion version) {
		logger.info("getXmlFile(DocumentVersion)");
		logger.debug(version);
		final File xmlFileDirectory = getXmlFileDirectory(version);
		logger.debug(xmlFileDirectory);
		final String xmlFileName = getXmlFileName(version);
		logger.debug(xmlFileName);
		return new File(xmlFileDirectory, xmlFileName);
	}
	

	/**
	 * Obtain the xml file for the given project.
	 * 
	 * @param project
	 *            The project to obtain the xml file for.
	 * @return The xml file.
	 */
	File getXmlFile(final Project project) {
		logger.info("getXmlFile(Project)");
		logger.debug(project);
		final File xmlFileDirectory = getXmlFileDirectory(project);
		logger.debug(xmlFileDirectory);
		final String xmlFileName = getXmlFileName(project);
		logger.debug(xmlFileName);
		return new File(xmlFileDirectory, xmlFileName);
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
		final String pathname = getPathname(document);
		logger.debug(pathname);
		return new File(pathname);
	}

	/**
	 * Obtain the directory of the xml file for the document version.
	 * 
	 * @param version
	 *            The document version to obtain the directory for.
	 * @return The directory of the document version's xml file.
	 */
	File getXmlFileDirectory(final DocumentVersion version) {
		logger.info("getXmlFileDirectory(DocumentVersion)");
		logger.debug(version);
		final Document document = version.getDocument();
		final String pathname = getPathname(document);
		logger.debug(pathname);
		return new File(pathname);
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
		final String parent = getPathname(project);
		logger.debug(parent);
		return new File(parent);
	}

	/**
	 * Create a stack of parent projects for the parity object.
	 * 
	 * @param parityObject
	 *            The parity object to fill in the stack for.
	 * @return The stack of parent projects.
	 */
	private Stack<Project> fillParentStack(final ParityObject parityObject) {
		final Stack<Project> parentStack = new Stack<Project>();
		Project parent = parityObject.getParent();
		while(null != parent) {
			parentStack.push(parent);
			parent = parent.getParent();
		}
		return parentStack;
	}

	/**
	 * Obtain the pathname of the parity object. This will traverse the parent
	 * project tree and build a path accordingly. If we are dealing with a
	 * project the project name is appended to the pathname. The metadata path
	 * is appended to the path once complete.
	 * 
	 * @param parityObject
	 *            The parity object to obtain the pathname for.
	 * @return The pathname of the parity object.
	 */
	private String getPathname(final ParityObject parityObject) {
		final Stack<Project> parentStack = fillParentStack(parityObject);
		final StringBuffer parent = new StringBuffer()
			.append(getRoot().getAbsolutePath());
		while(!parentStack.isEmpty()) {
			parent.append(File.separatorChar)
				.append(parentStack.pop().getName());
		}
		switch(parityObject.getType()) {
		case DOCUMENT:
			break;
		case PROJECT:
			parent.append(File.separatorChar)
				.append(parityObject.getName());
			break;
		default:
			Assert.assertUnreachable("getParent(ParityObject)");
		}
		return parent.toString();
	}

	/**
	 * Obtain the xml file name for the project.
	 * 
	 * @param project
	 *            The project to get the xml file name for.
	 * @return The xml file name.
	 */
	private String getXmlFileName(final Document document) {
		return new StringBuffer(document.getName())
			.append(IXmlIOConstants.FILE_EXTENSION_DOCUMENT)
			.toString();
	}

	/**
	 * Obtain the xml file name for the document's content.
	 * 
	 * @param content
	 *            The content to get the xml file name for.
	 * @return The xml file name.
	 */
	private String getXmlFileName(final DocumentContent content) {
		return new StringBuffer(content.getDocument().getName())
			.append(IXmlIOConstants.FILE_EXTENSION_DOCUMENT_CONTENT)
			.toString();
	}

	/**
	 * Obtain the xml file name for the document version.
	 * 
	 * @param version
	 *            The version to get the xml file name for.
	 * @return The xml file name.
	 */
	private String getXmlFileName(final DocumentVersion version) {
		final Document document = version.getDocument();
		return new StringBuffer(document.getName())
			.append(".")
			.append(version.getVersion())
			.append(IXmlIOConstants.FILE_EXTENSION_DOCUMENT_VERSION)
			.toString();
	}

	/**
	 * Obtain the xml file name for the project.
	 * 
	 * @param project
	 *            The project to get the xml file name for.
	 * @return The xml file name.
	 */
	private String getXmlFileName(final Project project) {
		return new StringBuffer(project.getName())
			.append(IXmlIOConstants.FILE_EXTENSION_PROJECT)
			.toString();
	}
}
