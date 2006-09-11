/*
 * Mar 21, 2006
 */
package com.thinkparity.ophelia.model.artifact;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;


import com.thinkparity.ophelia.model.AbstractAuditor;
import com.thinkparity.ophelia.model.Context;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.audit.event.KeyRequestDeniedEvent;
import com.thinkparity.ophelia.model.audit.event.SendConfirmEvent;
import com.thinkparity.ophelia.model.audit.event.SendKeyEvent;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class ArtifactModelAuditor extends AbstractAuditor {

	/**
	 * Create a ArtifactModelAuditor.
	 * 
	 * @param context
	 *            The parity context.
	 */
	public ArtifactModelAuditor(final Context context) {
		super(context);
	}

    /**
     * Audit the confirmation receipt of an artifact.
     * 
     * @param artifactId
     *            The artifact id.
     * @param createdBy
     *            The event creator.
     * @param createdOn
     *            The event creation date\time.
     * @param receivedBy
     *            The recipient of the artifact.
     */
    void confirmationReceipt(final Long artifactId,
            final Long artifactVersionId, final JabberId createdBy,
            final Calendar createdOn, final JabberId receivedFrom)
            throws ParityException {
        final SendConfirmEvent event = new SendConfirmEvent();
        event.setArtifactId(artifactId);
        event.setArtifactVersionId(artifactVersionId);
        event.setCreatedOn(createdOn);

        getInternalAuditModel().audit(event, createdBy, receivedFrom);
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
	void keyRequestDenied(final Long artifactId,
			final JabberId createdBy, final Calendar createdOn,
			final JabberId deniedBy) throws ParityException {
		final KeyRequestDeniedEvent event = new KeyRequestDeniedEvent();
		event.setArtifactId(artifactId);
		event.setCreatedOn(createdOn);

		getInternalAuditModel().audit(event, createdBy, deniedBy);
	}

    /**
     * Audit the send key api.
     * 
     * @param artifactId
     *            The artifact id.
     * @param artifactVersionId
     *            The artifact version id.
     * @param sentBy
     *            The user sending the key.
     * @param sentOn
     *            The sent date\time.
     * @param sentTo
     *            The user the key was sent to.
     */
    void sendKey(final Long artifactId, final Calendar createdOn,
            final JabberId createdBy, final Long artifactVersionId,
            final JabberId sentBy, final Calendar sentOn, final JabberId sentTo)
            throws ParityException {
        final SendKeyEvent sendKeyEvent = new SendKeyEvent();
        sendKeyEvent.setArtifactId(artifactId);
        sendKeyEvent.setArtifactVersionId(artifactVersionId);
        sendKeyEvent.setCreatedOn(sentOn);

        getInternalAuditModel().audit(sendKeyEvent, createdBy, sentTo);
    }
}
