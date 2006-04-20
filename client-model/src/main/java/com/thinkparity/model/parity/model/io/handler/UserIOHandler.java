/*
 * Apr 20, 2006
 */
package com.thinkparity.model.parity.model.io.handler;

import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface UserIOHandler {
    public User create(final User user);
    public User read(final JabberId jabberId);
    public User read(final Long userId);
}
