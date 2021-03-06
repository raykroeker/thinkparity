/*
 * Created On: Jun 27, 2006 4:31:56 PM
 */
package com.thinkparity.ophelia.model.util.sort.contact;

import com.thinkparity.codebase.sort.DefaultComparator;
import com.thinkparity.codebase.sort.StringComparator;

import com.thinkparity.codebase.model.contact.Contact;


/**
 * @author raymond@thinkparity.com
 * @version
 */
public class NameComparator extends DefaultComparator<Contact> {

    /** A general purpose string comparator. */
    private final StringComparator comparator;

    /**
     * Create NameComparator.
     * 
     * @param doCompareAscending
     *            Compare in ascending order.
     */
    public NameComparator(final Boolean doCompareAscending) {
        super();
        this.comparator = new StringComparator(doCompareAscending);
    }

    /**
     * @see java.util.Comparator#compare(T, T)
     * 
     */
    public int compare(final Contact o1, final Contact o2) {
        final int compareResult = comparator.compare(o1.getName(), o2.getName());
        if (0 == compareResult) {
            return subCompare(o1, o2);
        } else {
            return compareResult;
        }
    }
}
