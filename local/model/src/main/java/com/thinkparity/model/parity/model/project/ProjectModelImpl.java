/*
 * Feb 20, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.ParityObjectFlag;
import com.thinkparity.model.parity.api.events.CreationEvent;
import com.thinkparity.model.parity.api.events.CreationListener;
import com.thinkparity.model.parity.api.events.DeleteEvent;
import com.thinkparity.model.parity.api.events.UpdateEvent;
import com.thinkparity.model.parity.api.events.UpdateListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.artifact.ArtifactSorter;
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
	 * @param projectId
	 *            The project unique id.
	 * @param name
	 *            The project name.
	 * @param description
	 *            The project description.
	 * @return The project.
	 * @throws ParityException
	 */
	Project create(final UUID projectId, final String name,
			final String description) throws ParityException {
		logger.info("create(UUID,String,String)");
		logger.debug(projectId);
		logger.debug(name);
		logger.debug(description);
		assertCanCreateArtifacts();
		try {
			// create the project
			final Calendar now = DateUtil.getInstance();
			final Project project = new Project(preferences.getUsername(), now,
					description, NO_FLAGS, UUIDGenerator.nextUUID(), name,
					projectId, preferences.getUsername(), now);

			// create the project
			projectXmlIO.create(project);

			// flag the project as having been seen.
			flagAsSEEN(project);

			// fire a creation event
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
	void delete(final UUID projectId) throws ParityException {
		logger.info("delete(UUID)");
		logger.debug(projectId);
		try {
			// delete sub documents
			final DocumentModel documentModel = getDocumentModel();
			for(Document subDocument : documentModel.list(projectId)) {
				documentModel.delete(subDocument);
			}
			// delete sub project
			for(Project subProject : list(projectId)) {
				delete(subProject.getId());
			}

			// delete the project
			final Project project = get(projectId);
			projectXmlIO.delete(project);

			// fire an object deleted notification
			notifyUpdate_objectDeleted(project);
		}
		catch(IOException iox) {
			logger.error("delete(Project)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("delete(Project)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain a project for a given id.
	 * 
	 * @param id
	 *            The project id.
	 * @return The project.
	 * @throws ParityException
	 */
	Project get(final UUID id) throws ParityException {
		logger.info("get(UUID)");
		logger.debug(id);
		try { return projectXmlIO.get(id); }
		catch(IOException iox) {
			logger.error("get(UUID)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("get(UUID)", rx);
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
		logger.info("getMyProjects()");
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
	 * Determine whether a project has children or not.
	 * 
	 * @param projectId
	 *            The project unique id.
	 * @return True if the project contains any projects or documents; false
	 *         otherwise.
	 * @throws ParityException
	 */
	Boolean hasChildren(final UUID projectId) throws ParityException {
		logger.info("hasChildren(UUID)");
		logger.debug(projectId);
		try {
			final Project project = get(projectId);
			return projectXmlIO.hasChildren(project);
		}
		catch(IOException iox) {
			logger.error("hasChildren(Project)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("hasChildren(Project)", rx);
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
		try {
			final Collection<Project> list = new Vector<Project>(2);
			list.add(getInbox());
			list.add(getMyProjects());
			return list; 
		}
		catch(RuntimeException rx) {
			logger.error("list()", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain a list of projects for a given parent project.
	 * 
	 * @param projectId
	 *            The parent project unique id.
	 * @return A list of the parent's child projects
	 * @throws ParityException
	 */
	Collection<Project> list(final UUID projectId) throws ParityException {
		logger.info("list(Project)");
		logger.debug(projectId);
		try {
			final Project project = get(projectId);
			final Collection<Project> projects = projectXmlIO.list(project);
			ArtifactSorter.sortProjectsByName(projects);
			return projects;
		}
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
	 * Rename a project.
	 * 
	 * @param projectId
	 *            The project unique id.
	 * @param name
	 *            The new name.
	 * @throws ParityException
	 */
	void rename(final UUID projectId, final String name) throws ParityException {
		logger.info("rename(UUID,String)");
		logger.debug(projectId);
		logger.debug(name);
		if(null == name) { throw new IllegalArgumentException(); }
		try {
			final Project project = get(projectId);
			final DocumentModel documentModel = getDocumentModel();
			final Collection<Document> documents =
				documentModel.list(projectId);
			// rename the project
			project.setName(name);
			projectXmlIO.rename(project, documents);

			// fire an update event
			notifyUpdate_objectUpdated(project);
		}
		catch(IOException iox) {
			logger.error("rename(UUID,String)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("rename(UUID,String)", rx);
			throw ParityErrorTranslator.translate(rx);
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
		final String systemUsername = preferences.getSystemUsername();
		final Project inbox = new Project(
				systemUsername,
				IParityModelConstants.PROJECT_CREATED_ON_INBOX,
				IParityModelConstants.PROJECT_DESCRIPTION_INBOX,
				NO_FLAGS,
				IParityModelConstants.PROJECT_ID_INBOX,
				IParityModelConstants.PROJECT_NAME_INBOX,
				null,
				systemUsername,
				IParityModelConstants.PROJECT_UPDATED_ON_MYPROJECTS);
		inbox.add(ParityObjectFlag.SEEN);
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
		final String systemUsername = preferences.getSystemUsername();
		final Project myProjects = new Project(
				systemUsername,
				IParityModelConstants.PROJECT_CREATED_ON_MYPROJECTS,
				IParityModelConstants.PROJECT_DESCRIPTION_MYPROJECTS,
				NO_FLAGS,
				IParityModelConstants.PROJECT_ID_MYPROJECTS,
				IParityModelConstants.PROJECT_NAME_MYPROJECTS,
				null,
				systemUsername,
				IParityModelConstants.PROJECT_UPDATED_ON_MYPROJECTS);
		myProjects.add(ParityObjectFlag.SEEN);
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
}
