/*
 * Feb 20, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.CreationEvent;
import com.thinkparity.model.parity.api.events.CreationListener;
import com.thinkparity.model.parity.api.events.DeleteEvent;
import com.thinkparity.model.parity.api.events.UpdateEvent;
import com.thinkparity.model.parity.api.events.UpdateListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.io.xml.project.ProjectXmlIO;
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
	void addListener(final CreationListener listener) {
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
	void addListener(final UpdateListener listener) {
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
	 *            The parent.
	 * @param name
	 *            The project name.
	 * @param description
	 *            The project description.
	 * @return The project.
	 * @throws ParityException
	 */
	Project create(final Project parent, final String name,
			final String description) throws ParityException {
		logger.info("create(Project,String,String)");
		logger.debug(parent);
		logger.debug(name);
		logger.debug(description);
		try {
			// create the project
			final UUID id = UUIDGenerator.nextUUID();
			final Project project = new Project(parent, name,
					DateUtil.getInstance(), preferences.getUsername(),
					description, id);
			projectXmlIO.create(project);
			notifyCreation_objectCreated(project);
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
		try {
			final DocumentModel documentModel = DocumentModel.getModel();
			for(Document subDocument : documentModel.list(project)) {
				documentModel.delete(subDocument);
			}
			for(Project subProject : list(project)) { delete(subProject); }
			projectXmlIO.delete(project);
			notifyUpdate_objectDeleted(project);
		}
		catch(RuntimeException rx) {
			logger.error("delete(Project)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain the inbox project.
	 * 
	 * @return The inbox project.
	 * @throws ParityException
	 */
	Project getInbox() throws ParityException {
		logger.info("getInbox()");
		try {
			for(Project project : projectXmlIO.list()) {
				if(project.getName().equals(IParityModelConstants.PROJECT_NAME_INBOX)) {
					return project;
				}
			}
			return lazyCreateInbox();
		}
		catch(IOException iox) {
			logger.error("getInbox()", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("getInbox()", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	Project getMyProjects() throws ParityException {
		logger.info("getMyProject()");
		try {
			for(Project project : projectXmlIO.list()) {
				if(project.getName().equals(IParityModelConstants.PROJECT_NAME_MYPROJECTS)) {
					return project;
				}
			}
			return lazyCreateMyProjects();
		}
		catch(IOException iox) {
			logger.error("getMyProject()", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("getMyProject()", rx);
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
	void removeListener(final CreationListener listener) {
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
	void removeListener(final UpdateListener listener) {
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
			notifyUpdate_objectUpdated(project);
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
	 * Create the inbox project.
	 * 
	 * @return The inbox project.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Project lazyCreateInbox() throws FileNotFoundException, IOException {
		final Project inbox = new Project(
				IParityModelConstants.PROJECT_NAME_INBOX,
				IParityModelConstants.PROJECT_CREATED_ON_INBOX,
				IParityModelConstants.PROJECT_CREATED_BY_INBOX,
				IParityModelConstants.PROJECT_DESCRIPTION_INBOX,
				IParityModelConstants.PROJECT_ID_INBOX);
		projectXmlIO.create(inbox);
		return inbox;
	}

	/**
	 * Create the my projects project.
	 * 
	 * @return The my projects project.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Project lazyCreateMyProjects() throws FileNotFoundException,
			IOException {
		final Project myProjects = new Project(
				IParityModelConstants.PROJECT_NAME_MYPROJECTS,
				IParityModelConstants.PROJECT_CREATED_ON_MYPROJECTS,
				IParityModelConstants.PROJECT_CREATED_BY_MYPROJECTS,
				IParityModelConstants.PROJECT_DESCRIPTION_MYPROJECTS,
				IParityModelConstants.PROJECT_ID_MYPROJECTS);
		projectXmlIO.create(myProjects);
		return myProjects;
	}

	/**
	 * Fire the object created event for all of the creation listeners.
	 * 
	 * @param project
	 *            The project that was just created.
	 */
	private void notifyCreation_objectCreated(final Project project) {
		synchronized(ProjectModelImpl.creationListenersLock) {
			for(CreationListener listener : ProjectModelImpl.creationListeners) {
				listener.objectCreated(new CreationEvent(project));
			}
		}
	}

	/**
	 * Fire the object deleted event for all of the deletion listeners.
	 * 
	 * @param project
	 *            The project that was deleted.
	 */
	private void notifyUpdate_objectDeleted(final Project project) {
		synchronized(ProjectModelImpl.updateListenersLock) {
			for(UpdateListener listener : ProjectModelImpl.updateListeners) {
				listener.objectDeleted(new DeleteEvent(project));
			}
		}
	}

	/**
	 * Fire the object updated event for all of the update listeners.
	 * 
	 * @param project
	 *            The project that was updated.
	 */
	private void notifyUpdate_objectUpdated(final Project project) {
		synchronized(ProjectModelImpl.updateListenersLock) {
			for(UpdateListener listener : ProjectModelImpl.updateListeners) {
				listener.objectUpdated(new UpdateEvent(project));
			}
		}
	}

	/**
	 * Determine whether a project has children or not.
	 * 
	 * @param project
	 *            The project to check.
	 * @return True if the project contains any projects or documents; false
	 *         otherwise.
	 * @throws ParityException
	 */
	Boolean hasChildren(final Project project) throws ParityException {
		logger.info("hasChildren(Project)");
		logger.debug(project);
		try {
			return projectXmlIO.hasChildren(project);
		}
		catch(RuntimeException rx) {
			logger.error("hasChildren(Project)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}
}
