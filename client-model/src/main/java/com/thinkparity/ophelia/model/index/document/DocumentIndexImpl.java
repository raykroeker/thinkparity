/*
 * Created On: Aug 31, 2006 10:27:02 AM
 */
package com.thinkparity.ophelia.model.index.document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.index.AbstractIndexImpl;
import com.thinkparity.ophelia.model.index.lucene.DocumentBuilder;
import com.thinkparity.ophelia.model.index.lucene.FieldBuilder;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DocumentIndexImpl extends AbstractIndexImpl<DocumentIndexEntry, Long> {

    /** Container id index field. */
    private static final FieldBuilder IDX_CONTAINER_ID;

    /** Document id index field. */
    private static final FieldBuilder IDX_DOCUMENT_ID;

    /** Document name index field. */
    private static final FieldBuilder IDX_DOCUMENT_NAME;

    static {
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
    public void delete(final DocumentIndexEntry o) throws IOException {
        final Field field = IDX_DOCUMENT_ID.toSearchField();
        final Term term = new Term(field.name(), o.getDocument().getId().toString());
        delete(term);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#index(java.lang.Object)
     * 
     */
    public void index(final DocumentIndexEntry o) throws IOException {
        final DocumentBuilder indexBuilder = new DocumentBuilder(3)
            .append(IDX_CONTAINER_ID.setValue(o.getContainerId()).toField())
            .append(IDX_DOCUMENT_ID.setValue(o.getDocument().getId()).toField())
            .append(IDX_DOCUMENT_NAME.setValue(o.getDocument().getName()).toField());
        index(indexBuilder.toDocument());
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#search(java.lang.String)
     */
    public List<Long> search(final String expression) throws IOException {
        final List<Field> fields = new ArrayList<Field>();
        fields.add(IDX_DOCUMENT_NAME.toSearchField());
        return search(IDX_CONTAINER_ID.toSearchField(), fields, expression);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.AbstractIndexImpl#resolveHit(java.lang.String)
     * 
     */
    @Override
    protected Long resolveHit(final String hitIdValue) {
        return Long.parseLong(hitIdValue);
    }
}
