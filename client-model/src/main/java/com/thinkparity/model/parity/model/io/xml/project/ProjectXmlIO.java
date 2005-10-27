/*
 * Feb 20, 2005
 */
package com.thinkparity.model.parity.model.io.xml.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.IParityConstants;
import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.model.io.xml.XmlIO;
import com.thinkparity.model.parity.model.io.xml.IXmlIOConstants;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xstream.XStreamUtil;

/**
 * ProjectXmlIO
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class ProjectXmlIO extends XmlIO {

	/**
	 * Create a ProjectXmlIO.
	 * 
	 * @param workspace
	 *            The parity workspace to use.
	 */
	public ProjectXmlIO(final Workspace workspace) { super(workspace); }

	/**
	 * Create the xml for a project. This will create the project directory; the
	 * project xml file's directory and the project xml file.
	 * 
	 * @param project
	 *            The project to create.
	 */
	public void create(final Project project) throws FileNotFoundException,
			IOException {
		logger.info("create(Project)");
		logger.debug(project);
		final File xmlFile = getXmlFile(project);
		logger.debug(xmlFile);
		final File xmlFileDirectory = getXmlFileDirectory(project);
		logger.debug(xmlFileDirectory);
		final File projectDirectory = xmlFileDirectory.getParentFile();
		logger.debug(projectDirectory);

		Assert.assertTrue("create(Project)", projectDirectory.mkdir());
		Assert.assertTrue("create(Project)", xmlFileDirectory.mkdir());
		final String xml = XStreamUtil.toXML(project);
		logger.debug(xml);
		writeXmlFile(xml, xmlFile);
	}

	/**
	 * Delete the xml for a project. This will delete the project xml file; the
	 * project xml file's directory; and the project directory. At this point
	 * all document files should have been removed from these directories.
	 * 
	 * @param project
	 *            The project to delete.
	 */
	public void delete(final Project project) {
		logger.info("delete(Project)");
		logger.debug(project);
		// delete the xml file
		final File xmlFile = getXmlFile(project);
		logger.debug(xmlFile);
		Assert.assertTrue("delete(Project)", xmlFile.delete());
		// delete the xml file directory
		final File xmlFileDirectory = getXmlFileDirectory(project);
		logger.debug(xmlFileDirectory);
		Assert.assertTrue("delete(Project)", xmlFileDirectory.delete());
		// delete the project directory
		final File projectDirectory = xmlFileDirectory.getParentFile();
		logger.debug(projectDirectory);
		Assert.assertTrue("delete(Project)", projectDirectory.delete());
	}

	/**
	 * Determine whether or not the parity root project exists.
	 * 
	 * @return True if the root project exists, false otherwise.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Boolean doesRootProjectExist() throws FileNotFoundException,
			IOException {
		logger.info("doesRootProjectExist()");
		final File rootProjectXmlFile = getRootProjectXmlFile();
		logger.debug(rootProjectXmlFile);
		if(rootProjectXmlFile.exists()) {
			final String xml = readXmlFile(rootProjectXmlFile);
			final Project rootProject = (Project) XStreamUtil.fromXML(xml);
			Assert.assertNotNull("doesRootProjectExist()", rootProject);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Obtain a project of a given name within a parent project.
	 * 
	 * @param name
	 *            The name of the project to find.
	 * @param parent
	 *            The parent project.
	 * @return The project.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Project get(final String name, final Project parent)
			throws FileNotFoundException, IOException {
		logger.info("get(String,Project)");
		logger.debug(name);
		logger.debug(parent);
		final File parentXmlFileDirectory = getXmlFileDirectory(parent);
		logger.debug(parentXmlFileDirectory);
		final File parentProjectDirectory = parentXmlFileDirectory.getParentFile();
		logger.debug(parentProjectDirectory);
		final File projectDirectory = new File(parentProjectDirectory, name);
		logger.debug(projectDirectory);
		final File xmlFileDirectory = new File(projectDirectory, IParityConstants.META_DATA_DIRECTORY_NAME);
		logger.debug(xmlFileDirectory);
		final String xmlFileName = getXmlFileName(name);
		logger.debug(xmlFileName);
		final File xmlFile = new File(xmlFileDirectory, xmlFileName);
		logger.debug(xmlFile);
		if(xmlFile.exists()) {
			final String xml = readXmlFile(xmlFile);
			logger.debug(xml);
			final Project project = (Project) XStreamUtil.fromXML(xml);
			logger.debug(project);
			return project;
		}
		else { return null; }
	}

	/**
	 * Obtain a project with a given name for a given parent. If parent is
	 * specified the project must exist within the parent; otherwise the project
	 * must exist within the root project.
	 * 
	 * @param name
	 *            The name of the project.
	 * @param parent
	 *            The parent project (Optional)
	 * @return The project.
	 */
	public Project getRootProject() throws FileNotFoundException, IOException {
		logger.info("getRootProject()");
		final File rootProjectXmlFile = getRootProjectXmlFile();
		logger.debug(rootProjectXmlFile);
		final String xml = readXmlFile(rootProjectXmlFile);
		logger.debug(xml);
		final Project rootProject = (Project) XStreamUtil.fromXML(xml);
		logger.debug(rootProject);
		return rootProject;
	}

	/**
	 * Update the xml for a project.
	 * 
	 * @param project
	 *            The project to update.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void update(final Project project) throws FileNotFoundException,
			IOException {
		logger.info("update(Project)");
		logger.debug(project);
		final File xmlFile = getXmlFile(project);
		Assert.assertTrue("update(Project)", xmlFile.delete());
		final String xml = XStreamUtil.toXML(project);
		logger.debug(xml);
		writeXmlFile(xml, xmlFile);
	}

	/**
	 * Build an xml file reference for the project.
	 * 
	 * @param project
	 *            The project to build the xml file for.
	 * @return The xml file reference.
	 * @see XmlIO#getXmlFile(ParityObject)
	 */
	protected File getXmlFile(final Project project) {
		return super.getXmlFile(project);
	}

	/**
	 * Obtain the xml file for the root project.
	 * 
	 * @return The xml file for the root project.
	 */
	private File getRootProjectXmlFile() {
		final File projectDirectory =
			new File(getRootXmlDirectory(), IParityConstants.ROOT_PROJECT_NAME);
		final File xmlFileDirectory =
			new File(projectDirectory, IParityConstants.META_DATA_DIRECTORY_NAME);
		return new File(xmlFileDirectory, IXmlIOConstants.XML_FILE_NAME_ROOT_PROJECT);
	}

	private String getXmlFileName(final String name) {
		return new StringBuffer(name)
			.append(IXmlIOConstants.FILE_EXTENSION_PROJECT)
			.toString();
	}
}
