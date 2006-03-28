/*
 * Mar 28, 2006
 */
package com.thinkparity.model.parity.model.filter;

import java.util.Iterator;
import java.util.List;

import com.thinkparity.model.parity.model.artifact.Artifact;

/**
 * The ModelFilterManager is a singleton responsible for filtering the lists of
 * elements returned by the model.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ModelFilterManager {

	/**
	 * The filter manager singleont.
	 * 
	 */
	private static final ModelFilterManager SINGLETON;

	static { SINGLETON = new ModelFilterManager(); }

	/**
     * Filter a list of artifacts.
     * 
     * @param list
     *            The list of artifacts.
     * @param filter
     *            The artifact filter.
     */
	public static void filter(final List<? extends Artifact> list,
			final Filter<? super Artifact> filter) {
		SINGLETON.doFilter(list, filter);
	}

	/**
	 * Create a ModelFilterManager [Singleton]
	 * 
	 */
	private ModelFilterManager() { super(); }

	/**
     * Filter a list of artifacts.
     * 
     * @param list
     *            The list of artifacts.
     * @param filter
     *            The artifact filter.
     */
	private void doFilter(final List<? extends Artifact> list,
			final Filter<? super Artifact> filter) {
		Artifact artifact = null;
		for(final Iterator<? extends Artifact> i = list.iterator(); i.hasNext();) {
			artifact = i.next();
			if(filter.doFilter(artifact)) { i.remove(); }
		}
	}
}
