/*
 * Generated On: Jun 27 06 04:14:53 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.contact;

import java.util.Calendar;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.InternalModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;

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
        context.assertContextIsValid();
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
     * @param declinedBy
     *            By whom the invitation was declined.
     * @param declinedOn
     *            When the invitation was declined.
     */
    public void handleInvitationDeclined(final JabberId declinedBy,
            final Calendar declinedOn) {
        synchronized (getImplLock()) {
            getImpl().handleInvitationDeclined(declinedBy, declinedOn);
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
    public void handleInvitationExtended(final JabberId invitedBy,
            final Calendar invitedOn) {
        synchronized (getImplLock()) {
            getImpl().handleInvitationExtended(invitedBy, invitedOn);
        }
    }
}
