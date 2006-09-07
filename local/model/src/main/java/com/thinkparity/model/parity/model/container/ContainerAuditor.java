/*
 * Created On: Jun 29, 2006 8:30:14 AM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractAuditor;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.audit.event.AddTeamMemberEvent;
import com.thinkparity.model.parity.model.audit.event.CloseEvent;
import com.thinkparity.model.parity.model.audit.event.CreateEvent;
import com.thinkparity.model.parity.model.audit.event.PublishEvent;
import com.thinkparity.model.parity.model.audit.event.ReactivateEvent;
import com.thinkparity.model.parity.model.audit.event.SendKeyEvent;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
class ContainerAuditor extends AbstractAuditor {

    /**
     * Create ContainerAuditor.
     * 
     * @param context
     *            The thinkParity context.
     */
    ContainerAuditor(final Context context) { super(context); }

    void addTeamMember(final Long id, final JabberId createdBy,
            final Calendar createdOn, final JabberId jabberId)
            throws ParityException {
        final AddTeamMemberEvent event = new AddTeamMemberEvent();
        event.setArtifactId(id);
        event.setCreatedOn(createdOn);

        getInternalAuditModel().audit(event, createdBy, jabberId);
    }

    void close(final Long id, final JabberId closedBy, final Calendar closedOn,
            final JabberId createdBy, final Calendar createdOn)
            throws ParityException {
        final CloseEvent closeEvent = new CloseEvent();
        closeEvent.setArtifactId(id);
        closeEvent.setCreatedOn(createdOn);
        getInternalAuditModel().audit(closeEvent, createdBy, closedBy);
    }

    void create(final Long id, final JabberId createdBy,
            final Calendar createdOn) throws ParityException {
        final CreateEvent event = new CreateEvent();
        event.setArtifactId(id);
        event.setCreatedOn(createdOn);
        getInternalAuditModel().audit(event, createdBy);
    }

    void publish(final Long id, final Long versionId, final JabberId createdBy,
            final Calendar createdOn) throws ParityException {
        final PublishEvent event = new PublishEvent();
        event.setArtifactId(id);
        event.setArtifactVersionId(versionId);
        event.setCreatedOn(createdOn);
        getInternalAuditModel().audit(event, createdBy);
    }

    void reactivate(final Long id, final Long versionId,
            final JabberId createdBy, final Calendar createdOn,
            final JabberId reactivatedBy, Calendar reactivatedOn)
            throws ParityException {
        final ReactivateEvent event = new ReactivateEvent();
        event.setArtifactId(id);
        event.setArtifactVersionId(versionId);
        event.setCreatedOn(createdOn);
        getInternalAuditModel().audit(event, createdBy, reactivatedBy);
    }

    void sendKey(final Long id, final JabberId createdBy,
            final Calendar createdOn, final Long versionId,
            final JabberId sentBy, final Calendar sentOn, final JabberId sentTo)
            throws ParityException {
        final SendKeyEvent sendKeyEvent = new SendKeyEvent();
        sendKeyEvent.setArtifactId(id);
        sendKeyEvent.setArtifactVersionId(versionId);
        sendKeyEvent.setCreatedOn(sentOn);
        getInternalAuditModel().audit(sendKeyEvent, createdBy, sentTo);
    }
}
