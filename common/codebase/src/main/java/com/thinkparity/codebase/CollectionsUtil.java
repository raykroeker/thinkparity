/*
 * Feb 22, 2006
 */
package com.thinkparity.codebase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CollectionsUtil {

    public static <T> List<T> extract(final Map map, final Object key) {
        return null;
    }

	/**
     * Clone a list 1 level deep.
     * 
     * @param l
     *            A list of type T.
     * @return A list of type T.
     */
    public static <T> List<T> clone(final List<T> l) {
        final List<T> clone = new ArrayList<T>(l.size());
        Collections.copy(l, clone);
        return clone;
    }

    /**
	 * Proxy a collection to a list.
	 * 
	 * @param c
	 *            A collection of T.
	 * @return The list of T.
	 */
	public static <T> List<T> proxy(final Collection<T> c) {
		if(null == c) { return null; }
		if(c.isEmpty()) { return Collections.emptyList(); }
		final List<T> l = new LinkedList<T>();
		for(final T t : c) { l.add(t); }
		return l;
	}

    /**
     * Create a list of T from an array.
     * 
     * @param <T>
     *            Any type.
     * @param a
     *            An array of type t.
     * @return A list of type T.
     */
    public static <T> List<T> fromArray(final T[] a) {
        final List<T> l = new ArrayList<T>(a.length);
        for(final T t : a) { l.add(t); }
        return l;
    }

	/**
	 * Create a CollectionsUtil [Singleton]
	 * 
	 */
	private CollectionsUtil() { super(); }
}
