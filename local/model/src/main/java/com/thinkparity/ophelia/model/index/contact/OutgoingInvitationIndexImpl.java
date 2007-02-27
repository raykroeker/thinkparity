/*
 * Created On: Aug 31, 2006 12:15:19 PM
 */
package com.thinkparity.ophelia.model.index.contact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;
import com.thinkparity.ophelia.model.index.AbstractIndexImpl;
import com.thinkparity.ophelia.model.index.lucene.DocumentBuilder;
import com.thinkparity.ophelia.model.index.lucene.FieldBuilder;
import com.thinkparity.ophelia.model.workspace.Workspace;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

/**
 * <b>Title:</b>thinkParity OpheliaModel Outgoing Incoming Index Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class OutgoingInvitationIndexImpl extends
        AbstractIndexImpl<OutgoingInvitation, Long> {

    /** Contact name index field. */
    private static final FieldBuilder IDX_INVITATION_EMAIL;

    /** Contact name index field. */
    private static final FieldBuilder IDX_INVITATION_EMAIL_REV;

    /** Contact id index field. */
    private static final FieldBuilder IDX_INVITATION_ID;

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
                .setName("CONTACT_INVITATION_OUTGOING.CONTACT_INVITATION_ID")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_INVITATION_EMAIL = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTACT_INVITATION_OUTGOING.EMAIL")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_INVITATION_EMAIL_REV = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTACT_INVITATION_OUTGOING.EMAIL_REV")
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
    public OutgoingInvitationIndexImpl(final Workspace workspace,
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
    public void index(final OutgoingInvitation o) throws IOException {
        final DocumentBuilder builder = new DocumentBuilder(4)
            .append(IDX_INVITATION_ID.setValue(o.getId()).toField())
            .append(IDX_INVITATION_EMAIL.setValue(o.getEmail()).toField())
            .append(IDX_INVITATION_EMAIL_REV.setValue(reverse(IDX_INVITATION_EMAIL)).toField());
        index(builder.toDocument());
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#search(java.lang.String)
     */
    public List<Long> search(final String expression) throws IOException {
        final List<Field> fields = new ArrayList<Field>(1);
        fields.add(IDX_INVITATION_EMAIL.toSearchField());
        final List<Field> reverseFields = new ArrayList<Field>(1);
        reverseFields.add(IDX_INVITATION_EMAIL_REV.toSearchField());
        return search(IDX_INVITATION_ID.toSearchField(), fields, reverseFields, expression);
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
