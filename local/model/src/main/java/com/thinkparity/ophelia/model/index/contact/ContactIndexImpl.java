/*
 * Created On: Aug 31, 2006 12:15:19 PM
 */
package com.thinkparity.ophelia.model.index.contact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.model.contact.Contact;

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
public final class ContactIndexImpl extends AbstractIndexImpl<Contact, Long> {

    /** Contact id comparator. */
    private static final Comparator<Long> CONTACT_ID_COMPARATOR;

    /** Contact id index field. */
    private static final FieldBuilder IDX_CONTACT_ID;

    /** Contact name index field. */
    private static final FieldBuilder IDX_USER_NAME;

    /** Contact  index field. */
    private static final FieldBuilder IDX_USER_ORGANIZATION;

    /** Contact title index field. */
    private static final FieldBuilder IDX_USER_TITLE;

    static {
        CONTACT_ID_COMPARATOR = new Comparator<Long>() {
            public int compare(final Long o1, final Long o2) {
                return o1.compareTo(o2);
            }
        };

        IDX_CONTACT_ID = new FieldBuilder()
            .setIndex(Field.Index.UN_TOKENIZED)
            .setName("CONTACT.CONTACT_ID")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);

        IDX_USER_NAME = new FieldBuilder()
            .setIndex(Field.Index.TOKENIZED)
            .setName("CONTACT.USER.NAME")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);

        IDX_USER_TITLE = new FieldBuilder()
            .setIndex(Field.Index.TOKENIZED)
            .setName("CONTACT.USER.TITLE")
            .setStore(Field.Store.YES)
            .setTermVector(Field.TermVector.NO);

        IDX_USER_ORGANIZATION = new FieldBuilder()
            .setIndex(Field.Index.TOKENIZED)
            .setName("CONTACT.USER.ORGANIZATION")
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
    public ContactIndexImpl(final Workspace workspace,
            final InternalModelFactory modelFactory) {
        super(workspace, modelFactory);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#delete(java.lang.Object)
     * 
     */
    public void delete(final Long id) throws IOException {
        final Field idField = IDX_CONTACT_ID.toSearchField();
        final Term term = new Term(idField.name(), id.toString());
        delete(term);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#index(java.lang.Object)
     */
    public void index(final Contact o) throws IOException {
        final DocumentBuilder builder = new DocumentBuilder(4)
            .append(IDX_CONTACT_ID.setValue(o.getLocalId()).toField())
            .append(IDX_USER_NAME.setValue(o.getName()).toField())
            .append(IDX_USER_ORGANIZATION.setValue(o.getOrganization()).toField())
            .append(IDX_USER_TITLE.setValue(o.getTitle()).toField());
        index(builder.toDocument());
    }

    /**
     * @see com.thinkparity.ophelia.model.index.IndexImpl#search(java.lang.String)
     * 
     */
    public List<Long> search(final String expression) throws IOException {
        final List<Field> fields = new ArrayList<Field>(3);
        fields.add(IDX_USER_NAME.toSearchField());
        fields.add(IDX_USER_ORGANIZATION.toSearchField());
        fields.add(IDX_USER_TITLE.toSearchField());
        return search(IDX_CONTACT_ID.toSearchField(), fields, expression);
    }

    /**
     * @see com.thinkparity.ophelia.model.index.AbstractIndexImpl#getComparator()
     *
     */
    @Override
    protected Comparator<? super Long> getComparator() {
        return CONTACT_ID_COMPARATOR;
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
