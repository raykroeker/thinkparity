/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.ophelia.model.profile;

import java.util.List;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.util.jta.TransactionType;

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
