/*
 * Created On: Aug 31, 2006 12:15:19 PM
 */
package com.thinkparity.ophelia.model.index.profile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.sort.StringComparator;

import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.index.AbstractIndexImpl;
import com.thinkparity.ophelia.model.index.lucene.DocumentBuilder;
import com.thinkparity.ophelia.model.index.lucene.FieldBuilder;
import com.thinkparity.ophelia.model.workspace.Workspace;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

/**
 * <b>Title:</b>thinkParity OpheliaModel Contact Index Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ProfileIndexImpl extends
        AbstractIndexImpl<Profile, JabberId> {

    /** Contact id comparator. */
    private static final Comparator<JabberId> PROFILE_ID_COMPARATOR;

    /** Contact id index field. */
    private static final FieldBuilder IDX_PROFILE_ID;

    /** Contact name index field. */
    private static final FieldBuilder IDX_USER_NAME;

    /** Contact name index field. */
    private static final FieldBuilder IDX_USER_NAME_REV;

    /** Contact  index field. */
    private static final FieldBuilder IDX_USER_ORGANIZATION;

    /** Contact  index field. */
    private static final FieldBuilder IDX_USER_ORGANIZATION_REV;

    /** Contact title index field. */
    private static final FieldBuilder IDX_USER_TITLE;

    /** Contact title index field. */
    private static final FieldBuilder IDX_USER_TITLE_REV;

    static {
        PROFILE_ID_COMPARATOR = new Comparator<JabberId>() {
            final StringComparator stringComparator = new StringComparator(Boolean.TRUE);
            public int compare(final JabberId o1, final JabberId o2) {
                return stringComparator.compare(o1.getQualifiedJabberId(),
                        o2.getQualifiedJabberId());
            }
        };

        IDX_PROFILE_ID = new FieldBuilder()
                .setIndex(Field.Index.UN_TOKENIZED)
                .setName("PROFILE.PROFILE_ID")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_USER_NAME = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("PROFILE.USER.NAME")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_USER_NAME_REV = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("PROFILE.USER.NAME_REV")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_USER_TITLE = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("PROFILE.USER.TITLE")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_USER_TITLE_REV = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("PROFILE.USER.TITLE_REV")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_USER_ORGANIZATION = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("PROFILE.USER.ORGANIZATION")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_USER_ORGANIZATION_REV = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("PROFILE.USER.ORGANIZATION_REV")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);
    }

    /**
     * Create ContactIndexImpl.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @param modelFactory
     *            A thinkParity <code>InternalModelFactory</code>.
     */
    public ProfileIndexImpl(final Workspace workspace,
            final InternalModelFactory modelFactory) {
        super(workspace, modelFactory);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#delete(java.lang.Object)
     * 
     */
    public void delete(final JabberId id) throws IOException {
        final Field idField = IDX_PROFILE_ID.toSearchField();
        final Term term = new Term(idField.name(), id.getQualifiedUsername());
        delete(term);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#index(java.lang.Object)
     */
    public void index(final Profile o) throws IOException {
        final DocumentBuilder builder = new DocumentBuilder(4)
            .append(IDX_PROFILE_ID.setValue(o.getId()).toField())
            .append(IDX_USER_NAME.setValue(o.getName()).toField())
            .append(IDX_USER_NAME_REV.setValue(reverse(IDX_USER_NAME)).toField())
            .append(IDX_USER_ORGANIZATION.setValue(o.getOrganization()).toField())
            .append(IDX_USER_ORGANIZATION_REV.setValue(reverse(IDX_USER_ORGANIZATION)).toField())
            .append(IDX_USER_TITLE.setValue(o.getTitle()).toField())
            .append(IDX_USER_TITLE_REV.setValue(reverse(IDX_USER_TITLE)).toField());
        index(builder.toDocument());
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#search(java.lang.String)
     * 
     */
    public List<JabberId> search(final String expression) throws IOException {
        final List<Field> fields = new ArrayList<Field>(3);
        fields.add(IDX_USER_NAME.toSearchField());
        fields.add(IDX_USER_ORGANIZATION.toSearchField());
        fields.add(IDX_USER_TITLE.toSearchField());
        final List<Field> reverseFields = new ArrayList<Field>(3);
        reverseFields.add(IDX_USER_NAME_REV.toSearchField());
        reverseFields.add(IDX_USER_ORGANIZATION_REV.toSearchField());
        reverseFields.add(IDX_USER_TITLE_REV.toSearchField());
        return search(IDX_PROFILE_ID.toSearchField(), fields, reverseFields, expression);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.AbstractIndexImpl#getComparator()
     *
     */
    @Override
    protected Comparator<? super JabberId> getComparator() {
        return PROFILE_ID_COMPARATOR;
    }

    /**
     * @see com.thinkparity.ophelia.model.index.AbstractIndexImpl#resolveHit(java.lang.String)
     * 
     */
    @Override
    protected JabberId resolveHit(final String hitIdValue) {
        return JabberIdBuilder.parse(hitIdValue);
    }
}
