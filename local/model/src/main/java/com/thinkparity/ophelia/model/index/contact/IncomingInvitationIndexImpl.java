/*
 * Created On: Aug 31, 2006 12:15:19 PM
 */
package com.thinkparity.ophelia.model.index.contact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.index.AbstractIndexImpl;
import com.thinkparity.ophelia.model.index.lucene.DocumentBuilder;
import com.thinkparity.ophelia.model.index.lucene.FieldBuilder;
import com.thinkparity.ophelia.model.workspace.Workspace;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

/**
 * <b>Title:</b>thinkParity OpheliaModel Incoming Invitation Index
 * Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class IncomingInvitationIndexImpl extends
        AbstractIndexImpl<IncomingInvitation, Long> {

    /** Invitation id index field. */
    private static final FieldBuilder IDX_INVITATION_ID;

    /** Invitation invited as email index field. */
    private static final FieldBuilder IDX_INVITED_AS;

    /** Invitation invited as email index field. */
    private static final FieldBuilder IDX_INVITED_AS_REV;

    /** User name index field. */
    private static final FieldBuilder IDX_USER_NAME;

    /** User name index field. */
    private static final FieldBuilder IDX_USER_NAME_REV;

    /** User organization index field. */
    private static final FieldBuilder IDX_USER_ORGANIZATION;

    /** User organization index field. */
    private static final FieldBuilder IDX_USER_ORGANIZATION_REV;

    /** User title index field. */
    private static final FieldBuilder IDX_USER_TITLE;

    /** User title index field. */
    private static final FieldBuilder IDX_USER_TITLE_REV;

    /** Invitation id comparator. */
    private static final Comparator<Long> INVITATION_ID_COMPARATOR;

    static {
        INVITATION_ID_COMPARATOR = new Comparator<Long>() {
            public int compare(final Long o1, final Long o2) {
                return o1.compareTo(o2);
            }
        };

        IDX_INVITATION_ID = new FieldBuilder()
                .setIndex(Field.Index.UN_TOKENIZED)
                .setName("CONTACT_INVITATION_INCOMING.CONTACT_INVITATION_ID")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_INVITED_AS = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTACT_INVITATION_INCOMING.INVITED_AS")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_INVITED_AS_REV = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTACT_INVITATION_INCOMING.INVITED_AS_REV")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_USER_NAME = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTACT_INVITATION_INCOMING.PARITY_USER.NAME")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_USER_NAME_REV = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTACT_INVITATION_INCOMING.PARITY_USER.NAME_REV")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_USER_TITLE = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTACT_INVITATION_INCOMING.PARITY_USER.TITLE")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_USER_TITLE_REV = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTACT_INVITATION_INCOMING.PARITY_USER.TITLE_REV")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_USER_ORGANIZATION = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTACT_INVITATION_INCOMING.PARITY_USER.ORGANIZATION")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_USER_ORGANIZATION_REV = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTACT_INVITATION_INCOMING.PARITY_USER.ORGANIZATION_REV")
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
    public IncomingInvitationIndexImpl(final Workspace workspace,
            final InternalModelFactory modelFactory) {
        super(workspace, modelFactory);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#delete(java.lang.Object)
     */
    public void delete(final Long id) throws IOException {
        final Field idField = IDX_INVITATION_ID.toSearchField();
        final Term term = new Term(idField.name(), id.toString());
        delete(term);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#index(java.lang.Object)
     */
    public void index(final IncomingInvitation o) throws IOException {
        final User user = getUserModel().read(o.getInvitedBy());
        final DocumentBuilder builder = new DocumentBuilder(4)
            .append(IDX_INVITATION_ID.setValue(o.getId()).toField())
            .append(IDX_INVITED_AS.setValue(o.getInvitedAs()).toField())
            .append(IDX_INVITED_AS_REV.setValue(reverse(IDX_INVITED_AS)).toField())
            .append(IDX_USER_NAME.setValue(user.getName()).toField())
            .append(IDX_USER_NAME_REV.setValue(reverse(IDX_USER_NAME)).toField())
            .append(IDX_USER_ORGANIZATION.setValue(user.getOrganization()).toField())
            .append(IDX_USER_ORGANIZATION_REV.setValue(reverse(IDX_USER_ORGANIZATION)).toField())
            .append(IDX_USER_TITLE.setValue(user.getTitle()).toField())
            .append(IDX_USER_TITLE_REV.setValue(reverse(IDX_USER_TITLE)).toField());
        index(builder.toDocument());
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#search(java.lang.String)
     */
    public List<Long> search(final String expression) throws IOException {
        final List<Field> fields = new ArrayList<Field>(4);
        fields.add(IDX_INVITED_AS.toSearchField());
        fields.add(IDX_USER_NAME.toSearchField());
        fields.add(IDX_USER_ORGANIZATION.toSearchField());
        fields.add(IDX_USER_TITLE.toSearchField());
        final List<Field> reverseFields = new ArrayList<Field>(4);
        reverseFields.add(IDX_INVITED_AS_REV.toSearchField());
        reverseFields.add(IDX_USER_NAME_REV.toSearchField());
        reverseFields.add(IDX_USER_ORGANIZATION_REV.toSearchField());
        reverseFields.add(IDX_USER_TITLE_REV.toSearchField());
        return search(IDX_INVITATION_ID.toSearchField(), fields, reverseFields,
                expression);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.AbstractIndexImpl#getComparator()
     *
     */
    @Override
    protected Comparator<? super Long> getComparator() {
        return INVITATION_ID_COMPARATOR;
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
