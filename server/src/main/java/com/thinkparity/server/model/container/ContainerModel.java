/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.server.model.container;

import java.util.UUID;

import com.thinkparity.model.artifact.ArtifactType;

import com.thinkparity.server.model.AbstractModel;
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

    public void publishArtifact(final UUID uniqueId, final Long versionId,
            final Integer count, final Integer index,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final ArtifactType type, final byte[] bytes) {
        synchronized(getImplLock()) {
            getImpl().publishArtifact(uniqueId, versionId, count, index,
                    artifactUniqueId, artifactVersionId, type, bytes);
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
