/*
 * Created On: Jun 8, 2006 11:37:58 AM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.provider.contact;

import java.util.HashSet;
import java.util.Set;

import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ShareProvider extends FlatContentProvider {

    /** The parity artifact interface. */
    private final ArtifactModel aModel;

    /** The parity session interface. */
    private final SessionModel sModel;

    /**
     * Create ShareProvider.
     * 
     * @param aModel
     *            An artifact model.
     * @param sModel
     *            A session model.
     */
    public ShareProvider(final ArtifactModel aModel, final SessionModel sModel) {
        super();
        this.aModel = aModel;
        this.sModel = sModel;
    }

    /** @see com.thinkparity.browser.application.browser.display.provider.FlatContentProvider#getElements(java.lang.Object) */
    public Object[] getElements(final Object input) {
        return getShareContacts(getInputArtifactId(input));
    }

    /**
     * Obtain an artifact id from the input.
     * 
     * @param input
     *            Provider input.
     * @return An artifact id.
     */
    protected Long getInputArtifactId(final Object input) {
        Assert.assertNotNull("[LBROWSER] [CONTACT SHARE PROVIDER] [GET INPUT ARTIFACT ID] [INPUT IS NULL]", input);
        Assert.assertOfType("[LBROWSER] [CONTACT SHARE PROVIDER] [GET INPUT ARTIFACT ID] [INPUT IS NOT OF TYPE LONG]", Long.class, input);
        return (Long) input;
    }

    /**
     * Obtain a list of contacts not on the artifact team. If there are more
     * contacts than the numberOfContacts provided; the list is cut short.
     * 
     * @param numberOfContacts
     *            The maximum number of contacts to return.
     * @param artifactId
     *            An artifact id.
     * @param aModel
     *            An artifact model.
     * @param sModel
     *            A session model.
     * @return A list of contacts.
     */
    protected Contact[] getShareContacts(final Integer numberOfContacts,
            final Long artifactId) {
        final Set<User> team = aModel.readTeam(artifactId);

        Set<Contact> contacts = null;
        try { contacts = sModel.readContacts(); }
        catch(final ParityException px) { throw new RuntimeException(px); }

        final Set<Contact> shareUsers = new HashSet<Contact>();
        for(final Contact contact : contacts) {
            if(numberOfContacts <= shareUsers.size()) { break; }
            if(!team.contains(contact)) { shareUsers.add(contact); }
        }
        return shareUsers.toArray(new Contact[] {});   
    }

    /**
     * Obtain a list of contacts not on the artifact team.
     * 
     * @param artifactId
     *            An artifact id.
     * @return A list of contacts.
     */
    private Contact[] getShareContacts(final Long artifactId) {
        return getShareContacts(Integer.MAX_VALUE, artifactId);
    }

}
