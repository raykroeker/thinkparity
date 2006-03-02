/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.session;

import java.util.Calendar;
import java.util.Collection;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.parity.model.audit.event.SendEvent;
import com.thinkparity.model.parity.model.audit.event.SendKeyEvent;
import com.thinkparity.model.parity.model.document.AbstractAuditor;
import com.thinkparity.model.xmpp.JabberId;
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
	void send(final Long artifactId, final Long artifactVersionId,
			final JabberId sentBy, final Calendar sentOn,
			final Collection<User> sentTo) {
		final SendEvent sendEvent = new SendEvent();
		sendEvent.setArtifactId(artifactId);
		sendEvent.setArtifactVersionId(artifactVersionId);
		sendEvent.setCreatedBy(sentBy);
		sendEvent.setCreatedOn(sentOn);
		sendEvent.setType(AuditEventType.SEND);
		for(final User u : sentTo) { sendEvent.add(u.getId()); }

		getInternalAuditModel().audit(sendEvent);
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
	void sendKey(final Long artifactId, final Long artifactVersionId,
			final JabberId sentBy, final Calendar sentOn, final JabberId sentTo) {
		final SendKeyEvent sendKeyEvent = new SendKeyEvent();
		sendKeyEvent.setArtifactId(artifactId);
		sendKeyEvent.setArtifactVersionId(artifactVersionId);
		sendKeyEvent.setCreatedBy(sentBy);
		sendKeyEvent.setCreatedOn(sentOn);
		sendKeyEvent.setSentTo(sentTo);
		sendKeyEvent.setType(AuditEventType.SEND_KEY);

		getInternalAuditModel().audit(sendKeyEvent);
	}
}
