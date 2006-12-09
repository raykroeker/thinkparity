/*
 * Created On: Jun 27, 2006 4:31:11 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.util.sort.user;

import java.util.Comparator;

import com.thinkparity.codebase.sort.DefaultComparator;

import com.thinkparity.codebase.model.user.User;


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

    public static Comparator<User> createOrganizationAndName(
            final Boolean ascending) {
        final DefaultComparator<User> comparator = new NameComparator(ascending);
        comparator.add(new OrganizationComparator(ascending));
        return comparator;
    }

    /** Create UserComparatorFactory. */
    private UserComparatorFactory() { super(); }
}
