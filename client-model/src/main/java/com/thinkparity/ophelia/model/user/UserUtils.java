/*
 * Created On: Oct 20 2006 08:35
 */
package com.thinkparity.ophelia.model.user;

import java.util.List;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.User;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class UserUtils {

    /** A singleton instance. */
    private static final UserUtils INSTANCE;

    static {
        INSTANCE = new UserUtils();
    }

    /**
     * Obtain an instance of the user utils.
     * 
     * @return A <code>UserUtils</code>.
     */
    public static UserUtils getInstance() {
        return INSTANCE;
    }

    /** Create UserUtils. */
    private UserUtils() {
        super();
    }

    /**
     * Determine if a list contains a user.
     * 
     * @param <U>
     *            A type of <code>User</code>.
     * @param list
     *            A <code>User</code> <code>List</code>.
     * @param o
     *            A user id <code>JabberId</code>.
     * @return True if the list contains the user.
     */
    public <T extends User> boolean contains(final List<T> list,
            final JabberId o) {
        return 0 < indexOf(list, o);
    }

    /**
     * Determine if a list contains a user.
     * 
     * @param <U>
     *            A type of <code>User</code>.
     * @param list
     *            A <code>User</code> <code>List</code>.
     * @param o
     *            A <code>User</code>
     * @return True if the list contains the user.
     */
    public <T extends User, U extends User> boolean contains(
            final List<T> list, final U o) {
        return 0 < indexOf(list, o);
    }

    /**
     * Filter a list of users by name.
     * 
     * @param <U>
     *            A user type.
     * @param users
     *            A list of users.
     * @param names
     *            A list of user's names.
     */
    public <T extends User> void filter(final List<T> users,
            final String... names) {
        if (null == names)
            throw new NullPointerException();
        FilterManager.filter(users, new Filter<T>() {
             public Boolean doFilter(final T o) {
                 for (final String name : names) {
                     if (o.getName().equals(name)) {
                         return Boolean.FALSE;
                     }
                 }
                 return Boolean.TRUE;
             }
        });
    }

    /**
     * Obtain the index of a user in the list.
     * 
     * @param <U>
     *            A type of <code>User</code>.
     * @param list
     *            A user <code>List</code>.
     * @param o
     *            A <code>User</code>
     * @return The index of the first user in the list with a matching id; or -1
     *         if no such user exists.
     */
    public <T extends User, U extends User> int indexOf(final List<T> list,
            final U o) {
        return indexOf(list, o.getId());
    }

    /**
     * Obtain the index of a user in the list with the given id.
     * 
     * @param <U>
     *            A type of <code>User</code>.
     * @param list
     *            A user <code>List</code>.
     * @param id
     *            A user id <code>JabberId</code>.
     * @return The index of the first user in the list with a matching id; or -1
     *         if no such user exists.
     */
    public <U extends User> int indexOf(final List<U> list, final JabberId o) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(o))
                return i;
        }
        return -1;
    }

    /**
     * Remove a user from a list.
     * 
     * @param <U>
     *            A type of <code>User</code>.
     * @param list
     *            A <code>User</code> list.
     * @param o
     *            A <code>User</code>.
     * @return True if the list contained the <code>User</code>.
     */
    public <T extends User, U extends User> boolean remove(final List<T> list,
            final U o) {
        if (contains(list, o)) {
            list.remove(indexOf(list, o));
            return true;
        } else {
            return false;
        }
    }
}
