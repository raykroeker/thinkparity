/*
 * Mar 21, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import java.util.Calendar;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractAuditor;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.parity.model.audit.event.ConfirmationReceipt;
import com.thinkparity.model.parity.model.audit.event.KeyRequestDeniedEvent;
import com.thinkparity.model.xmpp.JabberId;

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
    void confirmationReceipt(final Long artifactId, final JabberId createdBy,
            final Calendar createdOn, final JabberId receivedFrom) throws ParityException {
        final ConfirmationReceipt event = new ConfirmationReceipt();
        event.setArtifactId(artifactId);
        event.setCreatedOn(createdOn);
        event.setType(AuditEventType.CONFIRM_RECEIPT);

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
		event.setType(AuditEventType.KEY_REQUEST_DENIED);

		getInternalAuditModel().audit(event, createdBy, deniedBy);
	}
}
