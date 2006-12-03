/*
 * Created On: Aug 12, 2006 10:56:50 AM
 */
package com.thinkparity.ophelia.browser.util;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.user.User;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class UserUtil {

    /**
     * Determine whether or not the user exists in the list.
     * 
     * @param list
     *            A list of users.
     * @param user
     *            A user.
     * @return True if the user exists in the list; false otherwise.
     */
    public static Boolean contains(final List<? extends User> list,
            final User user) {
        return 0 < indexOf(list, user);
    }

    public static int indexOf(final List<? extends User> list,
            final JabberId userId) {
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).getId().equals(userId))
                return i;
        return -1;
    }

    /**
     * Obtain the index of a user in a user list.
     * 
     * @param list
     *            A list of users.
     * @param user
     *            A user.
     * @return The index of the user in the list; or -1 if the user does not
     *         exist in the list.
     */
    public static Integer indexOf(final List<? extends User> list,
            final User user) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getId().equals(user.getId())) {
                return i;
            }
        }
        return -1;
    }
}
