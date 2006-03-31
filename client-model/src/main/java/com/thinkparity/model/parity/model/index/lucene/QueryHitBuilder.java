/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index.lucene;

import java.io.IOException;

import org.apache.lucene.document.Field;
import org.apache.lucene.search.Hit;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class QueryHitBuilder {

	/**
	 * The query id field.
	 * 
	 */
	private final Field idField;

	/**
	 * Create a QueryHitBuilder.
	 * 
	 * @param capacity
	 *            The initial hit capacity.
	 */
	QueryHitBuilder(final Field idField) {
		super();
		this.idField = idField;
	}

	/**
	 * Obtain the query hit representation of a lucene hit.
	 * 
	 * @return The query hit.
	 */
	public QueryHit toQueryHit(final Hit hit) {
		final QueryHit queryHit = new QueryHit();
		try {
			queryHit.setDataId(parseLong(hit, idField));
			queryHit.setDocument(hit.getDocument());
		}
		catch(final IOException iox) {
			throw new IndexException("Could not extract hit value.", iox);
		}
		return queryHit;
	}

	/**
	 * Parse a hit's value for a long.
	 * 
	 * @param hit
	 *            The hit.
	 * @param field
	 *            The field to extract.
	 * @return The long value.
	 * @throws IOException
	 */
	private Long parseLong(final Hit hit, final Field field) throws IOException {
		return Long.parseLong(hit.get(field.name()));
	}
}
