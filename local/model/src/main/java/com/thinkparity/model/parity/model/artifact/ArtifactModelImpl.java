/*
 * Mar 2, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.assertion.TrueAssertion;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler;
import com.thinkparity.model.parity.model.message.system.InternalSystemMessageModel;
import com.thinkparity.model.parity.model.message.system.KeyRequestMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageType;
import com.thinkparity.model.parity.model.session.InternalSessionModel;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class ArtifactModelImpl extends AbstractModelImpl {

	/**
	 * The artifact model's auditor.
	 * 
	 */
	protected final ArtifactModelAuditor auditor;

	/**
	 * Artifact persistance io.
	 * 
	 */
	private final ArtifactIOHandler artifactIO;

	/**
	 * Create a ArtifactModelImpl.
	 * 
	 * @param workspace
	 *            The parity workspace
	 */
	ArtifactModelImpl(final Workspace workspace) {
		super(workspace);
		this.artifactIO = IOFactory.getDefault().createArtifactHandler();
		this.auditor = new ArtifactModelAuditor(getContext());
	}

	/**
	 * Accept the key request.
	 * 
	 * @param keyRequestId
	 *            The key request id.
	 */
	void acceptKeyRequest(final Long keyRequestId) throws ParityException {
		logger.info("[LMODEL] [ARTIFACT] [ACCEPT KEY REQUEST]");
		logger.debug(keyRequestId);
		final InternalSessionModel iSModel = getInternalSessionModel();
		final InternalSystemMessageModel iSMModel = getInternalSystemMessageModel();
		final KeyRequestMessage keyRequestMessage =
			(KeyRequestMessage) iSMModel.read(keyRequestId);
		// send a denial to all others
		final List<KeyRequest> requests = readKeyRequests(keyRequestMessage.getArtifactId());
		for(final KeyRequest request : requests) {
			if(request.getId().equals(keyRequestId)) {
				iSMModel.delete(request.getId());
				continue;
			}
			if(request.getRequestedBy().equals(keyRequestMessage.getRequestedBy())) {
				iSMModel.delete(request.getId());
				continue;
			}
			declineKeyRequest(request.getId());
		}
		// send acceptance
		iSModel.sendKeyResponse(
				keyRequestMessage.getArtifactId(),
				keyRequestMessage.getRequestedBy(), KeyResponse.ACCEPT);
	}

	/**
	 * Apply the key flag.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	void applyFlagKey(final Long artifactId) {
		logger.info("[LMODEL] [ARTIFACT] [APPLY KEY]");
		applyFlag(artifactId, ArtifactFlag.KEY);
	}

	/**
	 * Apply the seen flag.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	void applyFlagSeen(final Long artifactId) {
		logger.info("[LMODEL] [ARTIFACT] [APPLY SEEN]");
		applyFlag(artifactId, ArtifactFlag.SEEN);
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
	void auditKeyRequestDenied(final Long artifactId,
			final JabberId createdBy, final Calendar createdOn,
			final JabberId deniedBy) {
		auditor.KeyRequestDenied(artifactId, createdBy, createdOn, deniedBy);
	}

	void declineKeyRequest(final Long keyRequestId) throws ParityException {
		logger.info("[LMODEL] [ARTIFACT] [DENY KEY REQUEST]");
		logger.debug(keyRequestId);
		final InternalSystemMessageModel iSMModel =
			getInternalSystemMessageModel();
		final KeyRequestMessage keyRequestMessage =
			(KeyRequestMessage) iSMModel.read(keyRequestId);
		iSMModel.delete(keyRequestMessage.getId());

		getInternalSessionModel().sendKeyResponse(
				keyRequestMessage.getArtifactId(),
				keyRequestMessage.getRequestedBy(), KeyResponse.DENY);

	}

	/**
	 * Determine whether or not the artifact has been seen.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return True if the artifact has been seen.
	 */
	Boolean hasBeenSeen(final Long artifactId) {
		logger.info("[LMODEL] [ARTIFACT] [HAS BEEN SEEN]");
		return isFlagApplied(artifactId, ArtifactFlag.SEEN);
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
	Boolean isFlagApplied(final Long artifactId, final ArtifactFlag flag) {
		logger.info("[LMODEL] [ARTIFACT] [IS FLAG APPLIED]");
		logger.debug(artifactId);
		logger.debug(flag);
		final List<ArtifactFlag> flags = artifactIO.getFlags(artifactId);
		return flags.contains(flag);
	}

	/**
	 * Read all key requests for the given artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return A list of requests.
	 */
	List<KeyRequest> readKeyRequests(final Long artifactId)
			throws ParityException {
		logger.info("[LMODEL] [ARTIFACT] [READ KEY REQUESTS]");
		logger.debug(artifactId);
		final List<SystemMessage> messages =
			getInternalSystemMessageModel().readForArtifact(
					artifactId, SystemMessageType.KEY_REQUEST);
		final List<KeyRequest> requests = new LinkedList<KeyRequest>();
		for(final SystemMessage message : messages) {
			requests.add(createKeyRequest((KeyRequestMessage) message));
		}
		return requests;
	}

	/**
	 * Remove the key flag.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	void removeFlagKey(final Long artifactId) {
		logger.info("[LMODEL] [ARTIFACT] [REMOVE KEY]");
		removeFlag(artifactId, ArtifactFlag.KEY);
	}

	/**
	 * Remove the seen flag.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	void removeFlagSeen(final Long artifactId) {
		logger.info("[LMODEL] [ARTIFACT] [REMOVE SEEN]");
		removeFlag(artifactId, ArtifactFlag.SEEN);
	}

	/**
	 * Apply a flag to an artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param flag
	 *            The flag.
	 */
	private void applyFlag(final Long artifactId, final ArtifactFlag flag) {
		logger.info("[LMODEL] [ARTIFACT] [APPLY FLAG]");
		logger.debug(artifactId);
		logger.debug(flag);
		final List<ArtifactFlag> flags = artifactIO.getFlags(artifactId);
		if(flags.contains(flag)) {
			logger.warn("Artifact [" + artifactId
					+ "] has already been flagged as [" + flag + "].");
		}
		else {
			flags.add(flag);
			artifactIO.updateFlags(artifactId, flags);
		}
	}

	/**
	 * Create a key request based upon a key request system message.
	 * 
	 * @param message
	 *            The key request system message.
	 * @return The key request.
	 */
	private KeyRequest createKeyRequest(final KeyRequestMessage message) {
		final KeyRequest request = new KeyRequest();
		request.setArtifactId(message.getArtifactId());
		request.setRequestedBy(message.getRequestedBy());
		request.setRequestedByName(message.getRequestedByName());
		request.setId(message.getId());
		return request;
	}

	/**
	 * Remove a flag from an artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param flag
	 *            The flag.
	 * @throws TrueAssertion
	 *             <ul>
	 *             <li>If the flag has not been applied
	 *             </ul>
	 */
	private void removeFlag(final Long artifactId, final ArtifactFlag flag) {
		logger.info("[] [] []");
		final List<ArtifactFlag> flags = artifactIO.getFlags(artifactId);
		if(flags.contains(flag)) {
			flags.remove(flag);
			artifactIO.updateFlags(artifactId, flags);
		}
		else {
			logger.warn("Artifact [" + artifactId
					+ "] has no flag [" + flag + "].");
		}
	}
}
