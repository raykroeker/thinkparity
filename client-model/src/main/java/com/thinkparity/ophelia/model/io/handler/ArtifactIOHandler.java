/*
 * Mar 2, 2006
 */
package com.thinkparity.ophelia.model.io.handler;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.artifact.ArtifactVersionFlag;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;

/**
 * <b>Title:</b>thinkParity Ophelia Model Artifact IO Interface<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ArtifactIOHandler {

    /**
     * Create an artifact team member relationship.
     * 
     * @param artifactId
     *            The artifact id.
     * @param userId
     *            The user id.
     */
    public void createTeamRel(final Long artifactId, final Long userId);

    /**
     * Delete an artifact team relationship in its entirety.
     * 
     * @param artifactId
     *            The artifact id.
     * @throws HypersonicException
     */
    public void deleteTeamRel(final Long artifactId) throws HypersonicException;

    /**
     * Delete an artifact team relationship.
     * 
     * @param artifactId
     *            The artifact id.
     * @param userId
     *            The user id.
     * @throws HypersonicException
     */
    public void deleteTeamRel(final Long artifactId, final Long userId)
            throws HypersonicException;

    /**
     * Determine if the artifact exists.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @return True if the artifact exists.
     */
    public Boolean doesExist(final Long artifactId);

    /**
     * Determine if the artifact exists.
     * 
     * @param artifactId
     *            A unique id <code>UUID</code>.
     * @return True if the artifact exists.
     */
    public Boolean doesExist(final UUID uniqueId);

    /**
     * Determine if a version exists.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @return True if a version exists.
     */
    public Boolean doesVersionExist(final Long artifactId);

	/**
     * Determine if the artifact version exists.
     * 
     * @param artifactId
     *            An artifact id.
     * @param versionId
     *            An artifact version id.
     * @return True if the artifact version exists.
     */
	public Boolean doesVersionExist(final Long artifactId, final Long versionId);

    /**
     * Determine whether or not the seen flag is applied to any version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if any version is flagged as seen.
     */
    public Boolean isVersionSeenFlagApplied(final Artifact artifact);

    /**
     * Determine whether or not the seen flag is applied to all versions.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if all versions are flagged as seen.
     */
    public Boolean isVersionSeenFlagAppliedAll(final Artifact artifact);

    /**
     * Read the earliest version id.
     * 
     * @param artifactId
     *            An artifact id.
     * @return A version id.
     */
    public Long readEarliestVersionId(final Long artifactId);

    /**
     * Read the artifact version flags.
     * 
     * @param version
     *            An <code>ArtifactVersion</code>.
     * @return A <code>List<ArtifactVersionFlag</code>.
     */
    public List<ArtifactVersionFlag> readFlags(final ArtifactVersion version);

	/**
	 * Obtain a list of all flags for an artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return A list of all flags for the artifact.
	 */
	public List<ArtifactFlag> readFlags(final Long artifactId);

    /**
     * Read the artifact id.
     * 
     * @param uniqueId
     *            A unique id.
     * @return An id.
     * @throws HypersonicException
     */
    public Long readId(final UUID uniqueId) throws HypersonicException;

    /**
     * Read the latest version id.
     * 
     * @param artifactId
     *            An artifact id.
     * @return A version id.
     */
    public Long readLatestVersionId(final Long artifactId);

    /**
     * Read the next version id in ascending order.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param versionId
     *            An artifact version id <code>Long</code>.
     * @return A <code>Long</code> version id.
     */
    public Long readNextVersionId(final Long artifactId, final Long versionId);

    /**
     * Read the previous version id in ascending order.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param versionId
     *            An artifact version id <code>Long</code>.
     * @return A <code>Long</code> version id.
     */
    public Long readPreviousVersionId(final Long artifactId, final Long versionId);

    /**
     * Read the artifact state.
     * 
     * @param artifactId
     *            The artifact id.
     * @return The artifact state.
     */
    public ArtifactState readState(final Long artifactId);

    /**
     * Read the team for an artifact.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The team.
     * @throws HypersonicException
     */
    public Set<User> readTeamRel(final Long artifactId)
            throws HypersonicException;

    /**
     * Read the team member for an artifact.
     * 
     * @param artifactId
     *            An artifact id.
     * @param userId
     *            A local user id.
     * @return The team member.
     * @throws HypersonicException
     */
    public TeamMember readTeamRel(final Long artifactId, final Long userId);

    /**
     * Read the team for an artifact.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The team.
     * @throws HypersonicException
     */
    public List<TeamMember> readTeamRel2(final Long artifactId);

    /**
     * Read the artifact type.
     * 
     * @param artifactId
     *            The artifact id.
     * @return The artifact type.
     */
    public ArtifactType readType(final Long artifactId);

    /**
     * Read the artifact unique id.
     * 
     * @param artifactId
     *            The artifact id.
     * @return The artifact unique id.
     */
    public UUID readUniqueId(final Long artifactId);

    /**
     * Update the artifact version flags.
     * 
     * @param version
     *            An <code>ArtifactVersion</code>.
     * @param A
     *            <code>List<ArtifactVersionFlag></code>.
     */
    public void updateFlags(final ArtifactVersion version,
            final List<ArtifactVersionFlag> flags);

	/**
	 * Set the flags for the artifact.
	 * 
	 * @param session
	 *            The database session.
	 * @param artifactId
	 *            The artifact id.
	 * @param flags
	 *            The flags.
	 * @throws HypersonicException
	 */
	public void updateFlags(final Long artifactId, final List<ArtifactFlag> flags)
			throws HypersonicException;

    /**
     * Update the artifact state.
     * 
     * @param artifactId
     *            The artifact id.
     * @param The artifact state.
     */
    public void updateState(final Long artifactId, final ArtifactState state);
}
