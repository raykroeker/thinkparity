/*
 * Apr 20, 2006
 */
package com.thinkparity.ophelia.model.user;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Internal User Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.4
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalUserModel extends UserModel {

    /**
     * Read a user. If the user does not exist then create the user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    public User readLazyCreate(final JabberId userId);

    /**
     * Read a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>User</code>.
     */
    User read(User user);
}
