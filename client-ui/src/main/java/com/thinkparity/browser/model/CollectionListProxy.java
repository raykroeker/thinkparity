/*
 * Jan 6, 2006
 */
package com.thinkparity.browser.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.thinkparity.model.parity.model.document.Document;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CollectionListProxy {

	/**
	 * Translate a collection of documents into a list of documents.
	 * 
	 * @param c
	 *            The collection of documents.
	 * @return The list of documents.
	 */
	public static List<Document> translate(final Collection<Document> c) {
		if(null == c) { return null; }
		if(c.isEmpty()) { return Collections.emptyList(); }
		final List<Document> l = new LinkedList<Document>();
		for(Document d : c) { l.add(d); }
		return l;
	}

	static <T> void fromArrayToCollection(T[] a, Collection<T> c) {
		for (T o : a) { c.add(o); /* correct*/ }
	}

	static {
		fromArrayToCollection(new Document[] {}, new Vector<Object>());
	}

	/**
	 * Create a CollectionListProxy [Singleton]
	 * 
	 */
	private CollectionListProxy() { super(); }
}
