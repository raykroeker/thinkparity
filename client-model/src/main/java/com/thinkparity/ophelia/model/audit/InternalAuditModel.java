/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.audit;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.audit.event.*;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalAuditModel extends AuditModel {

	/**
	 * Create a InternalAuditModel.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 * @param context
	 *            The parity model context.
	 */
	InternalAuditModel(final Context context, final Environment environment,
            final Workspace workspace) {
		super(environment, workspace);
	}

    public void audit(final AddTeamMemberEvent event,
            final JabberId createdBy, final JabberId teamMember)
            throws ParityException {
        synchronized(getImplLock()) {
                getImpl().audit(event, createdBy, teamMember);
        }
    }

    public void audit(final AddTeamMemberConfirmEvent event,
            final JabberId createdBy, final JabberId teamMember)
            throws ParityException {
        synchronized(getImplLock()) {
                getImpl().audit(event, createdBy, teamMember);
        }
    }

	public void audit(final ArchiveEvent event, final JabberId createdBy)
            throws ParityException {
		synchronized(getImplLock()) { getImpl().audit(event, createdBy); }
	}

	public void audit(final CloseEvent event, final JabberId createdBy,
            final JabberId closedBy) throws ParityException {
		synchronized(getImplLock()) { getImpl().audit(event, createdBy, closedBy); }
	}

	public void audit(final CreateEvent event) {
		synchronized (getImplLock()) {
            getImpl().audit(event);
		}
	}

	public void audit(final CreateRemoteEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException {
		synchronized(getImplLock()) {
            getImpl().audit(event, createdBy, receivedFrom);
        }
	}

    public void audit(final RenameEvent event, final JabberId createdBy)
        throws ParityException {
        synchronized(getImplLock()) { getImpl().audit(event, createdBy); }
    }

    public void audit(final SendConfirmEvent event) {
        synchronized (getImplLock()) {
            getImpl().audit(event);
        }
    }

	public void audit(final KeyRequestDeniedEvent event,
            final JabberId createdBy, final JabberId deniedBy)
            throws ParityException {
		synchronized(getImplLock()) { getImpl().audit(event, createdBy, deniedBy); }
	}

	public void audit(final KeyResponseDeniedEvent event,
            final JabberId createdBy, final JabberId requestedBy)
            throws ParityException {
		synchronized(getImplLock()) { getImpl().audit(event, createdBy, requestedBy); }
	}

    public void audit(final PublishEvent event) {
        synchronized (getImplLock()) {
            getImpl().audit(event);
        }
    }

    public void audit(final ReactivateEvent event, final JabberId createdBy,
            final JabberId reactivatedBy) throws ParityException {
        synchronized(getImplLock()) { getImpl().audit(event, createdBy, reactivatedBy); }
    }

	public void audit(final ReceiveEvent event) {
		synchronized (getImplLock()) {
            getImpl().audit(event);
		}
	}

	public void audit(final ReceiveKeyEvent event, final JabberId createdBy,
            final JabberId receivedFrom) throws ParityException {
		synchronized(getImplLock()) { getImpl().audit(event, createdBy, receivedFrom); }
	}

	public void audit(final RequestKeyEvent event, final JabberId createdBy,
            final JabberId requestedBy, final JabberId requestedFrom)
            throws ParityException {
		synchronized(getImplLock()) { getImpl().audit(event, createdBy, requestedBy, requestedFrom); }
	}

	public void audit(final SendEvent event, final JabberId createdBy,
            final JabberId sentTo) throws ParityException {
		synchronized(getImplLock()) { getImpl().audit(event, createdBy, sentTo); }
	}

	public void audit(final SendKeyEvent event, final JabberId createdBy,
            final JabberId sentTo) throws ParityException {
		synchronized(getImplLock()) { getImpl().audit(event, createdBy, sentTo); }
	}

	public void delete(final Long artifactId) {
		synchronized(getImplLock()) { getImpl().delete(artifactId); }
	}

	public List<AuditEvent> read(final Long artifactId) {
		synchronized(getImplLock()) { return getImpl().read(artifactId); }
	}
}
