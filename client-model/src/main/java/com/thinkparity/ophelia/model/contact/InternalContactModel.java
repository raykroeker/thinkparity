/*
 * Generated On: Jun 27 06 04:14:53 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.contact;

import java.util.Calendar;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.Context;

import com.thinkparity.ophelia.model.InternalModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Contact Internal Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version $Revision$
 */
public class InternalContactModel extends ContactModel implements InternalModel {

    /**
     * Create InternalContactModel
     *
     * @param workspace
     *		A thinkParity workspace.
     * @param context
     *		A thinkParity internal context.
     */
    InternalContactModel(final Workspace workspace, final Context context) {
        super(workspace);
    }

    /**
     * Handle the remote event generated when a contact is deleted.
     * 
     * @param deletedBy
     *            By whom the contact was deleted <code>JabberId</code>.
     * @param deletedOn
     *            When the contact was deleted <code>Calendar</code>.
     */
    public void handleContactDeleted(final JabberId deletedBy,
            final Calendar deletedOn) {
        synchronized (getImplLock()) {
            getImpl().handleContactDeleted(deletedBy, deletedOn);
        }
    }

    /**
     * Handle the remote event generated when a contact is updated.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     * @param updatedOn
     *            When the contact was updated <code>Calendar</code>.
     */
    public void handleContactUpdated(final JabberId contactId,
            final Calendar updatedOn) {
        synchronized (getImplLock()) {
            getImpl().handleContactUpdated(contactId, updatedOn);
        }
    }

    /**
     * Handle the invitation extended remote event.
     * 
     * @param acceptedBy
     *            By whom the invitation was accepted.
     * @param acceptedOn
     *            When the invitation was accepted.
     */
    public void handleInvitationAccepted(final JabberId acceptedBy,
            final Calendar acceptedOn) {
        synchronized (getImplLock()) {
            getImpl().handleInvitationAccepted(acceptedBy, acceptedOn);
        }
    }

    /**
     * Handle the invitation extended remote event.
     * 
     * @param invitedAs
     *            The original invitation e-mail address.
     * @param declinedBy
     *            By whom the invitation was declined.
     * @param declinedOn
     *            When the invitation was declined.
     */
    public void handleInvitationDeclined(final EMail invitedAs,
            final JabberId declinedBy, final Calendar declinedOn) {
        synchronized (getImplLock()) {
            getImpl().handleInvitationDeclined(invitedAs, declinedBy, declinedOn);
        }
    }

    /**
     * Handle the remote contact invitation deleted remote event.
     * 
     * @param invitedAs
     *            The original invitation e-mail address.
     * @param deletedBy
     *            By whom the invitation was deleted.
     * @param deletedOn
     *            When the invitation was deleted.
     */
    public void handleInvitationDeleted(final EMail invitedAs,
            final JabberId deletedBy, final Calendar deletedOn) {
        synchronized (getImplLock()) {
            getImpl().handleInvitationDeleted(invitedAs, deletedBy, deletedOn);
        }
    }

    /**
     * Handle the invitation extended remote event.
     * 
     * @param invitedBy
     *            By whom the invitation was extended.
     * @param invitedOn
     *            When the invitation was extended.
     */
    public void handleInvitationExtended(final EMail invitedAs, final JabberId invitedBy,
            final Calendar invitedOn) {
        synchronized (getImplLock()) {
            getImpl().handleInvitationExtended(invitedAs, invitedBy, invitedOn);
        }
    }
}
