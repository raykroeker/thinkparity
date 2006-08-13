/**
 * Created On: 2-Aug-06 3:56:43 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.provider.dialog.container;

import com.thinkparity.browser.application.browser.display.provider.CompositeFlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.container.ContainerModel;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ManageTeamProvider extends CompositeFlatContentProvider {
    
    /**
     * Providers.
     */
    private final FlatContentProvider teamProvider;
    private final FlatContentProvider contactsProvider;
    
    /** The set of providers. */
    private final FlatContentProvider[] flatProviders;  

    /**
     * Create a ManageContactsProvider.
     */
    public ManageTeamProvider(final Profile profile, final ContainerModel containerModel, final ContactModel contactModel) {
        super(profile);
        this.teamProvider = new FlatContentProvider(profile) {
            public Object[] getElements(final Object input) {
                final Long containerId = (Long) input;
                return containerModel.readTeam(containerId).toArray(new TeamMember[] {});
            }              
        };
        this.contactsProvider = new FlatContentProvider(profile) {
            public Object[] getElements(final Object input) {
                return contactModel.read().toArray(new Contact[] {});
            }            
        };
        this.flatProviders = new FlatContentProvider[] {contactsProvider, teamProvider};
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatContentProvider#getElements(java.lang.Integer,
     *      java.lang.Object)
     * 
     */
    public Object[] getElements(final Integer index, final Object input) {
        Assert.assertNotNull("Index cannot be null.", index);
        Assert.assertTrue(
                "Index must lie within [0," + (flatProviders.length - 1) + "]",
                index >= 0 && index < flatProviders.length);
        return flatProviders[index].getElements(input);
    }
}
