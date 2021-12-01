/*
 * Mar 28, 2006
 */
package com.thinkparity.codebase.filter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A filter chain is an ordered set of filters linked together.
 * 
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class FilterChain<T> extends AbstractFilter<T> implements Iterable<Filter<T>> {

	/** An ordered set of filters. */
	private final Set<Filter<T>> chain;

	/** Create FilterChain. */
	public FilterChain() {
        this.chain = new HashSet<Filter<T>>();
	}

	/**
     * Create FilterChain.
     * 
     * @param filter
     *            A <code>Filter</code>.
     * 
     * @see FilterChain#addFilter(Filter)
     */
	public FilterChain(final Filter<T> filter) {
		this();
		addFilter(filter);
	}

	/**
     * Add a filter to the chain.
     * 
     * @param filter
     *            A <code>Filter</code>.
     * @return A reference to the filter chain.
     */
	public FilterChain<T> addFilter(final Filter<T> filter) {
		chain.add(filter);
		return this;
	}

	/**
     * Removes all filters from the filter chain.
     * 
     * @return A reference to the filter chain.
     */
    public FilterChain<T> clearFilters() {
        chain.clear();
        return this;
    }

    /**
     * Determine whether or not the filter chain contains the filter.
     * 
     * @param filter
     *            The filter.
     * @return True if the filter chain contains the filter; false otherwise.
     * 
     * @see List#contains(java.lang.Object)
     */
	public Boolean containsFilter(final Filter<T> filter) {
		return chain.contains(filter);
	}

	/**
	 * @see com.thinkparity.ophelia.model.util.filter.Filter#doFilter(T)
	 * 
	 */
	public Boolean doFilter(final T o) {
		for(final Filter<T> f : chain) {
			if(f.doFilter(o)) { return true; }
		}
		return false;
	}

    /**
     * Determine whether or not the filter chain is empty.
     * 
     * @return True if it is empty; false otherwise.
     */
    public Boolean isEmpty() { return chain.isEmpty(); }

    /**
     * @see java.lang.Iterable#iterator()
     * 
     */
    public Iterator<Filter<T>> iterator() { return chain.iterator(); }

    /**
     * Remove a filter from the filter chain.
     * 
     * @param filter
     *            The filter.
     * @return A reference to the filter chain.
     */
	public FilterChain<T> removeFilter(final Filter<T> filter) {
		chain.remove(filter);
		return this;
	}
}
