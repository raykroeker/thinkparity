/*
 * Feb 18, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.io.File;

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
	 * Project api implementation.
	 */
	private static final ProjectModelImpl impl = new ProjectModelImpl();

	public static void addCreationListener(
			final CreationListener creationListener) {
		impl.addCreationListener(creationListener);
	}

	public static void addUpdateListener(final UpdateListener updateListener) {
		impl.addUpdateListener(updateListener);
	}

	/**
	 * Obtain a handle to the project model.
	 * 
	 * @return A handle to the project model.
	 */
	public static ProjectModel getModel() {
		final ProjectModel projectModel = new ProjectModel();
		return projectModel;
	}

	public static Project getProject(final File projectMetaDataFile)
			throws ParityException {
		return impl.getProject(projectMetaDataFile);
	}

	public static void removeCreationListener(
			final CreationListener creationListener) {
		impl.removeCreationListener(creationListener);
	}

	public static void removeUpdateListener(final UpdateListener updateListener) {
		impl.removeUpdateListener(updateListener);
	}

	public static void updateProject(final Project project)
			throws ParityException {
		impl.updateProject(project);
	}

	/**
	 * Instance implementation.
	 * @see ProjectModel#impl2Lock
	 */
	private final ProjectModelImpl impl2;

	/**
	 * Synchronization lock for the implementation.
	 * @see ProjectModel#impl2
	 */
	private final Object impl2Lock;

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
		this.impl2 = new ProjectModelImpl(workspace);
		this.impl2Lock = new Object();
	}

	public Project createProject(final Project parent, final String name,
			final String description) throws ParityException {
		return impl2.createProject(parent, name, description);
	}

	public Project getRootProject(final Workspace workspace)
			throws ParityException {
		synchronized(impl2Lock) { return impl2.getRootProject(workspace); }
	}
}
