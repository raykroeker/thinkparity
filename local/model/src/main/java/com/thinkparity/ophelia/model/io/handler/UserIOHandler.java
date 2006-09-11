/*
 * Apr 20, 2006
 */
package com.thinkparity.ophelia.model.io.handler;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.user.User;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface UserIOHandler {
    public void create(final User user);
    public User read(final JabberId jabberId);
    public User read(final Long userId);
}
