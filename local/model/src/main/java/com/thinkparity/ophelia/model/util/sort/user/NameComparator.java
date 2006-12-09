/*
 * Created On: Jun 27, 2006 4:31:56 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.util.sort.user;

import com.thinkparity.codebase.sort.DefaultComparator;
import com.thinkparity.codebase.sort.StringComparator;

import com.thinkparity.codebase.model.user.User;


/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class NameComparator extends DefaultComparator<User> {

    /** A general purpose string comparator. */
    private final StringComparator comparator;

    /**
     * Create NameComparator.
     * 
     * @param doCompareAscending
     *            Compare in ascending order.
     */
    NameComparator(final Boolean ascending) {
        super();
        this.comparator = new StringComparator(ascending);
    }

    /**
     * @see java.util.Comparator#compare(T, T)
     * 
     */
    public int compare(final User o1, final User o2) {
        final int compareResult = comparator.compare(o1.getName(), o2.getName());
        if (0 == compareResult) {
            return subCompare(o1, o2);
        } else {
            return compareResult;
        }
    }
}
