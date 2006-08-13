/*
 * Mar 6, 2006
 */
package com.thinkparity.model.parity.model.index;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.index.IndexFilterManager;
import com.thinkparity.model.parity.model.index.lucene.DocumentBuilder;
import com.thinkparity.model.parity.model.index.lucene.FieldBuilder;
import com.thinkparity.model.parity.model.index.lucene.QueryHit;
import com.thinkparity.model.parity.model.index.lucene.Searcher;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class IndexModelImpl extends AbstractModelImpl {

	/** An artifact contacts index field builder. */
	private static final FieldBuilder IDX_ARTIFACT_CONTACTS;

	/** An artifact id index field builder. */
	private static final FieldBuilder IDX_ARTIFACT_ID;

    /** An artifact name index field builder. */
    private static final FieldBuilder IDX_ARTIFACT_NAME;

    /** An artifact type index field builder. */
    private static final FieldBuilder IDX_ARTIFACT_TYPE;

	static {
		IDX_ARTIFACT_ID = new FieldBuilder()
                .setIndex(Field.Index.UN_TOKENIZED)
                .setName("ARTIFACT.ARTIFACT_ID")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_ARTIFACT_NAME = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("ARTIFACT.ARTIFACT_NAME")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_ARTIFACT_TYPE = new FieldBuilder()
                .setIndex(Field.Index.UN_TOKENIZED)
                .setName("ARTIFACT.ARTIFACT_TYPE")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

		IDX_ARTIFACT_CONTACTS = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("ARTIFACT.CONTACTS")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);
	}

	/**
     * Obtain a log4j api id.
     * 
     * @param api
     *            The api.
     * @return The log4j api id.
     */
    protected static StringBuffer getApiId(final String api) {
        return getModelId("INDEX").append(" ").append(api);
    }

	/** Index filter for containers. */
    private final Filter<? super IndexHit> containerFilter;

    /** Index filter for documents. */
    private final Filter<? super IndexHit> documentFilter;

	/**
	 * The index analyzer to use when creating/updating index entries.
	 * 
	 */
	private final Analyzer indexAnalyzer;

    /**
	 * Index hit builder.  Is responsible for converting a query hit to an index
	 * hit.
	 * 
	 */
	private final IndexHitBuilder indexHitBuilder;

    /**
	 * Create a IndexModelImpl.
	 * 
	 * @param workspace
	 *            The parity workspace.
	 */
	IndexModelImpl(final Workspace workspace) {
		super(workspace);
        this.containerFilter = new com.thinkparity.model.parity.model.filter.index.Container();
        this.documentFilter = new com.thinkparity.model.parity.model.filter.index.Document();
		this.indexAnalyzer = new StandardAnalyzer();
		this.indexHitBuilder = new IndexHitBuilder();
	}

	/**
     * Create an index entry for the container.
     * 
     * @param containerId
     *            The container id.
     * @param containerName
     *            The container name.
     */
    void createContainer(final Long containerId, final String containerName)
            throws ParityException {
        logger.info(getApiId("[CREATE CONTAINER]"));
        logger.debug(containerId);
        logger.debug(containerName);

        final Set<User> team = getInternalArtifactModel().readTeam(containerId);

        final DocumentBuilder documentBuilder = new DocumentBuilder(6);
        documentBuilder.append(IDX_ARTIFACT_ID.setValue(containerId).toField())
            .append(IDX_ARTIFACT_TYPE.setValue(ArtifactType.CONTAINER).toField())
            .append(IDX_ARTIFACT_NAME.setValue(containerName).toField())
            .append(IDX_ARTIFACT_CONTACTS.setValue(team).toField());

        index(documentBuilder.toDocument());
    }

	/**
	 * Create an index entry for an artifact.
	 * 
	 * @param index
	 *            The artifact index entry.
	 * @throws ParityException
	 */
	void createDocument(final Long documentId, final String documentName)
			throws ParityException {
		logger.info(getApiId("[CREATE DOCUMENT]"));
		logger.debug(documentId);
		logger.debug(documentName);

		final Set<User> documentTeam = getInternalArtifactModel().readTeam(documentId);

		final DocumentBuilder db = new DocumentBuilder(6);
		db.append(IDX_ARTIFACT_ID.setValue(documentId).toField())
            .append(IDX_ARTIFACT_TYPE.setValue(ArtifactType.DOCUMENT).toField())
			.append(IDX_ARTIFACT_NAME.setValue(documentName).toField())
			.append(IDX_ARTIFACT_CONTACTS.setValue(documentTeam).toField());

		index(db.toDocument());
	}

	/**
     * Delete an index entry for an artifact.
     * 
     * @param artifactId
     *            The artifact id.
     * @throws ParityException
     */
	void deleteArtifactIndex(final Long artifactId) {
		logger.info("[LMODEL] [INDEX] [DELETE ARTIFACT INDEX]");
		logger.debug(artifactId);
		IndexReader indexReader = null;
		try {
            indexReader = openIndexReader();
			final Field idField = IDX_ARTIFACT_ID.toSearchField();
			final Term idTerm = new Term(idField.name(), artifactId.toString());
			Assert.assertTrue(
					"[LMODEL] [INDEX] [DELETE ARTIFACT INDEX] [CORRUPT INDEX]",
					1 == indexReader.deleteDocuments(idTerm));
		} catch(final Throwable t) {
            throw translateError("[DELETE ARTIFACT INDEX]", t);
		} finally {
            if (null != indexReader) {
                try {
                    closeIndexReader(indexReader);
                } catch(final Throwable t) {
                    throw translateError(
                            "[DELETE ARTIFACT INDEX] [COULD NOT CLOSE INDEX READER]",
                            t);
                }
            }
		}
	}

    /**
     * Search the container index.
     * 
     * @param expression
     *            The search expression.
     * @return A list of index hits representing containers.
     * @throws ParityException
     */
    List<IndexHit> searchContainers(final String expression) {
        return searchArtifacts(expression, containerFilter);
    }

    /**
     * Search the document index.
     * 
     * @param expression
     *            The search expression.
     * @return A list of index hits representing documents.
     * @throws ParityException
     */
	List<IndexHit> searchDocuments(final String expression) throws ParityException {
        return searchArtifacts(expression, documentFilter);
	}

    /**
     * Close the index reader.
     * 
     * @param indexReader
     *            The index reader.
     * @throws ParityException
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
     * @throws ParityException
     */
	private void closeIndexWriter(final IndexWriter indexWriter)
			throws ParityException {
		try {
			indexWriter.close();
			getIndexDirectory().close();
		}
		catch(final IOException iox) {
			logger.error("[LMODEL] [INDEX] [INDEX DOCUMENT] [CLOSE WRITER IO ERROR]", iox);
			throw ParityErrorTranslator.translate(iox);
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
	 * Index a lucene document.
	 * 
	 * @param document
	 *            The lucene document.
	 * @throws ParityException
	 *             If the document could not be indexed.
	 */
	private void index(final Document document) throws ParityException {
		final IndexWriter indexWriter = openIndexWriter();
		try {
			indexWriter.addDocument(document);
			indexWriter.optimize();
		}
		catch(final IOException iox) {
			logger.error("[LMODEL] [INDEX] [INDEX DOCUMENT] [IO ERROR]", iox);
			logger.error(document);
			throw ParityErrorTranslator.translate(iox);
		}
		finally { closeIndexWriter(indexWriter); }
	}

	/**
	 * Create a lucene index reader.
	 * 
	 * @return A lucene index reader.
	 * @throws ParityException
	 *             If the index reader cannot be opened.
	 */
	private IndexReader openIndexReader() throws IOException {
		return IndexReader.open(getIndexDirectory());
	}

	/**
	 * Create a lucene index writer.
	 * 
	 * @return The lucene index writer.
	 * @throws ParityException
	 *             If the index writer cannot be created.
	 * @see #indexAnalyzer
	 */
	private IndexWriter openIndexWriter() throws ParityException {
		try {
			final Directory directory = getIndexDirectory();

			final Boolean doCreate;
			if(0 == directory.list().length) { doCreate = Boolean.TRUE; }
			else { doCreate = Boolean.FALSE; }		

			return new IndexWriter(directory, indexAnalyzer, doCreate);
		}
		catch(final IOException iox) {
			logger.error("[LMODEL] [INDEX] [OPEN WRITER] [IO ERROR]", iox);
			throw ParityErrorTranslator.translate(iox);
		}
	}

	/**
     * Search the index.
     * 
     * @param expression
     *            A search expression.
     * @param filter
     *            A result filter.
     * @return A list of index hits.
     * @throws ParityException
     */
    private List<IndexHit> searchArtifacts(final String expression,
            final Filter<? super IndexHit> filter) {
        logger.info(getApiId("[SEARCH ARTIFACTS]"));
        logger.debug(expression);
        logger.debug(filter);
        IndexReader indexReader = null;
        try {
            indexReader = openIndexReader();
            final List<Field> fields = new LinkedList<Field>();
            fields.add(IDX_ARTIFACT_NAME.toSearchField());
            fields.add(IDX_ARTIFACT_CONTACTS.toSearchField());
    
            final Searcher searcher =
                new Searcher(logger, indexAnalyzer, indexReader,
                        IDX_ARTIFACT_ID.toSearchField(), fields,
                        IDX_ARTIFACT_TYPE.toSearchField());
            final List<QueryHit> queryHits = searcher.search(expression);
    
            final List<IndexHit> indexHits = new LinkedList<IndexHit>();
            for(final QueryHit queryHit : queryHits) {
                indexHits.add(indexHitBuilder.toIndexHit(queryHit));
            }
            IndexFilterManager.filter(indexHits, filter);
            return indexHits;
        }
        catch(final Throwable t) {
            throw translateError("[SEARCH ARTIFACTS]", t);
        }
        finally {
            if (null != indexReader) {
                try {
                    closeIndexReader(indexReader);
                } catch(final Throwable t) {
                    throw translateError("[SEARCH ARTIFACTS] [CLOSE INDEX READER]", t);
                }
            }
        }
    }
}
