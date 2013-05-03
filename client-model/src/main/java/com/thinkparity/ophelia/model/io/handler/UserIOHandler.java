/*
 * Apr 20, 2006
 */
package com.thinkparity.ophelia.model.io.handler;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.user.UserFlag;

/**
 * <b>Title:</b>thinkParity OpheliaModel User IO<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public interface UserIOHandler {

    /**
     * Read the list of flags for the user.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return A <code>List</code> of <code>UserFlag</code>.
     */
    public void applyFlags(final Long userId, final List<UserFlag> flags);

    /**
     * Create a user.
     * 
     * @param user
     *            A <code>User</code>.
     */
    public void create(final User user);

    /**
     * Read all users.
     * 
     * @return A <code>List<User></code>.
     */
    public List<User> read();

    /**
     * Read a user.
     * 
     * @param jabberId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    public User read(final JabberId jabberId);

    /**
     * Read a user.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return A <code>User</code>.
     */
    public User read(final Long userId);

    /**
     * Read the list of flags for the user.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return A <code>List</code> of <code>UserFlag</code>.
     */
    public List<UserFlag> readFlags(final Long userId);
}
