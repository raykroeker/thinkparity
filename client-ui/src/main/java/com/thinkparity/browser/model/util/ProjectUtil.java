/*
 * Dec 20, 2005
 */
package com.thinkparity.browser.model.util;

import java.util.Collection;
import java.util.UUID;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProjectUtil {

	/**
	 * Singleton instance.
	 */
	private static final ProjectUtil singleton;

	/**
	 * Singleton synchronization lock.
	 */
	private static final Object singletonLock;

	static {
		singleton = new ProjectUtil();
		singletonLock = new Object();
	}

	/**
	 * Determine whether or not a child project with the given NAME exists.
	 * 
	 * @param NAME
	 *            The child NAME.
	 * @param projectId
	 *            The project.
	 * @return True if a child with the same NAME exists; false otherwise.
	 */
	public static Boolean doesChildProjectExist(final String name,
			final UUID projectId) throws ParityException {
		synchronized(singletonLock) {
			return singleton.doesChildProjectExistImpl(name, projectId);
		}
	}

	/**
	 * Handle to the project api.
	 */
	private final ProjectModel projectModel;

	/**
	 * Create a ProjectUtil.
	 */
	private ProjectUtil() {
		super();
		this.projectModel = ProjectModel.getModel();
	}

	/**
	 * Determine whether or not a child project with the given NAME exists.
	 * 
	 * @param NAME
	 *            The child NAME.
	 * @param projectId
	 *            The project.
	 * @return True if a child with the same NAME exists; false otherwise.
	 */
	public Boolean doesChildProjectExistImpl(final String name,
			final UUID projectId) throws ParityException {
		final Collection<Project> children = projectModel.list(projectId);
		for(Project child : children) {
			if(child.getName().equals(name)) { return Boolean.TRUE; }
		}
		return Boolean.FALSE;
	}
}
