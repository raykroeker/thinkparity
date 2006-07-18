/*
 * Created On: Jun 27, 2006 4:31:56 PM
 */
package com.thinkparity.model.parity.model.sort.contact;

import java.util.Comparator;

import com.thinkparity.model.parity.model.sort.AbstractComparator;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class NameComparator extends AbstractComparator<Contact> implements
        Comparator<Contact> {

    /**
     * Create NameComparator.
     * 
     * @param doCompareAscending
     *            Compare in ascending order.
     */
    public NameComparator(final Boolean doCompareAscending) {
        super(doCompareAscending);
    }

    /**
     * @see java.util.Comparator#compare(T, T)
     * 
     */
    public int compare(final Contact o1, final Contact o2) {
        final int compareResult = o1.getName().compareTo(o2.getName());
        if(0 == compareResult) { return subCompare(o1, o2); }
        else { return compareResult * resultMultiplier; }
    }
}
