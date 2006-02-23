/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.document;

import java.util.Calendar;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.parity.model.audit.event.CloseEvent;
import com.thinkparity.model.parity.model.audit.event.CreateEvent;
import com.thinkparity.model.parity.model.audit.event.ReceiveEvent;

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

	void close(final Long documentId, final String closedBy,
			final Calendar closedOn) {
		final CloseEvent closeEvent = new CloseEvent();
		closeEvent.setArtifactId(documentId);
		closeEvent.setCreatedBy(closedBy);
		closeEvent.setCreatedOn(closedOn);
		closeEvent.setType(AuditEventType.CLOSE);

		getInternalAuditModel().audit(closeEvent);
	}

	void create(final Long documentId, final String createdBy,
			final Calendar createdOn) {
		final CreateEvent createEvent = new CreateEvent();
		createEvent.setArtifactId(documentId);
		createEvent.setCreatedBy(createdBy);
		createEvent.setCreatedOn(createdOn);
		createEvent.setType(AuditEventType.CREATE);

		getInternalAuditModel().audit(createEvent);
	}

	void recieve(final Long artifactId, final Long artifactVersionId,
			final String receivedFrom, final String receivedBy,
			final Calendar receivedOn) {
		final ReceiveEvent receiveEvent = new ReceiveEvent();
		receiveEvent.setArtifactId(artifactId);
		receiveEvent.setArtifactVersionId(artifactVersionId);
		receiveEvent.setCreatedBy(receivedBy);
		receiveEvent.setCreatedOn(receivedOn);
		receiveEvent.setReceivedFrom(receivedFrom);
		receiveEvent.setType(AuditEventType.RECEIVE);

		getInternalAuditModel().audit(receiveEvent);
	}
}
