/*
 * Mar 2, 2006
 */
package com.thinkparity.ophelia.model.artifact;

import java.util.Set;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Artifact Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.6
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface ArtifactModel {

	/**
	 * Apply the seen flag to the artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	public void applyFlagSeen(final Long artifactId);

	/**
	 * Determine whether or not the artifact has been seen.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return True if the artifact has been seen; false otherwise.
	 */
	public Boolean hasBeenSeen(final Long artifactId);

	/**
	 * Determine whether or not an artifact has a flag applied.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param flag
	 *            The artifact flag.
	 * @return True if the flag is applied; false otherwise.
	 */
	public Boolean isFlagApplied(final Long artifactId, final ArtifactFlag flag);

    /**
     * Read the team for the artifact.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The artifact team.
     */
    public Set<User> readTeam(final Long artifactId);

	/**
     * Read the artifact's type.
     * 
     * @param artifactId
     *            An artifact id.
     * @return An <code>ArtifactType</code>.
     */
    public ArtifactType readType(final Long artifactId);

    /**
	 * Remove the seen flag from the artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	public void removeFlagSeen(final Long artifactId);
}
