/*
 * Mar 10, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.tab.contact;

import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingEMailInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingUserInvitation;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.user.UserModel;

import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;

/**
 * Provide a flat list of contacts from the session model.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ContactProvider extends CompositeFlatSingleContentProvider {

    /** A thinkParity <code>ContactModel</code>. */
    private final ContactModel contactModel;

    /** A thinkParity <code>ProfileModel</code>. */
    private final ProfileModel profileModel;

    /** A thinkParity <code>UserModel</code>. */
    private final UserModel userModel;

    /**
     * Create ContactProvider.
     * 
     * @param profile
     *            A thinkParity user's profile.
     * @param profileModel
     *            A thinkParity profile interface.
     * @param contactModel
     *            A thinkParity contact interface.
     * @param userModel
     *            A thinkParity user interface.
     */
	public ContactProvider(final Profile profile, final ProfileModel profileModel,
            final ContactModel contactModel, final UserModel userModel) {
		super(profile);
        this.contactModel = contactModel;
        this.profileModel = profileModel;
        this.userModel = userModel;
	}

    @Override
    public Object getElement(final Integer index, final Object input) {
        throw Assert.createUnreachable("Deprecated paradigm.");
    }

    @Override
    public Object[] getElements(final Integer index, final Object input) {
        throw Assert.createUnreachable("Deprecated paradigm.");
    }

    public Contact readContact(final JabberId contactId) {
        return contactModel.read(contactId);
    }

    public List<Contact> readContacts() {
        return contactModel.read();
    }

    public List<ProfileEMail> readEmails() {
        return profileModel.readEmails();
    }

    public IncomingInvitation readIncomingInvitation(final Long invitationId) {
        return contactModel.readIncomingInvitation(invitationId);
    }

    public List<IncomingInvitation> readIncomingInvitations() {
        return contactModel.readIncomingInvitations();
    }

    public OutgoingEMailInvitation readOutgoingEMailInvitation(
            final Long invitationId) {
        return contactModel.readOutgoingEMailInvitation(invitationId);
    }

    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations() {
        return contactModel.readOutgoingEMailInvitations();
    }

    public OutgoingUserInvitation readOutgoingUserInvitation(
            final Long invitationId) {
        return contactModel.readOutgoingUserInvitation(invitationId);
    }

    public List<OutgoingUserInvitation> readOutgoingUserInvitations() {
        return contactModel.readOutgoingUserInvitations();
    }

    public Profile readProfile() {
        return profileModel.read();
    }

    public User readUser(final JabberId userId) {
        return userModel.read(userId);
    }

    public List<JabberId> search(final String expression) {
        return contactModel.search(expression);
    }

    public List<Long> searchIncomingInvitations(final String expression) {
        return contactModel.searchIncomingInvitations(expression);
    }

    public List<Long> searchOutgoingEMailInvitations(final String expression) {
        return contactModel.searchOutgoingEMailInvitations(expression);
    }

    public List<Long> searchOutgoingUserInvitations(final String expression) {
        return contactModel.searchOutgoingUserInvitations(expression);
    }
}
