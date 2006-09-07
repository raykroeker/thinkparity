/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.audit.event.*;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.AuditIOHandler;
import com.thinkparity.model.parity.model.user.InternalUserModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.user.User;

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
	AuditModelImpl(final Workspace workspace) {
		super(workspace);
		this.auditIO = IOFactory.getDefault().createAuditHandler();
	}

    void audit(final AddTeamMemberEvent event, final JabberId createdBy,
            final JabberId teamMember) throws ParityException {
        logger.info("[LMODEL] [AUDIT] [AUDIT TEAM MEMBER ADDED]");
        logger.debug(event);
        logger.debug(createdBy);
        logger.debug(teamMember);
        event.setCreatedBy(lookupUser(createdBy));
        event.setTeamMember(lookupUser(teamMember));
        auditIO.audit(event);
    }

    void audit(final AddTeamMemberConfirmEvent event, final JabberId createdBy,
            final JabberId teamMember) throws ParityException {
        logger.info("[LMODEL] [AUDIT] [AUDIT CONFIRM TEAM MEMBER ADDED]");
        logger.debug(event);
        logger.debug(createdBy);
        logger.debug(teamMember);
        event.setCreatedBy(lookupUser(createdBy));
        event.setTeamMember(lookupUser(teamMember));
        auditIO.audit(event);
    }

	void audit(final ArchiveEvent event, final JabberId createdBy)
            throws ParityException {
		logger.info("[LMODEL] [AUDIT] [AUDIT ARCHIVE]");
		logger.debug(event);
        logger.debug(createdBy);
        event.setCreatedBy(lookupUser(createdBy));
		auditIO.audit(event);
	}

	void audit(final CloseEvent event, final JabberId createdBy,
            final JabberId closedBy) throws ParityException {
		logger.info("[LMODEL] [AUDIT] [AUDIT CLOSE]");
		logger.debug(event);
        logger.debug(createdBy);
        logger.debug(closedBy);
        event.setCreatedBy(lookupUser(createdBy));
        event.setClosedBy(lookupUser(closedBy));
		auditIO.audit(event);
	}

    void audit(final SendConfirmEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException {
        logger.info("[LMODEL] [AUDIT] [AUDIT CONFIRMATION RECEIPT]");
        logger.debug(event);
        logger.debug(createdBy);
        logger.debug(receivedFrom);
        event.setCreatedBy(lookupUser(createdBy));
        event.setConfirmedBy(lookupUser(receivedFrom));
        auditIO.audit(event);
    }

	void audit(final CreateEvent event, final JabberId createdBy)
            throws ParityException {
		logger.info("[LMODEL] [AUDIT] [AUDIT CREATE]");
        logger.debug(event);
        logger.debug(createdBy);
        event.setCreatedBy(lookupUser(createdBy));
		auditIO.audit(event);
	}

	void audit(final CreateRemoteEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException {
		logger.info("[LMODEL] [AUDIT] [AUDIT CREATE REMOTE]");
        logger.debug(event);
        logger.debug(createdBy);
        logger.debug(receivedFrom);
        event.setCreatedBy(lookupUser(createdBy));
        event.setReceivedFrom(lookupUser(receivedFrom));
		auditIO.audit(event);
	}

	void audit(final KeyRequestDeniedEvent event, final JabberId createdBy,
            final JabberId deniedBy) throws ParityException {
		logger.info("[LMODEL] [AUDIT] [KEY REQUEST DENIED]");
		logger.debug(event);
        logger.debug(createdBy);
        logger.debug(deniedBy);
        event.setCreatedBy(lookupUser(createdBy));
        event.setDeniedBy(lookupUser(deniedBy));
		auditIO.audit(event);
	}

	void audit(final KeyResponseDeniedEvent event, final JabberId createdBy,
            final JabberId requestedBy) throws ParityException {
		logger.info("[LMODEL] [AUDIT] [KEY RESPONSE DENIED]");
		logger.debug(event);
        logger.debug(createdBy);
        logger.debug(requestedBy);
        event.setCreatedBy(lookupUser(createdBy));
        event.setRequestedBy(lookupUser(requestedBy));
		auditIO.audit(event);
	}

    void audit(final PublishEvent event, final JabberId createdBy)
            throws ParityException {
        logger.info("[LMODEL] [AUDIT] [AUDIT PUBLISH]");
        logger.debug(event);
        logger.debug(createdBy);
        event.setCreatedBy(lookupUser(createdBy));
        auditIO.audit(event);
    }

    void audit(final ReactivateEvent event, final JabberId createdBy,
            final JabberId reactivatedBy) throws ParityException {
        logger.info("[LMODEL] [AUDIT] [AUDIT REACTIVATE]");
        logger.debug(event);
        logger.debug(createdBy);
        logger.debug(reactivatedBy);
        event.setCreatedBy(lookupUser(createdBy));
        event.setReactivatedBy(lookupUser(reactivatedBy));
        auditIO.audit(event);
    }

	void audit(final ReceiveEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException {
		logger.info("[LMODEL] [AUDIT] [AUDIT RECEIVE]");
		logger.debug(event);
        logger.debug(createdBy);
        logger.debug(receivedFrom);
        event.setCreatedBy(lookupUser(createdBy));
        event.setReceivedFrom(lookupUser(receivedFrom));
		auditIO.audit(event);
	}

	void audit(final ReceiveKeyEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException {
		logger.info("[LMODEL] [AUDIT] [AUDIT RECEIVE KEY]");
		logger.debug(event);
        logger.debug(createdBy);
        event.setCreatedBy(lookupUser(createdBy));
        event.setReceivedFrom(lookupUser(receivedFrom));
		auditIO.audit(event);
	}

    void audit(final RenameEvent event, final JabberId createdBy)
            throws ParityException {
        logger.info("[LMODEL] [AUDIT] [AUDIT RENAME]");
        logger.debug(event);
        logger.debug(createdBy);
        event.setCreatedBy(lookupUser(createdBy));
        auditIO.audit(event);
    }

	void audit(final RequestKeyEvent event, final JabberId createdBy,
            final JabberId requestedBy, final JabberId requestedFrom)
            throws ParityException {
		logger.info("[LMODEL] [AUDIT] [AUDIT REQUEST KEY]");
		logger.debug(event);
        logger.debug(createdBy);
        logger.debug(requestedBy);
        logger.debug(requestedFrom);
        event.setCreatedBy(lookupUser(createdBy));
        event.setRequestedBy(lookupUser(requestedBy));
        event.setRequestedFrom(lookupUser(requestedFrom));
		auditIO.audit(event);
	}

	void audit(final SendEvent event, final JabberId createdBy,
            final JabberId sentTo) throws ParityException {
		logger.info("[LMODEL] [AUDIT] [AUDIT SEND]");
		logger.debug(event);
        logger.debug(createdBy);
        logger.debug(sentTo);
        event.setCreatedBy(lookupUser(createdBy));
        event.setSentTo(lookupUser(sentTo));
		auditIO.audit(event);
	}

	void audit(final SendKeyEvent event, final JabberId createdBy,
            final JabberId sentTo) throws ParityException {
		logger.info("[LMODEL] [AUDIT] [AUDIT SEND KEY]");
		logger.debug(event);
        logger.debug(createdBy);
        logger.debug(sentTo);
        event.setCreatedBy(lookupUser(createdBy));
        event.setSentTo(lookupUser(sentTo));
		auditIO.audit(event);
	}

	void delete(final Long artifactId) {
		logger.info("[LMODEL] [AUDIT] [DELETE]");
		logger.debug(artifactId);
		auditIO.delete(artifactId);
	}

	List<AuditEvent> read(final Long artifactId) {
		logger.info("[LMODEL] [AUDIT] [READ]");
		logger.debug(artifactId);
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
