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

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.IParityConstants;
import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.CreationEvent;
import com.thinkparity.model.parity.api.events.CreationListener;
import com.thinkparity.model.parity.api.events.UpdateEvent;
import com.thinkparity.model.parity.api.events.UpdateListener;
import com.thinkparity.model.parity.api.project.xml.ProjectXml;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.UUIDGenerator;

/**
 * ProjectApi_Impl
 * @author raykroeker@gmail.com
 * @version 1.3
 */
class ProjectModelImpl extends AbstractModelImpl implements IParityConstants {

	/**
	 * Cached root project reference.
	 */
	private static Project cachedRootProject;

	/**
	 * Project creation event listeners.
	 * @see ProjectModelImpl#creationListenersLock
	 */
	private static final Collection<CreationListener> creationListeners;

	/**
	 * Synchronization lock for creation listeners.
	 * @see ProjectModelImpl#creationListeners
	 */
	private static final Object creationListenersLock;

	/**
	 * Project update event listeners.
	 * @see ProjectModelImpl#updateListenersLock
	 */
	private static final Collection<UpdateListener> updateListeners;

	/**
	 * Synchronization lock for the update listener list.
	 * @see ProjectModelImpl#updateListeners
	 */
	private static final Object updateListenersLock;

	static {
		// initialize creation listeners
		creationListeners = new Vector<CreationListener>(7);
		creationListenersLock = new Object();
		// initialize update listeners
		updateListeners = new Vector<UpdateListener>(7);
		updateListenersLock = new Object();
	}

	/**
	 * Create a ProjectModelImpl.
	 * 
	 * @param workspace
	 *            The workspace reference to use.
	 */
	ProjectModelImpl(final Workspace workspace) {
		super(workspace);
	}

	/**
	 * Add a listener for project creation.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	void addCreationListener(final CreationListener listener) {
		logger.info("addCreationListener(CreationListener)");
		logger.debug(listener);
		Assert.assertNotNull("addCreationListener(CreationListener)", listener);
		synchronized (ProjectModelImpl.creationListenersLock) {
			Assert.assertNotTrue(
					"addCreationListener(CreationListener)",
					ProjectModelImpl.creationListeners.contains(listener));
			ProjectModelImpl.creationListeners.add(listener);
		}
	}

	/**
	 * Add a project update event listener.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	void addUpdateListener(final UpdateListener listener) {
		logger.info("addUpdateListener(UpdateListener)");
		logger.debug(listener);
		Assert.assertNotNull("addUpdateListener(UpdateListener)", listener);
		synchronized (ProjectModelImpl.updateListenersLock) {
			Assert.assertNotTrue("Update listener already registered.",
					ProjectModelImpl.updateListeners.contains(listener));
			ProjectModelImpl.updateListeners.add(listener);
		}
	}

	/**
	 * Create a new project.
	 * 
	 * @param parent
	 *            The parent project.
	 * @param name
	 *            The name.
	 * @param description
	 *            The description.
	 * @return The new project.
	 * @throws ParityException
	 */
	Project create(final Project parent, final String name,
			final String description) throws ParityException {
		logger.info("create(Project,String,String)");
		logger.debug(parent);
		logger.debug(name);
		logger.debug(description);
		Assert.assertNotNull("create(Project,String,String)", parent);
		Assert.assertNotNull("create(Project,String,String)", name);
		try {
			final File directory = buildDirectory(parent, name);
			final UUID projectId = UUIDGenerator.nextUUID();
			final Project project = new Project(parent, name, DateUtil.getInstance(),
					preferences.getUsername(), preferences.getUsername(),
					description, directory, projectId);
			createDirectory(project.getDirectory());
			createMetaDataDirectory(project);
			createMetaData(project);
			updateParentMetaData(parent, project);
			notifyCreation(project);
			return project;
		}
		catch(IOException iox) {
			logger.error("create(Project,String,String)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("create(Project,String,String)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Delete an existing project. Note that this will delete any sub-projects
	 * and\or sub-documents in the project without warning.
	 * 
	 * @param project
	 *            The project to delete.
	 * @throws ParityException
	 */
	void delete(final Project project) throws ParityException {
		logger.info("delete(Project)");
		logger.debug(project);
		
		final DocumentModel documentModel = DocumentModel.getModel();
		for(Document subDocument : project.getDocuments()) {
			documentModel.delete(subDocument);
		}
		for(Project subProject : project.getProjects()) {
			delete(subProject);
		}
		deleteFile(project.getMetaDataFile());
		deleteTree(project.getDirectory());
	}

	/**
	 * Delete a file.
	 * 
	 * @param file
	 *            The file to delete.
	 */
	private void deleteFile(final File file) {
		Assert.assertTrue("Could not delete file.", file.delete());
	}

	/**
	 * Delete a directory tree beneath.
	 * 
	 * @param directory
	 *            The directory below which to delete all files\directories.
	 */
	private void deleteTree(final File directory) {
		logger.warn("deleteTree(File):  " + directory.getAbsolutePath());
		FileUtil.deleteTree(directory);
	}

	/**
	 * Obtain a project for a given meta data file.
	 * 
	 * @param metaDataFile
	 *            The project's meta data file.
	 * @return The project.
	 * @throws ParityException
	 */
	Project getProject(final File metaDataFile) throws ParityException {
		logger.info("getProject(File)");
		logger.debug(metaDataFile);
		try {
			if(!metaDataFile.exists()) { return null; }
			else { return ProjectXml.readXml(metaDataFile); }
		}
		catch(IOException iox) {
			logger.error("getProject(File)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
	}

	/**
	 * Obtain the root project.
	 * 
	 * @return The root project.
	 * @throws ParityException
	 */
	Project getRootProject(final Workspace workspace) throws ParityException {
		logger.info("getRootProject(Workspace)");
		logger.debug(workspace);
		try {
			if(null == ProjectModelImpl.cachedRootProject) {
				ProjectModelImpl.cachedRootProject = createRootProject(workspace);
			}
			return ProjectModelImpl.cachedRootProject;
		}
		catch(IOException iox) {
			logger.error("getRootProject(Workspace)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("getRootProject(Workspace)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Remove a project creation event listener.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	void removeCreationListener(final CreationListener listener) {
		logger.info("removeCreationListener(CreationListener)");
		logger.debug(listener);
		Assert.assertNotNull(
				"removeCreationListener(CreationListener)", listener);
		synchronized(ProjectModelImpl.creationListenersLock) {
			Assert.assertTrue(
					"removeCreationListener(CreationListener)",
					ProjectModelImpl.creationListeners.contains(listener));
			ProjectModelImpl.creationListeners.remove(listener);
		}
	}

	/**
	 * Remove a project update event listener.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	void removeUpdateListener(final UpdateListener listener) {
		logger.info("removeUpdateListener(UpdateListener)");
		logger.debug(listener);
		Assert.assertNotNull("removeUpdateListener(UpdateListener)", listener);
		synchronized (ProjectModelImpl.updateListenersLock) {
			Assert.assertTrue(
					"removeUpdateListener(UpdateListener)",
					ProjectModelImpl.updateListeners.contains(listener));
			ProjectModelImpl.updateListeners.remove(listener);
		}
	}

	/**
	 * Update a project.
	 * 
	 * @param project
	 *            The project to update.
	 * @throws ParityException
	 */
	void update(final Project project) throws ParityException {
		logger.info("update(Project)");
		logger.debug(project);
		try {
			updateMetaData(project);
			notifyUpdate(project);
		}
		catch(IOException iox) {
			logger.error("update(Project)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("update(Project)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	private File buildDirectory(final Project parent, final String name) {
		return new File(parent.getDirectory(), name);
	}

	private void createDirectory(final File directory) {
		Assert.assertTrue("createDirectory(File)", directory.mkdir());
	}

	private void createMetaData(final Project project) throws IOException {
		ProjectXml.writeCreationXml(project);
	}

	private void createMetaDataDirectory(final Project project) {
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
			throws IOException, ParityException {
		final File rootProjectMetaDataFile = new File(new File(workspace
				.getDataURL().getFile(), META_DATA_DIRECTORY_NAME),
				ROOT_PROJECT_META_DATA_FILE_NAME);
		Project rootProject = getProject(rootProjectMetaDataFile);
		if(null != rootProject) { return rootProject; } 
		else {
			final Preferences preferences = workspace.getPreferences();
			final String systemUsername = preferences.getSystemUsername();
			final UUID rootProjectId = UUIDGenerator.nextUUID();
			rootProject = new Project(ROOT_PROJECT_NAME,
					DateUtil.getInstance(), systemUsername, systemUsername,
					ROOT_PROJECT_DESCRIPTION, new File(workspace.getDataURL()
							.getFile()), rootProjectId);
			rootProject.setCustomName(ROOT_PROJECT_CUSTOM_NAME);
			createMetaDataDirectory(rootProject);
			createMetaData(rootProject);
			notifyCreation(rootProject);
			return getProject(rootProjectMetaDataFile);
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

	private void updateMetaData(final Project project) throws IOException {
		ProjectXml.writeUpdateXml(project);
	}

	private void updateParentMetaData(final Project parent,
			final Project project) throws IOException {
		parent.addProject(project);
		ProjectXml.writeUpdateXml(parent);
	}
}
