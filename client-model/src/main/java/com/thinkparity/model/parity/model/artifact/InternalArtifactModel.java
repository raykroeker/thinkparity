/*
 * Mar 2, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalArtifactModel extends ArtifactModel {

	/**
	 * Create a InternalArtifactModel.
	 * 
	 * @param context
	 *            The parity context.
	 * @param workspace
	 *            The workspace.
	 */
	InternalArtifactModel(final Context context, final Workspace workspace) {
		super(workspace);
		context.assertContextIsValid();
	}

	public void applyFlagKey(final Long artifactId) {
		synchronized(getImplLock()) { getImpl().applyFlagKey(artifactId); }
	}

	/**
	 * Audit the denial of a key request for an artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param createdBy
	 *            The creator.
	 * @param creatdOn
	 *            The creation date.
	 * @param deniedBy
	 *            The user denying the request.
	 */
	public void auditKeyRequestDenied(final Long artifactId,
			final JabberId createdBy, final Calendar createdOn,
			final JabberId deniedBy) {
		synchronized(getImplLock()) {
			getImpl().auditKeyRequestDenied(artifactId, createdBy, createdOn, deniedBy);
		}
	}

	/**
     * Create the artifact's remote info.
     * 
     * @param artifactId
     *            The artifact id.
     * @param updatedBy
     *            The remote user to update the artifact.
     * @param updatedOn
     *            The last time the artifact was updated.
     */
	public void createRemoteInfo(final Long artifactId,
			final JabberId updatedBy, final Calendar updatedOn) {
		synchronized(getImplLock()) {
			getImpl().createRemoteInfo(artifactId, updatedBy, updatedOn);
		}
	}

	/**
     * Delete the artifact's remote info.
     * 
     * @param artifactId
     *            The artifact id.
     */
	public void deleteRemoteInfo(final Long artifactId) {
		synchronized(getImplLock()) {
			getImpl().deleteRemoteInfo(artifactId);
		}
	}

	/**
	 * Obtain all pending key requests for the artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return A list of key requests.
	 * @throws ParityException
	 */
	public List<KeyRequest> readKeyRequests(final Long artifactId)
			throws ParityException {
		synchronized(getImplLock()) {
			return getImpl().readKeyRequests(artifactId);
		}
	}

	public void removeFlagKey(final Long artifactId) {
		synchronized(getImplLock()) { getImpl().removeFlagKey(artifactId); }
	}

	/**
     * Update the artifact's remote info.
     * 
     * @param artifactId
     *            The artifact id.
     * @param updatedBy
     *            The last user to update the artifact.
     * @param updatedOn
     *            The last time the artifact was updated.
     */
	public void updateRemoteInfo(final Long artifactId,
			final JabberId updatedBy, final Calendar updatedOn) {
		synchronized(getImplLock()) {
			getImpl().updateRemoteInfo(artifactId, updatedBy, updatedOn);
		}
	}
}
