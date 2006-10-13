/*
 * Created On: Jul 4, 2006 6:10:55 PM
 */
package com.thinkparity.ophelia.model.util.filter.history;

import java.util.Iterator;
import java.util.List;

import com.thinkparity.ophelia.model.audit.HistoryItem;
import com.thinkparity.ophelia.model.util.filter.Filter;


/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class HistoryFilterManager {

    /** A singleton implementation. */
    private static final HistoryFilterManager SINGLETON;

    static { SINGLETON = new HistoryFilterManager(); }

    /**
     * Filter a list of history item.
     * 
     * @param list
     *            A list of history items.
     * @param filter
     *            An history item filter.
     */
    public static void filter(final List<? extends HistoryItem> list,
            final Filter<? super HistoryItem> filter) {
        SINGLETON.doFilter(list, filter);
    }

    /** Create IndexFilterManager. */
    private HistoryFilterManager() { super(); }

    /**
     * Filter a list of index hits.
     * 
     * @param list
     *            A list of index hits.
     * @param filter
     *            An index hit filter.
     */
    private void doFilter(final List<? extends HistoryItem> list,
            final Filter<? super HistoryItem> filter) {
        HistoryItem historyItem;
        for (final Iterator<? extends HistoryItem> i = list.iterator();
                i.hasNext();) {
            historyItem = i.next();
            if(filter.doFilter(historyItem)) { i.remove(); }
        }
    }
}
