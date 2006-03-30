/*
 * Mar 29, 2006
 */
package com.thinkparity.model.parity.model.filter.artifact;

import java.util.List;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.filter.AbstractFilter;
import com.thinkparity.model.parity.model.index.IndexHit;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Search extends AbstractFilter<Artifact> {

	/**
	 * Result of an index search.
	 * 
	 */
	private final List<IndexHit> searchResults;

	/**
	 * Create a Search.
	 */
	public Search(final List<IndexHit> searchResults) {
		this.searchResults = searchResults;
	}

	/**
	 * @see com.thinkparity.model.parity.model.filter.Filter#doFilter(T)
	 * 
	 */
	public Boolean doFilter(final Artifact o) {
		for(final IndexHit indexHit : searchResults) {
			if(indexHit.getId().equals(o.getId())) { return Boolean.FALSE; }
		}
		return Boolean.TRUE;
	}

	public void setResults(final List<IndexHit> searchResults) {
		this.searchResults.clear();
		this.searchResults.addAll(searchResults);
	}
}
