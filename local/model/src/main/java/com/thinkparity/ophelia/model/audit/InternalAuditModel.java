/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.audit;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.audit.event.AddTeamMemberConfirmEvent;
import com.thinkparity.ophelia.model.audit.event.AddTeamMemberEvent;
import com.thinkparity.ophelia.model.audit.event.ArchiveEvent;
import com.thinkparity.ophelia.model.audit.event.AuditEvent;
import com.thinkparity.ophelia.model.audit.event.CloseEvent;
import com.thinkparity.ophelia.model.audit.event.CreateEvent;
import com.thinkparity.ophelia.model.audit.event.CreateRemoteEvent;
import com.thinkparity.ophelia.model.audit.event.KeyRequestDeniedEvent;
import com.thinkparity.ophelia.model.audit.event.KeyResponseDeniedEvent;
import com.thinkparity.ophelia.model.audit.event.PublishEvent;
import com.thinkparity.ophelia.model.audit.event.ReactivateEvent;
import com.thinkparity.ophelia.model.audit.event.ReceiveEvent;
import com.thinkparity.ophelia.model.audit.event.ReceiveKeyEvent;
import com.thinkparity.ophelia.model.audit.event.RenameEvent;
import com.thinkparity.ophelia.model.audit.event.RequestKeyEvent;
import com.thinkparity.ophelia.model.audit.event.SendConfirmEvent;
import com.thinkparity.ophelia.model.audit.event.SendEvent;
import com.thinkparity.ophelia.model.audit.event.SendKeyEvent;

/**
 * <b>Title:</b>thinkParity Internal Audit Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.6
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalAuditModel extends AuditModel {

    public void audit(final AddTeamMemberConfirmEvent event,
            final JabberId createdBy, final JabberId teamMember)
            throws ParityException;

    public void audit(final AddTeamMemberEvent event,
            final JabberId createdBy, final JabberId teamMember)
            throws ParityException;

	public void audit(final ArchiveEvent event, final JabberId createdBy)
            throws ParityException;

	public void audit(final CloseEvent event, final JabberId createdBy,
            final JabberId closedBy) throws ParityException;

	public void audit(final CreateEvent event);

	public void audit(final CreateRemoteEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException;

    public void audit(final KeyRequestDeniedEvent event,
            final JabberId createdBy, final JabberId deniedBy)
            throws ParityException;

    public void audit(final KeyResponseDeniedEvent event,
            final JabberId createdBy, final JabberId requestedBy)
            throws ParityException;

	public void audit(final PublishEvent event);

	public void audit(final ReactivateEvent event, final JabberId createdBy,
            final JabberId reactivatedBy) throws ParityException;

    public void audit(final ReceiveEvent event);

    public void audit(final ReceiveKeyEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException;

	public void audit(final RenameEvent event, final JabberId createdBy)
        throws ParityException;

	public void audit(final RequestKeyEvent event, final JabberId createdBy,
            final JabberId requestedBy, final JabberId requestedFrom)
            throws ParityException;

	public void audit(final SendConfirmEvent event);

	public void audit(final SendEvent event, final JabberId createdBy,
            final JabberId sentTo) throws ParityException;

	public void audit(final SendKeyEvent event, final JabberId createdBy,
            final JabberId sentTo) throws ParityException;

	public void delete(final Long artifactId);

	public List<AuditEvent> read(final Long artifactId);
}
