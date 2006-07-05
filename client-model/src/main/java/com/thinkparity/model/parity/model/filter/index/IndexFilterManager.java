/*
 * Created On: Jul 4, 2006 6:10:55 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.filter.index;

import java.util.Iterator;
import java.util.List;

import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.index.IndexHit;


/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class IndexFilterManager {

    /** A singleton implementation. */
    private static final IndexFilterManager SINGLETON;

    static { SINGLETON = new IndexFilterManager(); }

    /**
     * Filter a list of index hits.
     * 
     * @param list
     *            A list of index hits.
     * @param filter
     *            An index hit filter.
     */
    public static void filter(final List<? extends IndexHit> list,
            final Filter<? super IndexHit> filter) {
        SINGLETON.doFilter(list, filter);
    }

    /** Create IndexFilterManager. */
    private IndexFilterManager() { super(); }

    /**
     * Filter a list of index hits.
     * 
     * @param list
     *            A list of index hits.
     * @param filter
     *            An index hit filter.
     */
    private void doFilter(final List<? extends IndexHit> list,
            final Filter<? super IndexHit> filter) {
        IndexHit indexHit;
        for (final Iterator<? extends IndexHit> i = list.iterator();
                i.hasNext();) {
            indexHit = i.next();
            if(filter.doFilter(indexHit)) { i.remove(); }
        }
    }
}
