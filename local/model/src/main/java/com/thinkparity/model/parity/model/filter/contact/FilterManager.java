/*
 * Created On: Jul 17, 2006 2:55:22 PM
 */
package com.thinkparity.model.parity.model.filter.contact;

import java.util.Iterator;
import java.util.List;

import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.xmpp.contact.Contact;


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
}
