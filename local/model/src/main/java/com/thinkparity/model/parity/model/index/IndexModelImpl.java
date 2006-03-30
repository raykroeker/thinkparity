/*
 * Mar 6, 2006
 */
package com.thinkparity.model.parity.model.index;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;

import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.index.lucene.DocumentBuilder;
import com.thinkparity.model.parity.model.index.lucene.FieldBuilder;
import com.thinkparity.model.parity.model.index.lucene.QueryHit;
import com.thinkparity.model.parity.model.index.lucene.Searcher;
import com.thinkparity.model.parity.model.session.InternalSessionModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class IndexModelImpl extends AbstractModelImpl {

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

	/**
	 * Artifact created on index field.
	 * 
	 */
	private static final FieldBuilder IDX_ARTIFACT_CREATED_ON;

	/**
	 * Artifact created by index field.
	 * 
	 */
	private static final FieldBuilder IDX_ARTIFACT_CREATED_BY;

	/**
	 * Artifact key holder index field.
	 * 
	 */
	private static final FieldBuilder IDX_ARTIFACT_KEYHOLDER;

	/**
	 * Artifact contacts index field.
	 * 
	 */
	private static final FieldBuilder IDX_ARTIFACT_CONTACTS;

	static {
		IDX_ARTIFACT_ID = new FieldBuilder()
			.setIndex(Field.Index.NO)
			.setName("ARTIFACT.ARTIFACT_ID")
			.setStore(Field.Store.YES)
			.setTermVector(Field.TermVector.NO);

		IDX_ARTIFACT_NAME = new FieldBuilder()
			.setIndex(Field.Index.TOKENIZED)
			.setName("ARTIFACT.ARTIFACT_NAME")
			.setStore(Field.Store.YES)
			.setTermVector(Field.TermVector.NO);

		IDX_ARTIFACT_CREATED_ON = new FieldBuilder()
			.setIndex(Field.Index.UN_TOKENIZED)
			.setName("ARTIFACT.CREATED_ON")
			.setStore(Field.Store.YES)
			.setTermVector(Field.TermVector.NO);

		IDX_ARTIFACT_CREATED_BY = new FieldBuilder()
			.setIndex(Field.Index.TOKENIZED)
			.setName("ARTIFACT.CREATED_BY")
			.setStore(Field.Store.YES)
			.setTermVector(Field.TermVector.NO);

		IDX_ARTIFACT_KEYHOLDER = new FieldBuilder()
			.setIndex(Field.Index.TOKENIZED)
			.setName("ARTIFACT.KEYHOLDER")
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
	void index(final ArtifactIndex index) throws ParityException {
		logger.info("[LMODEL] [INDEX] [INDEX ARTIFACT]");
		logger.debug(index);

		final InternalSessionModel iSModel = getInternalSessionModel();
		final List<JabberId> jabberIds = new LinkedList<JabberId>();
		jabberIds.add(index.getCreatedBy());
		jabberIds.add(index.getKeyHolder());
		jabberIds.addAll(index.getContacts());
		final List<User> users = iSModel.readUsers(jabberIds);

		final List<User> contacts = filter(users, index.getContacts());
		final User createdBy = filter(users, index.getKeyHolder());
		final User keyHolder = filter(users, index.getCreatedBy());

		final DocumentBuilder db = new DocumentBuilder(6);
		db.append(IDX_ARTIFACT_ID.setValue(index.getId()).toField())
			.append(IDX_ARTIFACT_NAME.setValue(index.getName()).toField())
			.append(IDX_ARTIFACT_CREATED_ON.setValue(index.getCreatedOn(), DateTools.Resolution.DAY).toField())
			.append(IDX_ARTIFACT_CREATED_BY.setValue(createdBy).toField())
			.append(IDX_ARTIFACT_KEYHOLDER.setValue(keyHolder).toField())
			.append(IDX_ARTIFACT_CONTACTS.setValue(contacts).toField());

		index(db.toDocument());
	}

	protected List<User> filter(final List<User> users, final List<JabberId> jabberId) {
		final List<User> filtered = new LinkedList<User>();
		for(final User user : users) {
			if(user.getId().equals(jabberId)) { filtered.add(user); }
		}
		return filtered;
	}

	protected User filter(final List<User> users, final JabberId jabberId) {
		for(final User user : users) {
			if(user.getId().equals(jabberId)) { return user; }
		}
		return null;
	}

	/**
	 * Search the index.
	 * 
	 * @param expression
	 *            The search expression.
	 * @return A list of index hits.
	 * @throws ParityException
	 */
	List<IndexHit> search(final String expression) throws ParityException {
		logger.info("[LMODEL] [INDEX] [SEARCH]");
		logger.debug(expression);

		final List<Field> fields = new LinkedList<Field>();
		fields.add(IDX_ARTIFACT_NAME.toSearchField());
		fields.add(IDX_ARTIFACT_CREATED_BY.toSearchField());
		fields.add(IDX_ARTIFACT_KEYHOLDER.toSearchField());
		fields.add(IDX_ARTIFACT_CONTACTS.toSearchField());

		final Searcher searcher =
			new Searcher(logger, indexAnalyzer, openIndexReader(),
					IDX_ARTIFACT_ID.toSearchField(), fields);
		final List<QueryHit> queryHits = searcher.search(expression + "*");

		final List<IndexHit> indexHits = new LinkedList<IndexHit>();
		for(final QueryHit queryHit : queryHits) {
			indexHits.add(indexHitBuilder.toIndexHit(queryHit));
		}
		return indexHits;
	}

	/**
	 * Create a lucene index writer.
	 * 
	 * @return The lucene index writer.
	 * @throws ParityException
	 *             If the index writer cannot be created.
	 * @see #indexAnalyzer
	 */
	private IndexWriter createIndexWriter() throws ParityException {
		final File indexDirectory = workspace.getIndexDirectory();
		final Boolean doCreate;
		if(0 == indexDirectory.listFiles().length) { doCreate = Boolean.TRUE; }
		else { doCreate = Boolean.FALSE; }

		try { return new IndexWriter(indexDirectory, indexAnalyzer, doCreate); }
		catch(final IOException iox) {
			logger.error("Could not create index writer.", iox);
			logger.error(indexDirectory);
			throw ParityErrorTranslator.translate(iox);
		}
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
		final IndexWriter indexWriter = createIndexWriter();
		try {
			indexWriter.addDocument(document);
			indexWriter.optimize();
		}
		catch(final IOException iox) {
			logger.error("Could not index document.", iox);
			logger.error(document);
			throw ParityErrorTranslator.translate(iox);
		}
		finally {
			try { indexWriter.close(); }
			catch(final IOException iox) {
				logger.error("Could not close index.", iox);
				logger.error(document);
				throw ParityErrorTranslator.translate(iox);
			}
		}
	}

	/**
	 * Create a lucene index reader.
	 * 
	 * @return A lucene index reader.
	 * @throws ParityException
	 *             If the index reader cannot be opened.
	 */
	private IndexReader openIndexReader() throws ParityException {
		try {  return IndexReader.open(workspace.getIndexDirectory()); }
		catch(final IOException iox) {
			logger.error("Could not open index.", iox);
			logger.error(workspace.getIndexDirectory());
			throw ParityErrorTranslator.translate(iox);
		}
	}
}
