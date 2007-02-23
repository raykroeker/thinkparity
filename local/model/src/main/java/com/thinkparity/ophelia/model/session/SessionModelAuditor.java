/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.session;

import java.util.Calendar;
import java.util.Collection;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.audit.AbstractAuditor;
import com.thinkparity.ophelia.model.audit.event.KeyResponseDeniedEvent;
import com.thinkparity.ophelia.model.audit.event.RequestKeyEvent;
import com.thinkparity.ophelia.model.audit.event.SendEvent;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionModelAuditor extends AbstractAuditor {

    /**
     * Create SessionModelAuditor.
     * 
     * @param modelFactory
     *            A thinkParity <code>InternalModelFactory</code>.
     */
    public SessionModelAuditor(final InternalModelFactory modelFactory) {
        super(modelFactory);
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
            final Calendar createdOn, final JabberId requestedBy) {
		final KeyResponseDeniedEvent event = new KeyResponseDeniedEvent();
		event.setArtifactId(artifactId);
		event.setCreatedOn(createdOn);

		getInternalAuditModel().audit(event, createdBy, requestedBy);
	}

	void requestKey(final Long artifactId, final JabberId createdBy,
			final Calendar createdOn, final JabberId requestedBy,
			final JabberId requestedFrom) {
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
            final Collection<User> sentTo) {
		final SendEvent sendEvent = new SendEvent();
		sendEvent.setArtifactId(artifactId);
		sendEvent.setArtifactVersionId(artifactVersionId);
		sendEvent.setCreatedOn(createdOn);

        // generate a new event for each user
        for(final User u : sentTo) {
            getInternalAuditModel().audit(sendEvent, createdBy, u.getId());
		}
	}
}
