/*
 * Created On:  Feb 21, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.document;

import java.util.Calendar;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractAuditor;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.audit.event.*;
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
	DocumentModelAuditor(final Context context) { super(context); }

	void archive(final Long artifactId, final JabberId createdBy,
            final Calendar createdOn) throws ParityException {
		final ArchiveEvent event = new ArchiveEvent();
		event.setArtifactId(artifactId);
        event.setCreatedOn(createdOn);

		getInternalAuditModel().audit(event, createdBy);
	}

	void close(final Long documentId, final JabberId closedBy,
            final Calendar closedOn, final JabberId createdBy)
            throws ParityException {
		final CloseEvent closeEvent = new CloseEvent();
		closeEvent.setArtifactId(documentId);
		closeEvent.setCreatedOn(closedOn);

		getInternalAuditModel().audit(closeEvent, createdBy, closedBy);
	}

	void create(final Long documentId, final JabberId createdBy,
			final Calendar createdOn) throws ParityException {
		final CreateEvent createEvent = new CreateEvent();
		createEvent.setArtifactId(documentId);
		createEvent.setCreatedOn(createdOn);

		getInternalAuditModel().audit(createEvent, createdBy);
	}

    void createRemote(final Long documentId, final JabberId createdBy,
            final Calendar createdOn, final JabberId receivedFrom)
            throws ParityException {
        final CreateRemoteEvent event = new CreateRemoteEvent();
        event.setArtifactId(documentId);
        event.setCreatedOn(createdOn);

        getInternalAuditModel().audit(event, createdBy, receivedFrom);
    }

    void publish(final Long artifactId, final Long versionId,
            final Calendar createdOn, final JabberId createdBy)
            throws ParityException {
        final PublishEvent event = new PublishEvent();
        event.setArtifactId(artifactId);
        event.setArtifactVersionId(versionId);
        event.setCreatedOn(createdOn);

        getInternalAuditModel().audit(event, createdBy);
    }

    void reactivate(final Long artifactId, final Calendar createdOn,
            final JabberId createdBy, final Long versionId,
            final JabberId reactivatedBy, final Calendar reactivatedOn)
            throws ParityException {
        final ReactivateEvent event = new ReactivateEvent();
        event.setArtifactId(artifactId);
        event.setArtifactVersionId(versionId);
        event.setCreatedOn(createdOn);

        getInternalAuditModel().audit(event, createdBy, reactivatedBy);
    }

	void receive(final Long artifactId, final Calendar createdOn,
            final JabberId createdBy, final Long artifactVersionId,
            final JabberId receivedFrom, final JabberId receivedBy,
            final Calendar receivedOn) throws ParityException {
		final ReceiveEvent receiveEvent = new ReceiveEvent();
		receiveEvent.setArtifactId(artifactId);
		receiveEvent.setArtifactVersionId(artifactVersionId);
		receiveEvent.setCreatedOn(createdOn);

		getInternalAuditModel().audit(receiveEvent, createdBy, receivedFrom);
	}

	void receiveKey(final Long artifactId, final JabberId createdBy,
            final Calendar createdOn, final JabberId receivedFrom)
            throws ParityException {
		final ReceiveKeyEvent event = new ReceiveKeyEvent();
		event.setArtifactId(artifactId);
        event.setCreatedOn(createdOn);

		getInternalAuditModel().audit(event, createdBy, receivedFrom);
	}

    void rename(final Long artifactId, final Calendar createdOn,
            final JabberId createdBy, final String from, final String to)
            throws ParityException {
        final RenameEvent event = new RenameEvent();
        event.setArtifactId(artifactId);
        event.setCreatedOn(createdOn);
        event.setFrom(from);
        event.setTo(to);

        getInternalAuditModel().audit(event, createdBy);
    }

    void addTeamMember(final Long artifactId, final JabberId createdBy,
            final Calendar createdOn, final JabberId teamMember)
            throws ParityException {
        final AddTeamMemberEvent event = new AddTeamMemberEvent();
        event.setArtifactId(artifactId);
        event.setCreatedOn(createdOn);

        getInternalAuditModel().audit(event, createdBy, teamMember);
    }

    void confirmAddTeamMember(final Long artifactId, final JabberId createdBy,
            final Calendar createdOn, final JabberId teamMember)
            throws ParityException {
        final AddTeamMemberConfirmEvent event = new AddTeamMemberConfirmEvent();
        event.setArtifactId(artifactId);
        event.setCreatedOn(createdOn);

        getInternalAuditModel().audit(event, createdBy, teamMember);
    }
}
