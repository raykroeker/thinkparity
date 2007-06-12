/*
 * Created On:  28-Feb-07 8:23:22 PM
 */
package com.thinkparity.desdemona.model.rules;

import com.thinkparity.codebase.jabber.JabberId;

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
    public Boolean isPublishRestrictedFrom(final JabberId publishFrom);

    
    /**
     * Determine if the model user is restricted from publishing to a user.
     * 
     * @param publishTo
     *            A publish to user id <code>JabberId</code>.
     * @return True if publish to the user is restricted.
     */
    public Boolean isPublishRestrictedTo(final JabberId publishTo);
}
