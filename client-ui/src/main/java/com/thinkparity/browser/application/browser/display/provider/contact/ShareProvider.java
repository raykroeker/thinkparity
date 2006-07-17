/*
 * Created On: Jun 8, 2006 11:37:58 AM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.provider.contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ShareProvider extends FlatContentProvider {

    /** The thinkParity artifact interface. */
    private final ArtifactModel aModel;

    /** The thinkParity contact interface. */
    private final ContactModel cModel;

    /**
     * Create ShareProvider.
     * 
     * @param aModel
     *            A thinkParity artifact interface.
     * @param cModel
     *            A thinkParity contact interface.
     */
    public ShareProvider(final Profile profile, final ArtifactModel aModel,
            final ContactModel cModel) {
        super(profile);
        this.aModel = aModel;
        this.cModel = cModel;
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

        final List<Contact> contacts = cModel.read();
        final List<Contact> shareUsers = new ArrayList<Contact>(contacts.size());
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
