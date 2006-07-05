/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index.lucene;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Searcher {

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * The index analyzer.
	 * 
	 */
	private final Analyzer analyzer;

	/**
	 * The fields to query.
	 * 
	 */
	private final List<Field> fields;

	/** The index id field. */
	private final Field idField;

    /** The index type field. */
    private final Field typeField;

	/**
	 * The index searcher.
	 * 
	 */
	private final IndexSearcher indexSearcher;

	/**
	 * Create a Searcher.
	 * 
	 * @param queryBuilder
	 *            The query builder.
	 */
	public Searcher(final Logger logger, final Analyzer analyzer,
			final IndexReader indexReader, final Field idField,
			final List<Field> fields, final Field typeField) {
		super();
		this.analyzer = analyzer;
		this.fields = fields;
		this.idField = idField;
		this.indexSearcher = new IndexSearcher(indexReader);
		this.logger = logger;
        this.typeField = typeField;
	}

	/**
	 * Search the list of fields for the given expression.
	 * 
	 * @param fields
	 *            The fields to search.
	 * @param expression
	 *            The expression to search for.
	 * @return A list of query hits.
	 */
	public List<QueryHit> search(final String expression) {
		final List<Query> queries = createQueries(expression);

		final List<QueryHit> hits = new LinkedList<QueryHit>();
		final QueryHitBuilder queryHitBuilder = new QueryHitBuilder(idField, typeField);
		Iterator iHits;
		for(final Query query : queries) {
			logger.info("[LMODEL] [INDEX] [SEARCH] [QUERY]");
			logger.debug(query.toString());
			try { iHits = indexSearcher.search(query).iterator(); }
			catch(final IOException iox) {
				throw new IndexException("Could not search index.", iox);
			}
			while(iHits.hasNext()) {
				hits.add(queryHitBuilder.toQueryHit((Hit) iHits.next()));
			}
		}
		return hits;
	}

	/**
	 * Create a list of lucene queries for the given search expression.
	 * 
	 * @param expression
	 *            The search expression.
	 * @return A list of lucene queries.
	 */
	private List<Query> createQueries(final String expression) {
		final List<Query> queries = new LinkedList<Query>();
		QueryParser queryParser;
		for(final Field field : fields) {
			queryParser = new QueryParser(field.name(), analyzer);
			try { queries.add(queryParser.parse(expression)); }
			catch(final ParseException px) {
				throw new IndexException("Could not create query.", px);
			}
		}
		return queries;
	}
}
