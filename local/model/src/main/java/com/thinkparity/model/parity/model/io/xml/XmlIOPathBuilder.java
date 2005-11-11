/*
 * 26-Oct-2005
 */
package com.thinkparity.model.parity.model.io.xml;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.model.document.Document;
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
	 * Obtain the index xml file.
	 * 
	 * @return The index xml file.
	 */
	File getIndexXmlFile() {
		return new File(getRoot(), getIndexXmlFileName());
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
	 * @param stack
	 *            The parent stack of the document.
	 * @return The xml file.
	 */
	File getXmlFile(final Document document, final Stack<Project> stack) {
		logger.info("getXmlFile(Document,Stack<Project>)");
		logger.debug(document);
		logger.debug(stack);
		final File xmlFileDirectory = getXmlFileDirectory(document, stack);
		logger.debug(xmlFileDirectory);
		final String xmlFileName = getXmlFileName(document);
		return new File(xmlFileDirectory, xmlFileName);
	}

	/**
	 * Obtain the xml file for the given project.
	 * 
	 * @param project
	 *            The project to obtain the xml file for.
	 * @param stack
	 *            The parent stack of the project.
	 * @return The xml file.
	 */
	File getXmlFile(final Project project, final Stack<Project> stack) {
		logger.info("getXmlFile(Project,Stack<Project>");
		logger.debug(project);
		logger.debug(stack);
		final File xmlFileDirectory = getXmlFileDirectory(project, stack);
		logger.debug(xmlFileDirectory);
		final String xmlFileName = getXmlFileName(project);
		logger.debug(xmlFileName);
		return new File(xmlFileDirectory, xmlFileName);
	}

	/**
	 * Obtain all of the xml files for the given document. This includes the
	 * document, the document content as well as all of the document version
	 * files.
	 * 
	 * @param document
	 *            The document to obtain the xml files for.
	 * @param stack
	 *            The parent stack for the document.
	 * @return The list of xml files for the document.
	 */
	File[] getXmlFiles(final Document document, final Stack<Project> stack) {
		logger.info("getXmlFile(Document)");
		logger.debug(document);
		final File xmlFileDirectory = getXmlFileDirectory(document, stack);
		logger.debug(xmlFileDirectory);
		return xmlFileDirectory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if(name.startsWith(document.getName())) {
					if(name.endsWith(IXmlIOConstants.FILE_EXTENSION_DOCUMENT))
						return true;
					if(name.endsWith(IXmlIOConstants.FILE_EXTENSION_DOCUMENT_CONTENT))
						return true;
					if(name.endsWith(IXmlIOConstants.FILE_EXTENSION_DOCUMENT_VERSION))
						return true;
				}
				return false;
			}
		});
	}

	/**
	 * Obtain the index xml file's name.
	 * 
	 * @return The index xml file's name.
	 */
	private String getIndexXmlFileName() {
		return IXmlIOConstants.FILE_EXTENSION_INDEX;
	}

	/**
	 * Obtain the pathname of the parity object. This will traverse the parent
	 * project tree and build a path accordingly. If we are dealing with a
	 * project the project name is appended to the pathname. The metadata path
	 * is appended to the path once complete.
	 * 
	 * @param parityObject
	 *            The parity object to obtain the pathname for.
	 * @param stack
	 *            The parent stack for the parity object.
	 * @return The pathname of the parity object.
	 */
	private String getPathname(final ParityObject parityObject,
			final Stack<Project> stack) {
		// add the root; then the parent stack to the pathname
		final StringBuffer pathname = new StringBuffer()
			.append(getRoot().getAbsolutePath());
		while(!stack.isEmpty()) {
			pathname.append(File.separatorChar)
				.append(stack.pop().getName());
		}
		switch(parityObject.getType()) {
		case DOCUMENT:
			break;
		case PROJECT:
			pathname.append(File.separatorChar)
				.append(parityObject.getName());
			break;
		default:
			Assert.assertUnreachable("getPathname(ParityObject,Stack<Project>)");
		}
		return pathname.toString();
	}

	/**
	 * Obtain the directory of the xml file for the document.
	 * 
	 * @param document
	 *            The document to obtain the directory for.
	 * @param stack
	 *            The parent stack of the document.
	 * @return The directory of the document's xml file.
	 */
	private File getXmlFileDirectory(final Document document, final Stack<Project> stack) {
		logger.info("getXmlFileDirectory(Document,Stack<Project>)");
		logger.debug(document);
		logger.debug(stack);
		final String pathname = getPathname(document, stack);
		logger.debug(pathname);
		return new File(pathname);
	}

	/**
	 * Obtain the directory of the xml file for the project.
	 * 
	 * @param project
	 *            The project to obtain the xml file directory for.
	 * @param stack
	 *            The parent stack of the project.
	 * @return The directory of the project xml file.
	 */
	private File getXmlFileDirectory(final Project project, final Stack<Project> stack) {
		logger.info("getXmlFileDirectory(Project,Stack<Project>)");
		logger.debug(project);
		logger.debug(stack);
		final String parent = getPathname(project, stack);
		logger.debug(parent);
		return new File(parent);
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
