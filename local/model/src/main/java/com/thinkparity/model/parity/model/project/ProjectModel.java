/*
 * Feb 18, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.util.Collection;
import java.util.UUID;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.CreationListener;
import com.thinkparity.model.parity.api.events.UpdateListener;
import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * ProjectModel
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProjectModel extends AbstractModel {

	/**
	 * Obtain a handle to the project model.
	 * 
	 * @return A handle to the project model.
	 */
	public static ProjectModel getModel() {
		final ProjectModel projectModel = new ProjectModel();
		return projectModel;
	}

	/**
	 * Instance implementation.
	 * @see ProjectModel#implLock
	 */
	private final ProjectModelImpl impl;

	/**
	 * Synchronization lock for the implementation.
	 * @see ProjectModel#impl
	 */
	private final Object implLock;

	/**
	 * Handle to the current workspace.
	 */
	private final Workspace workspace;

	/**
	 * Create a ProjectModel [Singleton]
	 */
	private ProjectModel() {
		super();
		this.workspace = WorkspaceModel.getModel().getWorkspace();
		this.impl = new ProjectModelImpl(workspace);
		this.implLock = new Object();
	}

	/**
	 * Add a listener for project creation.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addListener(final CreationListener listener) {
		synchronized(implLock) { impl.addListener(listener); }
	}

	/**
	 * Add a project update event listener.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addListener(final UpdateListener listener) {
		synchronized(implLock) { impl.addListener(listener); }
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
	public Project create(final Project parent, final String name,
			final String description) throws ParityException {
		synchronized(implLock) {
			return impl.create(parent, name, description);
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
	public void delete(final Project project) throws ParityException {
		synchronized(implLock) { impl.delete(project); }
	}

	/**
	 * Obtain a project for a given id.
	 * 
	 * @param id
	 *            The project id.
	 * @return The project.
	 * @throws ParityException
	 */
	public Project get(final UUID id) throws ParityException {
		synchronized(implLock) { return impl.get(id); }
	}

	/**
	 * Obtain the inbox project.
	 * 
	 * @return The inbox project.
	 * @throws ParityException
	 */
	public Project getInbox() throws ParityException {
		synchronized(implLock) { return impl.getInbox(); }
	}

	/**
	 * Obtain the "My Projects" project. This is where the client should create
	 * their own projects and documents.
	 * 
	 * @return My project.
	 * @throws ParityException
	 */
	public Project getMyProjects() throws ParityException {
		synchronized(implLock) { return impl.getMyProjects(); }
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
	public Boolean hasChildren(final Project project) throws ParityException {
		synchronized(implLock) { return impl.hasChildren(project); }
	}

	/**
	 * Obtain a list of the root projects.
	 * 
	 * @return A list of the root projects.
	 * @throws ParityException
	 */
	public Collection<Project> list() throws ParityException {
		synchronized(implLock) { return impl.list(); }
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
			throws ParityException {
		synchronized(implLock) { return impl.list(parent); }
	}

	/**
	 * Remove a project creation event listener.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removeListener(final CreationListener listener) {
		synchronized(implLock) { impl.removeListener(listener); }
	}

	/**
	 * Remove a project update event listener.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removeListener(final UpdateListener listener) {
		synchronized(implLock) { impl.removeListener(listener); }
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
	public void rename(final UUID projectId, final String name)
			throws ParityException {
		synchronized(implLock) { impl.rename(projectId, name); }
	}

	/**
	 * Update a project.
	 * 
	 * @param project
	 *            The project.
	 * @throws ParityException
	 */
	public void update(final Project project) throws ParityException {
		synchronized(implLock) { impl.update(project); }
	}
}
