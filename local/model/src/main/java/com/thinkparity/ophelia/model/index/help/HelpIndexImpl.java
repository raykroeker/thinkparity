/*
 * Created On: Aug 31, 2006 10:27:02 AM
 */
package com.thinkparity.ophelia.model.index.help;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.index.AbstractIndexImpl;
import com.thinkparity.ophelia.model.index.lucene.DocumentBuilder;
import com.thinkparity.ophelia.model.index.lucene.FieldBuilder;
import com.thinkparity.ophelia.model.workspace.Workspace;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

/**
 * <b>Title:</b>thinkParity OpheliaModel Help Index Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HelpIndexImpl extends
        AbstractIndexImpl<HelpIndexEntry, Long> {

    /** Help content index field. */
    private static final FieldBuilder IDX_CONTENT;

    /** Artifact id index field. */
    private static final FieldBuilder IDX_TOPIC_ID;

    /** Help topic name index field. */
    private static final FieldBuilder IDX_TOPIC_NAME;

    /** Help topic id <code>Comparator</code>. */
    private static final Comparator<Long> TOPIC_ID_COMPARATOR;

    static {
        TOPIC_ID_COMPARATOR = new Comparator<Long>() {
            public int compare(final Long o1, final Long o2) {
                return o1.compareTo(o2);
            }
        };

        IDX_TOPIC_ID = new FieldBuilder()
            .setIndex(Field.Index.UN_TOKENIZED)
            .setName("HELP.TOPIC_ID")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);

        IDX_TOPIC_NAME = new FieldBuilder()
            .setIndex(Field.Index.TOKENIZED)
            .setName("HELP.TOPIC_NAME")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);

        IDX_CONTENT = new FieldBuilder()
            .setIndex(Field.Index.TOKENIZED)
            .setName("HELP.TOPIC_CONTENT")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);
    }

    /**
     * Create ArtifactIndexImpl.
     * 
     * @param context
     *            A thinkParity model context.
     */
    public HelpIndexImpl(final Workspace workspace,
            final InternalModelFactory modelFactory) {
        super(workspace, modelFactory);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#delete(java.lang.Object)
     * 
     */
    public void delete(final Long helpTopicId) throws IOException {
        final Field idField = IDX_TOPIC_ID.toSearchField();
        final Term term = new Term(idField.name(), helpTopicId.toString());
        delete(term);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#index(java.lang.Object)
     * 
     */
    public void index(final HelpIndexEntry o) throws IOException {
        final DocumentBuilder indexBuilder = new DocumentBuilder(5)
            .append(IDX_TOPIC_ID.setValue(o.getTopic().getId()).toField())
            .append(IDX_TOPIC_NAME.setValue(o.getTopic().getName()).toField())
            .append(IDX_CONTENT.setValue(o.getContent().getContent()).toField());
        index(indexBuilder.toDocument());
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#search(java.lang.String)
     * 
     */
    public List<Long> search(final String expression) throws IOException {
        final List<Field> fields = new ArrayList<Field>();
        fields.add(IDX_TOPIC_NAME.toSearchField());
        fields.add(IDX_CONTENT.toSearchField());
        return search(IDX_TOPIC_ID.toSearchField(), fields, expression);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.AbstractIndexImpl#getComparator()
     *
     */
    @Override
    protected Comparator<? super Long> getComparator() {
        return TOPIC_ID_COMPARATOR;
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
