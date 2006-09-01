/*
 * Created On: Aug 31, 2006 10:27:02 AM
 */
package com.thinkparity.model.parity.model.index.container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.index.AbstractIndexImpl;
import com.thinkparity.model.parity.model.index.lucene.DocumentBuilder;
import com.thinkparity.model.parity.model.index.lucene.FieldBuilder;
import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerIndexImpl extends AbstractIndexImpl<Container, Long> {

    /** Artifact id index field. */
    private static final FieldBuilder IDX_CONTAINER_ID;

    /** Artifact name index field. */
    private static final FieldBuilder IDX_CONTAINER_NAME;

    /** Artifact team members index field. */
    private static final FieldBuilder IDX_CONTAINER_TEAM_MEMBERS;

    static {
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

        IDX_CONTAINER_TEAM_MEMBERS = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTAINER.CONTAINER_TEAM_MEMBERS")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);
    }

    /**
     * Create ArtifactIndexImpl.
     * 
     * @param context
     *            A thinkParity model context.
     */
    public ContainerIndexImpl(final Context context, final Workspace workspace) {
        super(context, workspace);
    }

    /**
     * @see com.thinkparity.model.parity.model.index.IndexImpl#delete(java.lang.Object)
     */
    public void delete(final Container o) throws IOException {
        final Field field = IDX_CONTAINER_ID.toSearchField();
        final Term term = new Term(field.name(), o.getId().toString());
        delete(term);
    }

    /**
     * @see com.thinkparity.model.parity.model.index.IndexImpl#index(java.lang.Object)
     * 
     */
    public void index(final Container o) throws IOException {
        final List<TeamMember> team = getArtifactModel().readTeam2(((Container) o).getId());
        final DocumentBuilder indexBuilder = new DocumentBuilder(3)
            .append(IDX_CONTAINER_ID.setValue(o.getId()).toField())
            .append(IDX_CONTAINER_NAME.setValue(o.getName()).toField())
            .append(IDX_CONTAINER_TEAM_MEMBERS.setValue(team).toField());
        index(indexBuilder.toDocument());
    }

    /**
     * @see com.thinkparity.model.parity.model.index.IndexImpl#search(java.lang.String)
     * 
     */
    public List<Long> search(final String expression) throws IOException {
        final List<Field> fields = new ArrayList<Field>();
        fields.add(IDX_CONTAINER_NAME.toSearchField());
        fields.add(IDX_CONTAINER_TEAM_MEMBERS.toSearchField());
        return search(IDX_CONTAINER_ID.toSearchField(), fields, expression);
    }

    /**
     * @see com.thinkparity.model.parity.model.index.AbstractIndexImpl#resolveHit(java.lang.String)
     */
    @Override
    protected Long resolveHit(final String hitIdValue) {
        return Long.parseLong(hitIdValue);
    }
}
