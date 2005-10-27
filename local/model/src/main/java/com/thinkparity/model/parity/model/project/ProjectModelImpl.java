/*
 * Feb 20, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.CreationEvent;
import com.thinkparity.model.parity.api.events.CreationListener;
import com.thinkparity.model.parity.api.events.UpdateEvent;
import com.thinkparity.model.parity.api.events.UpdateListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.io.xml.project.ProjectXmlIO;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.UUIDGenerator;

/**
 * ProjectApi_Impl
 * @author raykroeker@gmail.com
 * @version 1.3
 */
class ProjectModelImpl extends AbstractModelImpl {

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
	 * Project xml input\output;
	 */
	private final ProjectXmlIO projectXmlIO;

	/**
	 * Create a ProjectModelImpl.
	 * 
	 * @param workspace
	 *            The workspace reference to use.
	 */
	ProjectModelImpl(final Workspace workspace) {
		super(workspace);
		this.projectXmlIO = new ProjectXmlIO(workspace);
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
			// create the project
			final UUID id = UUIDGenerator.nextUUID();
			final Project project = new Project(parent, name,
					DateUtil.getInstance(), preferences.getUsername(),
					description, id);
			projectXmlIO.create(project);
			// update the parent
			parent.addProject(project);
			projectXmlIO.update(parent);

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
		projectXmlIO.delete(project);
	}

	/**
	 * Obtain the root project.
	 * 
	 * @return The root project.
	 * @throws ParityException
	 */
	Project getRootProject() throws ParityException {
		logger.info("getRootProject()");
		logger.debug(workspace);
		try {
			if(!projectXmlIO.doesRootProjectExist()) { createRootProject(); }
			final Project rootProject = projectXmlIO.getRootProject();
			Assert.assertNotNull("getRootProject()", rootProject);
			return rootProject;
		}
		catch(IOException iox) {
			logger.error("getRootProject()", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("getRootProject()", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain a list of the root projects.
	 * 
	 * @return A list of the root projects.
	 * @throws ParityException
	 */
	Collection<Project> list() throws ParityException {
		logger.info("list()");
		try { return projectXmlIO.list(); }
		catch(IOException iox) {
			logger.error("list()", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("list()", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain a list of projects for a given parent project.
	 * 
	 * @param parent
	 *            A parent project.
	 * @return A list of the parent's child projects
	 * @throws ParityException
	 */
	Collection<Project> list(final Project parent) throws ParityException {
		logger.info("list(Project)");
		logger.debug(parent);
		try { return projectXmlIO.list(parent); }
		catch(IOException iox) {
			logger.error("list(Project)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("list(Project)", rx);
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
			projectXmlIO.update(project);
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

	/**
	 * Create the root parity project.
	 * 
	 * @throws IOException
	 * @throws ParityException
	 */
	private void createRootProject() throws FileNotFoundException, IOException {
		final Preferences preferences = workspace.getPreferences();
		final String systemUsername = preferences.getSystemUsername();
		final UUID rootProjectId = UUIDGenerator.nextUUID();
		final Project root = new Project(IParityModelConstants.ROOT_PROJECT_NAME,
				DateUtil.getInstance(), systemUsername,
				IParityModelConstants.ROOT_PROJECT_DESCRIPTION, rootProjectId);
		root.setCustomName(IParityModelConstants.ROOT_PROJECT_CUSTOM_NAME);
		projectXmlIO.create(root);
		notifyCreation(root);
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
}
