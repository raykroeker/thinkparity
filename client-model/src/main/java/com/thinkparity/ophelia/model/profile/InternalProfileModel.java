/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.ophelia.model.profile;

import java.util.List;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.util.jta.TransactionType;
import com.thinkparity.codebase.model.util.xmpp.event.profile.ActiveEvent;
import com.thinkparity.codebase.model.util.xmpp.event.profile.payment.PaymentEvent;
import com.thinkparity.codebase.model.util.xmpp.event.profile.payment.PaymentPlanArrearsEvent;

import com.thinkparity.ophelia.model.util.ProcessMonitor;

/**
 * <b>Title:</b>thinkParity Internal Profile Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.12
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalProfileModel extends ProfileModel {

    /**
     * Handle a profile disabled event.
     * 
     * @param event
     *            A <code>DisabledEvent</code>.
     */
    void handleEvent(ActiveEvent event);

    /**
     * Handle a profile payment plan failed event.
     * 
     * @param event
     *            A <code>PaymentEvent</code>.
     */
    void handleEvent(PaymentEvent event);

    /**
     * Handle a profile payment plan in arrears event.
     * 
     * @param event
     *            A <code>PaymentPlanArrearsEvent</code>.
     */
    void handleEvent(PaymentPlanArrearsEvent event);

    /**
     * Initialize the user's profile. Download the profile data from the server
     * and save it locally.
     * 
     */
    void initialize();

    /**
     * Read the user's credentials.
     * 
     * @return An instance of <code>Credentials</code>.
     */
    Credentials readCredentials();

    /**
     * Read a list of the profile e-mail addresses.
     * 
     * @return A <code>List<ProfileEMail></code>.
     */
    List<ProfileEMail> readEMails();

    /**
     * Restore backup.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     */
    void restoreBackup(ProcessMonitor monitor);

    /**
     * Update the release for the profile.
     * 
     * @param release
     *            A <code>Release</code>.
     */
    void updateProductRelease();
}
