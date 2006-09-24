/*
 * Mar 2, 2006
 */
package com.thinkparity.ophelia.model.artifact;

import java.util.List;
import java.util.Set;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactModel extends AbstractModel<ArtifactModelImpl> {

	/**
	 * Obtain an internal artifact model.
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @param context
	 *            The parity context.
	 * @return An internal artifact model.
	 */
	public static InternalArtifactModel getInternalModel(final Context context,
            final Workspace workspace) {
		return new InternalArtifactModel(context, workspace);
	}

	/**
	 * Obtain an artifact model.
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @return An artifact model.
	 */
	public static ArtifactModel getModel(final Workspace workspace) {
		return new ArtifactModel(workspace);
	}

	/**
	 * Create a ArtifactModel.
	 * 
	 */
	protected ArtifactModel(final Workspace workspace) {
		super(new ArtifactModelImpl(workspace));
	}

	/**
	 * Apply the seen flag to the artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	public void applyFlagSeen(final Long artifactId) {
		synchronized(getImplLock()) { getImpl().applyFlagSeen(artifactId); }
	}

	/**
	 * Determine whether or not the artifact has been seen.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return True if the artifact has been seen; false otherwise.
	 */
	public Boolean hasBeenSeen(final Long artifactId) {
		synchronized(getImplLock()) { return getImpl().hasBeenSeen(artifactId); }
	}

	/**
	 * Determine whether or not an artifact has a flag applied.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param flag
	 *            The artifact flag.
	 * @return True if the flag is applied; false otherwise.
	 */
	public Boolean isFlagApplied(final Long artifactId, final ArtifactFlag flag) {
		synchronized(getImplLock()) { return getImpl().isFlagApplied(artifactId, flag); }
	}

    /**
     * Read the artifact key holder.
     * 
     * @param artifactId
     *            The artifact id.
     * @return The artifact key holder.
     */
    public JabberId readKeyHolder(final Long artifactId) {
        synchronized (getImplLock()) {
            return getImpl().readKeyHolder(artifactId);
        }
    }

	/**
     * @deprecated =>
     *             {@link com.thinkparity.ophelia.model.container.ContainerModel#readKeyRequests(Long)}
     */
    @Deprecated
	public List<KeyRequest> readKeyRequests(final Long documentId)
			throws ParityException {
		throw Assert.createUnreachable("ArtifactModel#readKeyRequests(Long) => ContainerModel#readKeyRequests(Long)");
	}

    /**
     * Read the team for the artifact.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The artifact team.
     */
    public Set<User> readTeam(final Long artifactId) {
        synchronized(getImplLock()) { return getImpl().readTeam(artifactId); }
    }

	/**
     * Read the artifact's type.
     * 
     * @param artifactId
     *            An artifact id.
     * @return An <code>ArtifactType</code>.
     */
    public ArtifactType readType(final Long artifactId) {
        synchronized (getImplLock()) {
            return getImpl().readType(artifactId);
        }
    }

    /**
	 * Remove the seen flag from the artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	public void removeFlagSeen(final Long artifactId) {
		synchronized(getImplLock()) { getImpl().removeFlagSeen(artifactId); }
	}
}
