/*
 * Generated On: Sep 01 06 10:06:21 AM
 */
package com.thinkparity.ophelia.model.archive;

import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.model.artifact.Artifact;

import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.util.filter.Filter;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Archive Model<br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
public class ArchiveModel {

	/**
	 * Create a thinkParity Archive interface.
	 * 
	 * @param context
	 *            A thinkParity model context.
	 * @return A thinkParity Archive interface.
	 */
	public static InternalArchiveModel getInternalModel(final Context context,
            final Workspace workspace) {
		return new InternalArchiveModel(workspace, context);
	}

	/**
	 * Create a thinkParity Archive interface.
	 * 
	 * @return A thinkParity Archive interface.
	 */
	public static ArchiveModel getModel(final Workspace workspace) {
		return new ArchiveModel(workspace);
	}

	/** The model implementation. */
	private final ArchiveModelImpl impl;

	/** The model implementation synchronization lock. */
	private final Object implLock;

	/**
	 * Create ArchiveModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected ArchiveModel(final Workspace workspace) {
		super();
		this.impl = new ArchiveModelImpl(workspace);
		this.implLock = new Object();
	}

	public List<Artifact> read() {
        synchronized (getImplLock()) {
            return getImpl().read();
        }
    }

	public List<Artifact> read(final Comparator<Artifact> comparator) {
        synchronized (getImplLock()) {
            return getImpl().read(comparator);
        }
    }

    public List<Artifact> read(final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        synchronized (getImplLock()) {
            return getImpl().read(comparator, filter);
        }
    }

    public List<Artifact> read(final Filter<? super Artifact> filter) {
        synchronized (getImplLock()) {
            return getImpl().read(filter);
        }
    }

    /**
	 * Obtain the model implementation.
	 * 
	 * @return The model implementation.
	 */
	protected ArchiveModelImpl getImpl() { return impl; }

    /**
	 * Obtain the model implementation synchronization lock.
	 * 
	 * @return The model implementation synchrnoization lock.
	 */
	protected Object getImplLock() { return implLock; }
}
