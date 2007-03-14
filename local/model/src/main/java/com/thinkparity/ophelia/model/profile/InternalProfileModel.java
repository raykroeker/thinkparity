/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.ophelia.model.profile;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Internal Profile Model<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.6
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalProfileModel extends ProfileModel {

    /**
     * Create the user's profile locally.
     *
     */
    public Profile create();

    /**
     * Determine if the backup feature is available.
     * 
     * @return True if backup is available for the profile.
     */
    public Boolean isBackupAvailable();

    /**
     * Read the user's credentials.
     * 
     * @return An instance of <code>Credentials</code>.
     */
    public Credentials readCredentials();
}
