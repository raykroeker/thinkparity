/*
 * Created On: Aug 31, 2006 10:27:02 AM
 */
package com.thinkparity.ophelia.model.index.container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.Pair;

import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.index.AbstractIndexImpl;
import com.thinkparity.ophelia.model.index.lucene.DocumentBuilder;
import com.thinkparity.ophelia.model.index.lucene.FieldBuilder;
import com.thinkparity.ophelia.model.workspace.Workspace;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

/**
 * <b>Title:</b>thinkParity OpheliaModel Container Index Implementation<br>
 * <b>Description:</b>Indexes container versions and allows a client to bring
 * back a container from a search expression.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerVersionIndexImpl extends
        AbstractIndexImpl<ContainerVersion, Pair<Long, Long>> {

    /** Container id <code>Comparator</code>. */
    private static final Comparator<? super Pair<Long, Long>> CONTAINER_VERSION_ID_COMPARATOR;

    /** The separator character between the container id and the version id. */
    private static final char ID_SEP;

    /** Container verison comment index field. */
    private static final FieldBuilder IDX_CONTAINER_VERSION_COMMENT;

    /** Container verison comment index field. */
    private static final FieldBuilder IDX_CONTAINER_VERSION_COMMENT_REV;

    /** The version name index field. */
    private static final FieldBuilder IDX_CONTAINER_VERSION_NAME;

    /** The version name index field. */
    private static final FieldBuilder IDX_CONTAINER_VERSION_NAME_REV;

    /** Composite container version id index field. */
    private static final FieldBuilder IDX_CONTAINER_VERSION_ID;

    static {
        CONTAINER_VERSION_ID_COMPARATOR = new Comparator<Pair<Long, Long>>() {
            public int compare(final Pair<Long, Long> o1, final Pair<Long, Long> o2) {
                final int resultOne = o1.getOne().compareTo(o2.getOne());
                if (0 == resultOne) {
                    return o1.getTwo().compareTo(o2.getTwo());
                } else {
                    return resultOne;
                }
            }
        };

        ID_SEP = '.';

        IDX_CONTAINER_VERSION_ID = new FieldBuilder()
            .setIndex(Field.Index.UN_TOKENIZED)
            .setName("CONTAINER_VERSION.CONTAINER_VERSION_ID")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);

        IDX_CONTAINER_VERSION_COMMENT = new FieldBuilder()
            .setIndex(Field.Index.TOKENIZED)
            .setName("CONTAINER_VERSION.COMMENT")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);
        
        IDX_CONTAINER_VERSION_COMMENT_REV = new FieldBuilder()
            .setIndex(Field.Index.TOKENIZED)
            .setName("CONTAINER_VERSION.COMMENT_REV")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);

        IDX_CONTAINER_VERSION_NAME = new FieldBuilder()
            .setIndex(Field.Index.TOKENIZED)
            .setName("CONTAINER_VERSION.NAME")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);

        IDX_CONTAINER_VERSION_NAME_REV = new FieldBuilder()
            .setIndex(Field.Index.TOKENIZED)
            .setName("CONTAINER_VERSION.NAME_REV")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);
    }

    /**
     * Create ArtifactIndexImpl.
     * 
     * @param context
     *            A thinkParity model context.
     */
    public ContainerVersionIndexImpl(final Workspace workspace,
            final InternalModelFactory modelFactory) {
        super(workspace, modelFactory);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#delete(java.lang.Object)
     *
     */
    public void delete(final Pair<Long, Long> containerVersionId) throws IOException {
        final Field idField = IDX_CONTAINER_VERSION_ID.toSearchField();
        final Term term = new Term(idField.name(), buildStringId(containerVersionId));
        delete(term);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#index(java.lang.Object)
     * 
     */
    public void index(final ContainerVersion o) throws IOException {
        final DocumentBuilder indexBuilder = new DocumentBuilder(3)
            .append(IDX_CONTAINER_VERSION_ID.setValue(buildStringId(o)).toField());
        if (o.isSetComment()) {
            indexBuilder.append(IDX_CONTAINER_VERSION_COMMENT.setValue(o.getComment()).toField())
                .append(IDX_CONTAINER_VERSION_COMMENT_REV.setValue(reverse(IDX_CONTAINER_VERSION_COMMENT)).toField());
        }
        if (o.isSetName()) {
            indexBuilder.append(IDX_CONTAINER_VERSION_NAME.setValue(o.getName()).toField())
                .append(IDX_CONTAINER_VERSION_NAME_REV.setValue(reverse(IDX_CONTAINER_VERSION_NAME)).toField());
        }
        index(indexBuilder.toDocument());
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#search(java.lang.String)
     * 
     */
    public List<Pair<Long, Long>> search(final String expression) throws IOException {
        final List<Field> fields = new ArrayList<Field>();
        fields.add(IDX_CONTAINER_VERSION_COMMENT.toSearchField());
        fields.add(IDX_CONTAINER_VERSION_NAME.toSearchField());
        final List<Field> reversedFields = new ArrayList<Field>();
        reversedFields.add(IDX_CONTAINER_VERSION_COMMENT_REV.toSearchField());
        reversedFields.add(IDX_CONTAINER_VERSION_NAME_REV.toSearchField());
        return search(IDX_CONTAINER_VERSION_ID.toSearchField(), fields, reversedFields, expression);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.AbstractIndexImpl#getComparator()
     *
     */
    @Override
    protected Comparator<? super Pair<Long, Long>> getComparator() {
        return CONTAINER_VERSION_ID_COMPARATOR;
    }

    /**
     * @see com.thinkparity.ophelia.model.index.AbstractIndexImpl#resolveHit(java.lang.String)
     * 
     */
    @Override
    protected Pair<Long, Long> resolveHit(final String hitIdValue) {
        return buildPairId(hitIdValue);
    }

    private Pair<Long, Long> buildPairId(final String stringId) {
        final int index = stringId.indexOf(ID_SEP);
        final Long containerId = Long.valueOf(stringId.substring(0, index));
        final Long versionId = Long.valueOf(stringId.substring(index + 1));
        return new Pair<Long, Long>(containerId, versionId);
    }

    private String buildStringId(final ContainerVersion version) {
        return buildStringId(version.getArtifactId(), version.getVersionId());
    }

    private String buildStringId(final Long containerId, final Long versionId) {
        return new StringBuffer().append(containerId)
            .append(ID_SEP).append(versionId).toString();
    }

    private String buildStringId(final Pair<Long, Long> containerVersionId) {
        return buildStringId(containerVersionId.getOne(), containerVersionId.getTwo());
    }
}
