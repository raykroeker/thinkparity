/*
 * Created On:  28-Feb-07 8:23:22 PM
 */
package com.thinkparity.desdemona.model.rules;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity Desdemona Rule Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface RuleModel {

    /**
     * Determine if the model user is restricted from being published to by a
     * user.
     * 
     * @param publishFrom
     *            A publish from user id <code>JabberId</code>.
     * @return True if publish to the user is restricted.
     */
    Boolean isPublishRestrictedFrom(JabberId publishFrom);

    /**
     * Determine if the model user is restricted from publishing to a user.
     * 
     * @param publishTo
     *            A publish to user id <code>JabberId</code>.
     * @return True if publish to the user is restricted.
     */
    Boolean isPublishRestrictedTo(JabberId publishTo);

    /**
     * Determine if the model user is restricted from publishing to any of the
     * e-mail addresses OR users.
     * 
     * @param emails
     *            A <code>List<EMail></code>.
     * @param users
     *            A <code>List<User></code>.
     * @return True if the user is restricted from publishing to any of the
     *         targets.
     */
    Boolean isPublishRestrictedTo(List<EMail> emails, List<User> users);
}
