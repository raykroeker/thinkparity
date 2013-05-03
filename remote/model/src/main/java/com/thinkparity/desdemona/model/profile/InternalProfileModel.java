/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.desdemona.model.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.User;


/**
 * <b>Title:</b>thinkParity DesdemonaModel Internal Profile Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.10
 */
public interface InternalProfileModel extends ProfileModel {

    /**
     * Determine whether or not the model user's queue is readable.
     * 
     * @return True if the profile's queue is readable.
     */
    Boolean isQueueReadable();

    /**
     * Read a user's e-mail addresses.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param user
     *            A <code>User</code>.
     * @return A <code>List</code> of <code>EMail</code> addresses.
     */
    List<EMail> readEMails(JabberId userId, User user);
}
