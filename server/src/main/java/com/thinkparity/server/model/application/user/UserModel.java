/*
 * Created On: Mar 1, 2006
 */
package com.thinkparity.desdemona.model.user;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity Desdemona User Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface UserModel {

	public List<User> read();

	public User read(final EMail email);

    /**
     * Read a list of users.
     * 
     * @return A list of users.
     */
    public List<User> read(final Filter<? super User> filter);

    public User read(final JabberId userId);

    public List<User> read(final List<JabberId> userIds);

    public <T extends com.thinkparity.codebase.model.user.UserVCard> T readVCard(
            final T vcard);

    public void updateVCard(
            final com.thinkparity.codebase.model.user.UserVCard vcard);
}
