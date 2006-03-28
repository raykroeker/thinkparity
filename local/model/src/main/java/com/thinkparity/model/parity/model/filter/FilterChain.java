/*
 * Mar 28, 2006
 */
package com.thinkparity.model.parity.model.filter;

import java.util.LinkedList;
import java.util.List;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class FilterChain<T> extends AbstractFilter<T> {

	private final List<Filter<T>> chain;

	/**
	 * Create a FilterChain.
	 * 
	 */
	public FilterChain(final Filter<T> filter) {
		this.chain = new LinkedList<Filter<T>>();
		addFilter(filter);
	}

	public FilterChain addFilter(final Filter<T> filter) {
		chain.add(filter);
		return this;
	}

	/**
	 * @see com.thinkparity.model.parity.model.filter.Filter#doFilter(T)
	 * 
	 */
	public Boolean doFilter(final T o) {
		for(final Filter<T> f : chain) {
			if(f.doFilter(o)) { return true; }
		}
		return false;
	}
}
