/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index.lucene;

import java.io.IOException;

import org.apache.lucene.document.Field;
import org.apache.lucene.search.Hit;

import com.thinkparity.model.parity.model.artifact.ArtifactType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class QueryHitBuilder {

	/** The index id field. */
	private final Field idField;

    /** The index type field. */
    private final Field typeField;

	/**
	 * Create a QueryHitBuilder.
	 * 
	 * @param capacity
	 *            The initial hit capacity.
	 */
	QueryHitBuilder(final Field idField, final Field typeField) {
		super();
		this.idField = idField;
        this.typeField = typeField;
	}

	/**
	 * Obtain the query hit representation of a lucene hit.
	 * 
	 * @return The query hit.
	 */
	public QueryHit toQueryHit(final Hit hit) {
		final QueryHit queryHit = new QueryHit();
		try {
            queryHit.setId(Long.parseLong(hit.getDocument().get(idField.name())));
            queryHit.setType(ArtifactType.valueOf(hit.getDocument().get(typeField.name())));
		}
		catch(final IOException iox) {
			throw new IndexException("Could not extract hit value.", iox);
		}
		return queryHit;
	}
}
