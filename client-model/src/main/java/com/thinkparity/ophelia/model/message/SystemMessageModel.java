/*
 * Jan 31, 2006
 */
package com.thinkparity.ophelia.model.message;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;


import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.events.SystemMessageListener;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessageModel extends AbstractModel {

	/**
	 * Obtain an internal system message model.
	 * 
	 * @param context
	 *            The parity context.
	 * @return The internal system message model.
	 */
	public static InternalSystemMessageModel getInternalModel(
			final Context context) {
		final Workspace workspace = getWorkspaceModel().getWorkspace();
		return new InternalSystemMessageModel(workspace, context);
	}

	/**
	 * Obtain a <code>SystemMessageModel</code>
	 * 
	 * @return A SystemMessageModel.
	 */
	public static SystemMessageModel getModel() {
		final Workspace workspace = getWorkspaceModel().getWorkspace();
		return new SystemMessageModel(workspace);
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
	protected SystemMessageModel(final Workspace workspace) {
		super();
		this.impl = new SystemMessageModelImpl(workspace);
		this.implLock = new Object();
	}

	/**
	 * Add a system message listener.
	 * 
	 * @param listener
	 *            The system message listener.
	 */
	public void addListener(final SystemMessageListener listener) {
		synchronized(implLock) { impl.addListener(listener); }
	}

	/**
     * @deprecated => No replacement.
     */
    @Deprecated
	public void delete(final Long systemMessageId) {
		throw Assert.createUnreachable("SystemMessageModel#delete(Long) => No replacement.");
	}

	/**
	 * Read all system messages.
	 * 
	 * @return A list of system messages.
	 * @throws ParityException
	 */
	public Collection<SystemMessage> read() throws ParityException {
		synchronized(implLock) { return impl.read(); }
	}

	/**
	 * Obtain a list of system messages ordered according to the comparator.
	 * 
	 * @param comparator
	 *            The system message comparator.
	 * @return A list of system messages.
	 * @throws ParityException
	 */
	public Collection<SystemMessage> read(
			final Comparator<SystemMessage> comparator) throws ParityException {
		synchronized(implLock) { return impl.read(comparator); }
	}

	/**
	 * Read a system message.
	 * 
	 * @param systemMessageId
	 *            The system message id.
	 * @return The system message.
	 * @throws ParityException
	 */
	public SystemMessage read(final Long systemMessageId) {
		synchronized(implLock) { return impl.read(systemMessageId); }
	}

	/**
	 * Read all system messaages not related to artifacts.
	 * 
	 * @return A list of system messages.
	 * @throws ParityException
	 * 
	 * @deprecated Should use a generic filter instead.
	 */
	public List<SystemMessage> readForNonArtifacts() throws ParityException {
		synchronized(implLock) { return impl.readForNonArtifacts(); }
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

	/**
	 * Obtain the implementation.
	 * 
	 * @return The implementation.
	 */
	protected SystemMessageModelImpl getImpl() { return impl; }

	/**
	 * Obtain the implementation lock.
	 * 
	 * @return The implementation lock.
	 */
	protected Object getImplLock() { return implLock; }
}
