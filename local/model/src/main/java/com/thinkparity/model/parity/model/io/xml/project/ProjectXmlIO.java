/*
 * Feb 20, 2005
 */
package com.thinkparity.model.parity.model.io.xml.project;

import java.io.File;
import java.io.IOException;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.model.io.xml.AbstractXmlIO;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xstream.XStreamUtil;

/**
 * ProjectXmlIO
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class ProjectXmlIO extends AbstractXmlIO {

	/**
	 * Create a ProjectXmlIO.
	 * 
	 * @param workspace
	 *            The parity workspace to use.
	 */
	public ProjectXmlIO(final Workspace workspace) { super(workspace); }

	/**
	 * Build an xml file reference for the project.
	 * 
	 * @param project
	 *            The project to build the xml file for.
	 * @return The xml file reference.
	 * @see AbstractXmlIO#getXmlFile(ParityObject)
	 */
	public File getXmlFile(final Project project) {
		return super.getXmlFile(project);
	}

	/**
	 * Read a project from a given xml file.
	 * 
	 * @param projectMetaDataFile
	 *            The project xml file.
	 * @return The project.
	 * @throws IOException
	 */
	public Project readXml(final File projectMetaDataFile)
			throws IOException {
		logger.info("readXml(File)");
		logger.debug(projectMetaDataFile);
		final Project project = (Project) XStreamUtil.read(projectMetaDataFile);
		logger.debug(project);
		return project;
	}

	/**
	 * Write creation xml for the given project.
	 * 
	 * @param project
	 *            The project to create.
	 * @throws IOException
	 */
	public void writeCreationXml(final Project project) throws IOException {
		logger.info("writeCreationXml(Project)");
		logger.debug(project);
		XStreamUtil.write(project);
	}

	/**
	 * Write update xml for the given project.
	 * 
	 * @param project
	 *            The project to update.
	 * @throws IOException
	 */
	public void writeUpdateXml(final Project project) throws IOException {
		logger.info("writeUpdateXml(Project)");
		logger.debug(project);
		final boolean didDelete = XStreamUtil.delete(project);
		if(false == didDelete)
			throw new IOException("Could not delete project xml.");
		XStreamUtil.write(project);
	}
}
