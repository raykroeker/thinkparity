/*
 * Created On: Sep 11, 2006 12:38:13 PM
 */
package com.thinkparity.codebase.filter;

import java.util.Iterator;
import java.util.List;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class FilterManager {

    /** A singleton instance. */
    private static final FilterManager SINGLETON;

    static { SINGLETON = new FilterManager(); }

    /**
     * Create a default filter of a given type. The default filter will not
     * filter anything.
     * 
     * @param <T>
     *            A filter type.
     * @return A <code>Filter</code>.
     */
    public static <T> Filter<T> createDefault() {
        return SINGLETON.doCreateDefault();
    }

    /**
     * Filter a list with the given filter.
     * 
     * @param <T>
     *            A filter type.
     * @param list
     *            A <code>List&lt;T&gt;</code>.
     * @param filter
     *            A <code>Filter&lt;T&gt;</code>.
     */
    public static <T> void filter(final List<T> list, final Filter<? super T> filter) {
        SINGLETON.doFilter(list, filter);
    }

    /** Create FilterManager. */
    private FilterManager() {
        super();
    }

    /**
     * Create a default filter of a given type. The default filter will not
     * filter anything.
     * 
     * @param <T>
     *            A filter type.
     * @return A <code>Filter</code>.
     */
    private <T> Filter<T> doCreateDefault() {
        return new Filter<T> () {
            public Boolean doFilter(final T o) {
                return false;
            }
        };
    }

    /**
     * Filter a list with the given filter.
     * 
     * @param <T>
     *            A filter type.
     * @param list
     *            A <code>List&lt;T&gt;</code>.
     * @param filter
     *            A <code>Filter&lt;T&gt;</code>.
     */
    private <T> void doFilter(final List<T> list, final Filter<? super T> filter) {
        T o;
        for(final Iterator<T> i = list.iterator(); i.hasNext();) {
            o = i.next();
            if (filter.doFilter(o)) {
                i.remove();
            }
        }
    }
}
