/*
 * Feb 18, 2005
 */
package com.thinkparity.model.parity.model.project;

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
	public void addCreationListener(final CreationListener listener) {
		synchronized(implLock) { impl.addCreationListener(listener); }
	}

	/**
	 * Add a project update event listener.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addUpdateListener(final UpdateListener listener) {
		synchronized(implLock) { impl.addUpdateListener(listener); }
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
	 * Obtain a project for a given meta data file.
	 * 
	 * @param name
	 *            The project's name.
	 * @param parent
	 *            The project's parent project.
	 * @return The project; or null if the project cannot be found.
	 * @throws ParityException
	 */
	public Project getProject(final String name, final Project parent)
			throws ParityException {
		synchronized(implLock) { return impl.getProject(name, parent); }
	}

	/**
	 * Obtain the root project.
	 * 
	 * @return The root project.
	 * @throws ParityException
	 */
	public Project getRootProject() throws ParityException {
		synchronized(implLock) { return impl.getRootProject(); }
	}

	/**
	 * Remove a project creation event listener.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removeCreationListener(final CreationListener listener) {
		synchronized(implLock) { impl.removeCreationListener(listener); }
	}

	/**
	 * Remove a project update event listener.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	public void removeUpdateListener(final UpdateListener listener) {
		synchronized(implLock) { impl.removeUpdateListener(listener); }
	}

	/**
	 * Update a project.
	 * 
	 * @param project
	 *            The project to update.
	 * @throws ParityException
	 */
	public void update(final Project project) throws ParityException {
		synchronized(implLock) { impl.update(project); }
	}
}
