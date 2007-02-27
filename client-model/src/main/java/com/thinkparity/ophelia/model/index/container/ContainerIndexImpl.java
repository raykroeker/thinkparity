/*
 * Created On: Aug 31, 2006 10:27:02 AM
 */
package com.thinkparity.ophelia.model.index.container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.user.TeamMember;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.index.AbstractIndexImpl;
import com.thinkparity.ophelia.model.index.lucene.DocumentBuilder;
import com.thinkparity.ophelia.model.index.lucene.FieldBuilder;
import com.thinkparity.ophelia.model.workspace.Workspace;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

/**
 * <b>Title:</b>thinkParity OpheliaModel Container Index Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerIndexImpl extends
        AbstractIndexImpl<Container, Long> {

    /** Container id <code>Comparator</code>. */
    private static final Comparator<Long> CONTAINER_ID_COMPARATOR;

    /** Artifact id index field. */
    private static final FieldBuilder IDX_CONTAINER_ID;

    /** Container name index field. */
    private static final FieldBuilder IDX_CONTAINER_NAME;

    /** Container name reverse index field. */
    private static final FieldBuilder IDX_CONTAINER_NAME_REV;

    /** Container team members index field. */
    private static final FieldBuilder IDX_CONTAINER_TEAM_MEMBERS;

    /** Container team members reverse index field. */
    private static final FieldBuilder IDX_CONTAINER_TEAM_MEMBERS_REV;

    static {
        CONTAINER_ID_COMPARATOR = new Comparator<Long>() {
            public int compare(final Long o1, final Long o2) {
                return o1.compareTo(o2);
            }
        };

        IDX_CONTAINER_ID = new FieldBuilder()
                .setIndex(Field.Index.UN_TOKENIZED)
                .setName("CONTAINER.CONTAINER_ID")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_CONTAINER_NAME = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTAINER.CONTAINER_NAME")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_CONTAINER_NAME_REV = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTAINER.CONTAINER_NAME_REV")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_CONTAINER_TEAM_MEMBERS = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTAINER.CONTAINER_TEAM_MEMBERS")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_CONTAINER_TEAM_MEMBERS_REV = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTAINER.CONTAINER_TEAM_MEMBERS_REV")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);
    }

    /**
     * Create ArtifactIndexImpl.
     * 
     * @param context
     *            A thinkParity model context.
     */
    public ContainerIndexImpl(final Workspace workspace,
            final InternalModelFactory modelFactory) {
        super(workspace, modelFactory);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#delete(java.lang.Object)
     */
    public void delete(final Long containerId) throws IOException {
        final Field idField = IDX_CONTAINER_ID.toSearchField();
        final Term term = new Term(idField.name(), containerId.toString());
        delete(term);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#index(java.lang.Object)
     * 
     */
    public void index(final Container o) throws IOException {
        final List<TeamMember> team = getArtifactModel().readTeam2(((Container) o).getId());
        final DocumentBuilder indexBuilder = new DocumentBuilder(3)
            .append(IDX_CONTAINER_ID.setValue(o.getId()).toField())
            .append(IDX_CONTAINER_NAME.setValue(o.getName()).toField())
            .append(IDX_CONTAINER_NAME_REV.setValue(reverse(IDX_CONTAINER_NAME)).toField())
            .append(IDX_CONTAINER_TEAM_MEMBERS.setValue(team).toField())
            .append(IDX_CONTAINER_TEAM_MEMBERS_REV.setValue(reverse(IDX_CONTAINER_TEAM_MEMBERS)).toField());
        index(indexBuilder.toDocument());
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#search(java.lang.String)
     * 
     */
    public List<Long> search(final String expression) throws IOException {
        final List<Field> fields = new ArrayList<Field>();
        fields.add(IDX_CONTAINER_NAME.toSearchField());
        fields.add(IDX_CONTAINER_TEAM_MEMBERS.toSearchField());
        final List<Field> reversedFields = new ArrayList<Field>();
        reversedFields.add(IDX_CONTAINER_NAME_REV.toSearchField());
        reversedFields.add(IDX_CONTAINER_TEAM_MEMBERS_REV.toSearchField());
        return search(IDX_CONTAINER_ID.toSearchField(), fields, reversedFields, expression);
    }

    
    /**
     * @see com.thinkparity.ophelia.model.index.AbstractIndexImpl#getComparator()
     *
     */
    @Override
    protected Comparator<? super Long> getComparator() {
        return CONTAINER_ID_COMPARATOR;
    }

    /**
     * @see com.thinkparity.ophelia.model.index.AbstractIndexImpl#resolveHit(java.lang.String)
     */
    @Override
    protected Long resolveHit(final String hitIdValue) {
        return Long.parseLong(hitIdValue);
    }
}
