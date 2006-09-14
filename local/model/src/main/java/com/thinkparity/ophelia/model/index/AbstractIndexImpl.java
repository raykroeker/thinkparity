/*
 * Created On: Aug 31, 2006 10:32:43 AM
 */
package com.thinkparity.ophelia.model.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hit;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.index.lucene.Searcher;
import com.thinkparity.ophelia.model.workspace.Workspace;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractIndexImpl<T, U> implements IndexImpl<T, U> {

    /** The index analyzer to use when creating/updating index entries. */
    private final Analyzer indexAnalyzer;

    /** A thinkParity internal model factory. */
    private final InternalModelFactory internalModelFactory;

    /** The thinkParity workspace. */
    private final Workspace workspace;

    /**
     * Create AbstractIndexImpl.
     * 
     * @param context
     *            A thinkParity model context.
     */
    protected AbstractIndexImpl(final Workspace workspace,
            final InternalModelFactory internalModelFactory) {
        super();
        this.indexAnalyzer = new StandardAnalyzer();
        this.internalModelFactory = internalModelFactory;
        this.workspace = workspace;
    }

    /**
     * Delete a term from the index.
     * 
     * @param term
     *            A term.
     * @throws IOException
     */
    protected void delete(final Term term) throws IOException {
        final IndexReader indexReader = openIndexReader();
        try {
            Assert.assertTrue("COULD NOT DELETE FROM INDEX",
                    1 == indexReader.deleteDocuments(term));
        } finally {
            closeIndexReader(indexReader);
        }
    }

    /**
     * Obtain a thinkParity internal artifact interface.
     * 
     * @return A thinkParity internal artifact interface
     *         <code>InternalArtifactModel</code>.
     */
    protected InternalArtifactModel getArtifactModel() {
        return internalModelFactory.getArtifactModel();
    }

    /**
     * Index a lucene document.
     * 
     * @param document
     *            The lucene document.
     * @throws IOException
     *             If the document could not be indexed.
     */
    protected void index(final Document document) throws IOException {
        final IndexWriter indexWriter = openIndexWriter();
        try {
            indexWriter.addDocument(document);
            indexWriter.optimize();
        } finally {
            closeIndexWriter(indexWriter);
        }
    }

    /**
     * Resolve the indexed type.
     * 
     * @param idFieldValue
     *            The value of the id field in the index.
     * @return <code>U</code>.
     */
    protected abstract U resolveHit(final String hitIdValue);

    /**
     * Search the index.
     * 
     * @param idField
     *            An id field.
     * @param fields
     *            A list of fields to search.
     * @param filter
     *            A filter to apply to the search results.
     * @param expression
     *            A search expression.
     * @return A list of index hits.
     * @throws IOException
     */
    protected List<U> search(final Field idField, final List<Field> fields,
            final String expression) throws IOException {
        final IndexReader indexReader = openIndexReader();
        try {
            final Searcher searcher =
                new Searcher(indexAnalyzer, indexReader, idField, fields);
            final List<Hit> hits = searcher.search(createFinalExpression(expression));
    
            final List<U> indexHits = new ArrayList<U>();
            for (final Hit hit : hits) {
                indexHits.add(resolveHit(hit.get(idField.name())));
            }
            return indexHits;
        } finally {
            closeIndexReader(indexReader);
        }
    }

    /**
     * Close the index reader.
     * 
     * @param indexReader
     *            The index reader.
     * @throws IOException
     */
    private void closeIndexReader(final IndexReader indexReader)
            throws IOException {
        indexReader.close();
        getIndexDirectory().close();
    }

    /**
     * Close the index writer.
     * 
     * @param indexWriter
     *            The index writer.
     * @throws IOException
     */
    private void closeIndexWriter(final IndexWriter indexWriter)
            throws IOException {
        indexWriter.close();
        getIndexDirectory().close();
    }

    /**
     * Create a final search expression. This will prepend a wild-card character
     * to the expression as well as append one.
     * 
     * @param expression
     *            A search expression.
     * @return The final search expression.
     */
    private String createFinalExpression(final String expression) {
        if (null == expression) {
            return null;
        } else {
            final StringBuffer expressionBuffer = new StringBuffer(expression.trim());
            if (expression.charAt(expression.length() - 1) != '*') {
                expressionBuffer.append('*');
            }
            return expressionBuffer.toString();
        }
    }

    /**
     * Obtain the lucene index directory.
     * 
     * @return The lucene index directory.
     * @throws IOException
     */
    private Directory getIndexDirectory() throws IOException {
        return FSDirectory.getDirectory(workspace.getIndexDirectory(), false);
    }

    /**
     * Create a lucene index reader.
     * 
     * @return A lucene index reader.
     * @throws IOException
     *             If the index reader cannot be opened.
     */
    private IndexReader openIndexReader() throws IOException {
        return IndexReader.open(getIndexDirectory());
    }

    /**
     * Create a lucene index writer.
     * 
     * @return The lucene index writer.
     * @throws IOException
     *             If the index writer cannot be created.
     * @see #indexAnalyzer
     */
    private IndexWriter openIndexWriter() throws IOException {
        final Directory directory = getIndexDirectory();

        final Boolean doCreate;
        if(0 == directory.list().length) { doCreate = Boolean.TRUE; }
        else { doCreate = Boolean.FALSE; }      

        return new IndexWriter(directory, indexAnalyzer, doCreate);
    }
}