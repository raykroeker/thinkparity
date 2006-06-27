/*
 * Created On: Jun 27, 2006 4:31:11 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.sort.user;

import java.util.Comparator;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class UserComparatorFactory {

    /**
     * Create a user name comparator.
     * 
     * @param isAsending
     *            Whether or not to compare in ascending order.
     * @return The user comparator.
     */
    public static Comparator<User> createName(final Boolean isAsending) {
        return new NameComparator(isAsending);
    }

    /** Create UserComparatorFactory. */
    private UserComparatorFactory() { super(); }
}
