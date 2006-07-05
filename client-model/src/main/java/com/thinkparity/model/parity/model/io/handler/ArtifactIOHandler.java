/*
 * Mar 2, 2006
 */
package com.thinkparity.model.parity.model.io.handler;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface ArtifactIOHandler {

	/**
     * Create the remote info for the artifact.
     * 
     * @param artifactId
     *            The artifact id.
     * @param updatedBy
     *            The last person to remotely update this artifact.
     * @param updatedOn
     *            The last time this artifact was remotely updated.
     * @throws HypersonicException
     */
	public void createRemoteInfo(final Long artifactId,
			final JabberId updatedBy, final Calendar updatedOn)
			throws HypersonicException;

    /**
     * Create an artifact team relationship.
     * 
     * @param artifactId
     *            The artifact id.
     * @param userId
     *            The user id.
     * @throws HypersonicException
     */
    public void createTeamRel(final Long artifactId, final Long userId)
            throws HypersonicException;

    /**
     * Delete the remote info for the artifact.
     * 
     * @param artifactId
     *            The artifact id.
     * @throws HypersonicException
     */
	public void deleteRemoteInfo(final Long artifactId)
			throws HypersonicException;

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
	 * Obtain a list of all flags for an artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return A list of all flags for the artifact.
	 * @throws HypersonicException
	 */
	public List<ArtifactFlag> getFlags(final Long artifactId)
			throws HypersonicException;

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
     * Update the remote info for the artifact.
     * 
     * @param artifactId
     *            The artifact id.
     * @param updatedBy
     *            The last person to remotely update this artifact.
     * @param updatedOn
     *            The last time this artifact was remotely updated.
     * @throws HypersonicException
     */
	public void updateRemoteInfo(final Long artifactId,
			final JabberId updatedBy, final Calendar updatedOn)
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
