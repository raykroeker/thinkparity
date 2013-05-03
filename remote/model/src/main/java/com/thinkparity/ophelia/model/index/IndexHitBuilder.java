/*
 * Mar 7, 2006
 */
package com.thinkparity.ophelia.model.index;

import org.apache.lucene.search.Hit;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface IndexHitBuilder<T> {

	/**
	 * Obtain the index hit from a query hit.
	 * 
	 * @param queryHit
	 *            The query hit.
	 * @return The index hit.
	 */
	IndexHit<T> toIndexHit(final Hit hit);
}
