/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.util.Calendar;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractAuditor;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.parity.model.audit.event.ArchiveEvent;
import com.thinkparity.model.parity.model.audit.event.CloseEvent;
import com.thinkparity.model.parity.model.audit.event.CreateEvent;
import com.thinkparity.model.parity.model.audit.event.ReceiveEvent;
import com.thinkparity.model.parity.model.audit.event.ReceiveKeyEvent;
import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class DocumentModelAuditor extends AbstractAuditor {

	/**
	 * Create a DocumentModelAuditor.
	 * 
	 * @param context
	 *            The parity context.
	 */
	DocumentModelAuditor(final Context context) {
		super(context);
	}

	void archive(final Long artifactId, final JabberId createdBy,
            final Calendar createdOn) throws ParityException {
		final ArchiveEvent event = new ArchiveEvent();
		event.setArtifactId(artifactId);
				event.setCreatedOn(createdOn);
		event.setType(AuditEventType.ARCHIVE);

		getInternalAuditModel().audit(event, createdBy);
	}

	void close(final Long documentId, final JabberId closedBy,
            final Calendar closedOn, final JabberId createdBy)
            throws ParityException {
		final CloseEvent closeEvent = new CloseEvent();
		closeEvent.setArtifactId(documentId);
		closeEvent.setCreatedOn(closedOn);
		closeEvent.setType(AuditEventType.CLOSE);

		getInternalAuditModel().audit(closeEvent, createdBy, closedBy);
	}

	void create(final Long documentId, final JabberId createdBy,
			final Calendar createdOn) throws ParityException {
		final CreateEvent createEvent = new CreateEvent();
		createEvent.setArtifactId(documentId);
		createEvent.setCreatedOn(createdOn);
		createEvent.setType(AuditEventType.CREATE);

		getInternalAuditModel().audit(createEvent, createdBy);
	}

	void receive(final Long artifactId, final Calendar createdOn,
            final JabberId createdBy, final Long artifactVersionId,
            final JabberId receivedFrom, final JabberId receivedBy,
            final Calendar receivedOn) throws ParityException {
		final ReceiveEvent receiveEvent = new ReceiveEvent();
		receiveEvent.setArtifactId(artifactId);
		receiveEvent.setArtifactVersionId(artifactVersionId);
		receiveEvent.setCreatedOn(createdOn);
		receiveEvent.setType(AuditEventType.RECEIVE);

		getInternalAuditModel().audit(receiveEvent, createdBy, receivedFrom);
	}

	void receiveKey(final Long artifactId, final JabberId createdBy,
            final Calendar createdOn, final JabberId receivedFrom)
            throws ParityException {
		final ReceiveKeyEvent event = new ReceiveKeyEvent();
		event.setArtifactId(artifactId);
				event.setCreatedOn(createdOn);
		event.setType(AuditEventType.RECEIVE_KEY);

		getInternalAuditModel().audit(event, createdBy, receivedFrom);
	}
}
