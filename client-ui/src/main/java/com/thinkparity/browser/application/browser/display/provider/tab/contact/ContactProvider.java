/*
 * Mar 10, 2006
 */
package com.thinkparity.browser.application.browser.display.provider.tab.contact;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.browser.application.browser.display.provider.SingleContentProvider;

import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.contact.IncomingInvitation;
import com.thinkparity.model.parity.model.contact.OutgoingInvitation;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.user.UserModel;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * Provide a flat list of contacts from the session model.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ContactProvider extends CompositeFlatSingleContentProvider {

    /** A contact provider. */
    private final SingleContentProvider contactProvider;

    /** A contact list provider. */
    private final FlatContentProvider contactsProvider;

    /** An array of flat providers. */
    private final FlatContentProvider[] flatProviders;

    /** An incoming invitation provider. */
    private final SingleContentProvider incomingInvitationProvider;

    /** An incoming invitations provider. */
    private final FlatContentProvider incomingInvitationsProvider;

    /** An outgoing invitation provider. */
    private final SingleContentProvider outgoingInvitationProvider;

    /** An outgoing invitations provider. */
    private final FlatContentProvider outgoingInvitationsProvider;

    /** A contact id list provider (search). */
    private final FlatContentProvider searchResultsProvider;

    /** An array of single providers. */
    private final SingleContentProvider[] singleProviders;

    /** A user provider. */
    private final SingleContentProvider userProvider;

    /**
     * Create ContactProvider.
     * 
     * @param profile
     *            A thinkParity user's profile.
     * @param contactModel
     *            A thinkParity contact interface.
     */
	public ContactProvider(final Profile profile,
            final ContactModel contactModel, final UserModel userModel) {
		super(profile);
        this.contactProvider = new SingleContentProvider(profile) {
            @Override
            public Object getElement(final Object input) {
                Assert.assertNotNull("NULL INPUT", input);
                Assert.assertOfType("INPUT IS OF WRONG TYPE", JabberId.class, input);
                return contactModel.read((JabberId) input);
            }
        };
        this.contactsProvider = new FlatContentProvider(profile) {
            @Override
            public Object[] getElements(final Object input) {
                return contactModel.read().toArray(new Contact[] {});
            }
        };
        this.incomingInvitationProvider = new SingleContentProvider(profile) {
            @Override
            public Object getElement(final Object input) {
                Assert.assertNotNull("NULL INPUT", input);
                Assert.assertOfType("INPUT IS OF WRONG TYPE", Long.class, input);
                return contactModel.readIncomingInvitation((Long) input);
            }
        };
        this.incomingInvitationsProvider = new FlatContentProvider(profile) {
            @Override
            public Object[] getElements(final Object input) {
                return contactModel.readIncomingInvitations().toArray(new IncomingInvitation[] {});
            }
        };
        this.outgoingInvitationProvider = new SingleContentProvider(profile) {
            @Override
            public Object getElement(Object input) {
                Assert.assertNotNull("NULL INPUT", input);
                Assert.assertOfType("INPUT IS OF WRONG TYPE", Long.class, input);
                return contactModel.readOutgoingInvitation((Long) input);
            }
        };
        this.outgoingInvitationsProvider = new FlatContentProvider(profile) {
            @Override
            public Object[] getElements(Object input) {
                return contactModel.readOutgoingInvitations().toArray(new OutgoingInvitation[] {});
            }
        };
        this.searchResultsProvider = new FlatContentProvider(profile) {
            @Override
            public Object[] getElements(final Object input) {
                Assert.assertNotNull("NULL INPUT", input);
                Assert.assertOfType("INPUT IS OF WRONG TYPE", String.class, input);
                return contactModel.search((String) input).toArray(new JabberId[] {});
            }
        };
        this.userProvider = new SingleContentProvider(profile) {
            @Override
            public Object getElement(Object input) {
                Assert.assertNotNull("NULL INPUT", input);
                Assert.assertOfType("INPUT IS OF WRONG TYPE", JabberId.class, input);
                return userModel.read((JabberId) input);
            }
        };
        this.flatProviders = new FlatContentProvider[] {
                contactsProvider, incomingInvitationsProvider,
                outgoingInvitationsProvider, searchResultsProvider };
        this.singleProviders = new SingleContentProvider[] {
                contactProvider, incomingInvitationProvider,
                outgoingInvitationProvider, userProvider };
	}

    /**
     * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElement(java.lang.Integer, java.lang.Object)
     */
    @Override
    public Object getElement(final Integer index, final Object input) {
        Assert.assertInBounds("PROVIDER INDEX OUT OF BOUNDS", index, singleProviders);
        return singleProviders[index].getElement(input);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider#getElements(java.lang.Integer, java.lang.Object)
     */
    @Override
    public Object[] getElements(Integer index, Object input) {
        Assert.assertInBounds("PROVIDER INDEX OUT OF BOUNDS", index, flatProviders);
        return flatProviders[index].getElements(input);
    }
}
