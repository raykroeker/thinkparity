/*
 * Created On: Jul 17, 2006 2:55:22 PM
 */
package com.thinkparity.ophelia.model.util.filter.contact;

import java.util.Iterator;
import java.util.List;

import com.thinkparity.codebase.model.contact.Contact;


import com.thinkparity.ophelia.model.contact.ContactInvitation;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;
import com.thinkparity.ophelia.model.util.filter.Filter;


/**
 * @author raymond@thinkparity.com
 * @version
 */
public class FilterManager {

    /** A thinkParity filter manager singleton implementation. */
    private static final FilterManager SINGLETON;

    static { SINGLETON = new FilterManager(); }

    /**
     * Filter a list of contacts.
     * 
     * @param list
     *            A list of contacts.
     * @param filter
     *            A contact filter.
     */
    public static void filter(final List<Contact> list,
            final Filter<? super Contact> filter) {
        SINGLETON.doFilter(list, filter);
    }

    public static void filterIncomingInvitations(final List<IncomingInvitation> list,
            final Filter<? super ContactInvitation> filter) {
        SINGLETON.doFilterIncomingInvitations(list, filter);
    }

    public static void filterOutgoingInvitations(final List<OutgoingInvitation> list,
            final Filter<? super ContactInvitation> filter) {
        SINGLETON.doFilterOutgoingInvitations(list, filter);
    }

    /** Create FilterManager. */
    private FilterManager() { super(); }

    /**
     * Filter a list of contacts.
     * 
     * @param list
     *            A list of contacts.
     * @param filter
     *            A contact filter.
     */
    private void doFilter(final List<Contact> list,
            final Filter<? super Contact> filter) {
        Contact contact;
        for(final Iterator<Contact> i = list.iterator(); i.hasNext();) {
            contact = i.next();
            if(filter.doFilter(contact)) { i.remove(); }
        }
    }

    /**
     * Filter a list of contact invitations.
     * 
     * @param list
     *            A list of contacts.
     * @param filter
     *            A contact filter.
     */
    private void doFilterIncomingInvitations(final List<IncomingInvitation> list,
            final Filter<? super ContactInvitation> filter) {
        IncomingInvitation invitation;
        for(final Iterator<IncomingInvitation> i = list.iterator(); i.hasNext();) {
            invitation = i.next();
            if(filter.doFilter(invitation)) { i.remove(); }
        }
    }

    /**
     * Filter a list of contact invitations.
     * 
     * @param list
     *            A list of contacts.
     * @param filter
     *            A contact filter.
     */
    private void doFilterOutgoingInvitations(final List<OutgoingInvitation> list,
            final Filter<? super ContactInvitation> filter) {
        OutgoingInvitation invitation;
        for(final Iterator<OutgoingInvitation> i = list.iterator(); i.hasNext();) {
            invitation = i.next();
            if(filter.doFilter(invitation)) { i.remove(); }
        }
    }
}
