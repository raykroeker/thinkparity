/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index;

import com.thinkparity.model.parity.model.index.lucene.QueryHit;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class IndexHitBuilder {

	/**
	 * Create a IndexHitBuilder.
	 * 
	 */
	IndexHitBuilder() { super(); }

	/**
	 * Obtain the index hit from a query hit.
	 * 
	 * @param queryHit
	 *            The query hit.
	 * @return The index hit.
	 */
	IndexHit toIndexHit(final QueryHit queryHit) {
		return new IndexHit(queryHit.getDataId());
	}
}
