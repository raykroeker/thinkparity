/*
 * Created On: Aug 31, 2006 12:15:19 PM
 */
package com.thinkparity.model.parity.model.index.contact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.index.AbstractIndexImpl;
import com.thinkparity.model.parity.model.index.lucene.DocumentBuilder;
import com.thinkparity.model.parity.model.index.lucene.FieldBuilder;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContactIndexImpl extends AbstractIndexImpl<Contact, JabberId> {

    /** Contact id index field. */
    private static final FieldBuilder IDX_CONTACT_ID;

    /** Contact name index field. */
    private static final FieldBuilder IDX_CONTACT_NAME;

    /** Contact  index field. */
    private static final FieldBuilder IDX_CONTACT_ORGANIZATION;

    /** Contact title index field. */
    private static final FieldBuilder IDX_CONTACT_TITLE;

    static {
        IDX_CONTACT_ID = new FieldBuilder()
                .setIndex(Field.Index.UN_TOKENIZED)
                .setName("CONTACT.CONTACT_ID")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_CONTACT_NAME = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTACT.CONTACT_NAME")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_CONTACT_TITLE= new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTACT.CONTACT_TITLE")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);

        IDX_CONTACT_ORGANIZATION = new FieldBuilder()
                .setIndex(Field.Index.TOKENIZED)
                .setName("CONTACT.CONTACT_ORGANIZATION")
                .setStore(Field.Store.YES)
                .setTermVector(Field.TermVector.NO);
    }

    /**
     * Create ContactIndexImpl.
     * 
     * @param context
     *            A thinkParity model context.
     * @param workspace
     *            A thinKParity workspace.
     */
    public ContactIndexImpl(final Context context, final Workspace workspace) {
        super(context, workspace);
    }

    /**
     * @see com.thinkparity.model.parity.model.index.IndexImpl#delete(java.lang.Object)
     */
    public void delete(final Contact o) throws IOException {
        final Field id = IDX_CONTACT_ID.toSearchField();
        final Term term = new Term(id.name(), o.getId().getQualifiedUsername());
        delete(term);
    }

    /**
     * @see com.thinkparity.model.parity.model.index.IndexImpl#index(java.lang.Object)
     */
    public void index(final Contact o) throws IOException {
        final DocumentBuilder builder = new DocumentBuilder(4)
            .append(IDX_CONTACT_ID.setValue(o.getId()).toField())
            .append(IDX_CONTACT_NAME.setValue(o.getName()).toField());
        if (o.isSetOrganization())
            builder.append(IDX_CONTACT_ORGANIZATION.setValue(o.getOrganization()).toField());
        if (o.isSetTitle())
            builder.append(IDX_CONTACT_TITLE.setValue(o.getTitle()).toField());
        index(builder.toDocument());
    }

    /**
     * @see com.thinkparity.model.parity.model.index.IndexImpl#search(java.lang.String)
     */
    public List<JabberId> search(final String expression) throws IOException {
        final List<Field> fields = new ArrayList<Field>(3);
        fields.add(IDX_CONTACT_NAME.toSearchField());
        fields.add(IDX_CONTACT_ORGANIZATION.toSearchField());
        fields.add(IDX_CONTACT_TITLE.toSearchField());
        return search(IDX_CONTACT_ID.toSearchField(), fields, expression);
    }

    /**
     * @see com.thinkparity.model.parity.model.index.AbstractIndexImpl#resolveHit(java.lang.String)
     * 
     */
    @Override
    protected JabberId resolveHit(final String hitIdValue) {
        return JabberIdBuilder.parseQualifiedJabberId(hitIdValue);
    }
}
