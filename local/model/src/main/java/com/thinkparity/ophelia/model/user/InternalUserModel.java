/*
 * Apr 20, 2006
 */
package com.thinkparity.ophelia.model.user;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.user.UserFlag;
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
     * Read the user's flags.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>List</code> of <code>UserFlag</code>s.
     */
    public List<UserFlag> readFlags(final User user);

    /**
     * Read a user. If the user does not exist then create the user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    public User readLazyCreate(final JabberId userId);

    /**
     * Initialize for release v1_0-20070718-1615.
     *
     */
    public void initialize();
}
