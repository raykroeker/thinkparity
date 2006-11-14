/*
 * Generated On: Jun 27 06 04:14:53 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.contact;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationExtendedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent;

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
    InternalContactModel(final Context context, final Environment environment,
            final Workspace workspace) {
        super(environment, workspace);
    }

    /**
     * Handle the remote event generated when a contact is deleted.
     * 
     * @param deletedBy
     *            By whom the contact was deleted <code>JabberId</code>.
     * @param deletedOn
     *            When the contact was deleted <code>Calendar</code>.
     */
    public void handleContactDeleted(final ContactDeletedEvent event) {
        synchronized (getImplLock()) {
            getImpl().handleContactDeleted(event);
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
    public void handleContactUpdated(final ContactUpdatedEvent event) {
        synchronized (getImplLock()) {
            getImpl().handleContactUpdated(event);
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
    public void handleInvitationAccepted(
            final ContactInvitationAcceptedEvent event) {
        synchronized (getImplLock()) {
            getImpl().handleInvitationAccepted(event);
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
    public void handleInvitationDeclined(
            final ContactInvitationDeclinedEvent event) {
        synchronized (getImplLock()) {
            getImpl().handleInvitationDeclined(event);
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
    public void handleInvitationDeleted(
            final ContactInvitationDeletedEvent event) {
        synchronized (getImplLock()) {
            getImpl().handleInvitationDeleted(event);
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
    public void handleInvitationExtended(
            final ContactInvitationExtendedEvent event) {
        synchronized (getImplLock()) {
            getImpl().handleInvitationExtended(event);
        }
    }
}
