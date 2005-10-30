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

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.io.xml.IXmlIOConstants;
import com.thinkparity.model.parity.model.io.xml.XmlIO;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.workspace.Workspace;

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
		// create the xml file directory
		Assert.assertTrue(
				"create(Project)",
				getXmlFileDirectory(project).mkdir());
		write(project, getXmlFile(project));
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
		Assert.assertTrue("delete(Project)", getXmlFile(project).delete());
		// delete the xml file directory
		Assert.assertTrue("delete(Project)", getXmlFileDirectory(project).delete());
	}

	/**
	 * Obtain a list of root projects.
	 * 
	 * @return A list of projects.
	 * @throws ParityException
	 */
	public Collection<Project> list() throws FileNotFoundException, IOException {
		logger.info("list()");
		return list(null, getXmlFileDirectories());
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
		return list(parent, getXmlFileDirectories(parent));
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
		write(project, getXmlFile(project));
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

	/**
	 * Obtain a list of xml file directories for the root of the xml io
	 * filesystem.
	 * 
	 * @return A list of xml file directories.
	 */
	private File[] getXmlFileDirectories() {
		return getRoot().listFiles(new FileFilter() {
			public boolean accept(final File pathname) {
				return pathname.isDirectory();
			}
		});
	}

	/**
	 * Obtain a list of xml file directories for the parent.
	 * 
	 * @param parent
	 *            The parent project.
	 * @return A list of xml file directories.
	 */
	private File[] getXmlFileDirectories(final Project parent) {
		return getXmlFileDirectory(parent).listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
	}

	/**
	 * Obtain a list of projects within a list of project xml file directories.
	 * 
	 * @param parent
	 *            The parent project (Optional).
	 * @param xmlFileDirectories
	 *            The project xml file directories.
	 * @return A list of projects.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Collection<Project> list(final Project parent,
			final File[] xmlFileDirectories) throws FileNotFoundException,
			IOException {
		final Collection<Project> projects = new Vector<Project>(7);
		File xmlFile;
		Project project;
		for(File xmlFileDirectory : xmlFileDirectories) {
			xmlFile = getXmlFile(xmlFileDirectory);
			project = readProject(xmlFile);
			project.setParent(parent);
			projects.add(project);
		}
		return projects;
	}
}
