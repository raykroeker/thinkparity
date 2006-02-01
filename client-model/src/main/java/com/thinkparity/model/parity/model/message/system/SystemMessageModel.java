/*
 * Jan 31, 2006
 */
package com.thinkparity.model.parity.model.message.system;

import java.util.Collection;
import java.util.Comparator;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.SystemMessageListener;
import com.thinkparity.model.parity.model.AbstractModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessageModel extends AbstractModel {

	/**
	 * Obtain a <code>SystemMessageModel</code>
	 * 
	 * @return A SystemMessageModel.
	 */
	public static SystemMessageModel getModel() {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		final SystemMessageModel systemMessageModel = new SystemMessageModel(workspace);
		return systemMessageModel;
	}

	/**
	 * Internal implementation.
	 * 
	 */
	private final SystemMessageModelImpl impl;

	/**
	 * Syncrhonization lock.
	 * 
	 */
	private final Object implLock;

	/**
	 * Create a SystemMessageModel.
	 * 
	 */
	private SystemMessageModel(final Workspace workspace) {
		super();
		this.impl = new SystemMessageModelImpl(workspace);
		this.implLock = new Object();
	}

	public void addListener(final SystemMessageListener listener) {
		synchronized(implLock) { impl.addListener(listener); }
	}
	/**
	 * Obtain a system message.
	 * 
	 * @param systemMessageId
	 *            The system message id.
	 * @return The system message.
	 * @throws ParityException
	 */
	public SystemMessage get(final SystemMessageId systemMessageId)
			throws ParityException {
		synchronized(implLock) { return impl.get(systemMessageId); }
	}

	/**
	 * Obtain a list of system messages.
	 * 
	 * @return A list of system messages.
	 * @throws ParityException
	 */
	public Collection<SystemMessage> list() throws ParityException {
		synchronized(implLock) { return impl.list(); }
	}

	/**
	 * Obtain a list of system messages ordered according to the comparator.
	 * 
	 * @param comparator
	 *            The system message comparator.
	 * @return A list of system messages.
	 * @throws ParityException
	 */
	public Collection<SystemMessage> list(
			final Comparator<SystemMessage> comparator) throws ParityException {
		synchronized(implLock) { return impl.list(comparator); }
	}

	/**
	 * Remove a system message listener.
	 * 
	 * @param listener
	 *            The system message listener to remove.
	 */
	public void removeListener(final SystemMessageListener listener) {
		synchronized(implLock) { impl.removeListener(listener); }
	}
}
