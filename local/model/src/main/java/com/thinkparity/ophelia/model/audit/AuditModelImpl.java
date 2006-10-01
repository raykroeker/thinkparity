/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.audit;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.audit.event.*;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.AuditIOHandler;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class AuditModelImpl extends AbstractModelImpl {

	private final AuditIOHandler auditIO;

	/**
	 * Create a AuditModelImpl.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	AuditModelImpl(final Environment environment, final Workspace workspace) {
		super(environment, workspace);
		this.auditIO = IOFactory.getDefault(workspace).createAuditHandler();
	}

    void audit(final AddTeamMemberEvent event, final JabberId createdBy,
            final JabberId teamMember) throws ParityException {
        logger.logApiId();
        logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", teamMember);
        event.setCreatedBy(lookupUser(createdBy));
        event.setTeamMember(lookupUser(teamMember));
        auditIO.audit(event);
    }

    void audit(final AddTeamMemberConfirmEvent event, final JabberId createdBy,
            final JabberId teamMember) throws ParityException {
        logger.logApiId();
        logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", teamMember);
        event.setCreatedBy(lookupUser(createdBy));
        event.setTeamMember(lookupUser(teamMember));
        auditIO.audit(event);
    }

	void audit(final ArchiveEvent event, final JabberId createdBy)
            throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        event.setCreatedBy(lookupUser(createdBy));
		auditIO.audit(event);
	}

	void audit(final CloseEvent event, final JabberId createdBy,
            final JabberId closedBy) throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", closedBy);
        event.setCreatedBy(lookupUser(createdBy));
        event.setClosedBy(lookupUser(closedBy));
		auditIO.audit(event);
	}

    void audit(final SendConfirmEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException {
        logger.logApiId();
        logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", receivedFrom);
        event.setCreatedBy(lookupUser(createdBy));
        event.setConfirmedBy(lookupUser(receivedFrom));
        auditIO.audit(event);
    }

	void audit(final CreateEvent event, final JabberId createdBy)
            throws ParityException {
		logger.logApiId();
        logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        event.setCreatedBy(lookupUser(createdBy));
		auditIO.audit(event);
	}

	void audit(final CreateRemoteEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException {
		logger.logApiId();
        logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", receivedFrom);
        event.setCreatedBy(lookupUser(createdBy));
        event.setReceivedFrom(lookupUser(receivedFrom));
		auditIO.audit(event);
	}

	void audit(final KeyRequestDeniedEvent event, final JabberId createdBy,
            final JabberId deniedBy) throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", deniedBy);
        event.setCreatedBy(lookupUser(createdBy));
        event.setDeniedBy(lookupUser(deniedBy));
		auditIO.audit(event);
	}

	void audit(final KeyResponseDeniedEvent event, final JabberId createdBy,
            final JabberId requestedBy) throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", requestedBy);
        event.setCreatedBy(lookupUser(createdBy));
        event.setRequestedBy(lookupUser(requestedBy));
		auditIO.audit(event);
	}

    void audit(final PublishEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final InternalUserModel userModel = getInternalUserModel();
            userModel.readLazyCreate(event.getPublishedBy());
            auditIO.audit(event);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    void audit(final ReactivateEvent event, final JabberId createdBy,
            final JabberId reactivatedBy) throws ParityException {
        logger.logApiId();
        logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", reactivatedBy);
        event.setCreatedBy(lookupUser(createdBy));
        event.setReactivatedBy(lookupUser(reactivatedBy));
        auditIO.audit(event);
    }

	void audit(final ReceiveEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", receivedFrom);
        event.setCreatedBy(lookupUser(createdBy));
        event.setReceivedFrom(lookupUser(receivedFrom));
		auditIO.audit(event);
	}

	void audit(final ReceiveKeyEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        event.setCreatedBy(lookupUser(createdBy));
        event.setReceivedFrom(lookupUser(receivedFrom));
		auditIO.audit(event);
	}

    void audit(final RenameEvent event, final JabberId createdBy)
            throws ParityException {
        logger.logApiId();
        logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        event.setCreatedBy(lookupUser(createdBy));
        auditIO.audit(event);
    }

	void audit(final RequestKeyEvent event, final JabberId createdBy,
            final JabberId requestedBy, final JabberId requestedFrom)
            throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", requestedBy);
        logger.logVariable("variable", requestedFrom);
        event.setCreatedBy(lookupUser(createdBy));
        event.setRequestedBy(lookupUser(requestedBy));
        event.setRequestedFrom(lookupUser(requestedFrom));
		auditIO.audit(event);
	}

	void audit(final SendEvent event, final JabberId createdBy,
            final JabberId sentTo) throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", sentTo);
        event.setCreatedBy(lookupUser(createdBy));
        event.setSentTo(lookupUser(sentTo));
		auditIO.audit(event);
	}

	void audit(final SendKeyEvent event, final JabberId createdBy,
            final JabberId sentTo) throws ParityException {
		logger.logApiId();
		logger.logVariable("variable", event);
        logger.logVariable("variable", createdBy);
        logger.logVariable("variable", sentTo);
        event.setCreatedBy(lookupUser(createdBy));
        event.setSentTo(lookupUser(sentTo));
		auditIO.audit(event);
	}

	void delete(final Long artifactId) {
		logger.logApiId();
		logger.logVariable("variable", artifactId);
		auditIO.delete(artifactId);
	}

	List<AuditEvent> read(final Long artifactId) {
		logger.logApiId();
		logger.logVariable("variable", artifactId);
		return auditIO.list(artifactId);
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
        final InternalUserModel iUModel = getInternalUserModel();
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
