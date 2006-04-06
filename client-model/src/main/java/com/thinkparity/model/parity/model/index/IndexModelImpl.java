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

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.index.lucene.DocumentBuilder;
import com.thinkparity.model.parity.model.index.lucene.FieldBuilder;
import com.thinkparity.model.parity.model.index.lucene.QueryHit;
import com.thinkparity.model.parity.model.index.lucene.Searcher;
import com.thinkparity.model.parity.model.session.InternalSessionModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class IndexModelImpl extends AbstractModelImpl {

	/**
	 * Artifact contacts index field.
	 * 
	 */
	private static final FieldBuilder IDX_ARTIFACT_CONTACTS;

	/**
	 * Artifact id index field.
	 * 
	 */
	private static final FieldBuilder IDX_ARTIFACT_ID;

	/**
	 * Artifact name index field.
	 * 
	 */
	private static final FieldBuilder IDX_ARTIFACT_NAME;

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

		IDX_ARTIFACT_CONTACTS = new FieldBuilder()
			.setIndex(Field.Index.TOKENIZED)
			.setName("ARTIFACT.CONTACTS")
			.setStore(Field.Store.YES)
			.setTermVector(Field.TermVector.NO);
	}

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
		this.indexAnalyzer = new StandardAnalyzer();
		this.indexHitBuilder = new IndexHitBuilder();
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
		logger.info("[LMODEL] [INDEX] [CREATE DOCUMENT INDEX]");
		logger.debug(documentId);
		logger.debug(documentName);

		// since a document's name is a filename; we can assume it follows this
		// format document.doc
		// therefore; we want to set the value in the index to be
		// document.doc document
		final StringBuffer documentNameValue = new StringBuffer();
		if(-1 < documentName.indexOf('.')) {
			documentNameValue.append(documentName.substring(
					0, documentName.lastIndexOf('.')))
				.append(Separator.SemiColon)
				.append(documentName);
		}
		logger.debug("[LMODEL] [INDEX] [CREATE DOCUMENT INDEX] [DOCUMENT NAME VALUE] [" + documentNameValue + "]");

		final InternalSessionModel iSModel = getInternalSessionModel();
		final Set<User> documentTeam = iSModel.readArtifactTeam(documentId);

		final DocumentBuilder db = new DocumentBuilder(6);
		db.append(IDX_ARTIFACT_ID.setValue(documentId).toField())
			.append(IDX_ARTIFACT_NAME.setValue(documentNameValue.toString()).toField())
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
	void deleteArtifactIndex(final Long artifactId) throws ParityException {
		logger.info("[LMODEL] [INDEX] [DELETE ARTIFACT INDEX]");
		logger.debug(artifactId);
		final IndexReader indexReader = openIndexReader();
		try {
			final Field idField = IDX_ARTIFACT_ID.toSearchField();
			final Term idTerm = new Term(idField.name(), artifactId.toString());
			Assert.assertTrue(
					"[LMODEL] [INDEX] [DELETE ARTIFACT INDEX] [CORRUPT INDEX]",
					1 == indexReader.deleteDocuments(idTerm));
		}
		catch(final IOException iox) {
			logger.error("[LMODEL] [INDEX] [DELETE ARTIFACT INDEX] [IO ERROR]", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		finally { closeIndexReader(indexReader); }
	}

	/**
	 * Search the artifact index.
	 * 
	 * @param expression
	 *            The search expression.
	 * @return A list of index hits.
	 * @throws ParityException
	 */
	List<IndexHit> searchArtifact(final String expression) throws ParityException {
		logger.info("[LMODEL] [INDEX] [SEARCH ARTIFACT]");
		logger.debug(expression);
		final IndexReader indexReader = openIndexReader();
		try {
			final List<Field> fields = new LinkedList<Field>();
			fields.add(IDX_ARTIFACT_NAME.toSearchField());
			fields.add(IDX_ARTIFACT_CONTACTS.toSearchField());
	
			final Searcher searcher =
				new Searcher(logger, indexAnalyzer, indexReader,
						IDX_ARTIFACT_ID.toSearchField(), fields);
			final List<QueryHit> queryHits = searcher.search(expression);
	
			final List<IndexHit> indexHits = new LinkedList<IndexHit>();
			for(final QueryHit queryHit : queryHits) {
				indexHits.add(indexHitBuilder.toIndexHit(queryHit));
			}
			return indexHits;
		}
		finally { closeIndexReader(indexReader); }
	}

	/**
     * Close the index reader.
     * 
     * @param indexReader
     *            The index reader.
     * @throws ParityException
     */
	private void closeIndexReader(final IndexReader indexReader)
			throws ParityException {
		try {
			indexReader.close();
			getIndexDirectory().close();
		}
		catch(final IOException iox) {
			logger.error("[LMODEL] [INDEX] [CLOSE READER] [IO ERROR]", iox);
			throw ParityErrorTranslator.translate(iox);
		}
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
	private IndexReader openIndexReader() throws ParityException {
		try { return IndexReader.open(getIndexDirectory()); }
		catch(final IOException iox) {
			logger.error("[LMODEL] [INDEX] [OPEN READER] [IO ERROR]", iox);
			throw ParityErrorTranslator.translate(iox);
		}
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
}
