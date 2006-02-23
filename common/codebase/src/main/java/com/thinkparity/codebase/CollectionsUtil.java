/*
 * Feb 22, 2006
 */
package com.thinkparity.codebase;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CollectionsUtil {

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
	 * Create a CollectionsUtil [Singleton]
	 * 
	 */
	private CollectionsUtil() { super(); }
}
