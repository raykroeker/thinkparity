/*
 * Mar 21, 2006
 */
package com.thinkparity.ophelia.model.artifact;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.audit.AbstractAuditor;
import com.thinkparity.ophelia.model.audit.event.KeyRequestDeniedEvent;
import com.thinkparity.ophelia.model.audit.event.ReceiveEvent;
import com.thinkparity.ophelia.model.audit.event.SendKeyEvent;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class ArtifactModelAuditor extends AbstractAuditor {

    /**
     * Create ArtifactModelAuditor.
     * 
     * @param modelFactory
     *            A thinkParity <code>InternalModelFactory</code>.
     */
    public ArtifactModelAuditor(final InternalModelFactory modelFactory) {
        super(modelFactory);
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
    void received(final Long artifactId, final Long versionId,
            final JabberId receivedBy, final Calendar receivedOn) {
        final ReceiveEvent event = new ReceiveEvent();
        event.setArtifactId(artifactId);
        event.setArtifactVersionId(versionId);
        event.setCreatedOn(receivedOn);
        getInternalAuditModel().audit(event);
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
			final JabberId deniedBy) {
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
            final JabberId sentBy, final Calendar sentOn, final JabberId sentTo) {
        final SendKeyEvent sendKeyEvent = new SendKeyEvent();
        sendKeyEvent.setArtifactId(artifactId);
        sendKeyEvent.setArtifactVersionId(artifactVersionId);
        sendKeyEvent.setCreatedOn(sentOn);

        getInternalAuditModel().audit(sendKeyEvent, createdBy, sentTo);
    }
}
