/**
 * Created On: 2-Aug-06 3:56:43 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.dialog.container;


import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.user.TeamMember;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ManageTeamProvider extends CompositeFlatSingleContentProvider {
    
    /**
     * Providers.
     */
    private final FlatContentProvider teamProvider;
    private final FlatContentProvider contactsProvider;
    private final SingleContentProvider profileProvider;
    
    /** The set of flat providers. */
    private final FlatContentProvider[] flatProviders;
    
    /** The set of single providers. */
    private final SingleContentProvider[] singleProviders;

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
        this.profileProvider = new SingleContentProvider(profile) {
            public Object getElement(final Object input) {
                return profile;
            }            
        };
        this.flatProviders = new FlatContentProvider[] {contactsProvider, teamProvider};
        this.singleProviders = new SingleContentProvider[] {profileProvider};
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatContentProvider#getElements(java.lang.Integer, java.lang.Object)
     * 
     */
    public Object[] getElements(final Integer index, final Object input) {
        Assert.assertNotNull("Index cannot be null.", index);
        Assert.assertTrue(
                "Index must lie within [0," + (flatProviders.length - 1) + "]",
                index >= 0 && index < flatProviders.length);
        return flatProviders[index].getElements(input);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElement(java.lang.Integer, java.lang.Object)
     */
    public Object getElement(final Integer index, final Object input) {
        Assert.assertNotNull("Index cannot be null.", index);
        Assert.assertTrue(
                "Index must lie within [0," + (singleProviders.length - 1) + "]",
                index >= 0 && index < singleProviders.length);
        return singleProviders[index].getElement(input);
    }
    
    
}
