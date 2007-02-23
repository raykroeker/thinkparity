/*
 * Created On:  Feb 21, 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.document;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.audit.AbstractAuditor;
import com.thinkparity.ophelia.model.audit.event.AddTeamMemberConfirmEvent;
import com.thinkparity.ophelia.model.audit.event.AddTeamMemberEvent;
import com.thinkparity.ophelia.model.audit.event.ArchiveEvent;
import com.thinkparity.ophelia.model.audit.event.CloseEvent;
import com.thinkparity.ophelia.model.audit.event.CreateEvent;
import com.thinkparity.ophelia.model.audit.event.CreateRemoteEvent;
import com.thinkparity.ophelia.model.audit.event.ReceiveKeyEvent;
import com.thinkparity.ophelia.model.audit.event.RenameEvent;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class DocumentModelAuditor extends AbstractAuditor {

    /**
     * Create DocumentModelAuditor.
     * 
     * @param modelFactory
     *            A thinkParity <code>InternalModelFactory</code>.
     */
    public DocumentModelAuditor(final InternalModelFactory modelFactory) {
        super(modelFactory);
    }

	void addTeamMember(final Long artifactId, final JabberId createdBy,
            final Calendar createdOn, final JabberId teamMember) {
        final AddTeamMemberEvent event = new AddTeamMemberEvent();
        event.setArtifactId(artifactId);
        event.setCreatedOn(createdOn);

        getInternalAuditModel().audit(event, createdBy, teamMember);
    }

	void archive(final Long artifactId, final JabberId createdBy,
            final Calendar createdOn) {
		final ArchiveEvent event = new ArchiveEvent();
		event.setArtifactId(artifactId);
        event.setCreatedOn(createdOn);

		getInternalAuditModel().audit(event, createdBy);
	}

	void close(final Long documentId, final JabberId closedBy,
            final Calendar closedOn, final JabberId createdBy) {
		final CloseEvent closeEvent = new CloseEvent();
		closeEvent.setArtifactId(documentId);
		closeEvent.setCreatedOn(closedOn);

		getInternalAuditModel().audit(closeEvent, createdBy, closedBy);
	}

    void confirmAddTeamMember(final Long artifactId, final JabberId createdBy,
            final Calendar createdOn, final JabberId teamMember) {
        final AddTeamMemberConfirmEvent event = new AddTeamMemberConfirmEvent();
        event.setArtifactId(artifactId);
        event.setCreatedOn(createdOn);

        getInternalAuditModel().audit(event, createdBy, teamMember);
    }

    void create(final Document document, final JabberId createdBy,
            final Calendar createdOn) {
		final CreateEvent event = new CreateEvent();
		event.setArtifactId(document.getId());
		event.setCreatedBy(createdBy);
		event.setCreatedOn(createdOn);
		getInternalAuditModel().audit(event);
	}

    void createRemote(final Long documentId, final JabberId createdBy,
            final Calendar createdOn, final JabberId receivedFrom) {
        final CreateRemoteEvent event = new CreateRemoteEvent();
        event.setArtifactId(documentId);
        event.setCreatedOn(createdOn);

        getInternalAuditModel().audit(event, createdBy, receivedFrom);
    }

    void receiveKey(final Long artifactId, final JabberId createdBy,
            final Calendar createdOn, final JabberId receivedFrom) {
		final ReceiveKeyEvent event = new ReceiveKeyEvent();
		event.setArtifactId(artifactId);
        event.setCreatedOn(createdOn);

		getInternalAuditModel().audit(event, createdBy, receivedFrom);
	}

    void rename(final Long artifactId, final Calendar createdOn,
            final JabberId createdBy, final String from, final String to) {
        final RenameEvent event = new RenameEvent();
        event.setArtifactId(artifactId);
        event.setCreatedOn(createdOn);
        event.setFrom(from);
        event.setTo(to);

        getInternalAuditModel().audit(event, createdBy);
    }
}
