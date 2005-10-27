/*
 * Feb 20, 2005
 */
package com.thinkparity.model.parity.model.io.xml.project;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.model.io.xml.IXmlIOConstants;
import com.thinkparity.model.parity.model.io.xml.XmlIO;
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
		final File xmlFileDirectory = getXmlFileDirectory(project);
		logger.debug(xmlFileDirectory);
		final File xmlFile = getXmlFile(project);
		logger.debug(xmlFile);

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
		final File xmlFile = getRootProjectXmlFile();
		logger.debug(xmlFile);
		if(xmlFile.exists()) {
			final String xml = readXmlFile(xmlFile);
			final Project rootProject = (Project) XStreamUtil.fromXML(xml);
			Assert.assertNotNull("doesRootProjectExist()", rootProject);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
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
		final File xmlFile = getRootProjectXmlFile();
		logger.debug(xmlFile);
		final String xml = readXmlFile(xmlFile);
		logger.debug(xml);
		final Project rootProject = (Project) XStreamUtil.fromXML(xml);
		logger.debug(rootProject);
		return rootProject;
	}

	/**
	 * Obtain a list of root projects.
	 * 
	 * @return A list of the parent's child projects
	 * @throws ParityException
	 */
	public Collection<Project> list() throws FileNotFoundException, IOException {
		logger.info("list()");
		final File[] xmlFileDirectories = getRootXmlDirectory().listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		final Collection<Project> projects = new Vector<Project>(7);
		File xmlFile;
		for(File xmlFileDirectory : xmlFileDirectories) {
			xmlFile = getXmlFile(xmlFileDirectory);
			projects.add(fromXml(xmlFile));
		}
		return projects;
	}

	/**
	 * Obtain a list of projects for a given parent project.
	 * 
	 * @param parent
	 *            A parent project.
	 * @return A list of the parent's child projects
	 * @throws ParityException
	 */
	public Collection<Project> list(final Project parent)
			throws FileNotFoundException, IOException {
		logger.info("list(Project)");
		logger.debug(parent);
		final File[] xmlFileDirectories = getXmlFileDirectory(parent).listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		final Collection<Project> projects = new Vector<Project>(7);
		File xmlFile;
		for(File xmlFileDirectory : xmlFileDirectories) {
			xmlFile = getXmlFile(xmlFileDirectory);
			projects.add(fromXml(xmlFile));
		}
		return projects;
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
	 * Convert a project xml file into a project.
	 * 
	 * @param xmlFile
	 *            Read the xml file and stream it into a project.
	 * @return A project
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Project fromXml(final File xmlFile) throws FileNotFoundException,
			IOException {
		final String xml = readXmlFile(xmlFile);
		return (Project) XStreamUtil.fromXML(xml);
	}

	/**
	 * Obtain the xml file for the root project.
	 * 
	 * @return The xml file for the root project.
	 */
	private File getRootProjectXmlFile() {
		final File xmlFileDirectory =
			new File(getRootXmlDirectory(), IParityModelConstants.ROOT_PROJECT_NAME);
		return new File(xmlFileDirectory, IXmlIOConstants.XML_FILE_NAME_ROOT_PROJECT);
	}

	/**
	 * Obtain a list of project xml files for a given project xml file
	 * directory.
	 * 
	 * @param xmlFileDirectory
	 *            The project's xml file directory.
	 * @return The project xml files.
	 */
	private File getXmlFile(final File xmlFileDirectory) {
		final File[] xmlFiles = xmlFileDirectory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if(name.endsWith(IXmlIOConstants.FILE_EXTENSION_PROJECT)) {
					return true;
				}
				return false;
			}
			
		});
		Assert.assertTrue("getXmlFile(File)", xmlFiles.length == 1);
		return xmlFiles[0];
	}
}
