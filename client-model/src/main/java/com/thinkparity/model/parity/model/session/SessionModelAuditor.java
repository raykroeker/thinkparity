/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.session;

import java.util.Calendar;
import java.util.Collection;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractAuditor;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.audit.event.KeyResponseDeniedEvent;
import com.thinkparity.model.parity.model.audit.event.RequestKeyEvent;
import com.thinkparity.model.parity.model.audit.event.SendEvent;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionModelAuditor extends AbstractAuditor {

	/**
	 * Create a SessionModelAuditor.
	 * 
	 * @param context
	 *            The parity context.
	 */
	SessionModelAuditor(final Context context) {
		super(context);
	}

	void requestKey(final Long artifactId, final JabberId createdBy,
			final Calendar createdOn, final JabberId requestedBy,
			final JabberId requestedFrom) throws ParityException {
		final RequestKeyEvent event = new RequestKeyEvent();
		event.setArtifactId(artifactId);
				event.setCreatedOn(createdOn);

		getInternalAuditModel().audit(event, createdBy, requestedBy, requestedFrom);
	}

	/**
	 * Audit the sent api.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param artifactVersionId
	 *            The artifact version id.
	 * @param sentBy
	 *            The user sending the artifact.
	 * @param sentOn
	 *            The sent date\time.
	 * @param users
	 *            The sent to list.
	 */
	void send(final Long artifactId, final Calendar createdOn,
            final JabberId createdBy, final Long artifactVersionId,
            final JabberId sentBy, final Calendar sentOn,
            final Collection<User> sentTo) throws ParityException {
		final SendEvent sendEvent = new SendEvent();
		sendEvent.setArtifactId(artifactId);
		sendEvent.setArtifactVersionId(artifactVersionId);
		sendEvent.setCreatedOn(createdOn);

        // generate a new event for each user
        for(final User u : sentTo) {
            getInternalAuditModel().audit(sendEvent, createdBy, u.getId());
		}
	}

	/**
	 * Audit the key response denied.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param createdBy
	 *            The creator.
	 * @param createdOn
	 *            The creation date.
	 * @param requestedBy
	 *            The key requestor.
	 */
	void keyResponseDenied(final Long artifactId, final JabberId createdBy,
            final Calendar createdOn, final JabberId requestedBy)
            throws ParityException {
		final KeyResponseDeniedEvent event = new KeyResponseDeniedEvent();
		event.setArtifactId(artifactId);
		event.setCreatedOn(createdOn);

		getInternalAuditModel().audit(event, createdBy, requestedBy);
	}
}
