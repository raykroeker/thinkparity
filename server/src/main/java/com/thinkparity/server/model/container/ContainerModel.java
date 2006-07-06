/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.server.model.container;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.session.Session;

/**
 * <b>Title:</b>thinkParity Container Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public class ContainerModel extends AbstractModel {

	/**
	 * Create a Container interface.
	 * 
	 * @return The Container interface.
	 */
	public static ContainerModel getModel(final Session session) {
		return new ContainerModel(session);
	}

	/** The model implementation. */
	private final ContainerModelImpl impl;

	/** The model implementation synchronization lock. */
	private final Object implLock;

	/**
	 * Create ContainerModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected ContainerModel(final Session session) {
		super();
		this.impl = new ContainerModelImpl(session);
		this.implLock = new Object();
	}

    /**
     * Reactivate a container.
     * 
     * @param uniqueId
     *            The container unique id.
     * @param versionId
     *            The container version id.
     * @param name
     *            The container name.
     * @param team
     *            The container team.
     * @param reactivatedBy
     *            Who reactivated the container.
     * @param reactivatedOn
     *            When the container was reactivated.
     * @throws ParityServerModelException
     */
	public void reactivate(final UUID uniqueId, final Long versionId,
            final String name, final List<JabberId> team,
            final JabberId reactivatedBy, final Calendar reactivatedOn)
            throws ParityServerModelException {
	    synchronized (implLock) {
            impl.reactivate(uniqueId, versionId, name, team, reactivatedBy,
                    reactivatedOn);
        }
    }

	/**
	 * Obtain the model implementation.
	 * 
	 * @return The model implementation.
	 */
	protected ContainerModelImpl getImpl() { return impl; }

	/**
	 * Obtain the model implementation synchronization lock.
	 * 
	 * @return The model implementation synchrnoization lock.
	 */
	protected Object getImplLock() { return implLock; }
}
