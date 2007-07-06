/*
 * Created On: Aug 31, 2006 10:27:02 AM
 */
package com.thinkparity.ophelia.model.index.document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.index.AbstractIndexImpl;
import com.thinkparity.ophelia.model.index.lucene.DocumentBuilder;
import com.thinkparity.ophelia.model.index.lucene.FieldBuilder;
import com.thinkparity.ophelia.model.workspace.Workspace;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

/**
 * <b>Title:</b>thinkParity OpheliaModel Document Index Implementation<br>
 * <b>Description:</b>Indexes documents and allows the client to obtain a
 * container from a search expression.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DocumentIndexImpl extends
        AbstractIndexImpl<DocumentIndexEntry, Long> {

    /** Document id <code>Comparator</code>. */
    private static final Comparator<Long> DOCUMENT_ID_COMPARATOR;

    /** Container id index field. */
    private static final FieldBuilder IDX_CONTAINER_ID;

    /** Document file extension index field. */
    private static final FieldBuilder IDX_DOCUMENT_FILE_EXTENSION;

    /** Document file name index field. */
    private static final FieldBuilder IDX_DOCUMENT_FILE_NAME;

    /** Document id index field. */
    private static final FieldBuilder IDX_DOCUMENT_ID;

    /** Document name index field. */
    private static final FieldBuilder IDX_DOCUMENT_NAME;

    static {
        DOCUMENT_ID_COMPARATOR = new Comparator<Long>() {
            public int compare(final Long o1, final Long o2) {
                return o1.compareTo(o2);
            }
        };

        IDX_CONTAINER_ID = new FieldBuilder()
            .setIndex(Field.Index.UN_TOKENIZED)
            .setName("DOCUMENT.CONTAINER_ID")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);

        IDX_DOCUMENT_ID = new FieldBuilder()
            .setIndex(Field.Index.UN_TOKENIZED)
            .setName("DOCUMENT.DOCUMENT_ID")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);

        IDX_DOCUMENT_NAME = new FieldBuilder()
            .setIndex(Field.Index.TOKENIZED)
            .setName("DOCUMENT.DOCUMENT_NAME")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);

        IDX_DOCUMENT_FILE_NAME = new FieldBuilder()
            .setIndex(Field.Index.TOKENIZED)
            .setName("DOCUMENT.DOCUMENT_FILE_NAME")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);

        IDX_DOCUMENT_FILE_EXTENSION = new FieldBuilder()
            .setIndex(Field.Index.UN_TOKENIZED)
            .setName("DOCUMENT.DOCUMENT_FILE_EXTENSION")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);
    }

    /**
     * Create DocumentIndexImpl.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @param modelFactory
     *            A thinkParity <code>InternalModelFactory</code>.
     */
    public DocumentIndexImpl(final Workspace workspace,
            final InternalModelFactory modelFactory) {
        super(workspace, modelFactory);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#delete(java.lang.Object)
     */
    public void delete(final Long documentId) throws IOException {
        final Field idField = IDX_DOCUMENT_ID.toSearchField();
        final Term term = new Term(idField.name(), documentId.toString());
        delete(term);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#index(java.lang.Object)
     * 
     */
    public void index(final DocumentIndexEntry o) throws IOException {
        final String name = getName(o.getDocument());
        final DocumentBuilder indexBuilder = new DocumentBuilder(3)
            .append(IDX_CONTAINER_ID.setValue(o.getContainerId()).toField())
            .append(IDX_DOCUMENT_ID.setValue(o.getDocument().getId()).toField())
            .append(IDX_DOCUMENT_NAME.setValue(o.getDocument().getName()).toField())
            .append(IDX_DOCUMENT_FILE_NAME.setValue(name).toField());
        final String extension = getExtension(o.getDocument());
        if (null != extension) {
            indexBuilder.append(IDX_DOCUMENT_FILE_EXTENSION.setValue(extension).toField());
        }
        index(indexBuilder.toDocument());
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#search(java.lang.String)
     */
    public List<Long> search(final String expression) throws IOException {
        final List<Field> fields = new ArrayList<Field>(1);
        fields.add(IDX_DOCUMENT_NAME.toSearchField());
        fields.add(IDX_DOCUMENT_FILE_NAME.toSearchField());
        fields.add(IDX_DOCUMENT_FILE_EXTENSION.toSearchField());
        return search(IDX_CONTAINER_ID.toSearchField(), fields, expression);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.AbstractIndexImpl#getComparator()
     *
     */
    @Override
    protected Comparator<? super Long> getComparator() {
        return DOCUMENT_ID_COMPARATOR;
    }

    /**
     * @see com.thinkparity.ophelia.model.index.AbstractIndexImpl#resolveHit(java.lang.String)
     * 
     */
    @Override
    protected Long resolveHit(final String hitIdValue) {
        return Long.parseLong(hitIdValue);
    }

    /**
     * Obtain the document's file name extension excluding the dot.
     * 
     * @param document
     *            A <code>Document</code>.
     * @return An extension <code>String</code> or null if no extension
     *         exists.
     */
    private String getExtension(final Document document) {
        final String name = document.getName();
        final int dot = name.lastIndexOf('.');
        if (-1 == dot) {
            return null;
        } else {
            if (dot + 1 < name.length()) {
                return document.getName().substring(dot + 1);
            } else {
                return null;
            }
        }
    }

    /**
     * Obtain the document's file name excluding the dot.
     * 
     * @param document
     *            A <code>Document</code>.
     * @return An extension <code>String</code> or null if no extension
     *         exists.
     */
    private String getName(final Document document) {
        final String name = document.getName();
        final int dot = name.lastIndexOf('.');
        if (-1 == dot) {
            return name;
        } else {
            return name.substring(0, dot);
        }
    }
}
