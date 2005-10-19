/*
 * Feb 20, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.LoggerFormatter;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.IParityConstants;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.CreationEvent;
import com.thinkparity.model.parity.api.events.CreationListener;
import com.thinkparity.model.parity.api.events.UpdateEvent;
import com.thinkparity.model.parity.api.events.UpdateListener;
import com.thinkparity.model.parity.api.project.xml.ProjectXml;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.ParityUtil;
import com.thinkparity.model.parity.util.UUIDGenerator;

/**
 * ProjectApi_Impl
 * @author raykroeker@gmail.com
 * @version 1.3
 */
class ProjectModelImpl extends AbstractModelImpl implements IParityConstants {

	private class ProjectException extends Exception {
		private static final long serialVersionUID = -1;

		private ProjectException(Exception x) { super(x); }

		private ProjectException(String message) { super(message); }
	}

	/**
	 * Cached root project reference.
	 */
	private static Project cachedRootProject;

	/**
	 * List of listeners interested when a project has been created.
	 */
	private static final Collection<CreationListener> creationListeners =
		new Vector<CreationListener>(10);

	/**
	 * Handle to an internal logger.
	 */
	private static final Logger logger =
		ModelLoggerFactory.getLogger(ProjectModelImpl.class);

	/**
	 * Handle to a class used to handle output of specific classes.
	 */
	private static final LoggerFormatter loggerFormatter = new LoggerFormatter();

	/**
	 * List of listeners interested when a project has been updated.
	 */
	private static final Collection<UpdateListener> updateListeners =
		new Vector<UpdateListener>(10);

	/**
	 * Create a ProjectModelImpl.
	 * @deprecated
	 */
	ProjectModelImpl() { this(null); }

	/**
	 * Create a ProjectApi_Impl
	 */
	ProjectModelImpl(final Workspace workspace) { super(workspace); }

	void addCreationListener(final CreationListener creationListener) {
		if(null != creationListener)
			if(!creationListeners.contains(creationListener))
				creationListeners.add(creationListener);
	}

	void addUpdateListener(final UpdateListener updateListener) {
		if(null != updateListener)
			if(!updateListeners.contains(updateListener))
				updateListeners.add(updateListener);
	}

	Project createProject(final Project parent, final String name,
			final String description) throws ParityException {
		Assert.assertNotNull("Parent is null.", parent);
		Assert.assertNotNull("Name is null.", name);
		try {
			final File directory = buildDirectory(parent, name);
			final UUID projectId = UUIDGenerator.nextUUID();
			final Project project = new Project(parent, name, DateUtil.getInstance(),
					preferences.getUsername(), preferences.getUsername(),
					description, directory, projectId);
			createDirectory(project);
			createMetaDataDirectory(project);
			createMetaData(project);
			updateParentMetaData(parent, project);
			notifyCreation(project);
			return project;
		}
		catch(ProjectException px) {
			throw new ParityException(px);
		}
	}

	Project getProject(final File metaDataFile) throws ParityException {
		logger.debug(metaDataFile);
		if(!metaDataFile.exists()) { return null; }
		else {
			try { return ProjectXml.readXml(metaDataFile); }
			catch(IOException iox) {
				throw new ParityException("Could not read project.", iox);
			}
		}
	}

	Project getRootProject(final Workspace workspace) throws ParityException {
		if(null == ProjectModelImpl.cachedRootProject) {
			ProjectModelImpl.cachedRootProject = createRootProject(workspace);
		}
		return ProjectModelImpl.cachedRootProject;
	}

	void removeCreationListener(final CreationListener creationListener) {
		if(null != creationListener)
			if(creationListeners.contains(creationListener))
				creationListeners.remove(creationListener);
	}

	void removeUpdateListener(final UpdateListener updateListener) {
		if(null != updateListener)
			if(updateListeners.contains(updateListener))
				updateListeners.remove(updateListener);
	}

	void updateProject(final Project project) throws ParityException {
		logger.debug(project);
		try {
			updateMetaData(project);
			notifyUpdate(project);
		}
		catch(ProjectException px) { throw new ParityException(px); }
	}

	private File buildDirectory(final Project parent, final String name) {
		return new File(parent.getDirectory(), name);
	}

	private void createDirectory(final Project project) throws ProjectException {
		try {
			if(false == project.getDirectory().mkdir())
				throw new ProjectException("Could not create project directory.");
		}
		catch(Exception x) { throw new ProjectException(x); }
	}

	private void createMetaData(final Project project) throws ProjectException {
		try { ProjectXml.writeCreationXml(project); }
		catch(IOException iox) { throw new ProjectException(iox); }
	}

	private void createMetaDataDirectory(final Project project) throws ProjectException {
		final File metaDataDirectory = project.getMetaDataDirectory();
		if(metaDataDirectory.exists()) {
			if(0 == metaDataDirectory.list().length) {
				Assert.assertTrue("Could not delete empty project meta data directory:  " +
						metaDataDirectory.getAbsolutePath(),
						metaDataDirectory.delete());
			}
		}
		Assert.assertTrue("Could not create project meta data directory:  " +
				metaDataDirectory.getAbsolutePath(),
				FileUtil.createDirectory(metaDataDirectory));
	}

	private Project createRootProject(final Workspace workspace)
			throws ParityException {
		final File rootProjectMetaDataFile = new File(new File(workspace
				.getDataURL().getFile(), META_DATA_DIRECTORY_NAME),
				ROOT_PROJECT_META_DATA_FILE_NAME);
		Project rootProject = getProject(rootProjectMetaDataFile);
		if(null != rootProject) { return rootProject; } 
		else {
			try {
				final UUID rootProjectId = UUIDGenerator.nextUUID();
				rootProject = new Project(ROOT_PROJECT_NAME, DateUtil
						.getInstance(), ParityUtil.getSystemUsername(), ParityUtil
						.getSystemUsername(), ROOT_PROJECT_DESCRIPTION,
						new File(workspace.getDataURL().getFile()), rootProjectId);
				rootProject.setCustomName(ROOT_PROJECT_CUSTOM_NAME);
				createMetaDataDirectory(rootProject);
				createMetaData(rootProject);
				notifyCreation(rootProject);
				return getProject(rootProjectMetaDataFile);
			}
			catch(ProjectException px) { throw new ParityException(px); }
		}
	}

	private void notifyCreation(final Project project) {
		for(Iterator<CreationListener> iCreationListeners = creationListeners.iterator();
			iCreationListeners.hasNext();) {
			iCreationListeners.next().objectCreated(new CreationEvent(project));
		}
	}

	private void notifyUpdate(final Project project) {
		for(Iterator<UpdateListener> iUpdateListeners = updateListeners.iterator();
			iUpdateListeners.hasNext();) {
			iUpdateListeners.next().objectUpdated(new UpdateEvent(project));
		}
	}

	private void updateMetaData(final Project project) throws ParityException,
			ProjectException {
		try { ProjectXml.writeUpdateXml(project); }
		catch(IOException iox) {
			logger.error("Could not update project meta-data.", iox);
			throw new ProjectException("Could not update the project meta-data.");
		}
	}

	private void updateParentMetaData(final Project parent,
			final Project project) throws ParityException, ProjectException {
		parent.addProject(project);
		try { ProjectXml.writeUpdateXml(parent); }
		catch(IOException iox) {
			logger.error("Could not update the parent project meta-data.", iox);
			throw new ProjectException("Could not update the parent project meta-data.");
		}
	}
}
