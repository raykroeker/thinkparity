/*
 * Created On: Oct 20 2006 08:35
 */
package com.thinkparity.ophelia.model.user;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.User;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 * TODO move to common/model
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
     * Extract the user's ids into a list.
     * 
     * @param <T>
     *            A user type.
     * @param users
     *            A <code>List</code> of <code>T</code>.
     * @param userIds
     *            A <code>List</code> to populate.
     * @return A <code>List</code> of <code>JabberId</code>s.
     */
    public <T extends User> List<JabberId> getIds(final List<T> users,
            final List<JabberId> userIds) {
        for (final User user : users)
            userIds.add(user.getId());
        return userIds;
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
        return -1 < indexOf(list, o);
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
        return -1 < indexOf(list, o);
    }

    /**
     * Determine if the map contains the user key.
     * 
     * @param <T>
     *            A <code>User</code> type.
     * @param <U>
     *            A <code>User</code> type.
     * @param map
     *            A <code>Map</code>.
     * @param user
     *            A <code>User</code>.
     * @return True if the user exists in the map as a key entry.
     */
    public <T extends User, U extends User> boolean containsKey(final Map<T, ?> map,
            final U user) {
        for (final Iterator<T> iUsers = map.keySet().iterator();
                iUsers.hasNext();)
            if (iUsers.next().getId().equals(user.getId()))
                return true;
        return false;
    }

    public <T extends User> boolean containsUser(final List<JabberId> userIds,
            final T user) {
        return -1 < indexOfUser(userIds, user);
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
    public <T extends User, U extends User> int indexOf(final List<T> users,
            final U user) {
        return indexOf(users, user.getId());
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

    public <U extends User> int indexOfUser(final List<JabberId> userIds,
            final U user) {
        for (int i = 0; i < userIds.size(); i++) {
            if (userIds.get(i).equals(user.getId()))
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
