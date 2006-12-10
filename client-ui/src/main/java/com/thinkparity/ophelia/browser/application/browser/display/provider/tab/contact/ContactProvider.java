/*
 * Mar 10, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.tab.contact;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;



import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.SingleContentProvider;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;
import com.thinkparity.ophelia.model.user.UserModel;
import java.util.List;
import com.thinkparity.codebase.model.user.User;

/**
 * Provide a flat list of contacts from the session model.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ContactProvider extends CompositeFlatSingleContentProvider {

    /** A thinkParity <code>ContactModel</code>. */
    private final ContactModel contactModel;

    /** A thinkParity <code>UserModel</code>. */
    private final UserModel userModel;

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
        this.contactModel = contactModel;
        this.userModel = userModel;
	}

    public List<Contact> readContacts() {
        return contactModel.read();
    }

    public Contact readContact(final JabberId contactId) {
        return contactModel.read(contactId);
    }

    public List<JabberId> search(final String expression) {
        return contactModel.search(expression);
    }

    public User readUser(final JabberId userId) {
        return userModel.read(userId);
    }

    @Override
    public Object getElement(final Integer index, final Object input) {
        throw Assert.createUnreachable("Deprecated paradigm.");
    }

    @Override
    public Object[] getElements(final Integer index, final Object input) {
        throw Assert.createUnreachable("Deprecated paradigm.");
    }
}
