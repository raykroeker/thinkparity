/*
 * Generated On: Jun 27 06 12:13:12 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.audit.event.AuditEvent;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;

/**
 * <b>Title:</b>thinkParity Container Internal Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version $Revision$
 */
public class InternalContainerModel extends ContainerModel implements InternalModel {

    /**
     * Create InternalContainerModel
     *
     * @param workspace
     *		A thinkParity workspace.
     * @param context
     *		A thinkParity internal context.
     */
    InternalContainerModel(final Workspace workspace, final Context context) {
        super(workspace);
        context.assertContextIsValid();
    }

    /**
     * Create a new version for a container.
     * 
     * @param containerId
     *            A container id.
     * @return A new container version.
     */
    public ContainerVersion createVersion(final Long containerId) {
        synchronized(getImplLock()) { return getImpl().createVersion(containerId); }
    }

    /**
     * Handle a remote close event.
     * 
     * @param containerId
     *            The container id.
     * @param closedBy
     *            By whom the container was closed.
     * @param closedOn
     *            When the container was closed.
     */
    public void handleClose(final Long containerId, final JabberId closedBy,
            final Calendar closedOn) throws ParityException {
        synchronized(getImplLock()) {
            getImpl().handleClose(containerId, closedBy, closedOn);
        }
    }

    /**
     * Determine if the container has been locally modified.
     * 
     * @param containerId
     * @return True if the container has been locally modified.
     */
    public Boolean isLocallyModified(final Long containerId)
            throws ParityException {
        synchronized(getImplLock()) {
            return getImpl().isLocallyModified(containerId);
        }
    }

    /**
     * Lock the container.
     * 
     * @param containerId
     *            The container id.
     */
    public void lock(final Long containerId) throws ParityException {
        synchronized(getImplLock()) { getImpl().lock(containerId); }
    }

    /**
     * Read the list of audit events for a container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of audit events.
     */
    public List<AuditEvent> readAuditEvents(final Long containerId) {
        synchronized(getImplLock()) {
            return getImpl().readAuditEvents(containerId);
        }
    }

    /**
     * Read the container draft.
     * 
     * @param containerId
     *            A container id.
     * @return A container draft.
     */
    public ContainerDraft readDraft(final Long containerId) {
        synchronized(getImplLock()) { return getImpl().readDraft(containerId); }
    }

}
