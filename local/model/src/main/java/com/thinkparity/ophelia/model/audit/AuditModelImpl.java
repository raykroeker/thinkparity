/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.audit;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.Model;
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
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.AuditIOHandler;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Audit Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public final class AuditModelImpl extends Model implements
        AuditModel, InternalAuditModel {

	private AuditIOHandler auditIO;

	/**
	 * Create a AuditModelImpl.
	 * 
	 */
	public AuditModelImpl() {
		super();
	}

    public void audit(final AddTeamMemberConfirmEvent event, final JabberId createdBy,
            final JabberId teamMember) throws ParityException {
        logger.logApiId();
        logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", teamMember);
        event.setTeamMember(lookupUser(teamMember));
        auditIO.audit(event);
    }

    public void audit(final AddTeamMemberEvent event, final JabberId createdBy,
            final JabberId teamMember) throws ParityException {
        logger.logApiId();
        logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", teamMember);
        event.setTeamMember(lookupUser(teamMember));
        auditIO.audit(event);
    }

    public void audit(final ArchiveEvent event, final JabberId createdBy)
            throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
		auditIO.audit(event);
	}

    public void audit(final CloseEvent event, final JabberId createdBy,
            final JabberId closedBy) throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", closedBy);
        event.setClosedBy(lookupUser(closedBy));
		auditIO.audit(event);
	}

    public void audit(final CreateEvent event) {
		logger.logApiId();
        logger.logVariable("event", event);
		auditIO.audit(event);
	}

    public void audit(final CreateRemoteEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException {
		logger.logApiId();
        logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", receivedFrom);
        event.setReceivedFrom(lookupUser(receivedFrom));
		auditIO.audit(event);
	}

    public void audit(final KeyRequestDeniedEvent event, final JabberId createdBy,
            final JabberId deniedBy) throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", deniedBy);
        event.setDeniedBy(lookupUser(deniedBy));
		auditIO.audit(event);
	}

    public void audit(final KeyResponseDeniedEvent event, final JabberId createdBy,
            final JabberId requestedBy) throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", requestedBy);
        event.setRequestedBy(lookupUser(requestedBy));
		auditIO.audit(event);
	}

    public void audit(final PublishEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final InternalUserModel userModel = getUserModel();
            userModel.readLazyCreate(event.getPublishedBy());
            auditIO.audit(event);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    public void audit(final ReactivateEvent event, final JabberId createdBy,
            final JabberId reactivatedBy) throws ParityException {
        logger.logApiId();
        logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", reactivatedBy);
        event.setReactivatedBy(lookupUser(reactivatedBy));
        auditIO.audit(event);
    }

    public void audit(final ReceiveEvent event) {
		logger.logApiId();
		logger.logVariable("event", event);
		auditIO.audit(event);
	}

    public void audit(final ReceiveKeyEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        event.setReceivedFrom(lookupUser(receivedFrom));
		auditIO.audit(event);
	}

    public void audit(final RenameEvent event, final JabberId createdBy)
            throws ParityException {
        logger.logApiId();
        logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        auditIO.audit(event);
    }

    public void audit(final RequestKeyEvent event, final JabberId createdBy,
            final JabberId requestedBy, final JabberId requestedFrom)
            throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", requestedBy);
        logger.logVariable("variable", requestedFrom);
        event.setRequestedBy(lookupUser(requestedBy));
        event.setRequestedFrom(lookupUser(requestedFrom));
		auditIO.audit(event);
	}

    public void audit(final SendConfirmEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        auditIO.audit(event);
    }

    public void audit(final SendEvent event, final JabberId createdBy,
            final JabberId sentTo) throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", sentTo);
        event.setSentTo(lookupUser(sentTo));
		auditIO.audit(event);
	}

    public void audit(final SendKeyEvent event, final JabberId createdBy,
            final JabberId sentTo) throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", sentTo);
        event.setSentTo(lookupUser(sentTo));
		auditIO.audit(event);
	}

    public void delete(final Long artifactId) {
		logger.logApiId();
		logger.logVariable("variable", artifactId);
		auditIO.delete(artifactId);
	}

    public List<AuditEvent> read(final Long artifactId) {
		logger.logApiId();
		logger.logVariable("variable", artifactId);
		return auditIO.list(artifactId);
	}

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.auditIO = IOFactory.getDefault(workspace).createAuditHandler();
    }

	/**
     * Read the user for the id.  If the user is not already in
     * the local db; the info is read remotely; then stored locally.
     *
     * @param jabberId
     *      The user id.
     * @return The user.
     */
    private User lookupUser(final JabberId jabberId) throws ParityException {
        final InternalUserModel iUModel = getUserModel();
        // read the local user
        User user = iUModel.read(jabberId);
        if(null == user) {
            // the local user doesn't exist; create it
            // from the remote info
            user = iUModel.create(jabberId);
        }
        return user;
    }
}
