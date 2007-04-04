/*
 * Mar 10, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.tab.contact;

import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;

import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;

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

    /**
     * Create ContactProvider.
     * 
     * @param profileModel
     *            A thinkParity profile interface.
     * @param contactModel
     *            A thinkParity contact interface.
     * @param userModel
     *            A thinkParity user interface.
     */
	public ContactProvider(final ProfileModel profileModel,
            final ContactModel contactModel) {
		super(profileModel);
        this.contactModel = contactModel;
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

    public IncomingEMailInvitation readIncomingEMailInvitation(final Long invitationId) {
        return contactModel.readIncomingEMailInvitation(invitationId);
    }

    public List<IncomingEMailInvitation> readIncomingEMailInvitations() {
        return contactModel.readIncomingEMailInvitations();
    }

    public IncomingUserInvitation readIncomingUserInvitation(final Long invitationId) {
        return contactModel.readIncomingUserInvitation(invitationId);
    }

    public List<IncomingUserInvitation> readIncomingUserInvitations() {
        return contactModel.readIncomingUserInvitations();
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

    public List<JabberId> search(final String expression) {
        return contactModel.search(expression);
    }

    public List<Long> searchIncomingEMailInvitations(final String expression) {
        return contactModel.searchIncomingEMailInvitations(expression);
    }

    public List<Long> searchIncomingUserInvitations(final String expression) {
        return contactModel.searchIncomingUserInvitations(expression);
    }

    public List<Long> searchOutgoingEMailInvitations(final String expression) {
        return contactModel.searchOutgoingEMailInvitations(expression);
    }

    public List<Long> searchOutgoingUserInvitations(final String expression) {
        return contactModel.searchOutgoingUserInvitations(expression);
    }

    public List<JabberId> searchProfile(final String expression) {
        return profileModel.search(expression);
    }
}
