/*
 * Created On: Mar 7, 2006
 */
package com.thinkparity.ophelia.model.index.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

	/** A lucene index analyzer. */
	private final Analyzer analyzer;

	/** A list of lucene index fields to query. */
	private final List<Field> fields;

	/** A lucene index searcher. */
	private final IndexSearcher indexSearcher;

    /**
     * Create Searcher.
     * 
     * @param analyzer
     *            A lucene index analyzer.
     * @param indexReader
     *            A lucene index reader.
     * @param id
     *            A lucene index id field.
     * @param fields
     *            A list of lucene index fields.
     */
	public Searcher(final Analyzer analyzer, final IndexReader indexReader,
            final Field id, final List<Field> fields) {
        super();
        this.analyzer = analyzer;
        this.fields = fields;
        this.indexSearcher = new IndexSearcher(indexReader);
    }

	/**
	 * Search the list of fields for the given expression.
	 * 
	 * @param fields
	 *            The fields to search.
	 * @param expression
	 *            The expression to search for.
	 * @return A list of lucence hits.
	 */
	public List<Hit> search(final String expression) {
		final List<Query> queries = createQueries(expression);

		final List<Hit> hits = new ArrayList<Hit>();
		Iterator iHits;
		for (final Query query : queries) {
			try { iHits = indexSearcher.search(query).iterator(); }
			catch (final IOException iox) {
				throw new IndexException("Could not search index.", iox);
			}
			while (iHits.hasNext()) {
				hits.add((Hit) iHits.next());
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
