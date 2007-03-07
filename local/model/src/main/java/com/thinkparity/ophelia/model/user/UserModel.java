/*
 * Apr 20, 2006
 */
package com.thinkparity.ophelia.model.user;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity User Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface UserModel {

    /**
     * Read a user.
     *
     * @param userId
     *      A user id.
     * @return A user.
     */
    public User read(final Long userId);

    /**
     * Read a user.
     *
     * @param jabberId
     *      The user's jabber id.
     * @return The user.
     */
    public User read(final JabberId jabberId);
}
