/*
 * Created On: Aug 14, 2006 12:18:10 PM
 */
package com.thinkparity.model.parity.model.sort.contact;

import java.util.Comparator;

import com.thinkparity.model.parity.model.contact.ContactInvitation;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InvitationIdComparator implements Comparator<ContactInvitation> {

    /** Create InvitationComparator. */
    public InvitationIdComparator() {
        super();
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(final ContactInvitation o1, final ContactInvitation o2) {
        return o1.getId().compareTo(o2.getId());
    }
}
