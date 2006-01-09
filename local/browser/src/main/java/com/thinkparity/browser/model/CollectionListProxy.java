/*
 * Jan 6, 2006
 */
package com.thinkparity.browser.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.model.document.Document;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CollectionListProxy {

	public static List<Document> translate(final Collection<Document> c) {
		if(null == c) { return null; }
		if(c.isEmpty()) { return Collections.emptyList(); }
		final List<Document> l = new LinkedList<Document>();
		for(Document d : c) { l.add(d); }
		return l;
	}

	/**
	 * Create a CollectionListProxy.
	 */
	public CollectionListProxy() {
		super();
		// TODO Auto-generated constructor stub
	}

}
